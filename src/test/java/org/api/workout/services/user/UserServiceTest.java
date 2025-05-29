package org.api.workout.services.user;

import org.api.workout.dto.user.RegisterRequestDTO;
import org.api.workout.entities.user.User;
import org.api.workout.exceptions.user.UserAlreadyExistsException;
import org.api.workout.exceptions.workout.AccessForbiddenException;
import org.api.workout.security.CustomUserDetails;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.*;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class UserServiceTest {

    @Mock
    private UserDBService userDBService;

    @Mock
    private PasswordEncoder passwordEncoder;

    @InjectMocks
    private UserService userService;

    @BeforeEach
    void setup() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void register_shouldSaveNewUser_whenUsernameIsFree() {
        RegisterRequestDTO request = new RegisterRequestDTO("john", "1234");

        when(userDBService.existsByUsername("john")).thenReturn(false);
        when(passwordEncoder.encode("1234")).thenReturn("hashed");
        when(userDBService.save(any(User.class))).thenAnswer(inv -> inv.getArgument(0));

        User result = userService.register(request);

        assertEquals("john", result.getUsername());
        assertEquals("hashed", result.getPassHash());
    }

    @Test
    void register_shouldThrow_whenUserAlreadyExists() {
        RegisterRequestDTO request = new RegisterRequestDTO("john", "1234");

        when(userDBService.existsByUsername("john")).thenReturn(true);

        assertThrows(UserAlreadyExistsException.class, () -> userService.register(request));
        verify(userDBService, never()).save(any());
    }


    @Test
    void login_shouldReturnUser_whenPasswordCorrect() {
        RegisterRequestDTO request = new RegisterRequestDTO("john", "1234");
        User user = new User("john", "hashed");

        when(userDBService.findByUsername("john")).thenReturn(user);
        when(passwordEncoder.matches("1234", "hashed")).thenReturn(true);
        when(userDBService.save(user)).thenReturn(user);

        User result = userService.login(request);

        assertEquals("john", result.getUsername());
    }

    @Test
    void login_shouldThrow_whenPasswordIncorrect() {
        RegisterRequestDTO request = new RegisterRequestDTO("john", "wrong");
        User user = new User("john", "hashed");

        when(userDBService.findByUsername("john")).thenReturn(user);
        when(passwordEncoder.matches("wrong", "hashed")).thenReturn(false);

        assertThrows(IllegalArgumentException.class, () -> userService.login(request));
    }


    @Test
    void getUserDTOById_shouldThrow_whenAccessForbiddenAndNotAdmin() {
        long requestedId = 1L;
        CustomUserDetails userDetails = new CustomUserDetails(
                2L, "someone", "pwd", List.of()
        );

        assertThrows(AccessForbiddenException.class, () ->
                userService.getUserDTOById(requestedId, userDetails));
    }

    @Test
    void getUserDTOById_shouldPass_whenUserAccessingSelf() {
        CustomUserDetails userDetails = new CustomUserDetails(
                1L, "self", "pwd", List.of()
        );

        User dummyUser = new User("self", "hash");
        dummyUser.setId(1L);

        when(userDBService.findById(1L)).thenReturn(dummyUser);

        assertDoesNotThrow(() -> userService.getUserDTOById(1L, userDetails));
    }

    @Test
    void getUserDTOById_shouldPass_whenUserIsAdmin() {
        CustomUserDetails userDetails = new CustomUserDetails(
                99L, "admin", "pwd", List.of(new SimpleGrantedAuthority("ROLE_ADMIN"))
        );

        User dummyUser = new User("someone", "hash");
        dummyUser.setId(1L);

        when(userDBService.findById(1L)).thenReturn(dummyUser);

        assertDoesNotThrow(() -> userService.getUserDTOById(1L, userDetails));
    }
}
