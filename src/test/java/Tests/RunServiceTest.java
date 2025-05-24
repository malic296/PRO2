package Tests;

import com.RaceForLife.RaceForLife.model.Run;
import com.RaceForLife.RaceForLife.repository.RunRepository;
import com.RaceForLife.RaceForLife.service.RunService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.*;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class RunServiceTest {

    @Mock
    private RunRepository runRepository;

    @InjectMocks
    private RunService runService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void getAllRuns_ShouldReturnAllRuns() {
        List<Run> mockRuns = Arrays.asList(new Run(), new Run());
        when(runRepository.findAll()).thenReturn(mockRuns);

        List<Run> result = runService.getAllRuns();

        assertEquals(2, result.size());
        verify(runRepository).findAll();
    }

    @Test
    void getUpcomingRuns_ShouldReturnOnlyFutureRunsSorted() {
        LocalDateTime now = LocalDateTime.now();
        Run pastRun = new Run();
        pastRun.setDate(now.minusDays(1));
        Run futureRun1 = new Run();
        futureRun1.setDate(now.plusDays(2));
        Run futureRun2 = new Run();
        futureRun2.setDate(now.plusDays(1));

        when(runRepository.findAll()).thenReturn(Arrays.asList(pastRun, futureRun1, futureRun2));

        List<Run> result = runService.getUpcomingRuns();

        assertEquals(2, result.size());
        assertTrue(result.get(0).getDate().isBefore(result.get(1).getDate()));
    }

    @Test
    void getPastRuns_ShouldReturnOnlyPastRuns() {
        LocalDateTime startOfToday = LocalDate.now().atStartOfDay();
        Run pastRun = new Run();
        pastRun.setDate(startOfToday.minusDays(1));

        when(runRepository.findByDateBeforeOrderByDateDesc(startOfToday)).thenReturn(List.of(pastRun));

        List<Run> result = runService.getPastRuns();

        assertEquals(1, result.size());
        assertTrue(result.get(0).getDate().isBefore(startOfToday));
    }

    @Test
    void createRun_WithFutureDate_ShouldSaveRun() {
        LocalDateTime futureDate = LocalDateTime.now().plusDays(1);
        when(runRepository.save(any(Run.class))).thenAnswer(invocation -> invocation.getArgument(0));

        runService.createRun("Test", "Desc", "123 Street", futureDate, "Admin");

        verify(runRepository).save(any(Run.class));
    }

    @Test
    void createRun_WithPastDate_ShouldThrowException() {
        LocalDateTime pastDate = LocalDateTime.now().minusDays(1);

        assertThrows(IllegalArgumentException.class, () -> {
            runService.createRun("Test", "Desc", "123 Street", pastDate, "Admin");
        });

        verify(runRepository, never()).save(any());
    }

    @Test
    void editRun_WithValidId_ShouldUpdateRun() {
        Long runId = 1L;
        LocalDateTime newDate = LocalDateTime.now().plusDays(1);
        Run existingRun = new Run();
        existingRun.setId(runId);
        when(runRepository.findById(runId)).thenReturn(Optional.of(existingRun));

        runService.editRun(runId, "Updated", "Updated Desc", "New Addr", newDate);

        assertEquals("Updated", existingRun.getName());
        verify(runRepository).save(existingRun);
    }

    @Test
    void editRun_WithNonexistentId_ShouldThrowException() {
        when(runRepository.findById(anyLong())).thenReturn(Optional.empty());

        assertThrows(IllegalArgumentException.class, () -> {
            runService.editRun(999L, "Name", "Desc", "Addr", LocalDateTime.now().plusDays(1));
        });
    }

    @Test
    void editRun_WithPastDate_ShouldThrowException() {
        Long id = 1L;
        Run run = new Run();
        run.setId(id);
        when(runRepository.findById(id)).thenReturn(Optional.of(run));

        LocalDateTime pastDate = LocalDateTime.now().minusDays(1);

        assertThrows(IllegalArgumentException.class, () -> {
            runService.editRun(id, "Name", "Desc", "Addr", pastDate);
        });
    }

    @Test
    void deleteRun_ShouldCallRepositoryDeleteById() {
        runService.deleteRun(5L);
        verify(runRepository).deleteById(5L);
    }

    @Test
    void getRunById_ShouldReturnOptionalRun() {
        Run run = new Run();
        run.setId(7L);
        when(runRepository.findById(7L)).thenReturn(Optional.of(run));

        Optional<Run> result = runService.getRunById(7L);

        assertTrue(result.isPresent());
        assertEquals(7L, result.get().getId());
    }
}
