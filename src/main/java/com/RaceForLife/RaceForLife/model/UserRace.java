package com.RaceForLife.RaceForLife.model;

import jakarta.persistence.*;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
@Entity
public class UserRace {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    private User user;

    @ManyToOne
    private Race race;

    public Long getId() {
        return id;
    }

    public User getUser() {
        return user;
    }

    public Race getRace() {
        return race;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public void setRace(Race race) {
        this.race = race;
    }
}

