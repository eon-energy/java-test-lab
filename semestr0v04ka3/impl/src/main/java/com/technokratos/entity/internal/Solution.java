package com.technokratos.entity.internal;

import com.technokratos.entity.external.ExternalSolution;
import jakarta.persistence.*;
import jakarta.validation.constraints.PositiveOrZero;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "solutions")
public class Solution {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(columnDefinition = "uuid", updatable = false)
    private UUID id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "sender_id")
    private Account sender;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "problem_id", nullable = false)
    private Problem problem;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "status_id", nullable = false)
    private SolutionStatus status;

    @OneToOne(mappedBy = "solution", cascade = CascadeType.ALL, orphanRemoval = true)
    private ExternalSolution externalSolution;

    @PositiveOrZero
    @Column(nullable = false)
    private int totalTests;

    @PositiveOrZero
    @Column(nullable = false)
    private int skippedTests;

    @PositiveOrZero
    @Column(nullable = false)
    private int startedTests;

    @PositiveOrZero
    @Column(nullable = false)
    private int abortedTests;

    @PositiveOrZero
    @Column(nullable = false)
    private int passedTests;

    @PositiveOrZero
    @Column(nullable = false)
    private int failedTests;

    @PositiveOrZero
    @Column(name = "execution_time_ms")
    private long executionTimeMs;

    @PositiveOrZero
    @Column(name = "memory_used_bytes")
    private long memoryUsedBytes;

    @Column(name = "log_message", columnDefinition = "text")
    private String logMessage;

    @Column(nullable = false)
    private String bucket;

    @Column(nullable = false)
    private String prefix;

    @UpdateTimestamp
    @Column(name = "updated_at", nullable = false)
    private LocalDateTime updatedAt;

    @CreationTimestamp
    @Column(name = "created_at", updatable = false, nullable = false)
    private LocalDateTime createdAt;
}
