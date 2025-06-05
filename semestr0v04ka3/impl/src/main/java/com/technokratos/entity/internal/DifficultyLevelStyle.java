package com.technokratos.entity.internal;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class DifficultyLevelStyle {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String color;

    @OneToOne
    @JoinColumn(name = "level_id")
    private DifficultyLevel level;
}
