package com.technokratos.worker;

import com.technokratos.dto.redis.ProblemPayload;
import com.technokratos.dto.tests.TestResult;
import com.technokratos.entity.enums.SolutionStatusCode;
import com.technokratos.properties.ProblemQueueWorkersProperties;
import com.technokratos.service.docker.DockerContainersService;
import com.technokratos.service.docker.impl.DockerContainerServiceImpl;
import com.technokratos.service.general.ProblemService;
import com.technokratos.service.general.SolutionService;
import com.technokratos.service.minio.MinioService;
import com.technokratos.service.redis.impl.RedisServiceImpl;
import jakarta.annotation.PostConstruct;
import jakarta.annotation.PreDestroy;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.jetbrains.annotations.NotNull;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.util.UUID;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;

@Component
@RequiredArgsConstructor
@Slf4j
public class ProblemQueueWorker {
    private final RedisServiceImpl redisService;
    private ExecutorService executorService;
    private final DockerContainersService dockerService;
    private final ProblemQueueWorkersProperties props;
    private final SolutionService solutionService;

    private volatile boolean running = true;


    @PostConstruct
    public void startWorkers() {
        log.info("ProblemQueueWorker: запускаем {} воркеров.", props.getWorkersCount());

        ThreadFactory namedThreadFactory = new ThreadFactory() {
            private final AtomicInteger counter = new AtomicInteger(1);

            @Override
            public Thread newThread(@NotNull Runnable r) {
                Thread t = new Thread(r, "problem-queue-worker-" + counter.getAndIncrement());
                t.setDaemon(true);
                return t;
            }
        };

        executorService = Executors.newFixedThreadPool(props.getWorkersCount(), namedThreadFactory);

        for (int i = 0; i < props.getWorkersCount(); i++) {
            executorService.submit(this::processQueue);
        }
    }

    @PreDestroy
    public void stopWorkers() {
        log.info("ProblemQueueWorker: инициирован shutdown воркеров.");
        running = false;

        executorService.shutdownNow();
        try {
            if (!executorService.awaitTermination(30, TimeUnit.SECONDS)) {
                log.warn("ProblemQueueWorker: не удалось дождаться завершения воркеров за 30 секунд.");
            } else {
                log.info("ProblemQueueWorker: все воркеры корректно завершились.");
            }
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            log.warn("ProblemQueueWorker: ожидание завершения воркеров было прервано.", e);
        }
    }


    private void processQueue() {
        while (running && !Thread.currentThread().isInterrupted()) {

            if (Thread.currentThread().isInterrupted()) {
                log.info("Поток {} получил interrupt — выходим из processQueue.", Thread.currentThread().getName());
                break;
            }

            try {
                ProblemPayload payload = redisService.dequeue(props.getRedisDequeueTimeoutSeconds(), TimeUnit.SECONDS);

                if (payload != null) {
                    handlePayload(payload);
                }

            } catch (Exception e) {
                log.error("Ошибка в процессе чтения/обработки из очереди в потоке {}", Thread.currentThread().getName(), e);
            }
        }
        log.info("Воркер {} вышел из цикла обработки очереди.", Thread.currentThread().getName());
    }

    private void handlePayload(ProblemPayload payload) {
        UUID solutionId = payload.getSolutionId();
        try {
            log.debug("Начинаем обработку payload.solutionId={}", solutionId);

            TestResult result = dockerService.executeCode(payload.getSolutionCodeLink(), payload.getTestCodeLink());
            solutionService.updateStats(payload.getSolutionId(), result);
            log.info("Успешно обработали payload.solutionId={}", solutionId);


        } catch (Exception e) {
            log.error("Ошибка при обработке payload.solutionId={}", solutionId, e);
        }
    }

}