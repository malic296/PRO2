package com.RaceForLife.RaceForLife.service;

import com.RaceForLife.RaceForLife.model.User;
import com.RaceForLife.RaceForLife.repository.UserRepository;
import com.RaceForLife.RaceForLife.util.PasswordUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class UserService {

    @Autowired
    private UserRepository userRepository;

    public User registerUser(String email, String password, int team) {
        if (userRepository.findByEmail(email).isPresent()) {
            throw new RuntimeException("Email already in use.");
        }

        User user = new User();
        user.setEmail(email);
        user.setEncryptedPassword(PasswordUtils.hashPassword(password));
        user.setAdmin(false);
        user.setTeam(team);

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
}

