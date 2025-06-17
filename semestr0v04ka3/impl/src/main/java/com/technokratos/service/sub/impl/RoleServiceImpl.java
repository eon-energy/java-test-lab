package com.technokratos.service.sub.impl;

import com.technokratos.entity.internal.Role;
import com.technokratos.repository.RoleRepository;
import com.technokratos.service.sub.RoleService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class RoleServiceImpl implements RoleService {
    private final RoleRepository roleRepository;


    public Role findByName(String name) {
        return roleRepository.findByName(name)
                .orElseThrow(IllegalArgumentException::new);
        // todo: добавить исключение

    }
}
