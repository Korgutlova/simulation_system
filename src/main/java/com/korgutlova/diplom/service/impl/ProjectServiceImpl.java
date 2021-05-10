package com.korgutlova.diplom.service.impl;

import com.korgutlova.diplom.model.dto.ProjectDto;
import com.korgutlova.diplom.model.entity.Project;
import com.korgutlova.diplom.model.mapper.ProjectMapper;
import com.korgutlova.diplom.repository.ProjectRepository;
import com.korgutlova.diplom.service.api.ProjectService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ProjectServiceImpl implements ProjectService {

    private final ProjectRepository projectRepository;
    private final ProjectMapper projectMapper;

    @Override
    public void save(ProjectDto projectDto) {
        Project project = projectMapper.toEntity(projectDto);
        projectRepository.save(project);
    }

    @Override
    public Project findById(Long id) {
        return projectRepository.findById(id).get();
    }
}
