package com.RaceForLife.RaceForLife.service;
import com.RaceForLife.RaceForLife.model.Run;
import com.RaceForLife.RaceForLife.repository.RunRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestParam;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.time.LocalDate;

@Service
public class RunService {
    // Injecting the RunRepository which interacts with the database
    @Autowired
    private RunRepository runRepository;

    // Method to get all runs from the database
    public List<Run> getAllRuns() {
        return runRepository.findAll();
    }

    public List<Run> getUpcomingRuns() {
        LocalDateTime startOfToday = LocalDate.now().atStartOfDay();
        return runRepository.findByDateAfterOrderByDateAsc(startOfToday);
    }

    public List<Run> getPastRuns() {
        LocalDateTime startOfToday = LocalDate.now().atStartOfDay();
        return runRepository.findByDateBeforeOrderByDateDesc(startOfToday);
    }

    public void createRun(String name, String description, String address, LocalDateTime date, String creator) {
        Run run = new Run();
        run.setName(name);
        run.setDescription(description);
        run.setAddress(address);
        run.setDate(date);
        run.setCreator(creator);

        runRepository.save(run);
    }

    public void editRun(Long id, String name, String description, String address, LocalDateTime date) {
        Optional<Run> optionalRun = runRepository.findById(id);
        if (optionalRun.isPresent()) {
            Run run = optionalRun.get();
            run.setName(name);
            run.setDescription(description);
            run.setAddress(address);
            run.setDate(date);

            runRepository.save(run); // This will update the existing run
        } else {
            throw new IllegalArgumentException("Run with ID " + id + " not found.");
        }
    }

    public void deleteRun(long id){
        runRepository.deleteById(id);
    }

    public Optional<Run> getRunById(Long id) {
        return runRepository.findById(id);
    }



}
