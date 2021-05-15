package com.korgutlova.diplom.service.api;

import com.korgutlova.diplom.model.dto.MessageDto;
import com.korgutlova.diplom.model.entity.Bot;
import com.korgutlova.diplom.model.entity.Message;
import com.korgutlova.diplom.model.entity.Simulation;
import com.korgutlova.diplom.model.entity.User;
import com.korgutlova.diplom.model.entity.view.MessageView;
import java.util.List;
import java.util.Set;

public interface MessageService {
    void save(Message message);

    Message findLastMessage(Simulation simulation);

    List<Message> findMessages(Simulation currentSim, Bot bot);

    Set<Bot> findActiveBots(Simulation currentSim);

    MessageView createMessage(MessageDto messageDto, Bot bot);

    MessageView createAnswerMessage(String answer, Bot bot, User user);
}
