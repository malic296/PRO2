package com.RaceForLife.RaceForLife.controller;

import com.RaceForLife.RaceForLife.model.Race;
import com.RaceForLife.RaceForLife.model.Run;
import com.RaceForLife.RaceForLife.model.User;
import com.RaceForLife.RaceForLife.service.*;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
@Controller
public class RaceController {

    @Autowired private RaceService raceService;
    @Autowired private UserService userService;
    @Autowired private UserRaceService userRaceService;

    @GetMapping("/races")
    public String showRaces(Model model, HttpSession session) {
        return loadRacesPage(model, session);
    }

    @PostMapping("/races/create")
    public String createRace(@ModelAttribute Race race, HttpSession session, Model model) {
        if (!Boolean.TRUE.equals(session.getAttribute("isAdmin"))) {
            return loadRacesPage(model,session);
        }

        if (race.getDate() == null || !race.getDate().isAfter(LocalDateTime.now())) {
            model.addAttribute("error", "Race date must be in the future.");
            return loadRacesPage(model,session);
        }

        race.setCreator((String) session.getAttribute("userEmail"));
        raceService.createRace(race);
        return loadRacesPage(model,session);
    }

    private String loadRacesPage(Model model, HttpSession session) {
        String userEmail = (String) session.getAttribute("userEmail");
        if (userEmail == null) return "redirect:/login";

        User user = userService.findByEmail(userEmail).orElseThrow();
        boolean isAdmin = (boolean) session.getAttribute("isAdmin");

        List<Race> allRaces = raceService.getUpcomingRaces(); // or findAll()
        List<Long> joinedRaceIds = userRaceService.getRaceIdsJoinedByUser(user);
        Map<Long, List<User>> participantsMap = new HashMap<>();
        for (Race race : allRaces) {
            participantsMap.put(race.getId(), userRaceService.getUsersByRace(race));
        }

        model.addAttribute("userEmail", userEmail);
        model.addAttribute("isAdmin", isAdmin);
        model.addAttribute("allRaces", allRaces);
        model.addAttribute("joinedRaceIds", joinedRaceIds);
        model.addAttribute("participantsMap", participantsMap);
        model.addAttribute("content", "races.html");

        return "main"; // or "races" depending on your layout structure
    }


    @PostMapping("/races/edit")
    public String editRace(@ModelAttribute Race race, HttpSession session) {
        if (!Boolean.TRUE.equals(session.getAttribute("isAdmin"))) return "redirect:/races";
        raceService.updateRace(race);
        return "redirect:/races";
    }

    @PostMapping("/races/delete")
    public String deleteRace(@RequestParam Long raceId, HttpSession session) {
        if (!Boolean.TRUE.equals(session.getAttribute("isAdmin"))) return "redirect:/races";
        raceService.deleteRace(raceId);
        return "redirect:/races";
    }

    @PostMapping("/races/viewParticipants")
    public String viewParticipants(@RequestParam long raceId, Model model, HttpSession session) {
        String userEmail = (String) session.getAttribute("userEmail");
        if (userEmail == null) return "redirect:/login";

        Optional<Race> optionalRace = raceService.getRaceById(raceId);
        if (optionalRace.isEmpty()) return "redirect:/races";

        Race race = optionalRace.get();
        List<User> participants = userRaceService.getUsersByRace(race);

        List<Race> allRaces = raceService.getUpcomingRaces();
        List<Long> joinedRaceIds = userRaceService.getRaceIdsJoinedByUser(userService.findByEmail(userEmail).get());

        Map<Long, List<User>> participantsMap = new HashMap<>();
        for (Race r : allRaces) {
            participantsMap.put(r.getId(), userRaceService.getUsersByRace(r));
        }

        model.addAttribute("allRaces", allRaces);
        model.addAttribute("joinedRaceIds", joinedRaceIds);
        model.addAttribute("selectedRace", race); // This triggers participants modal
        model.addAttribute("participants", participants);
        model.addAttribute("participantsMap", participantsMap);
        model.addAttribute("userEmail", userEmail);
        model.addAttribute("isAdmin", session.getAttribute("isAdmin"));
        model.addAttribute("content", "races.html");

        return "main";
    }


}
