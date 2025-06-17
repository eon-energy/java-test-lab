package com.technokratos.dto.tests;

import com.technokratos.entity.enums.SolutionStatusCode;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import lombok.ToString;

@Data
@AllArgsConstructor
@RequiredArgsConstructor
@ToString
public class TestDetail {
    private SolutionStatusCode status;
    private String methodName;
    private String errorMessage;
    private String stackTrace;
}
