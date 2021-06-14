package com.korgutlova.diplom.model.dto;

import com.korgutlova.diplom.model.enums.roles.TeamRole;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class BotDto {
    private Long id;

    private Long userId;

    private String login;

    private String firstName;

    private String lastName;

    private String thirdName;

    private TeamRole role;
}
