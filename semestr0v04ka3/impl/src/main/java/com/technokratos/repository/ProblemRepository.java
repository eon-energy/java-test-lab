package com.technokratos.repository;

import com.technokratos.entity.internal.Problem;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface ProblemRepository extends JpaRepository<Problem, UUID> {
}
