package com.korgutlova.diplom.model.dto;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class MessageDto {

    private String text;

    private Long botId;

    //simulationId будет автоматом ставится
    //direct message тоже
}
