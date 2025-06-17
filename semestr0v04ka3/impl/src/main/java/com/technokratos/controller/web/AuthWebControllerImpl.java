package com.technokratos.controller.web;

import com.technokratos.dto.request.SignInRequest;
import com.technokratos.dto.request.SignUpRequest;
import com.technokratos.service.auth.AuthService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

@Controller
@RequiredArgsConstructor
@RequestMapping("/auth")
public class AuthWebControllerImpl {
    private final AuthService authService;

    private static final String SIGNUP_VIEW = "auth/signup";
    private static final String SIGNUP_SUCCESS_VIEW = "auth/signup-success";
    private static final String SIGNIN_VIEW = "auth/signin";


    @GetMapping("/signup")
    public String showSignUp(@ModelAttribute("signUpRequest") SignUpRequest form,
                             @RequestParam(name = "success", required = false) String success) {

        return (success != null) ? SIGNUP_SUCCESS_VIEW : SIGNUP_VIEW;
    }

    @PostMapping("/signup")
    public String signUp(@ModelAttribute @Valid SignUpRequest request,
                         BindingResult bindingResult) {

        if (bindingResult.hasErrors()) {
            return SIGNUP_VIEW;
        }

        authService.register(request);
        return "redirect:/auth/signup?success=true";
    }

    @GetMapping("/signin")
    public String showSignIn(@ModelAttribute("signInRequest") SignInRequest form) {
        return SIGNIN_VIEW;
    }


}
