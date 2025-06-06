package com.technokratos.mapper;

import com.technokratos.dto.request.ProblemCreateRequest;
import com.technokratos.dto.response.DifficultyLevelResponse;
import com.technokratos.dto.problem.ProblemCreateDto;
import com.technokratos.dto.response.problem.ProblemCreateResponse;
import com.technokratos.dto.response.problem.ProblemFullContentResponse;
import com.technokratos.dto.response.problem.ProblemSolutionResponse;
import com.technokratos.dto.problem.ProblemSummaryDto;
import com.technokratos.entity.internal.DifficultyLevel;
import com.technokratos.entity.internal.Problem;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;

@Mapper(componentModel = "spring")
public interface ProblemMapper {

    @Mapping(source = "difficulty.name", target = "difficultyLevel")
    @Mapping(source = "creator.username", target = "creator")
    @Mapping(source = "difficulty.style.color", target = "difficultyLevelColor")
    ProblemSummaryDto toSummary(Problem entity);

    ProblemCreateDto toDto(ProblemCreateRequest request);

    @Mapping(source = "difficulty", target = "difficultyLevelResponse")
    ProblemFullContentResponse toFullContentResponse(Problem problem);

    @Mapping(source = "createdAt", target = "createdAt")
    ProblemCreateResponse toCreateResponse(Problem problem);

    @Mapping(source = "difficulty", target = "difficultyLevelResponse")
    ProblemSolutionResponse forSolving(Problem problem);

    @Mapping(source = "style.color", target = "color")
    DifficultyLevelResponse toDifficultyLevelDto(DifficultyLevel difficulty);


    @Mapping(source = "difficulty", target = "difficultyLevelResponse", qualifiedByName = "mapDifficulty")
    ProblemSolutionResponse toSolutionResponse(Problem problem);

    @Named("mapDifficulty")
    default DifficultyLevelResponse mapDifficulty(DifficultyLevel difficulty) {
        if (difficulty == null) {
            return null;
        }

        DifficultyLevelResponse response = new DifficultyLevelResponse();
        response.setName(difficulty.getName());
        response.setColor(difficulty.getStyle().getColor());
        return response;
    }

}
