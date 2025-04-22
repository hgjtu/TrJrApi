package com.course.travel_journal_web_service.controllers;


import com.course.travel_journal_web_service.services.AdminService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/admins")
@RequiredArgsConstructor
@Tag(name = "Администраторы")
public class AdminController {
    private final AdminService service;

//    @GetMapping("/get-admin")
//    @Operation(summary = "Получить роль ADMIN (для демонстрации)")
//    public void getAdmin() {
//        service.getAdmin();
//    }

    @GetMapping("/{username}/set-moderator")
    @Operation(summary = "Выдать пользователю роль модератора")
    public void setModerator(@PathVariable String username) {

    }
}
