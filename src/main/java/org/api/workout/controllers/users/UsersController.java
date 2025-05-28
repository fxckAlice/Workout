package org.api.workout.controllers.users;

import org.api.workout.controllers.dto.RegisterRequestDTO;
import org.api.workout.controllers.dto.UserDTO;
import org.api.workout.entities.user.User;
import org.api.workout.security.CustomUserDetails;
import org.api.workout.services.user.UserService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/users")
public class UsersController {
    private final UserService userService;

    public UsersController(UserService userService) {
        this.userService = userService;
    }

    @PostMapping("/register")
    public ResponseEntity<?> register(@RequestBody RegisterRequestDTO requestDTO) {
        User user = userService.register(requestDTO);
        return new ResponseEntity<>(user.getId(), HttpStatus.OK);
    }

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody RegisterRequestDTO requestDTO) {
        User user = userService.login(requestDTO);
        return new ResponseEntity<>(user.getId(), HttpStatus.OK);
    }

    @SuppressWarnings("unused")
    @PreAuthorize("hasRole('ADMIN') or #id == #userDetails.id")
    @GetMapping("/{id}")
    public ResponseEntity<?> getUser(@PathVariable long id, @AuthenticationPrincipal CustomUserDetails userDetails) {
        UserDTO userDTO = userService.getUserDTOById(id);
        return new ResponseEntity<>(userDTO, HttpStatus.OK);
    }

}
