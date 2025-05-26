package it.epicode.santo.model.factory;

import it.epicode.santo.model.core.Workout;

public abstract class WorkoutCreator{
    public abstract Workout createWorkout(String name, int duration, Object ...specificParams);

    protected void validateCommonParams(String name, int duration) {
        if (name == null || name.trim().isEmpty()) {
            throw new IllegalArgumentException("Workout name cannot be empty or null");
        }
        if (duration <= 0) {
            throw new IllegalArgumentException("Workout duration must be positive");
        }
    }

    
}