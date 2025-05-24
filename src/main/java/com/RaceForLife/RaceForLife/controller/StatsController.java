package com.RaceForLife.RaceForLife.controller;

import com.RaceForLife.RaceForLife.model.Run;
import com.RaceForLife.RaceForLife.model.Team;
import com.RaceForLife.RaceForLife.model.User;
import com.RaceForLife.RaceForLife.repository.RaceRepository;
import com.RaceForLife.RaceForLife.repository.RunRepository;
import com.RaceForLife.RaceForLife.repository.UserRepository;
import com.RaceForLife.RaceForLife.service.RunService;
import com.RaceForLife.RaceForLife.service.UserRunService;
import com.RaceForLife.RaceForLife.service.UserService;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
@Controller
public class StatsController {

    @Autowired
    private RunRepository runRepository;

    @Autowired
    private RaceRepository raceRepository;

    @Autowired
    private UserRepository userRepository;

    @GetMapping("/myStats")
    public String getStats(HttpSession session, Model model) {
        String userEmail = (String) session.getAttribute("userEmail");
        if (userEmail == null) {
            return "redirect:/login";
        }

        LocalDateTime now = LocalDateTime.now();

        int completed = runRepository.countCompletedByUserEmail(userEmail, now)
                + raceRepository.countCompletedByUserEmail(userEmail, now);

        int signedUp = runRepository.countUpcomingByUserEmail(userEmail, now)
                + raceRepository.countUpcomingByUserEmail(userEmail, now);

        Optional<User> optionalUser = userRepository.findByEmail(userEmail);

        String usersTeam = optionalUser
                .map(user -> user.getTeam() != null ? user.getTeam().getName() : "No team")
                .orElse("Unknown");


        model.addAttribute("finishedActivities", completed);
        model.addAttribute("signedUpActivities", signedUp);
        model.addAttribute("usersTeam", usersTeam);
        model.addAttribute("content", "myStats.html");

        return "main";
    }
}
