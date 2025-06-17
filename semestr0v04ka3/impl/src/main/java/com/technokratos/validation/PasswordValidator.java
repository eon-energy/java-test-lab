package com.technokratos.validation;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

import java.util.regex.Pattern;

public class PasswordValidator implements ConstraintValidator<ValidPassword, String> {

    /*
     - Минимум 8 символов
     - Хотя бы одна цифра
     - Хотя бы один спецсимвол (!@#$%^&*)
    */
    private static final String PASSWORD_PATTERN =
            "^(?=.*[0-9])(?=.*[!@#$%^&*])[a-zA-Z0-9!@#$%^&*]{8,}$";

    @Override
    public boolean isValid(String password, ConstraintValidatorContext context) {
        if (password == null) {
            return false;
        }
        return Pattern.compile(PASSWORD_PATTERN).matcher(password).matches();
    }
}
