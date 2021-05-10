package com.korgutlova.diplom.model.dto;

import com.korgutlova.diplom.model.enums.simulation.CommunicationType;
import com.korgutlova.diplom.model.enums.simulation.TaskDistributionType;
import io.swagger.annotations.ApiModelProperty;
import java.time.LocalDateTime;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class ProjectDto {

    private String name;

    private String description;

    @ApiModelProperty(example = "2021-05-01T09:00:00")
    private LocalDateTime startDate;

    @ApiModelProperty(example = "2021-05-15T18:00:00")
    private LocalDateTime endDate;

    private CommunicationType communicationType;

    private TaskDistributionType taskDistributionType;

    private int workHoursPerWeek;
}
