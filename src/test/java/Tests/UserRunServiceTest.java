package Tests;

import com.RaceForLife.RaceForLife.model.Run;
import com.RaceForLife.RaceForLife.model.User;
import com.RaceForLife.RaceForLife.model.UserRun;
import com.RaceForLife.RaceForLife.repository.UserRunRepository;
import com.RaceForLife.RaceForLife.service.UserRunService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.*;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class UserRunServiceTest {

    @Mock
    private UserRunRepository userRunRepository;

    @InjectMocks
    private UserRunService userRunService;

    private User user;
    private Run run;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        user = new User();
        user.setEmail("test@example.com");

        run = new Run();
        run.setId(1L);
    }

    @Test
    void testToggleParticipation_WhenNotJoined_ShouldJoin() {
        when(userRunRepository.findByUserAndRun(user, run)).thenReturn(Optional.empty());

        boolean result = userRunService.toggleParticipation(user, run);

        assertTrue(result); // Joined
        verify(userRunRepository, times(1)).save(any(UserRun.class));
    }

    @Test
    void testToggleParticipation_WhenAlreadyJoined_ShouldLeave() {
        UserRun userRun = new UserRun();
        userRun.setUser(user);
        userRun.setRun(run);
        when(userRunRepository.findByUserAndRun(user, run)).thenReturn(Optional.of(userRun));

        boolean result = userRunService.toggleParticipation(user, run);

        assertFalse(result); // Left
        verify(userRunRepository, times(1)).delete(userRun);
    }

    @Test
    void testIsUserJoinedRun_WhenJoined_ShouldReturnTrue() {
        when(userRunRepository.findByUserAndRun(user, run)).thenReturn(Optional.of(new UserRun()));

        boolean result = userRunService.isUserJoinedRun(user, run);

        assertTrue(result);
    }

    @Test
    void testIsUserJoinedRun_WhenNotJoined_ShouldReturnFalse() {
        when(userRunRepository.findByUserAndRun(user, run)).thenReturn(Optional.empty());

        boolean result = userRunService.isUserJoinedRun(user, run);

        assertFalse(result);
    }

    @Test
    void testGetRunIdsJoinedByUser() {
        Run run1 = new Run(); run1.setId(1L);
        Run run2 = new Run(); run2.setId(2L);

        UserRun ur1 = new UserRun(); ur1.setUser(user); ur1.setRun(run1);
        UserRun ur2 = new UserRun(); ur2.setUser(user); ur2.setRun(run2);

        when(userRunRepository.findByUser(user)).thenReturn(List.of(ur1, ur2));

        List<Long> runIds = userRunService.getRunIdsJoinedByUser(user);

        assertEquals(2, runIds.size());
        assertTrue(runIds.contains(1L));
        assertTrue(runIds.contains(2L));
    }

    @Test
    void testGetUsersByRun() {
        User user1 = new User(); user1.setEmail("a@example.com");
        User user2 = new User(); user2.setEmail("b@example.com");

        UserRun ur1 = new UserRun(); ur1.setUser(user1); ur1.setRun(run);
        UserRun ur2 = new UserRun(); ur2.setUser(user2); ur2.setRun(run);

        when(userRunRepository.findByRun(run)).thenReturn(List.of(ur1, ur2));

        List<User> users = userRunService.getUsersByRun(run);

        assertEquals(2, users.size());
        assertTrue(users.contains(user1));
        assertTrue(users.contains(user2));
    }
}
