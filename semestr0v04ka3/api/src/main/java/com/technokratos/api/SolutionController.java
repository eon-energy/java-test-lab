package com.technokratos.api;

import com.technokratos.dto.request.SolutionCreateRequest;
import com.technokratos.dto.response.error.ErrorResponse;
import com.technokratos.dto.response.solution.SolutionCreateResponse;
import com.technokratos.dto.response.solution.SolutionResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;


@Tag(name = "Solution API", description = "Управление решениями задач")
public interface SolutionController {

    @Operation(summary = "Получить статус решения", description = "Получение статуса решения по ID")
    @Parameter(name = "id", description = "UUID решения", required = true)
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Статус решения"),
            @ApiResponse(responseCode = "404", description = "Решение не найдено",
                    content = @Content(schema = @Schema(implementation = ErrorResponse.class)))
    })
    @GetMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    SolutionResponse getById(@PathVariable UUID id);

    @Operation(summary = "Отправить решение", description = "Отправка решения задачи")
    @ApiResponses({
            @ApiResponse(responseCode = "201", description = "Решение принято"),
            @ApiResponse(responseCode = "400", description = "Ошибка валидации",
                    content = @Content(schema = @Schema(implementation = ErrorResponse.class)))
    })
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    SolutionCreateResponse addSolution(@RequestBody SolutionCreateRequest solutionCreateRequest);
}
