package com.technokratos.service.auth.impl;

import com.technokratos.entity.internal.Account;
import com.technokratos.exception.accountServiceException.AccountNotFoundException;
import com.technokratos.exception.accountServiceException.UsernameAlreadyExistsException;
import com.technokratos.repository.AccountRepository;
import com.technokratos.service.auth.AccountService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AccountServiceImpl implements AccountService {
    private final AccountRepository accountRepository;


    @Override
    public Account findByUsername(String username) {
        return accountRepository.findByUsername(username)
                .orElseThrow(() -> new AccountNotFoundException(username));
    }

    @Override
    public Account findByUsernameWithRoles(String username) {
        return accountRepository.findByUsernameWithRole(username)
                .orElseThrow(() -> new AccountNotFoundException(username));
    }

    @Override
    public Account save(Account account) {
        if (existsByUsername(account.getUsername())) {
            throw new UsernameAlreadyExistsException(account.getUsername());
        }
        return accountRepository.save(account);
    }

    @Override
    public boolean existsByUsername(String username) {
        return accountRepository.existsByUsername(username);
    }

}
