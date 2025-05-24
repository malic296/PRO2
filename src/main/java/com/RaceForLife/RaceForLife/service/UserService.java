package com.RaceForLife.RaceForLife.service;

import com.RaceForLife.RaceForLife.model.Team;
import com.RaceForLife.RaceForLife.model.User;
import com.RaceForLife.RaceForLife.repository.TeamRepository;
import com.RaceForLife.RaceForLife.repository.UserRepository;
import com.RaceForLife.RaceForLife.util.PasswordUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class UserService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private TeamRepository teamRepository;

    public User registerUser(String email, String password, int teamId) {
        if (userRepository.findByEmail(email).isPresent()) {
            throw new RuntimeException("Email already in use.");
        }

        Optional<Team> teamOptional = teamRepository.findById((long) teamId);
        if (teamOptional.isEmpty()) {
            throw new RuntimeException("Invalid team ID.");
        }

        User user = new User();
        user.setEmail(email);
        user.setEncryptedPassword(PasswordUtils.hashPassword(password));
        user.setAdmin(false);
        user.setTeam(teamOptional.get()); // Set the actual Team object

        return userRepository.save(user);
    }


    public boolean authenticateUser(String email, String password) {
        Optional<User> userOptional = userRepository.findByEmail(email);
        if (userOptional.isEmpty()) {
            return false;
        }

        User user = userOptional.get();
        return PasswordUtils.verifyPassword(password, user.getEncryptedPassword());
    }

    public boolean isAdmin(String email){
        Optional<User> userOptional = userRepository.findByEmail(email);
        User user = userOptional.get();
        if(user.isAdmin()){
            return true;
        }
        return false;

    };

    public Optional<User> findByEmail(String email) {
        return userRepository.findByEmail(email);
    }
}

