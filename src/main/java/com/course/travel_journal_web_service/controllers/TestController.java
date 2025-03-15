package com.course.travel_journal_web_service.controllers;

import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api")
@AllArgsConstructor
public class TestController {
    @GetMapping(path = "/welcome")
    public ResponseEntity<?> welcome(){
        return ResponseEntity.ok("Незащищенная страница для всех");
    }

    @GetMapping("/users")
    @PreAuthorize("hasAuthority(('ROLE_USER'))")
    public ResponseEntity<?> pageForUsers(){
        return ResponseEntity.ok("Страница для пользователя с ролью: ЮЗЕР");
    }

    @GetMapping("/admins")
    @PreAuthorize("hasAuthority(('ROLE_ADMIN'))")
    public ResponseEntity<?> pageForAdmins(){
        return ResponseEntity.ok("Страница для пользователя с ролью: АДМИН");
    }

    @GetMapping(path = "/all")
    public ResponseEntity<?> pageForAll(){
        return ResponseEntity.ok("Страница для всех пользователей");
    }
}
