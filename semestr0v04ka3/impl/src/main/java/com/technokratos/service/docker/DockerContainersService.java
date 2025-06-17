package com.technokratos.service.docker;

import com.technokratos.dto.tests.TestResult;

import java.io.IOException;

public interface DockerContainersService{

    TestResult executeCode(String solutionLink, String testLink) throws IOException;
}
