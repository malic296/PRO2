package com.RaceForLife.RaceForLife.service;

import com.RaceForLife.RaceForLife.model.*;
import com.RaceForLife.RaceForLife.repository.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Service
public class TeamService {

    @Autowired
    private TeamRepository teamRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private UserRunRepository userRunRepository;

    @Autowired
    private UserRaceRepository userRaceRepository;

    public Map<String, Long> getCompletedActivitiesByTeam() {
        LocalDateTime now = LocalDateTime.now();

        Map<String, Long> counts = Stream.concat(
                        userRunRepository.findAll().stream()
                                .filter(ur -> ur.getRun().getDate().isBefore(now))
                                .map(ur -> ur.getUser()),
                        userRaceRepository.findAll().stream()
                                .filter(ur -> ur.getRace().getDate().isBefore(now))
                                .map(ur -> ur.getUser())
                )
                .filter(user -> user.getTeam() != null)
                .map(user -> user.getTeam().getName())
                .collect(Collectors.groupingBy(Function.identity(), Collectors.counting()));

        teamRepository.findAll().forEach(team ->
                counts.putIfAbsent(team.getName(), 0L)
        );

        return counts;
    }

    public Map<String, Long> getSignedUpActivitiesByTeam() {
        LocalDateTime now = LocalDateTime.now();

        Map<String, Long> counts = Stream.concat(
                        userRunRepository.findAll().stream()
                                .filter(ur -> ur.getRun().getDate().isAfter(now))
                                .map(ur -> ur.getUser()),
                        userRaceRepository.findAll().stream()
                                .filter(ur -> ur.getRace().getDate().isAfter(now))
                                .map(ur -> ur.getUser())
                )
                .filter(user -> user.getTeam() != null)
                .map(user -> user.getTeam().getName())
                .collect(Collectors.groupingBy(Function.identity(), Collectors.counting()));

        teamRepository.findAll().forEach(team ->
                counts.putIfAbsent(team.getName(), 0L)
        );

        return counts;
    }


    public List<Team> getAllTeams() {
        return teamRepository.findAll();
    }
}
