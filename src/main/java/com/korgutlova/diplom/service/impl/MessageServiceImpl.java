package com.korgutlova.diplom.service.impl;

import com.korgutlova.diplom.model.entity.Message;
import com.korgutlova.diplom.repository.MessageRepository;
import com.korgutlova.diplom.service.api.MessageService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class MessageServiceImpl implements MessageService {

    private final MessageRepository messageRepository;

    @Override
    public void save(Message message) {
        messageRepository.save(message);
    }
}
