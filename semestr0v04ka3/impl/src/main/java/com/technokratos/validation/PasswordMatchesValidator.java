package com.technokratos.validation;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import org.springframework.beans.BeanWrapperImpl;

import java.util.Objects;

public class PasswordMatchesValidator implements ConstraintValidator<PasswordMatches, Object> {
    private String passwordField;
    private String confirmationField;

    @Override
    public void initialize(PasswordMatches constraint) {
        this.passwordField       = constraint.passwordField();
        this.confirmationField   = constraint.confirmationField();
    }


    @Override
    public boolean isValid(Object target, ConstraintValidatorContext ctx) {
        if (target == null) {
            return true;
        }

        BeanWrapperImpl wrapper = new BeanWrapperImpl(target);
        Object password      = wrapper.getPropertyValue(passwordField);
        Object confirmation  = wrapper.getPropertyValue(confirmationField);

        boolean matched = Objects.equals(password, confirmation);

        if (!matched) {
            ctx.disableDefaultConstraintViolation();
            ctx.buildConstraintViolationWithTemplate(ctx.getDefaultConstraintMessageTemplate())
                    .addPropertyNode(confirmationField)
                    .addConstraintViolation();
        }
        return matched;

    }
}
