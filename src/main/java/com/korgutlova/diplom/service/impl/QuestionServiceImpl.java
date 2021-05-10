package com.korgutlova.diplom.service.impl;

import com.korgutlova.diplom.exception.QuestionNotFound;
import com.korgutlova.diplom.model.dto.QuestionCommandDto;
import com.korgutlova.diplom.model.entity.question.QuestionCommand;
import com.korgutlova.diplom.model.mapper.QuestionCommandMapper;
import com.korgutlova.diplom.repository.QuestionCommandRepository;
import com.korgutlova.diplom.service.api.QuestionService;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class QuestionServiceImpl implements QuestionService {
    private final QuestionCommandRepository questionCommandRepository;
    private final QuestionCommandMapper questionCommandMapper;

    @Override
    public String findQuestionByCommand(String command) {
        Optional<QuestionCommand> question = questionCommandRepository.findByCommand(command);
        if (question.isPresent()) {
            return question.get().getAnswer();
        }
        throw new QuestionNotFound("Вопрос по команде " + command + " не найден");
    }

    @Override
    public void create(QuestionCommandDto questionCommandDto) {
        questionCommandRepository.save(questionCommandMapper.toEntity(questionCommandDto));
    }
}
