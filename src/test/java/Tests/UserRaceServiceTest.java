package Tests;

import com.RaceForLife.RaceForLife.model.Race;
import com.RaceForLife.RaceForLife.model.User;
import com.RaceForLife.RaceForLife.model.UserRace;
import com.RaceForLife.RaceForLife.repository.UserRaceRepository;
import com.RaceForLife.RaceForLife.service.UserRaceService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.*;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class UserRaceServiceTest {

    @Mock
    private UserRaceRepository userRaceRepository;

    @InjectMocks
    private UserRaceService userRaceService;

    private User user;
    private Race race;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);

        user = new User();
        user.setEmail("user@example.com");

        race = new Race();
        race.setId(1L);
    }

    @Test
    void testJoinRace_whenNotAlreadyJoined() {
        when(userRaceRepository.existsByUserAndRace(user, race)).thenReturn(false);

        userRaceService.joinRace(user, race);

        verify(userRaceRepository, times(1)).save(any(UserRace.class));
    }

    @Test
    void testJoinRace_whenAlreadyJoined_doesNothing() {
        when(userRaceRepository.existsByUserAndRace(user, race)).thenReturn(true);

        userRaceService.joinRace(user, race);

        verify(userRaceRepository, never()).save(any(UserRace.class));
    }

    @Test
    void testLeaveRace_whenJoined() {
        UserRace ur = new UserRace();
        ur.setUser(user);
        ur.setRace(race);

        when(userRaceRepository.findByUserAndRace(user, race)).thenReturn(Optional.of(ur));

        userRaceService.leaveRace(user, race);

        verify(userRaceRepository, times(1)).delete(ur);
    }

    @Test
    void testLeaveRace_whenNotJoined_doesNothing() {
        when(userRaceRepository.findByUserAndRace(user, race)).thenReturn(Optional.empty());

        userRaceService.leaveRace(user, race);

        verify(userRaceRepository, never()).delete(any(UserRace.class));
    }

    @Test
    void testIsUserJoinedRace() {
        when(userRaceRepository.existsByUserAndRace(user, race)).thenReturn(true);
        assertTrue(userRaceService.isUserJoinedRace(user, race));

        when(userRaceRepository.existsByUserAndRace(user, race)).thenReturn(false);
        assertFalse(userRaceService.isUserJoinedRace(user, race));
    }

    @Test
    void testGetUsersByRace() {
        UserRace ur1 = new UserRace(); ur1.setUser(user); ur1.setRace(race);
        when(userRaceRepository.findByRace(race)).thenReturn(List.of(ur1));

        List<User> users = userRaceService.getUsersByRace(race);

        assertEquals(1, users.size());
        assertEquals(user, users.get(0));
    }

    @Test
    void testGetRaceIdsJoinedByUser() {
        Race race1 = new Race(); race1.setId(1L);
        Race race2 = new Race(); race2.setId(2L);

        UserRace ur1 = new UserRace(); ur1.setUser(user); ur1.setRace(race1);
        UserRace ur2 = new UserRace(); ur2.setUser(user); ur2.setRace(race2);

        when(userRaceRepository.findByUser(user)).thenReturn(List.of(ur1, ur2));

        List<Long> raceIds = userRaceService.getRaceIdsJoinedByUser(user);

        assertEquals(2, raceIds.size());
        assertTrue(raceIds.contains(1L));
        assertTrue(raceIds.contains(2L));
    }
}
