package com.korgutlova.diplom.model.dto;

import com.korgutlova.diplom.model.enums.task.TaskStatus;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class UserTaskDto {
    private Long id;

    private TaskStatus status;
}
