package com.korgutlova.diplom.controller;

import com.korgutlova.diplom.model.dto.ProjectDto;
import com.korgutlova.diplom.model.entity.Project;
import com.korgutlova.diplom.model.entity.Simulation;
import com.korgutlova.diplom.model.entity.User;
import com.korgutlova.diplom.model.enums.simulation.StatusProject;
import com.korgutlova.diplom.model.mapper.ProjectMapper;
import com.korgutlova.diplom.service.api.ProjectService;
import com.korgutlova.diplom.service.api.SimulationService;
import java.util.List;
import java.util.stream.Collectors;
import javax.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/project")
@RequiredArgsConstructor
public class ProjectController {

    private final ProjectService projectService;
    private final SimulationService simulationService;
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

        if (!bindingResult.hasErrors()) {
            Project project = projectService.findByNameOrShortName(projectForm);
            if (project == null ||
                    (project.getCreator().equals(currentUser) &&
                            project.getStatus() == StatusProject.DRAFT)) {
                if (project != null) {
                    projectForm.setId(project.getId());
                }
                projectForm.setCreator(currentUser);
                return "redirect:/bots/create?project_id=" + projectService.save(projectForm);
            }
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
    public String listProject(Model model) {
        User currentUser = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();

        List<Project> projects;
        if (currentUser.isOrganizer()) {
            projects = projectService.findByCreator(currentUser);
        } else {
            List<Simulation> simulations = simulationService.findSimulations(currentUser);
            projects = projectService.findAllActive();
            projects.removeAll(simulations
                    .stream()
                    .map(Simulation::getProject)
                    .collect(Collectors.toList())
            );
            model.addAttribute("simulations", simulations);
        }

        model.addAttribute("user", currentUser);
        model.addAttribute("projects", projects);

        return "projects";
    }
}
