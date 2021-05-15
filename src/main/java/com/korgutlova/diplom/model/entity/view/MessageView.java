package com.korgutlova.diplom.model.entity.view;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.korgutlova.diplom.model.entity.Bot;
import com.korgutlova.diplom.model.enums.DirectionMessage;
import java.time.LocalDateTime;
import lombok.Data;

@Data
public class MessageView {
    private String text;

    @JsonFormat(pattern = "HH:mm:ss dd.MM.YY")
    private LocalDateTime messageCreated;

    private Bot bot;

    private DirectionMessage directionMessage;

    public boolean isFromBot(){
        return directionMessage == DirectionMessage.BOT_TO_USER;
    }
}
