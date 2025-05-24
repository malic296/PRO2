package com.RaceForLife.RaceForLife.repository;

import com.RaceForLife.RaceForLife.model.UserRun;
import com.RaceForLife.RaceForLife.model.User;
import com.RaceForLife.RaceForLife.model.Run;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;
import java.util.List;

public interface UserRunRepository extends JpaRepository<UserRun, Long> {
    Optional<UserRun> findByUserAndRun(User user, Run run);
    List<UserRun> findByUser(User user);
    void deleteByUserAndRun(User user, Run run);
    List<UserRun> findByRun(Run run);
}
