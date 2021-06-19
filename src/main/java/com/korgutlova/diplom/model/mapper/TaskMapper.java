package com.korgutlova.diplom.model.mapper;

import com.korgutlova.diplom.model.entity.tasktracker.TaskInSimulation;
import com.korgutlova.diplom.model.entity.view.TaskView;
import java.util.List;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface TaskMapper {

    @Mapping(source = "taskInSimulation.task.description", target = "description")
    @Mapping(source = "taskInSimulation.task.name", target = "name")
    @Mapping(source = "taskInSimulation.task.creator", target = "creator")
    @Mapping(source = "taskInSimulation.task.priority", target = "priority")
    @Mapping(source = "taskInSimulation.task.severity", target = "severity")
    @Mapping(source = "taskInSimulation.task.typeTask", target = "typeTask")
    @Mapping(source = "taskInSimulation.task.shortId", target = "number")
    TaskView toView(TaskInSimulation taskInSimulation);

    List<TaskView> toView(List<TaskInSimulation> taskInSimulationList);
}
