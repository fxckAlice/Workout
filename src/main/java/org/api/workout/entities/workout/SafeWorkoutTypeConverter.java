package org.api.workout.entities.workout;

import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;

@Component
public class SafeWorkoutTypeConverter implements Converter<String, WorkoutType> {
    @SuppressWarnings("all")
    @Override
    public WorkoutType convert(String source) {
        if (source == null || source.isBlank()) return null;
        return WorkoutType.valueOf(source.toUpperCase());
    }
}
