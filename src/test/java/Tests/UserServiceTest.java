package Tests;

import com.RaceForLife.RaceForLife.model.Team;
import com.RaceForLife.RaceForLife.model.User;
import com.RaceForLife.RaceForLife.repository.TeamRepository;
import com.RaceForLife.RaceForLife.repository.UserRepository;
import com.RaceForLife.RaceForLife.service.UserService;
import com.RaceForLife.RaceForLife.util.PasswordUtils;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.*;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class UserServiceTest {

    @Mock private UserRepository userRepository;
    @Mock private TeamRepository teamRepository;

    @InjectMocks private UserService userService;

    private User user;
    private Team team;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);

        user = new User();
        user.setEmail("test@example.com");
        user.setEncryptedPassword(PasswordUtils.hashPassword("password123"));
        user.setAdmin(true);

        team = new Team();
        team.setId(1L);
        team.setName("Team Test");
    }

    @Test
    void testRegisterUser_Success() {
        when(userRepository.findByEmail("new@example.com")).thenReturn(Optional.empty());
        when(teamRepository.findById(1L)).thenReturn(Optional.of(team));
        when(userRepository.save(any(User.class))).thenAnswer(inv -> inv.getArgument(0));

        User newUser = userService.registerUser("new@example.com", "securePass", 1);

        assertEquals("new@example.com", newUser.getEmail());
        assertEquals(team, newUser.getTeam());
        assertFalse(newUser.isAdmin());
        assertTrue(PasswordUtils.verifyPassword("securePass", newUser.getEncryptedPassword()));
    }

    @Test
    void testRegisterUser_EmailAlreadyUsed_ThrowsException() {
        when(userRepository.findByEmail("existing@example.com")).thenReturn(Optional.of(user));

        RuntimeException ex = assertThrows(RuntimeException.class, () ->
                userService.registerUser("existing@example.com", "password123", 1));

        assertEquals("Email already in use.", ex.getMessage());
    }

    @Test
    void testRegisterUser_InvalidTeam_ThrowsException() {
        when(userRepository.findByEmail("new@example.com")).thenReturn(Optional.empty());
        when(teamRepository.findById(99L)).thenReturn(Optional.empty());

        RuntimeException ex = assertThrows(RuntimeException.class, () ->
                userService.registerUser("new@example.com", "password123", 99));

        assertEquals("Invalid team ID.", ex.getMessage());
    }

    @Test
    void testAuthenticateUser_Success() {
        when(userRepository.findByEmail("test@example.com")).thenReturn(Optional.of(user));

        boolean authenticated = userService.authenticateUser("test@example.com", "password123");

        assertTrue(authenticated);
    }

    @Test
    void testAuthenticateUser_InvalidPassword() {
        when(userRepository.findByEmail("test@example.com")).thenReturn(Optional.of(user));

        boolean authenticated = userService.authenticateUser("test@example.com", "wrongPass");

        assertFalse(authenticated);
    }

    @Test
    void testAuthenticateUser_UserNotFound() {
        when(userRepository.findByEmail("unknown@example.com")).thenReturn(Optional.empty());

        boolean authenticated = userService.authenticateUser("unknown@example.com", "password");

        assertFalse(authenticated);
    }

    @Test
    void testIsAdmin_UserIsAdmin() {
        when(userRepository.findByEmail("test@example.com")).thenReturn(Optional.of(user));

        assertTrue(userService.isAdmin("test@example.com"));
    }

    @Test
    void testIsAdmin_UserIsNotAdmin() {
        user.setAdmin(false);
        when(userRepository.findByEmail("test@example.com")).thenReturn(Optional.of(user));

        assertFalse(userService.isAdmin("test@example.com"));
    }

    @Test
    void testFindByEmail_ReturnsUser() {
        when(userRepository.findByEmail("test@example.com")).thenReturn(Optional.of(user));

        Optional<User> result = userService.findByEmail("test@example.com");

        assertTrue(result.isPresent());
        assertEquals(user, result.get());
    }
}
