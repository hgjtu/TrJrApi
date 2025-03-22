package com.course.travel_journal_web_service.controllers;

import com.course.travel_journal_web_service.dto.user.UserEditRequest;
import com.course.travel_journal_web_service.dto.user.UserResponse;
import com.course.travel_journal_web_service.services.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
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
    public UserResponse deleteUser(@RequestBody @Valid UserEditRequest request) {
        return userService.updateUserData(request);
    }

    // TODO УДАЛИТЬ
    @Operation(summary = "Изменение информации о пользователе")
    @DeleteMapping("/delete-user/{username}")
    public ResponseEntity<Void> updateUserData(@PathVariable String username) {
        userService.deleteUser(username);
        return ResponseEntity.noContent().build();
    }
}
