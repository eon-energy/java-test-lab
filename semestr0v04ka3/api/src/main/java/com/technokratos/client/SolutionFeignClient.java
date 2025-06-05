package com.technokratos.client;

import com.technokratos.api.SolutionController;
import org.springframework.cloud.openfeign.FeignClient;

@FeignClient(
        name = "solution-service",
        url = "${services.solution-service.url}"
)
public interface SolutionFeignClient extends SolutionController {

}
