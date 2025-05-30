package com.course.travel_journal_web_service.controllers;

import com.course.travel_journal_web_service.dto.user.ChangePasswordRequest;
import com.course.travel_journal_web_service.dto.user.UserEditRequest;
import com.course.travel_journal_web_service.dto.user.UserResponse;
import com.course.travel_journal_web_service.models.UserForResponse;
import com.course.travel_journal_web_service.services.AuthenticationService;
import com.course.travel_journal_web_service.services.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.core.io.Resource;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("/api/v1/users")
@RequiredArgsConstructor
@Tag(name = "Пользователи")
public class UserController {
    private final UserService userService;
    private final AuthenticationService authenticationService;

    @Operation(summary = "Проверка авторизации пользователя")
    @GetMapping("/check-session")
    public UserForResponse checkSession() {
        return userService.getUserMinData();
    }

    @Operation(summary = "Получение информации о пользователе")
    @GetMapping("/get-user-data")
    public UserResponse getUserData() {
        return userService.getUserData();
    }

    @Operation(summary = "Изменение информации о пользователе")
    @PutMapping(value = "/update-user-data", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public UserResponse updateUser(@RequestPart("user") @Valid UserEditRequest request,
                                   @RequestPart(value = "image", required = false) MultipartFile image) throws Exception {
        return userService.updateUserData(request, image);
    }

    @Operation(summary = "Изменение пароля")
    @PutMapping(value = "/change-password")
    public ResponseEntity<?> changePassword(@RequestBody @Valid ChangePasswordRequest request) {
        authenticationService.changePassword(request);
        return ResponseEntity.ok("");
    }

    @Operation(summary = "Получение информации о пользователе")
    @PostMapping("/reset-user-image")
    public ResponseEntity<?> resetUserImage() {
        userService.resetUserImage();
        return ResponseEntity.ok("");
    }
}
