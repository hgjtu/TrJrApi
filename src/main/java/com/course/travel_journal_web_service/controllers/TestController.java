package com.course.travel_journal_web_service.controllers;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api")
public class TestController {
    @GetMapping(path = "/ping")
    public ResponseEntity<?> ping(){
        return ResponseEntity.ok("pong");
    }

    @PostMapping("/ping/{name}")
    public ResponseEntity<?> pingPong(@RequestParam Long id, @PathVariable String name){
        return ResponseEntity.ok("pong-"+name);
    }
}
