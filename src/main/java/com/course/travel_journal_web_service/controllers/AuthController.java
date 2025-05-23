package com.course.travel_journal_web_service.controllers;

import com.course.travel_journal_web_service.dto.auth.SignInRequest;
import com.course.travel_journal_web_service.dto.auth.SignUpRequest;
import com.course.travel_journal_web_service.services.AuthenticationService;
import com.course.travel_journal_web_service.dto.auth.JwtAuthenticationResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/auth")
@RequiredArgsConstructor
@Tag(name = "Аутентификация")
public class AuthController {
    private final AuthenticationService authenticationService;

    @Operation(summary = "Регистрация пользователя")
    @PostMapping("/sign-up")
    public JwtAuthenticationResponse signUp(@Valid @RequestBody SignUpRequest request) {
        return authenticationService.signUp(request);
    }

    @Operation(summary = "Авторизация пользователя")
    @PostMapping("/sign-in")
    public JwtAuthenticationResponse signIn(@Valid @RequestBody SignInRequest request) {
        return authenticationService.signIn(request);
    }
}