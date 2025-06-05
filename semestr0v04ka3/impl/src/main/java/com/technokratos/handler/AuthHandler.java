package com.technokratos.handler;

import com.technokratos.exception.accountServiceException.UsernameAlreadyExistsException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@ControllerAdvice
@Slf4j
public class AuthHandler {


    @ExceptionHandler(UsernameAlreadyExistsException.class)
    public String handleUsernameExists(UsernameAlreadyExistsException ex, RedirectAttributes redirect) {
        log.warn("Registration failed: {}", ex.getMessage());
        return "redirect:/auth/signup?";
    }
}
