package com.korgutlova.diplom.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/questions")
public class QuestionController {

    @GetMapping("/command")
    public ResponseEntity<String> questionCommand(@RequestParam String command) {
        return ResponseEntity.ok("Ok");
    }
}
