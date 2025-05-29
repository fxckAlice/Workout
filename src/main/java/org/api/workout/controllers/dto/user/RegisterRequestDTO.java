package org.api.workout.controllers.dto.user;

public record RegisterRequestDTO(
        String username,
        String password
) {
}
