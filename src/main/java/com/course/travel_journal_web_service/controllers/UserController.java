package com.course.travel_journal_web_service.controllers;

import com.course.travel_journal_web_service.dto.SignInRequest;
import com.course.travel_journal_web_service.dto.UserEditRequest;
import com.course.travel_journal_web_service.dto.UserResponse;
import com.course.travel_journal_web_service.services.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/users")
@RequiredArgsConstructor
@Tag(name = "Пользователи")
public class UserController {
    private final UserService userService;

    @Operation(summary = "Получение информации о пользователе")
    @GetMapping("/get-user-data")
    public UserResponse getUserData() {
        return userService.getUserData();
    }

    @Operation(summary = "Изменение информации о пользователе")
    @PutMapping("/update-user-data")
    public UserResponse updateUserData(@RequestBody @Valid UserEditRequest request) {
        return userService.updateUserData(request);
    }
}
