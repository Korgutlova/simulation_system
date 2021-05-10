package com.korgutlova.diplom.controller;

import com.korgutlova.diplom.model.dto.ProjectDto;
import com.korgutlova.diplom.service.api.ProjectService;
import lombok.AllArgsConstructor;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/project")
@RequiredArgsConstructor
public class ProjectController {

    private final ProjectService projectService;

    @PostMapping("/create")
    public ResponseEntity<String> createProject(@RequestBody ProjectDto projectDto) {
        projectService.save(projectDto);
        return ResponseEntity.ok("Ok");
    }

    @PostMapping("/create-empty")
    public ResponseEntity<String> createEmptyProject(@RequestBody ProjectDto projectDto) {
        projectService.save(projectDto);
        return ResponseEntity.ok("Ok");
    }

    @PostMapping("/edit/{id}")
    public ResponseEntity<String> editProject(@PathVariable Long id) {
        //@RequestBody ProjectDto projectDto
        return ResponseEntity.ok("Ok");
    }

}
