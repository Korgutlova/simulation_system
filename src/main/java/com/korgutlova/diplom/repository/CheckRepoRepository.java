package com.korgutlova.diplom.repository;

import com.korgutlova.diplom.model.entity.CheckRepository;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CheckRepoRepository extends CrudRepository<CheckRepository, Long> {

}
