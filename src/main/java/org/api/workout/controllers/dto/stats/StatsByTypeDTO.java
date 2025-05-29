package org.api.workout.controllers.dto.stats;

public record StatsByTypeDTO(
        int StrengthWorkouts,
        int CardioWorkouts,
        int GymnasticsWorkouts,
        int TRXWorkouts,
        int YogaWorkouts,
        int OtherWorkouts
) {
}
