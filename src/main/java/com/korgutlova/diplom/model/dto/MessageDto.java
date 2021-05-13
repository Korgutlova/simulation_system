package com.korgutlova.diplom.model.dto;

import com.korgutlova.diplom.model.entity.User;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class MessageDto {

    private String text;

    private User user;

    //simulationId будет автоматом ставится
    //direct message тоже
}
