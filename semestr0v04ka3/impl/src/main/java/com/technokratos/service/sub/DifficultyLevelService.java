package com.technokratos.service.sub;

import com.technokratos.dto.response.DifficultyLevelResponse;
import com.technokratos.entity.internal.DifficultyLevel;

import java.util.List;

public interface DifficultyLevelService {
    List<DifficultyLevelResponse> findAll();

    DifficultyLevel findByName(String name);
}
