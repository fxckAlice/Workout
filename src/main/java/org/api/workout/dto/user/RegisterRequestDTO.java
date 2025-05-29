package org.api.workout.dto.user;

public record RegisterRequestDTO(
        String username,
        String password
) {
}
