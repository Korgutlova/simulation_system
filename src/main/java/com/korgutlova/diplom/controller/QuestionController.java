package com.korgutlova.diplom.controller;

import com.korgutlova.diplom.exception.QuestionNotFound;
import com.korgutlova.diplom.model.dto.QuestionCommandDto;
import com.korgutlova.diplom.model.entity.question.QuestionCommand;
import com.korgutlova.diplom.service.api.QuestionService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/questions")
@RequiredArgsConstructor
public class QuestionController {

    private final QuestionService questionService;

    @GetMapping("/command/{command}")
    public ResponseEntity<String> questionCommand(@PathVariable String command) {
        try {
            String result = questionService.findQuestionByCommand(command);
            return ResponseEntity.ok("Ответ: " + result);
        } catch (QuestionNotFound e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        }
    }

    @PostMapping("/command/create")
    public ResponseEntity<String> createQuestionCommand(
            @RequestBody QuestionCommandDto questionCommandDto) {
        questionService.create(questionCommandDto);
        return ResponseEntity.ok("ok");
    }
}
