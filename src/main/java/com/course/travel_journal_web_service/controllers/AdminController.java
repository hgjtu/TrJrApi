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

    // TODO УДАЛИТЬ
    @Operation(summary = "Получение доступа к странице администраторов")
    @GetMapping("")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public ResponseEntity<?> pageForAdmins(){
        return ResponseEntity.ok("Страница для пользователя с ролью: АДМИН");
    }

    @GetMapping("/get-admin")
    @Operation(summary = "Получить роль ADMIN (для демонстрации)")
    public void getAdmin() {
        service.getAdmin();
    }
}
