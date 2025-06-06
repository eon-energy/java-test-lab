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


    @Modifying
    @Query("""
            UPDATE Solution s
               SET s.totalTests   = :totalTests,
                   s.skippedTests = :skippedTests,
                   s.startedTests = :startedTests,
                   s.abortedTests = :abortedTests,
                   s.passedTests  = :passedTests,
                   s.failedTests  = :failedTests,
                   s.executionTimeMs = :executionTimeMs,
                   s.memoryUsedBytes = :memoryUsedBytes,
                   s.logMessage = :logMessage
             WHERE s.id = :id
            """)
    int updateTestStats(
            @Param("id") UUID id,
            @Param("totalTests") int totalTests,
            @Param("skippedTests") int skippedTests,
            @Param("startedTests") int startedTests,
            @Param("abortedTests") int abortedTests,
            @Param("passedTests") int passedTests,
            @Param("failedTests") int failedTests,
            @Param("executionTimeMs") int executionTimeMs,
            @Param("memoryUsedBytes") int memoryUsedBytes,
            @Param("logMessage") String logMessage

    );
}
