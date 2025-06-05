package com.technokratos.repository;

import com.technokratos.entity.internal.SolutionStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.lang.Nullable;

import java.util.Optional;

public interface SolutionStatusRepository extends JpaRepository<SolutionStatus, Long> {
    Optional<SolutionStatus> findByCode(String code);
}
