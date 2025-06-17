package com.technokratos.service.auth;

import com.technokratos.entity.internal.Account;

public interface AccountService {
    Account findByUsername(String username);

    Account findByUsernameWithRoles(String username);

    Account save(Account account);

    boolean existsByUsername(String username);
}
