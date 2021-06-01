package com.korgutlova.diplom.model.mapper;

import com.korgutlova.diplom.model.dto.ProjectDto;
import com.korgutlova.diplom.model.entity.Project;
import org.mapstruct.Mapper;

@Mapper(componentModel="spring")
public interface ProjectMapper {
    Project toEntity(ProjectDto projectDto);

    ProjectDto toDto(Project project);
}
