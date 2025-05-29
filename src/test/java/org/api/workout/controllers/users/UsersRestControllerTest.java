package org.api.workout.controllers.users;

import org.api.workout.dto.user.LoginResponseDTO;
import org.api.workout.dto.user.RegisterRequestDTO;
import org.api.workout.dto.user.RegisterResponseDTO;
import org.api.workout.dto.user.UserDTO;
import org.api.workout.entities.user.User;
import org.api.workout.security.CustomUserDetails;
import org.api.workout.security.DBUserDetailsService;
import org.api.workout.services.jwt.JwtService;
import org.api.workout.services.user.UserService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.*;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;

import java.time.LocalDateTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class UsersRestControllerTest {

    @Mock
    private UserService userService;

    @Mock
    private AuthenticationManager authenticationManager;

    @Mock
    private DBUserDetailsService userDetailsService;

    @Mock
    private JwtService jwtService;

    @InjectMocks
    private UsersRestController usersRestController;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void register_ShouldReturnUserIdAndToken() {
        RegisterRequestDTO request = new RegisterRequestDTO("user", "password");

        User mockUser = new User();
        mockUser.setId(42L);

        Authentication mockAuth = mock(Authentication.class);
        UserDetails mockDetails = mock(UserDetails.class);
        when(mockAuth.getPrincipal()).thenReturn(mockDetails);

        when(userService.register(request)).thenReturn(mockUser);
        when(authenticationManager.authenticate(any(UsernamePasswordAuthenticationToken.class))).thenReturn(mockAuth);
        when(jwtService.generateToken(mockDetails)).thenReturn("mock-token");

        ResponseEntity<?> response = usersRestController.register(request);

        assertEquals(HttpStatus.CREATED, response.getStatusCode());
        RegisterResponseDTO body = (RegisterResponseDTO) response.getBody();
        assertNotNull(body);
        assertEquals(42L, body.userId());
        assertEquals("mock-token", body.token());

        verify(userService).register(request);
        verify(jwtService).generateToken(mockDetails);
    }

    @Test
    void login_ShouldReturnToken() {
        RegisterRequestDTO request = new RegisterRequestDTO("user", "password");

        UserDetails mockDetails = mock(UserDetails.class);
        Authentication mockAuth = mock(Authentication.class);

        when(authenticationManager.authenticate(any(UsernamePasswordAuthenticationToken.class))).thenReturn(mockAuth);
        when(userDetailsService.loadUserByUsername("user")).thenReturn(mockDetails);
        when(jwtService.generateToken(mockDetails)).thenReturn("token123");

        ResponseEntity<?> response = usersRestController.login(request);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        LoginResponseDTO body = (LoginResponseDTO) response.getBody();
        assertNotNull(body);
        assertEquals("token123", body.token());

        verify(userService).login(request);
        verify(userDetailsService).loadUserByUsername("user");
        verify(jwtService).generateToken(mockDetails);
    }

    @Test
    void getUser_ShouldReturnUserDTO() {
        long userId = 1L;
        CustomUserDetails userDetails = mock(CustomUserDetails.class);
        UserDTO userDTO = new UserDTO(userId, "test", LocalDateTime.now(), List.of(), List.of());

        when(userService.getUserDTOById(userId, userDetails)).thenReturn(userDTO);

        ResponseEntity<?> response = usersRestController.getUser(userId, userDetails);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(userDTO, response.getBody());
        verify(userService).getUserDTOById(userId, userDetails);
    }
}
