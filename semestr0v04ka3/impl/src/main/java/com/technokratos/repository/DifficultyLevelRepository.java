package com.technokratos.repository;

import com.technokratos.entity.internal.DifficultyLevel;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface DifficultyLevelRepository extends JpaRepository<DifficultyLevel, Long> {
    Optional<DifficultyLevel> findByName(String name);
}
