package com.technokratos.client;

import com.technokratos.api.ProblemController;
import org.springframework.cloud.openfeign.FeignClient;

@FeignClient(
        name = "problem-service",
        url = "${services.problem-service.url}"
)
public interface ProblemFeignClient extends ProblemController {
}
