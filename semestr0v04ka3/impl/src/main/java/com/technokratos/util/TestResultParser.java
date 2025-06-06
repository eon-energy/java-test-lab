package com.technokratos.util;

import com.technokratos.dto.tests.TestDetail;
import com.technokratos.dto.tests.TestResult;
import com.technokratos.entity.enums.SolutionStatusCode;
import lombok.experimental.UtilityClass;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

@UtilityClass
public class TestResultParser {
    private static final Pattern TEST_STATS_PATTERN = Pattern.compile(
            "\\[\\s*(\\d+) tests? found\\s*\\]\\R" +
                    "\\[\\s*(\\d+) tests? skipped\\s*\\]\\R" +
                    "\\[\\s*(\\d+) tests? started\\s*\\]\\R" +
                    "\\[\\s*(\\d+) tests? aborted\\s*\\]\\R" +
                    "\\[\\s*(\\d+) tests? successful\\s*\\]\\R" +
                    "\\[\\s*(\\d+) tests? failed\\s*\\]"
    );

    private static final Pattern TEST_METHOD_PATTERN = Pattern.compile(
            "([├└]─ )?[^\\n]*?([\\w]+)\\(\\)\\s*([✔✘])"
    );

    private static final Pattern EXCEPTION_PATTERN = Pattern.compile(
            "([\\w.]+Exception|Error):?\\s*([^\\n]+)(\\R\\s+at\\s+[^\\n]+)*"
    );

    private static final Pattern FAILURE_SECTION_PATTERN = Pattern.compile(
            "Failures \\(\\d+\\):(.*?)(?=\\R\\R|\\R⧯|\\RTest run finished|$)",
            Pattern.DOTALL
    );

    private static final Pattern TIME_PATTERN = Pattern.compile("TIME:([\\d.]+)");
    private static final Pattern MEM_PATTERN  = Pattern.compile("MEM:(\\d+)");

    public static TestResult parseTestOutput(String logs) {
        TestResult result = new TestResult();

        if (logs.contains("COMPILATION FAILED")) {
            result.setStatus(SolutionStatusCode.COMPILATION_ERROR);
            result.setCompilationErrors(extractCompilationErrors(logs));
            return result;
        }

        extractTestStats(logs, result);

        result.setTestDetails(extractTestDetails(logs));

        determineOverallStatus(result, logs);

        extractMetrics(logs, result);

        return result;
    }

    private static void extractTestStats(String logs, TestResult result) {
        Matcher matcher = TEST_STATS_PATTERN.matcher(logs);
        if (matcher.find()) {
            result.setTotalTests(Integer.parseInt(matcher.group(1)));
            result.setSkippedTests(Integer.parseInt(matcher.group(2)));
            result.setStartedTests(Integer.parseInt(matcher.group(3)));
            result.setAbortedTests(Integer.parseInt(matcher.group(4)));
            result.setPassedTests(Integer.parseInt(matcher.group(5)));
            result.setFailedTests(Integer.parseInt(matcher.group(6)));
        }
    }

    private static String extractCompilationErrors(String logs) {
        int start = logs.indexOf("COMPILATION FAILED");
        if (start == -1) return "";

        int end = logs.indexOf("\n\n", start);
        if (end == -1) end = logs.length();

        return logs.substring(start, end).trim();
    }

    private static List<TestDetail> extractTestDetails(String logs) {
        List<TestDetail> details = new ArrayList<>();

        Matcher methodMatcher = TEST_METHOD_PATTERN.matcher(logs);
        while (methodMatcher.find()) {
            TestDetail detail = new TestDetail();
            detail.setMethodName(methodMatcher.group(2));
            detail.setStatus("✔".equals(methodMatcher.group(3)) ?
                    SolutionStatusCode.ACCEPTED : SolutionStatusCode.WRONG_ANSWER);
            details.add(detail);
        }

        Matcher failureSectionMatcher = FAILURE_SECTION_PATTERN.matcher(logs);
        if (failureSectionMatcher.find()) {
            String failuresSection = failureSectionMatcher.group(1);
            parseFailureSection(failuresSection, details);
        }

        if (details.stream().anyMatch(d -> d.getErrorMessage() == null && d.getStatus() != SolutionStatusCode.ACCEPTED)) {
            findExceptionsInLogs(logs, details);
        }

        return details;
    }

    private static void parseFailureSection(String failuresSection, List<TestDetail> details) {
        String[] failureBlocks = failuresSection.split("\\R  \\S");
        Map<String, TestDetail> detailMap = details.stream()
                .collect(Collectors.toMap(TestDetail::getMethodName, d -> d));

        for (String block : failureBlocks) {
            String[] lines = block.split("\\R");
            if (lines.length < 2) continue;

            String methodName = extractMethodNameFromFailureHeader(lines[0]);
            if (methodName == null) continue;

            TestDetail detail = detailMap.get(methodName);
            if (detail == null) {
                detail = new TestDetail();
                detail.setMethodName(methodName);
                detail.setStatus(SolutionStatusCode.WRONG_ANSWER);
                details.add(detail);
                detailMap.put(methodName, detail);
            }

            for (int i = 1; i < lines.length; i++) {
                if (lines[i].trim().startsWith("=>")) {
                    String errorLine = lines[i].substring(2).trim();
                    extractExceptionInfo(errorLine, detail);
                }
            }
        }
    }

    private static String extractMethodNameFromFailureHeader(String header) {
        int lastColon = header.lastIndexOf(':');
        if (lastColon == -1) return null;

        String methodPart = header.substring(lastColon + 1).trim();
        if (methodPart.endsWith("()")) {
            methodPart = methodPart.substring(0, methodPart.length() - 2);
        }

        return methodPart;
    }

    private static void extractExceptionInfo(String errorLine, TestDetail detail) {
        Matcher exceptionMatcher = EXCEPTION_PATTERN.matcher(errorLine);
        if (exceptionMatcher.find()) {
            String exceptionType = exceptionMatcher.group(1);
            String errorMessage = exceptionMatcher.group(2).trim();

            if (isRuntimeException(exceptionType)) {
                detail.setStatus(SolutionStatusCode.RUNTIME_ERROR);
            } else {
                detail.setStatus(SolutionStatusCode.WRONG_ANSWER);
            }

            detail.setErrorMessage(errorMessage);
        }
    }

    private static void findExceptionsInLogs(String logs, List<TestDetail> details) {
        Matcher exceptionMatcher = EXCEPTION_PATTERN.matcher(logs);
        while (exceptionMatcher.find()) {
            String exceptionType = exceptionMatcher.group(1);
            String errorMessage = exceptionMatcher.group(2).trim();

            for (TestDetail detail : details) {
                if (detail.getErrorMessage() == null && detail.getStatus() != SolutionStatusCode.ACCEPTED) {
                    if (isRuntimeException(exceptionType)) {
                        detail.setStatus(SolutionStatusCode.RUNTIME_ERROR);
                    }
                    detail.setErrorMessage(errorMessage);
                    break;
                }
            }
        }
    }

    private static boolean isRuntimeException(String exceptionType) {
        return exceptionType.startsWith("java.lang.") &&
                !exceptionType.equals("java.lang.AssertionError");
    }

    private static void determineOverallStatus(TestResult result, String logs) {
        boolean hasRuntime = result.getTestDetails().stream()
                .anyMatch(d -> d.getStatus() == SolutionStatusCode.RUNTIME_ERROR);

        boolean commonRuntimeError = result.getFailedTests() > 0 &&
                result.getTestDetails().stream()
                        .filter(d -> d.getStatus() != SolutionStatusCode.ACCEPTED)
                        .map(TestDetail::getErrorMessage)
                        .distinct()
                        .count() == 1;

        if (hasRuntime || commonRuntimeError) {
            result.setStatus(SolutionStatusCode.RUNTIME_ERROR);
        } else if (result.getFailedTests() > 0) {
            result.setStatus(SolutionStatusCode.WRONG_ANSWER);
        } else if (result.getPassedTests() > 0) {
            result.setStatus(SolutionStatusCode.ACCEPTED);
        } else {
            result.setStatus(SolutionStatusCode.INTERNAL_ERROR);
        }

        if (result.getStatus() == SolutionStatusCode.INTERNAL_ERROR &&
                logs.matches("(?s).*java\\.lang\\..+Exception.*")) {
            result.setStatus(SolutionStatusCode.RUNTIME_ERROR);
        }
    }

    private static void extractMetrics(String logs, TestResult result) {
        Matcher timeMatcher = TIME_PATTERN.matcher(logs);
        if (timeMatcher.find()) {
            try {
                double seconds = Double.parseDouble(timeMatcher.group(1));
                result.setExecutionTimeSeconds((int) seconds);
            } catch (NumberFormatException ignored) {
            }
        }

        Matcher memMatcher = MEM_PATTERN.matcher(logs);
        if (memMatcher.find()) {
            try {
                long memKb = Long.parseLong(memMatcher.group(1));
                result.setMaxMemoryMb((int) (memKb/1024));
            } catch (NumberFormatException ignored) {
            }
        }
    }
}
