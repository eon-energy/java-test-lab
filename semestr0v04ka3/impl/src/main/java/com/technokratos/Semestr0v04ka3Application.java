package com.technokratos;

import com.technokratos.properties.MinioProperties;
import com.technokratos.properties.ProblemQueueWorkersProperties;
import com.technokratos.properties.TestContainerProperties;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.context.annotation.EnableAspectJAutoProxy;

@SpringBootApplication
@EnableAspectJAutoProxy(proxyTargetClass = true)
@EnableConfigurationProperties({MinioProperties.class, ProblemQueueWorkersProperties.class, TestContainerProperties.class})
@EnableFeignClients(basePackages = "com.technokratos")
public class Semestr0v04ka3Application {
    public static void main(String[] args) {
        SpringApplication.run(Semestr0v04ka3Application.class, args);
    }
}
