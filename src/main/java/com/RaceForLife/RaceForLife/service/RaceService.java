package com.RaceForLife.RaceForLife.service;

import com.RaceForLife.RaceForLife.model.Race;
import com.RaceForLife.RaceForLife.model.Run;
import com.RaceForLife.RaceForLife.repository.RaceRepository;
import com.RaceForLife.RaceForLife.repository.RunRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestParam;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.time.LocalDate;
@Service
public class RaceService {

    @Autowired
    private RaceRepository raceRepository;

    public List<Race> getUpcomingRaces() {
        return raceRepository.findAllByDateAfterOrderByDateAsc(LocalDateTime.now());
    }

    public Race createRace(Race race) {
        if (race.getDate().isBefore(LocalDateTime.now())) {
            throw new IllegalArgumentException("Cannot create a race in the past.");
        }
        return raceRepository.save(race);
    }

    public Optional<Race> getRaceById(Long id) {
        return raceRepository.findById(id);
    }

    public void deleteRace(Long id) {
        raceRepository.deleteById(id);
    }

    public Race updateRace(Race updated) {
        if (updated.getDate().isBefore(LocalDateTime.now())) {
            throw new IllegalArgumentException("Cannot set a race date to the past.");
        }
        return raceRepository.save(updated);
    }
}

