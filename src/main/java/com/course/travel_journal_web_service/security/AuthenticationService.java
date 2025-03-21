package com.course.travel_journal_web_service.security;

import com.course.travel_journal_web_service.dto.SignInRequest;
import com.course.travel_journal_web_service.dto.SignUpRequest;
import com.course.travel_journal_web_service.models.Role;
import com.course.travel_journal_web_service.models.User;
import com.course.travel_journal_web_service.models.UserForResponse;
import com.course.travel_journal_web_service.services.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.io.Console;
import java.lang.reflect.InvocationTargetException;

@Service
@RequiredArgsConstructor
public class AuthenticationService {
    private final UserService userService;
    private final JwtService jwtService;
    private final PasswordEncoder passwordEncoder;
    private final AuthenticationManager authenticationManager;

    /**
     * Регистрация пользователя
     *
     * @param request данные пользователя
     * @return токен
     */
    public JwtAuthenticationResponse signUp(SignUpRequest request) {

        var user = User.builder()
                .username(request.getUsername())
                .email(request.getEmail())
                .password(passwordEncoder.encode(request.getPassword()))
                .role(Role.ROLE_USER)
                .build();

        userService.create(user);

        var jwt = jwtService.generateToken(user);
        var userForResponse = UserForResponse.builder()
                .username(request.getUsername())
                .role(Role.ROLE_USER)
                .build();

        return new JwtAuthenticationResponse(jwt, userForResponse);
    }

    /**
     * Аутентификация пользователя
     *
     * @param request данные пользователя
     * @return токен
     */
    public JwtAuthenticationResponse signIn(SignInRequest request) {
        authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(
                request.getUsername(),
                request.getPassword()
        ));

        var user = userService
                .userDetailsService()
                .loadUserByUsername(request.getUsername());

        var jwt = jwtService.generateToken(user);
        var userForResponse = UserForResponse.builder()
                .username(request.getUsername())
                .role(userService
                        .getByUsername(request.getUsername())
                        .getRole())
                .build();

        return new JwtAuthenticationResponse(jwt, userForResponse);
    }
}