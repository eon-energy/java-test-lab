package com.technokratos.mapper;

import com.technokratos.dto.response.solution.SolutionCreateResponse;
import com.technokratos.dto.response.solution.SolutionResponse;
import com.technokratos.entity.internal.Solution;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface SolutionMapper {

    @Mapping(source = "problem.id", target = "problemId")
    SolutionCreateResponse toCreateResponse(Solution solution);

    @Mapping(source = "problem.id", target = "problemId")
    @Mapping(source = "status.code", target = "statusCode")
    @Mapping(source = "createdAt", target = "createdAt")
    SolutionResponse toStatusResponse(Solution solution);
}
