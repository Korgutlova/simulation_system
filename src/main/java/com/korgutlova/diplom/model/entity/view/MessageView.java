package com.korgutlova.diplom.model.entity.view;

import com.korgutlova.diplom.model.entity.Bot;
import com.korgutlova.diplom.model.enums.DirectionMessage;
import java.time.LocalDateTime;
import lombok.Data;

@Data
public class MessageView {
    private String text;

    private LocalDateTime messageCreated;

    private Bot bot;

    private DirectionMessage directionMessage;
}
