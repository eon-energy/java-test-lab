package com.technokratos.mapper;

import com.technokratos.dto.request.SignUpRequest;
import com.technokratos.entity.internal.Account;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface AccountMapper {

    Account toAccount(SignUpRequest signUpRequest);
}
