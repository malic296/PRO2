package com.RaceForLife.RaceForLife.controller;

import com.RaceForLife.RaceForLife.model.Run;
import com.RaceForLife.RaceForLife.model.User;
import com.RaceForLife.RaceForLife.service.RunService;
import com.RaceForLife.RaceForLife.service.UserRunService;
import com.RaceForLife.RaceForLife.service.UserService;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.time.format.DateTimeParseException;
import java.util.List;
import java.util.Optional;

@Controller
public class RunsController {

    @Autowired
    private UserService userService;

    @Autowired
    private RunService runService;

    @Autowired
    private UserRunService userRunService;

    @GetMapping("/runs")
    public String showRuns(Model model, HttpSession session) {
        String userEmail = (String) session.getAttribute("userEmail");
        if (userEmail == null) {
            return "redirect:/login";
        }

        Optional<User> optionalUser = userService.findByEmail(userEmail);
        if (optionalUser.isEmpty()) {
            return "redirect:/login";
        }

        User user = optionalUser.get();
        List<Run> allRuns = runService.getUpcomingRuns();
        List<Long> joinedRunIds = userRunService.getRunIdsJoinedByUser(user);

        model.addAttribute("allRuns", allRuns);
        model.addAttribute("joinedRunIds", joinedRunIds);
        model.addAttribute("userEmail", userEmail);
        model.addAttribute("isAdmin", session.getAttribute("isAdmin"));
        model.addAttribute("content", "runs.html");

        return "main";
    }

    @PostMapping("/runs/create")
    public String createRun(@RequestParam String name,
                            @RequestParam String description,
                            @RequestParam String address,
                            @RequestParam String date, // ISO date-time string
                            HttpSession session, Model model) {
        String userEmail = (String) session.getAttribute("userEmail");
        if (userEmail == null) {
            return "redirect:/login";
        }

        LocalDateTime runDate;
        try {
            runDate = LocalDateTime.parse(date);
        } catch (DateTimeParseException e) {
            // Invalid date format, reload runs page or show error
            model.addAttribute("error", "Invalid date format.");
            return reloadRuns(model, session);
        }

        if (!runDate.isAfter(LocalDateTime.now())) {
            // Date is not in the future, fail silently or show error
            model.addAttribute("error", "Run date must be in the future.");
            return reloadRuns(model, session);
        }

        runService.createRun(name, description, address, runDate, userEmail);
        return reloadRuns(model, session);
    }


    @PostMapping("/runs/edit")
    public String editRun(@RequestParam long id,
                          @RequestParam String name,
                          @RequestParam String description,
                          @RequestParam String address,
                          @RequestParam String date, // Changed to String for simplicity in form binding
                          HttpSession session, Model model) {
        LocalDateTime runDate = LocalDateTime.parse(date); // Assumes valid ISO date-time string
        runService.editRun(id, name, description, address, runDate);
        return reloadRuns(model, session);
    }

    @PostMapping("/runs/delete")
    public String deleteRun(@RequestParam long runId,
                            HttpSession session, Model model) {
        runService.deleteRun(runId);
        return reloadRuns(model, session);
    }

    @PostMapping("/runs/action")
    public String toggleParticipation(@RequestParam int runId,
                                      HttpSession session, Model model) {
        String userEmail = (String) session.getAttribute("userEmail");
        if (userEmail == null) {
            return "redirect:/login";
        }

        Optional<User> userOpt = userService.findByEmail(userEmail);
        Optional<Run> runOpt = runService.getRunById((long) runId);

        if (userOpt.isPresent() && runOpt.isPresent()) {
            userRunService.toggleParticipation(userOpt.get(), runOpt.get());
        }

        return reloadRuns(model, session);
    }

    @PostMapping("/runs/viewParticipants")
    public String viewParticipants(@RequestParam long runId, Model model, HttpSession session) {
        String userEmail = (String) session.getAttribute("userEmail");
        if (userEmail == null) return "redirect:/login";

        Optional<Run> optionalRun = runService.getRunById(runId);
        if (optionalRun.isEmpty()) return "redirect:/runs";

        Run run = optionalRun.get();
        List<User> participants = userRunService.getUsersByRun(run);

        List<Run> allRuns = runService.getUpcomingRuns();
        List<Long> joinedRunIds = userRunService.getRunIdsJoinedByUser(userService.findByEmail(userEmail).get());

        model.addAttribute("allRuns", allRuns);
        model.addAttribute("joinedRunIds", joinedRunIds);
        model.addAttribute("selectedRun", run);
        model.addAttribute("participants", participants);
        model.addAttribute("userEmail", userEmail);
        model.addAttribute("isAdmin", session.getAttribute("isAdmin"));
        model.addAttribute("content", "runs.html");

        return "main"; // triggers runs.html inside main.html
    }


    private String reloadRuns(Model model, HttpSession session) {
        String userEmail = (String) session.getAttribute("userEmail");

        if (userEmail == null) {
            return "redirect:/login";
        }

        Optional<User> userOpt = userService.findByEmail(userEmail);
        if (userOpt.isEmpty()) {
            return "redirect:/login";
        }

        User user = userOpt.get();
        List<Run> allRuns = runService.getUpcomingRuns();
        List<Long> joinedRunIds = userRunService.getRunIdsJoinedByUser(user);

        model.addAttribute("allRuns", allRuns);
        model.addAttribute("joinedRunIds", joinedRunIds);
        model.addAttribute("userEmail", userEmail);
        model.addAttribute("isAdmin", session.getAttribute("isAdmin"));
        model.addAttribute("content", "runs.html");

        return "main";
    }
}
