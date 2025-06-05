package com.technokratos.repository;

import com.technokratos.entity.internal.Solution;
import com.technokratos.entity.internal.SolutionStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;
import java.util.UUID;

public interface SolutionRepository extends JpaRepository<Solution, UUID> {

    @Transactional
    @Modifying
    @Query("update Solution s set s.status = ?1")
    int updateStatusBy(SolutionStatus status);

    @Modifying
    @Transactional
    @Query("UPDATE Solution s SET s.status = :status WHERE s.id = :id")
    int updateStatusById(
            @Param("id") UUID id,
            @Param("status") SolutionStatus status
    );
}
