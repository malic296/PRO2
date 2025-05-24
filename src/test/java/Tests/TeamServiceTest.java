package com.RaceForLife.RaceForLife.service;

import com.RaceForLife.RaceForLife.model.*;
import com.RaceForLife.RaceForLife.repository.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.*;

import java.time.LocalDateTime;
import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class TeamServiceTest {

    @Mock private TeamRepository teamRepository;
    @Mock private UserRepository userRepository;
    @Mock private UserRunRepository userRunRepository;
    @Mock private UserRaceRepository userRaceRepository;

    @InjectMocks private TeamService teamService;

    private Team teamA, teamB;
    private User user1, user2;
    private Run pastRun, futureRun;
    private Race pastRace, futureRace;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);

        teamA = new Team(); teamA.setName("Team A");
        teamB = new Team(); teamB.setName("Team B");

        user1 = new User(); user1.setEmail("user1@example.com"); user1.setTeam(teamA);
        user2 = new User(); user2.setEmail("user2@example.com"); user2.setTeam(teamB);

        pastRun = new Run(); pastRun.setDate(LocalDateTime.now().minusDays(2));
        futureRun = new Run(); futureRun.setDate(LocalDateTime.now().plusDays(3));

        pastRace = new Race(); pastRace.setDate(LocalDateTime.now().minusDays(2));
        futureRace = new Race(); futureRace.setDate(LocalDateTime.now().plusDays(3));
    }


    @Test
    void getCompletedActivitiesByTeam_ShouldCountPastRunAndRace() {
        UserRun userRun = new UserRun(); userRun.setUser(user1); userRun.setRun(pastRun);
        UserRace userRace = new UserRace(); userRace.setUser(user2); userRace.setRace(pastRace);

        when(userRunRepository.findAll()).thenReturn(List.of(userRun));
        when(userRaceRepository.findAll()).thenReturn(List.of(userRace));
        when(teamRepository.findAll()).thenReturn(List.of(teamA, teamB));

        Map<String, Long> result = teamService.getCompletedActivitiesByTeam();

        assertEquals(2, result.size());
        assertEquals(1L, result.get("Team A"));
        assertEquals(1L, result.get("Team B"));
    }

    @Test
    void getSignedUpActivitiesByTeam_ShouldCountFutureRunAndRace() {
        UserRun userRun = new UserRun(); userRun.setUser(user1); userRun.setRun(futureRun);
        UserRace userRace = new UserRace(); userRace.setUser(user2); userRace.setRace(futureRace);

        when(userRunRepository.findAll()).thenReturn(List.of(userRun));
        when(userRaceRepository.findAll()).thenReturn(List.of(userRace));
        when(teamRepository.findAll()).thenReturn(List.of(teamA, teamB));

        Map<String, Long> result = teamService.getSignedUpActivitiesByTeam();

        assertEquals(2, result.size());
        assertEquals(1L, result.get("Team A"));
        assertEquals(1L, result.get("Team B"));
    }

    @Test
    void getCompletedActivitiesByTeam_ShouldDefaultToZeroForTeamsWithNoActivity() {
        when(userRunRepository.findAll()).thenReturn(Collections.emptyList());
        when(userRaceRepository.findAll()).thenReturn(Collections.emptyList());
        when(teamRepository.findAll()).thenReturn(List.of(teamA, teamB));

        Map<String, Long> result = teamService.getCompletedActivitiesByTeam();

        assertEquals(2, result.size());
        assertEquals(0L, result.get("Team A"));
        assertEquals(0L, result.get("Team B"));
    }

    @Test
    void getAllTeams_ShouldReturnAllTeams() {
        when(teamRepository.findAll()).thenReturn(List.of(teamA, teamB));

        List<Team> result = teamService.getAllTeams();

        assertEquals(2, result.size());
        assertTrue(result.contains(teamA));
        assertTrue(result.contains(teamB));
    }
}
