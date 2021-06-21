package com.korgutlova.diplom.service.impl;

import com.korgutlova.diplom.model.dto.ProjectDto;
import com.korgutlova.diplom.model.entity.Project;
import com.korgutlova.diplom.model.entity.User;
import com.korgutlova.diplom.model.mapper.ProjectMapper;
import com.korgutlova.diplom.repository.ProjectRepository;
import com.korgutlova.diplom.service.api.ProjectService;
import java.time.LocalDate;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ProjectServiceImpl implements ProjectService {

    private final ProjectRepository projectRepository;
    private final ProjectMapper projectMapper;

    @Override
    public Long save(ProjectDto projectDto) {
        Project project = projectMapper.toEntity(projectDto);
        projectRepository.save(project);
        return project.getId();
    }

    @Override
    public Project findById(Long id) {
        return projectRepository.findById(id).get();
    }

    @Override
    public List<Project> findByCreator(User currentUser) {
        return projectRepository.findByCreator(currentUser);
    }

    @Override
    public Project findByNameOrShortName(ProjectDto projectForm) {
        return projectRepository
                .findByShortNameOrName(projectForm.getShortName(), projectForm.getName())
                .orElse(null);
    }

    @Override
    public List<Project> findAllActive() {
        return (List<Project>) projectRepository.findAll();
    }
}
