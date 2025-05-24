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
public class UserRaceController {
    @Autowired
    private RaceService raceService;
    @Autowired private UserService userService;
    @Autowired private UserRaceService userRaceService;
    @PostMapping("/races/toggle")
    public String toggleRace(@RequestParam Long raceId, HttpSession session) {
        String email = (String) session.getAttribute("userEmail");
        if (email == null) return "redirect:/login";

        Optional<Race> raceOpt = raceService.getRaceById(raceId);
        if (raceOpt.isEmpty()) return "redirect:/races";

        User user = userService.findByEmail(email).orElse(null);
        if (user == null) return "redirect:/races";

        Race race = raceOpt.get();
        boolean joined = userRaceService.isUserJoinedRace(user, race);

        if (joined) {
            userRaceService.leaveRace(user, race);
        } else {
            int count = userRaceService.getUsersByRace(race).size();
            if (count < race.getCapacity()) {
                userRaceService.joinRace(user, race);
            }
        }

        return "redirect:/races";
    }
}
