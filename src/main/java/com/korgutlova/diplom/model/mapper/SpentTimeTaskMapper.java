package com.korgutlova.diplom.model.mapper;

import com.korgutlova.diplom.model.dto.SpentTimeTaskDto;
import com.korgutlova.diplom.model.entity.tasktracker.SpentTimeTask;
import com.korgutlova.diplom.service.api.TaskService;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring", uses = { TaskService.class })
public interface SpentTimeTaskMapper {

    @Mapping(source = "taskId", target = "task")
    SpentTimeTask toEntity(SpentTimeTaskDto dto);
}
