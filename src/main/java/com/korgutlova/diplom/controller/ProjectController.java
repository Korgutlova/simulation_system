package com.korgutlova.diplom.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/project")
public class ProjectController {

    @PostMapping("/create")
    public ResponseEntity<String> createProject() {
        return ResponseEntity.ok("Ok");
    }

    @PostMapping("/create-empty")
    public ResponseEntity<String> createEmptyProject() {
        return ResponseEntity.ok("Ok");
    }

    @PostMapping("/edit/{id}")
    public ResponseEntity<String> editProject(@PathVariable Long id) {
        //@RequestBody ProjectDto projectDto
        return ResponseEntity.ok("Ok");
    }

}
