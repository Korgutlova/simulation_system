package com.korgutlova.diplom.service.api;

import com.korgutlova.diplom.model.dto.ProjectDto;
import com.korgutlova.diplom.model.entity.Project;

public interface ProjectService {
    void save(ProjectDto projectDto);

    Project findById(Long id);

}
