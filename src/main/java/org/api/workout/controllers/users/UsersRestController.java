package org.api.workout.controllers.users;


import org.api.workout.dto.user.RegisterRequestDTO;
import org.api.workout.dto.user.LoginResponseDTO;
import org.api.workout.dto.user.RegisterResponseDTO;
import org.api.workout.dto.user.UserDTO;
import org.api.workout.entities.user.User;
import org.api.workout.security.CustomUserDetails;
import org.api.workout.security.DBUserDetailsService;
import org.api.workout.services.jwt.JwtService;
import org.api.workout.services.user.UserService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/users")
public class UsersRestController {
    private final UserService userService;
    private final AuthenticationManager authenticationManager;
    private final DBUserDetailsService userDetailsService;
    private final JwtService jwtService;

    public UsersRestController(UserService userService, AuthenticationManager authenticationManager, DBUserDetailsService userDetailsService, JwtService jwtService) {
        this.userService = userService;
        this.authenticationManager = authenticationManager;
        this.userDetailsService = userDetailsService;
        this.jwtService = jwtService;
    }

    @PostMapping("/register")
    public ResponseEntity<?> register(@RequestBody RegisterRequestDTO requestDTO) {
        User user = userService.register(requestDTO);

        Authentication auth = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(requestDTO.username(), requestDTO.password())
        );

        UserDetails userDetails = (UserDetails) auth.getPrincipal();

        String token = jwtService.generateToken(userDetails);
        RegisterResponseDTO response = new RegisterResponseDTO(user.getId(), token);

        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }

    @SuppressWarnings("unused")
    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody RegisterRequestDTO request) {
        userService.login(request);
        Authentication auth = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(request.username(), request.password())
        );

        UserDetails userDetails = userDetailsService.loadUserByUsername(request.username());
        String token = jwtService.generateToken(userDetails);

        return new ResponseEntity<>(new LoginResponseDTO(token), HttpStatus.OK);
    }

    @SuppressWarnings("unused")
    @GetMapping("/{id}")
    public ResponseEntity<?> getUser(@PathVariable long id, @AuthenticationPrincipal CustomUserDetails userDetails) {
        UserDTO userDTO = userService.getUserDTOById(id, userDetails);
        return new ResponseEntity<>(userDTO, HttpStatus.OK);
    }

}
