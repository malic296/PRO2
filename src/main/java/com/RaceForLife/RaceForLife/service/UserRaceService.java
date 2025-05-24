package com.RaceForLife.RaceForLife.service;
import com.RaceForLife.RaceForLife.model.Race;
import com.RaceForLife.RaceForLife.model.Run;
import com.RaceForLife.RaceForLife.model.User;
import com.RaceForLife.RaceForLife.model.UserRace;
import com.RaceForLife.RaceForLife.repository.RunRepository;
import com.RaceForLife.RaceForLife.repository.UserRaceRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestParam;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.time.LocalDate;
import java.util.stream.Collectors;

@Service
public class UserRaceService {

    @Autowired private UserRaceRepository userRaceRepository;

    public void joinRace(User user, Race race) {
        if (!isUserJoinedRace(user, race)) {
            UserRace ur = new UserRace();
            ur.setUser(user);
            ur.setRace(race);
            userRaceRepository.save(ur);
        }
    }

    public void leaveRace(User user, Race race) {
        userRaceRepository.findByUserAndRace(user, race)
                .ifPresent(userRaceRepository::delete);
    }

    public boolean isUserJoinedRace(User user, Race race) {
        return userRaceRepository.existsByUserAndRace(user, race);
    }

    public List<User> getUsersByRace(Race race) {
        return userRaceRepository.findByRace(race).stream()
                .map(UserRace::getUser)
                .collect(Collectors.toList());
    }

    public List<Long> getRaceIdsJoinedByUser(User user) {
        return userRaceRepository.findByUser(user).stream()
                .map(userRace -> userRace.getRace().getId())
                .collect(Collectors.toList());
    }
}

