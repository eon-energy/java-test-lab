package com.technokratos.api;

import com.technokratos.dto.request.ProblemCreateRequest;
import com.technokratos.dto.response.error.ErrorResponse;
import com.technokratos.dto.response.problem.ProblemCreateResponse;
import com.technokratos.dto.response.problem.ProblemFullContentResponse;
import com.technokratos.dto.response.problem.ProblemFileContentResponse;
import com.technokratos.dto.response.problem.ProblemSolutionResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.parameters.RequestBody;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@Tag(name = "Problem API", description = "Управление задачами (problems)")
public interface ProblemController {

    @Operation(summary = "Получить шаблоны задач", description = "Возвращает шаблонные файлы для создания задач")
    @ApiResponse(responseCode = "200", description = "Шаблоны успешно получены")
    @GetMapping("/templates")
    @ResponseStatus(HttpStatus.OK)
    ProblemFileContentResponse getTemplates();

    @Operation(summary = "Создать задачу", description = "Создание новой задачи с указанными параметрами")
    @ApiResponses({
            @ApiResponse(responseCode = "201", description = "Задача успешно создана"),
            @ApiResponse(responseCode = "400", description = "Невалидные входные данные",
                    content = @Content(schema = @Schema(implementation = ErrorResponse.class)))
    })
    @PostMapping("")
    @ResponseStatus(HttpStatus.CREATED)
    ProblemCreateResponse createProblem(
            @RequestBody(
                    description = "Данные для создания задачи",
                    required = true,
                    content = @Content(schema = @Schema(implementation = ProblemCreateRequest.class))
            )
            ProblemCreateRequest problemCreateRequest);

    @Operation(summary = "Получить задачу для решения", description = "Получение задачи в режиме решения по ID")
    @Parameter(name = "id", description = "UUID задачи", required = true, example = "123e4567-e89b-12d3-a456-426614174000")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Задача успешно получена"),
            @ApiResponse(responseCode = "404", description = "Задача не найдена",
                    content = @Content(schema = @Schema(implementation = ErrorResponse.class)))
    })
    @GetMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    ProblemSolutionResponse getForSolving(@PathVariable UUID id);

    @Operation(summary = "Получить задачу для редактирования", description = "Получение полного содержимого задачи по ID для редактирования")
    @Parameter(name = "id", description = "UUID задачи", required = true, example = "123e4567-e89b-12d3-a456-426614174000")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Содержимое задачи получено"),
            @ApiResponse(responseCode = "404", description = "Задача не найдена",
                    content = @Content(schema = @Schema(implementation = ErrorResponse.class)))
    })
    @GetMapping("/{id}/edit")
    @ResponseStatus(HttpStatus.OK)
    ProblemFullContentResponse getFullContentById(@PathVariable UUID id);
}
