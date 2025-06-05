package com.technokratos.service.auth;

import com.technokratos.dto.request.SignUpRequest;

public interface AuthService {
    void register(SignUpRequest request);
}
