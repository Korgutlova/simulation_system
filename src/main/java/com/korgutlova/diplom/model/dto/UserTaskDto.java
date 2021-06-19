package com.korgutlova.diplom.model.dto;

import com.korgutlova.diplom.model.enums.task.TaskStatus;
import lombok.Data;
import lombok.NoArgsConstructor;

import static com.korgutlova.diplom.model.enums.task.TaskStatus.IN_PROGRESS;
import static com.korgutlova.diplom.model.enums.task.TaskStatus.REVIEW;

@Data
@NoArgsConstructor
public class UserTaskDto {
    private Long id;

    private TaskStatus status;

    public UserTaskDto(Long id, TaskStatus status) {
        this.id = id;
        switch (status) {
            case IN_PROGRESS:
                this.status = REVIEW;
                break;
            case TO_DO:
                this.status = IN_PROGRESS;
                break;
            default:
                break;
        }
    }
}
