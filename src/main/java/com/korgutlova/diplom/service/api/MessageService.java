package com.korgutlova.diplom.service.api;

import com.korgutlova.diplom.model.entity.Bot;
import com.korgutlova.diplom.model.entity.Message;
import com.korgutlova.diplom.model.entity.Simulation;
import java.util.List;
import java.util.Set;

public interface MessageService {
    void save(Message message);

    Message findLastMessage(Simulation simulation);

    List<Message> findMessages(Simulation currentSim, Bot bot);

    Set<Bot> findActiveBots(Simulation currentSim);
}
