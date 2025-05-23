package com.RaceForLife.RaceForLife.service;

import com.RaceForLife.RaceForLife.model.Run;
import com.RaceForLife.RaceForLife.model.User;
import com.RaceForLife.RaceForLife.model.UserRun;
import com.RaceForLife.RaceForLife.repository.UserRunRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class UserRunService {

    @Autowired
    private UserRunRepository userRunRepository;

    public boolean toggleParticipation(User user, Run run) {
        Optional<UserRun> existing = userRunRepository.findByUserAndRun(user, run);
        if (existing.isPresent()) {
            userRunRepository.delete(existing.get());
            return false; // Indicates "left"
        } else {
            UserRun userRun = new UserRun();
            userRun.setUser(user);
            userRun.setRun(run);
            userRunRepository.save(userRun);
            return true; // Indicates "joined"
        }
    }

    public boolean isUserJoinedRun(User user, Run run) {
        return userRunRepository.findByUserAndRun(user, run).isPresent();
    }

    public List<Long> getRunIdsJoinedByUser(User user) {
        return userRunRepository.findByUser(user).stream()
                .map(userRun -> userRun.getRun().getId())
                .toList();
    }
}
