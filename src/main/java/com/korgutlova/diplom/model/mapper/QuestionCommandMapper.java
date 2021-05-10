package com.korgutlova.diplom.model.mapper;

import com.korgutlova.diplom.model.dto.QuestionCommandDto;
import com.korgutlova.diplom.model.entity.question.QuestionCommand;
import com.korgutlova.diplom.service.api.ProjectService;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring", uses = {ProjectService.class})
public interface QuestionCommandMapper {

    @Mapping(source = "projectId", target = "project")
    QuestionCommand toEntity(QuestionCommandDto dto);
}
