package com.technokratos.service.docker.impl;

import com.github.dockerjava.api.model.HostConfig;
import com.technokratos.dto.tests.TestResult;
import com.technokratos.properties.TestContainerProperties;
import com.technokratos.service.docker.DockerContainersService;
import com.technokratos.service.minio.MinioService;
import com.technokratos.util.TestResultParser;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.testcontainers.containers.GenericContainer;
import org.testcontainers.containers.output.ToStringConsumer;
import org.testcontainers.containers.output.WaitingConsumer;
import org.testcontainers.containers.wait.strategy.Wait;
import org.testcontainers.images.builder.ImageFromDockerfile;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.Duration;
import java.util.concurrent.TimeUnit;

@Slf4j
@Service
@RequiredArgsConstructor
public class DockerContainerServiceImpl implements DockerContainersService {
    private final TestContainerProperties props;

    @Override
    public TestResult executeCode(String solutionLink, String testLink) {
        ImageFromDockerfile image = new ImageFromDockerfile()
                .withDockerfile(Paths.get(props.getDockerFile()))
                .withFileFromPath("/app/run-tests.sh", Paths.get(props.getScript()));

        try (GenericContainer<?> container = new GenericContainer<>(image)) {
            container
                    .withEnv("SOLUTION_URL", solutionLink)
                    .withEnv("SOLUTION_TEST_URL", testLink)
                    .withCreateContainerCmdModifier(cmd -> cmd
                            .withHostConfig(new HostConfig()
                                    .withMemory(100 * 1024 * 1024L)
                                    .withCpuCount(1L)
                                    .withNetworkMode("host"))
                    );
            container.setWaitStrategy(Wait.forLogMessage(".*(COMPILATION FAILED|TEST OUTPUT|DOWNLOAD FAILED).*", 1)
                    .withStartupTimeout(Duration.ofMinutes(2)));


            ToStringConsumer logConsumer = new ToStringConsumer();
            WaitingConsumer waitingConsumer = new WaitingConsumer();

            container.start();

            container.followOutput(logConsumer);
            container.followOutput(waitingConsumer);

            waitingConsumer.waitUntilEnd(30, TimeUnit.SECONDS);
            return TestResultParser.parseTestOutput(container.getLogs());
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}

