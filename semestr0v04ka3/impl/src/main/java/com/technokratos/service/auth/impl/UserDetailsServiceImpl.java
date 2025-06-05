package com.technokratos.service.auth.impl;

import com.technokratos.dto.security.UserDetailsImpl;
import com.technokratos.repository.AccountRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserDetailsServiceImpl implements UserDetailsService {
    private final AccountRepository accountRepository;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        return new UserDetailsImpl(accountRepository.findByUsernameWithRole(username)
                .orElseThrow(() -> new UsernameNotFoundException("User with username %s not found".formatted(username))));
    }
}
