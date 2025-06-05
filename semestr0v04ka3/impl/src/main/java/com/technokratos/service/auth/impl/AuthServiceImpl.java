package com.technokratos.service.auth.impl;

import com.technokratos.dto.request.SignUpRequest;
import com.technokratos.entity.internal.Account;
import com.technokratos.entity.enums.AccountRole;
import com.technokratos.entity.internal.Role;
import com.technokratos.mapper.AccountMapper;
import com.technokratos.repository.RoleRepository;
import com.technokratos.service.auth.AccountService;
import com.technokratos.service.auth.AuthService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthServiceImpl implements AuthService {
    private final AccountService accountService;
    private final PasswordEncoder passwordEncoder;
    private final AccountMapper accountMapper;
    private final RoleRepository roleRepository;

    @Override
    public void register(SignUpRequest request) {
        Role defaultRole = roleRepository.findByName(AccountRole.USER.name())
                .orElseThrow(() -> new IllegalStateException("Default role USER not found"));

        Account account = accountMapper.toAccount(request);
        account.setHashPassword(passwordEncoder.encode(request.getPassword()));
        account.setRole(defaultRole);

        accountService.save(account);
    }
}
