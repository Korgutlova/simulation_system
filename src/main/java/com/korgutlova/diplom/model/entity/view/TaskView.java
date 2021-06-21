package com.korgutlova.diplom.model.entity.view;

import com.korgutlova.diplom.model.entity.Bot;
import com.korgutlova.diplom.model.enums.task.Priority;
import com.korgutlova.diplom.model.enums.task.Severity;
import com.korgutlova.diplom.model.enums.task.TaskStatus;
import com.korgutlova.diplom.model.enums.task.TypeTask;
import java.time.LocalDateTime;
import lombok.Data;

@Data
public class TaskView {

    //берем из taskSim
    private Long id;

    private String number;

    private String name;

    private String description;

    private LocalDateTime dueDate;

    private Bot creator;

    private Severity severity;

    private Priority priority;

    private TypeTask typeTask;

    private TaskStatus status;
}
