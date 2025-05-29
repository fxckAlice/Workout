package org.api.workout.controllers.users;


import org.api.workout.controllers.dto.user.RegisterRequestDTO;
import org.api.workout.controllers.dto.user.UserDTO;
import org.api.workout.entities.user.User;
import org.api.workout.security.CustomUserDetails;
import org.api.workout.security.DBUserDetailsService;
import org.api.workout.services.jwt.JwtService;
import org.api.workout.services.user.UserService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

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

        Map<String, Object> response = new HashMap<>();
        response.put("userId", user.getId());
        response.put("token", token);

        return ResponseEntity.ok(response);
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

        return ResponseEntity.ok(Collections.singletonMap("token", token));
    }

    @SuppressWarnings("unused")
    @PreAuthorize("hasRole('ADMIN') or #id == #userDetails.id")
    @GetMapping("/{id}")
    public ResponseEntity<?> getUser(@PathVariable long id, @AuthenticationPrincipal CustomUserDetails userDetails) {
        UserDTO userDTO = userService.getUserDTOById(id);
        return new ResponseEntity<>(userDTO, HttpStatus.OK);
    }

}
