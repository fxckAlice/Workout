package org.api.workout.controllers.dto;

public record RegisterRequestDTO(
        String username,
        String password
) {
}
