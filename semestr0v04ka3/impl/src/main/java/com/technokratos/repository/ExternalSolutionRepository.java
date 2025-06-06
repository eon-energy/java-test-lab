package com.technokratos.repository;

import com.technokratos.entity.external.ExternalSolution;
import com.technokratos.entity.internal.Solution;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;
import java.util.UUID;

public interface ExternalSolutionRepository extends JpaRepository<ExternalSolution, UUID> {
    Optional<ExternalSolution> findBySolution(Solution solution);

    Optional<ExternalSolution> findBySolutionId(UUID id);
}
