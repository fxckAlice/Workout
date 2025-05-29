package org.api.workout.dto.stats;

public record StatsByTypeDTO(
        int strengthWorkouts,
        int cardioWorkouts,
        int gymnasticsWorkouts,
        int trxWorkouts,
        int yogaWorkouts,
        int otherWorkouts
) {
}
