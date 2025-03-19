package com.course.travel_journal_web_service.security;

import com.course.travel_journal_web_service.dto.SignInRequest;
import com.course.travel_journal_web_service.dto.SignUpRequest;
import com.course.travel_journal_web_service.models.Role;
import com.course.travel_journal_web_service.models.User;
import com.course.travel_journal_web_service.services.UserService;
import lombok.RequiredArgsConstructor;
import org.apache.catalina.connector.Request;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthenticationService {
    private final UserService userService;
    private final JwtService jwtService;
    private final PasswordEncoder passwordEncoder;
    private final AuthenticationManager authenticationManager;
    public static final String HEADER_NAME = "RefreshToken";

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
        var refreshJwt = jwtService.generateRefreshToken(user);
        return new JwtAuthenticationResponse(jwt, refreshJwt);
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
        var refreshJwt = jwtService.generateRefreshToken(user);
        return new JwtAuthenticationResponse(jwt, refreshJwt);
    }

//    public JwtAuthenticationResponse refresh(Request request) {
//        String refreshToken = request.getHeader(HEADER_NAME);
//        String username = jwtService.extractUserName(refreshToken);
//
//        if (jwtService.isTokenValid(request.getRefreshToken(), username)) {
//            String accessToken = jwtTokenUtil.generateAccessToken(username);
//            return ResponseEntity.ok(new JwtResponse(accessToken, refreshTokenRequest.getRefreshToken()));
//        }
//
//        return ResponseEntity.badRequest().body("Invalid refresh token");
//
//        var jwt = jwtService.generateToken(user);
//        var refreshJwt = jwtService.generateRefreshToken(user);
//        return new JwtAuthenticationResponse(jwt, refreshJwt);
//    }
}