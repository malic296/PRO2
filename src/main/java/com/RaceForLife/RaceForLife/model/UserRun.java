package com.RaceForLife.RaceForLife.model;

import jakarta.persistence.*;

@Entity
@Table(name = "user_run", uniqueConstraints = @UniqueConstraint(columnNames = {"user_id", "run_id"}))
public class UserRun {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(optional = false)
    @JoinColumn(name = "user_id", referencedColumnName = "id")
    private User user;

    @ManyToOne(optional = false)
    @JoinColumn(name = "run_id", referencedColumnName = "id")
    private Run run;

    // Getters and setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public User getUser() { return user; }
    public void setUser(User user) { this.user = user; }

    public Run getRun() { return run; }
    public void setRun(Run run) { this.run = run; }
}
