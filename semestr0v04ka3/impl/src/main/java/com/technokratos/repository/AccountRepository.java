package com.technokratos.repository;

import com.technokratos.entity.internal.Account;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface AccountRepository extends JpaRepository<Account, String> {
    Optional<Account> findByUsername(String username);

    boolean existsByUsername(String username);

    @Query("SELECT a FROM Account a JOIN FETCH a.role WHERE a.username = :username")
    Optional<Account> findByUsernameWithRole(@Param("username") String username);
}
