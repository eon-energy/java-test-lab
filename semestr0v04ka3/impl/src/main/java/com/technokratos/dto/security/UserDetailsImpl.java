package com.technokratos.dto.security;

import com.technokratos.entity.internal.Account;
import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.Collections;

@Getter
@AllArgsConstructor
public class UserDetailsImpl implements UserDetails {
    private final Account account;

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return Collections.singleton(new SimpleGrantedAuthority(account.getRole().getName()));
    }

    @Override
    public String getPassword() {
        return account.getHashPassword();
    }

    @Override
    public String getUsername() {
        return account.getUsername();
    }
}
