package com.technokratos.dto.request;

import com.technokratos.validation.ValidPassword;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class SignInRequest {

    @NotBlank(message = "Username cannot be empty")
    @Size(min = 4, max = 20, message = "Username must be between 4 and 20 characters")
    private String username;


    @ValidPassword
    private String password;

}

