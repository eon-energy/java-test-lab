package com.technokratos.dto.tests;

import com.technokratos.entity.enums.SolutionStatusCode;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import lombok.ToString;

import java.util.ArrayList;
import java.util.List;

@Data
@AllArgsConstructor
@RequiredArgsConstructor
@ToString
public class TestResult {
    private SolutionStatusCode status;
    private String compilationErrors;
    private int totalTests;
    private int skippedTests;
    private int startedTests;
    private int abortedTests;
    private int passedTests;
    private int failedTests;
    private int executionTimeSeconds;
    private int maxMemoryMb;
    private List<TestDetail> testDetails = new ArrayList<>();
}