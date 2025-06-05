package com.technokratos.service.sub.impl;

import com.technokratos.dto.response.DifficultyLevelResponse;
import com.technokratos.entity.internal.DifficultyLevel;
import com.technokratos.exception.difficultyServiceException.DifficultyLevelNotFoundException;
import com.technokratos.mapper.DifficultyLevelMapper;
import com.technokratos.repository.DifficultyLevelRepository;
import com.technokratos.service.sub.DifficultyLevelService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class DifficultyServiceImpl implements DifficultyLevelService {
    private final DifficultyLevelRepository difficultyLevelRepository;
    private final DifficultyLevelMapper difficultyLevelMapper;

    @Override
    public List<DifficultyLevelResponse> findAll() {
        return difficultyLevelRepository.findAll().stream()
                .map(difficultyLevelMapper::toDto).toList();
    }

    @Override
    public DifficultyLevel findByName(String name) {
        return difficultyLevelRepository.findByName(name)
                .orElseThrow(() -> new DifficultyLevelNotFoundException(name));
    }
}
