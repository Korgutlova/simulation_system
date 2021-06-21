package com.korgutlova.diplom.model.dto;

import java.time.LocalDateTime;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class SpentTimeTaskDto {
    private Long taskId;

    private String description;

    private LocalDateTime startDate;

    private int hours;
}
