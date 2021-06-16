package com.korgutlova.diplom.service.api;

import com.korgutlova.diplom.model.dto.MessageDto;
import com.korgutlova.diplom.model.entity.Bot;

public interface ChatService {
    void processingIncomingMessages(MessageDto dto, Bot bot);
}
