package org.api.workout.dto.user;

public record RegisterResponseDTO(
        long userId,
        String token
) {
}
