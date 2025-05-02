package com.RaceForLife.RaceForLife.controller;

import com.RaceForLife.RaceForLife.service.UserService;
import com.RaceForLife.RaceForLife.model.User;
import com.RaceForLife.RaceForLife.service.RunService;
import com.RaceForLife.RaceForLife.model.Run;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestParam;
import jakarta.servlet.http.HttpSession;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@Controller
public class RunsController {

    @Autowired
    private UserService userService;

    @Autowired
    private RunService runService;

    @PostMapping("/runs/create")
    public String create(@RequestParam String name, @RequestParam String description, @RequestParam String address, @RequestParam LocalDateTime date, Model model, HttpSession session) {
        runService.createRun(name, description, address, date);
        List<Run> runs = runService.getAllRuns();
        model.addAttribute("allRuns", runs);
        model.addAttribute("content", "runs.html");
        return "main";
    }

    @PostMapping("/runs/edit")
    public String edit(@RequestParam long id, @RequestParam String name, @RequestParam String description, @RequestParam String address, @RequestParam LocalDateTime date, Model model, HttpSession session) {
        runService.editRun(id, name, description, address, date);
        List<Run> runs = runService.getAllRuns();
        model.addAttribute("allRuns", runs);
        model.addAttribute("content", "runs.html");
        return "main";
    }

    @PostMapping("/runs/delete")
    public String delete(@RequestParam long runId, Model model, HttpSession session) {
        runService.deleteRun(runId);
        List<Run> runs = runService.getAllRuns();
        model.addAttribute("allRuns", runs);
        model.addAttribute("content", "runs.html");
        return "main";
    }

    @PostMapping("/runs/action")
    public String action(@RequestParam int runId, Model model, HttpSession session) {

        List<Run> runs = runService.getAllRuns();
        model.addAttribute("allRuns", runs);
        model.addAttribute("content", "runs.html");
        return "main";
    }
}
