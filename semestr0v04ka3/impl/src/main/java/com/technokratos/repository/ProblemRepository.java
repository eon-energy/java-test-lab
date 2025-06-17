package com.technokratos.repository;

import com.technokratos.entity.internal.Account;
import com.technokratos.entity.internal.Problem;
import org.jetbrains.annotations.NotNull;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;
import java.util.UUID;

public interface ProblemRepository extends JpaRepository<Problem, UUID> {
    @EntityGraph(attributePaths = {
            "creator",
            "difficulty",
            "difficulty.style"
    })
    @NotNull
    Page<Problem> findAll(@NotNull Pageable pageable);

    @EntityGraph(attributePaths = {
            "creator",
            "difficulty",
            "difficulty.style"
    })
    Page<Problem> findByCreator(Account accountId, Pageable pageable);

    Optional<Problem> findByIdAndCreator(UUID id, Account account);
}
