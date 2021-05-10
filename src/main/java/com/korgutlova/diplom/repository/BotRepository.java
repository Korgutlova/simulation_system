package com.korgutlova.diplom.repository;

import com.korgutlova.diplom.model.entity.Bot;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface BotRepository extends CrudRepository<Bot, Long> {
}
