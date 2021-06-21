package com.korgutlova.diplom.model.dto;

import com.korgutlova.diplom.model.entity.User;
import com.korgutlova.diplom.model.enums.simulation.CommunicationType;
import com.korgutlova.diplom.model.enums.simulation.TaskDistributionType;
import io.swagger.annotations.ApiModelProperty;
import java.time.LocalDate;
import java.time.LocalDateTime;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;

@Data
@NoArgsConstructor
public class ProjectDto {

    private Long id;

    private String shortName;

    private String name;

    private String description;

    private String language;

    private String version;

    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private LocalDate startDate;

    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private LocalDate endDate;

    private CommunicationType communicationType = CommunicationType.WITHOUT_COMM;

    private TaskDistributionType taskDistributionType = TaskDistributionType.STANDART;

    private Integer workHoursPerWeek;

    private User creator;
}
