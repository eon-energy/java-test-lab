package com.technokratos.entity.external;

import com.technokratos.entity.internal.Problem;
import com.technokratos.entity.internal.Solution;
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
public class ExternalSolution {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @Column(nullable = false)
    private String callbackUrl;

    @Column(nullable = false)
    private String callbackSecret;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "solution_id")
    private Solution solution;

    @CreationTimestamp
    private LocalDateTime createdAt;
}
