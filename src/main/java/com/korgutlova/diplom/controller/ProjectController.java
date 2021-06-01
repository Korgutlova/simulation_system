package com.korgutlova.diplom.controller;

import com.korgutlova.diplom.model.dto.ProjectDto;
import com.korgutlova.diplom.model.entity.Project;
import com.korgutlova.diplom.model.entity.User;
import com.korgutlova.diplom.model.mapper.ProjectMapper;
import com.korgutlova.diplom.service.api.ProjectService;
import javax.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
@RequestMapping("/project")
@RequiredArgsConstructor
public class ProjectController {

    private final ProjectService projectService;
    private final ProjectMapper projectMapper;

    @GetMapping("/create")
    public String createProjectPage(@RequestParam(value = "project_id", required = false) Long projectId,
                                    Model model) {
        User currentUser = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();

        ProjectDto projectDto = new ProjectDto();
        if (projectId != null) {
            Project project = projectService.findById(projectId);
            if (project.getCreator().equals(currentUser)) {
                projectDto = projectMapper.toDto(project);
            }
        }

        model.addAttribute("user", currentUser);
        model.addAttribute("projectForm", projectDto);
        return "project";
    }

    @PostMapping("/create")
    public String createProject(@ModelAttribute("projectForm") @Valid ProjectDto projectForm,
                                BindingResult bindingResult, Model model) {
        User currentUser = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        if (!bindingResult.hasErrors() && projectService.findByNameOrShortName(projectForm) == null){
            projectForm.setCreator(currentUser);
            return "redirect:/bots/create?project_id=" + projectService.save(projectForm);
        }
        model.addAttribute("user", currentUser);
        return "project";
    }

    @PostMapping("/edit/{id}")
    public ResponseEntity<String> editProject(@PathVariable Long id) {
        //@RequestBody ProjectDto projectDto
        return ResponseEntity.ok("Ok");
    }

    @GetMapping("/all")
    public String listProject(Model model){
        User currentUser = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        //если не организатор то редирект на симуляции (пре авторайзед, на уровне класса)
        model.addAttribute("user", currentUser);
        model.addAttribute("projects", projectService.findByCreator(currentUser));
        return "projects";
    }
}
