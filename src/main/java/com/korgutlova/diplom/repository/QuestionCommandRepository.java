package com.korgutlova.diplom.repository;

import com.korgutlova.diplom.model.entity.question.QuestionCommand;
import java.util.Optional;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface QuestionCommandRepository extends CrudRepository<QuestionCommand, Long> {
    Optional<QuestionCommand> findByCommand(String command);
}
