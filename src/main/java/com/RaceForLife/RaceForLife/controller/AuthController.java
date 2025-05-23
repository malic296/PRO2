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
public class AuthController {

    @Autowired
    private UserService userService;

    @Autowired
    private RunService runService;

    @GetMapping("/")
    public String redirectToLogin() {
        return "redirect:/login";
    }

    @GetMapping("/login")
    public String loginPage() {
        return "login";
    }

    @PostMapping("/login")
    public String login(@RequestParam String email, @RequestParam String password, Model model, HttpSession session) {
        boolean isAuthenticated = userService.authenticateUser(email, password);
        if (isAuthenticated) {
            model.addAttribute("content", "myStats.html");
            session.setAttribute("userEmail", email);
            if(userService.isAdmin(email)){
                session.setAttribute("isAdmin", true);
            }
            else{
                session.setAttribute("isAdmin", false);
            }
            return "main";
        } else {
            model.addAttribute("error", "Invalid credentials");
            return "login";
        }
    }

    @GetMapping("/register")
    public String registerPage() {
        return "register";
    }

    @PostMapping("/register")
    public String register(@RequestParam String email, @RequestParam String password, @RequestParam String confirmPassword, @RequestParam int team, Model model, HttpSession session) {
        System.out.println("register");
        if(!confirmPassword.equals(password)){
            model.addAttribute("error", "Passwords are not the same.");
            return "register";
        }
        try {
            userService.registerUser(email, password, team);
            session.setAttribute("userEmail", email);
            session.setAttribute("isAdmin", false);
            model.addAttribute("content", "myStats.html");
            return "main";
        } catch (RuntimeException e) {
            model.addAttribute("error", e.getMessage());
            return "register";
        }
    }

    @GetMapping("/main")
    public String main() {
        return "main";
    }

    @GetMapping("/races")
    public String races(HttpSession session, Model model) {
        String userEmail = (String) session.getAttribute("userEmail");
        if (userEmail != null) {
            model.addAttribute("content", "races.html");
            return "main";
        } else {
            return "redirect:/login";
        }
    }

    @GetMapping("/teams")
    public String teams(HttpSession session, Model model) {
        String userEmail = (String) session.getAttribute("userEmail");
        if (userEmail != null) {
            model.addAttribute("content", "teams.html");
            return "main";
        } else {
            return "redirect:/login";
        }
    }

    @GetMapping("/myStats")
    public String myStats(HttpSession session, Model model) {
        String userEmail = (String) session.getAttribute("userEmail");
        if (userEmail != null) {
            model.addAttribute("content", "myStats.html");
            return "main";
        } else {
            return "redirect:/login";
        }
    }

    @PostMapping("/logout")
    public String logout(HttpSession session) {
        session.invalidate();
        return "redirect:/login";
    }
}
