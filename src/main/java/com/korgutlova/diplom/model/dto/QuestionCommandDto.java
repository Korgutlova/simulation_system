package com.korgutlova.diplom.model.dto;

import com.korgutlova.diplom.model.enums.roles.TeamRole;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class QuestionCommandDto {
    private String question;

    private String answer;

    private String command;

    private Long projectId;

    private TeamRole questionTopic;
}
