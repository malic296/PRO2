package com.RaceForLife.RaceForLife.controller;

import com.RaceForLife.RaceForLife.model.Team;
import com.RaceForLife.RaceForLife.service.TeamService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@Controller
public class TeamController {

    @Autowired
    private TeamService teamService;

    @GetMapping("/teams")
    public String showTeamStats(Model model) {
        model.addAttribute("teams", teamService.getAllTeams());
        model.addAttribute("content", "teams.html");
        model.addAttribute("completedStats", teamService.getCompletedActivitiesByTeam());
        model.addAttribute("upcomingStats", teamService.getSignedUpActivitiesByTeam());
        return "main"; // your HTML template name (teams.html)
    }
}

