package Tests;

import com.RaceForLife.RaceForLife.model.Race;
import com.RaceForLife.RaceForLife.repository.RaceRepository;
import com.RaceForLife.RaceForLife.service.RaceService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.*;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class RaceServiceTest {

    @InjectMocks
    private RaceService raceService;

    @Mock
    private RaceRepository raceRepository;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testGetUpcomingRaces() {
        Race futureRace = new Race();
        futureRace.setDate(LocalDateTime.now().plusDays(5));
        when(raceRepository.findAllByDateAfterOrderByDateAsc(any()))
                .thenReturn(List.of(futureRace));

        List<Race> result = raceService.getUpcomingRaces();

        assertEquals(1, result.size());
        verify(raceRepository).findAllByDateAfterOrderByDateAsc(any());
    }

    @Test
    void testCreateRace_withValidDate() {
        Race race = new Race();
        race.setDate(LocalDateTime.now().plusDays(3));

        when(raceRepository.save(race)).thenReturn(race);

        Race result = raceService.createRace(race);

        assertNotNull(result);
        verify(raceRepository).save(race);
    }

    @Test
    void testCreateRace_withPastDate_throwsException() {
        Race race = new Race();
        race.setDate(LocalDateTime.now().minusDays(1));

        assertThrows(IllegalArgumentException.class, () -> raceService.createRace(race));
        verify(raceRepository, never()).save(any());
    }

    @Test
    void testGetRaceById_found() {
        Race race = new Race();
        race.setId(1L);

        when(raceRepository.findById(1L)).thenReturn(Optional.of(race));

        Optional<Race> result = raceService.getRaceById(1L);

        assertTrue(result.isPresent());
        assertEquals(1L, result.get().getId());
        verify(raceRepository).findById(1L);
    }

    @Test
    void testGetRaceById_notFound() {
        when(raceRepository.findById(999L)).thenReturn(Optional.empty());

        Optional<Race> result = raceService.getRaceById(999L);

        assertFalse(result.isPresent());
        verify(raceRepository).findById(999L);
    }

    @Test
    void testDeleteRace() {
        raceService.deleteRace(1L);

        verify(raceRepository).deleteById(1L);
    }

    @Test
    void testUpdateRace_withValidDate() {
        Race updated = new Race();
        updated.setDate(LocalDateTime.now().plusDays(1));

        when(raceRepository.save(updated)).thenReturn(updated);

        Race result = raceService.updateRace(updated);

        assertNotNull(result);
        verify(raceRepository).save(updated);
    }

    @Test
    void testUpdateRace_withPastDate_throwsException() {
        Race updated = new Race();
        updated.setDate(LocalDateTime.now().minusDays(2));

        assertThrows(IllegalArgumentException.class, () -> raceService.updateRace(updated));
        verify(raceRepository, never()).save(any());
    }
}
