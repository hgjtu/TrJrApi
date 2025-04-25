package com.course.travel_journal_web_service.controllers;

import com.course.travel_journal_web_service.services.AdminService;
import com.course.travel_journal_web_service.services.ModeratorService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/moderators")
@RequiredArgsConstructor
@Tag(name = "Модераторы")
public class ModeratorController {
    private final ModeratorService service;

    @PostMapping("/{post_id}/decision/{decision}")
    @Operation(summary = "Решение модератора по посту")
    public ResponseEntity<?> positiveDecision(@PathVariable Long post_id, @PathVariable String decision) {
        service.setDecision(post_id, decision);
        return ResponseEntity.ok("");
    }
}
