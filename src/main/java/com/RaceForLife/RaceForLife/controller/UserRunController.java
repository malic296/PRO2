package com.RaceForLife.RaceForLife.controller;

import com.RaceForLife.RaceForLife.model.Run;
import com.RaceForLife.RaceForLife.model.User;
import com.RaceForLife.RaceForLife.model.UserRun;
import com.RaceForLife.RaceForLife.repository.RunRepository;
import com.RaceForLife.RaceForLife.repository.UserRepository;
import com.RaceForLife.RaceForLife.repository.UserRunRepository;
import com.RaceForLife.RaceForLife.service.UserRunService;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@Controller
@RequestMapping("/runs")
public class UserRunController {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private RunRepository runRepository;

    @Autowired
    private UserRunService userRunService;

    @PostMapping("/toggle")
    public String joinOrLeaveRun(@RequestParam Long runId, HttpSession session) {
        String userEmail = (String) session.getAttribute("userEmail");
        if (userEmail == null) {
            return "redirect:/login";
        }

        Optional<User> optionalUser = userRepository.findByEmail(userEmail);
        Optional<Run> optionalRun = runRepository.findById(runId);

        if (optionalUser.isEmpty() || optionalRun.isEmpty()) {
            return "redirect:/runs"; // Optionally add error message
        }

        userRunService.toggleParticipation(optionalUser.get(), optionalRun.get());

        return "redirect:/runs";
    }
}
