package com.korgutlova.diplom.service.api;

import com.korgutlova.diplom.model.dto.ProjectDto;
import com.korgutlova.diplom.model.entity.Project;
import com.korgutlova.diplom.model.entity.User;
import java.util.List;

public interface ProjectService {
    Long save(ProjectDto projectDto);

    Project findById(Long id);

    List<Project> findByCreator(User currentUser);

    Project findByNameOrShortName(ProjectDto projectForm);
}
