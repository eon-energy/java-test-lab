package com.technokratos.mapper;

import com.technokratos.dto.response.DifficultyLevelResponse;
import com.technokratos.entity.internal.DifficultyLevel;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface DifficultyLevelMapper {

    @Mapping(source = "style.color", target = "color")
    DifficultyLevelResponse toDto(DifficultyLevel entity);
}
