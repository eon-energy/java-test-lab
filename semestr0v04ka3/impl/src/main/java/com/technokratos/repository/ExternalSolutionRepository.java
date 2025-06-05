package com.technokratos.repository;

import com.technokratos.entity.external.ExternalSolution;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface ExternalSolutionRepository extends JpaRepository<ExternalSolution, UUID> {
}
