package com.korgutlova.diplom.repository;

import com.korgutlova.diplom.model.entity.Project;
import com.korgutlova.diplom.model.entity.User;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ProjectRepository extends CrudRepository<Project, Long> {
    List<Project> findByCreator(User currentUser);

    Optional<Project> findByShortNameOrName(String shortName, String name);

//    TODO
//    List<Project> findAllByStartDateBeforeAndEndDateAfter(LocalDate currentDate, LocalDate current);
}
