package com.technokratos.entity.internal;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Problem {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @ManyToOne
    @JoinColumn(name = "creator_id")
    private Account creator;

    @Column(nullable = false)
    private String title;

    @Column(nullable = false)
    private String bucket;

    @Column(nullable = false)
    private String prefix;

    @Column(nullable = false)
    private int maxExecutionTimeMs = 2000;

    @Column(nullable = false)
    private int maxMemoryUsedMb = 256;

    @Column(nullable = false)
    private boolean isPrivate;

    @ManyToOne
    @JoinColumn(name = "difficulty_id")
    private DifficultyLevel difficulty;

    @CreationTimestamp
    @Column(updatable = false, nullable = false)
    private LocalDateTime createdAt;

}
