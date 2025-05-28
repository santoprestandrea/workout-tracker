package it.epicode.santo.model.factory;

import java.util.logging.Logger;

import it.epicode.santo.model.core.Workout;
import it.epicode.santo.util.LoggerManager;

/**
 * Abstract factory class for creating {@link Workout} instances.
 * <p>
 * Subclasses should implement the
 * {@link #createWorkout(String, int, Object...)} method
 * to instantiate specific types of workouts.
 * </p>
 *
 * <p>
 * This class also provides a protected utility method to validate common
 * parameters
 * such as workout name and duration.
 * </p>
 */
public abstract class WorkoutCreator{
    private static final Logger LOGGER = LoggerManager.getLogger();

    /**
     * Creates a new Workout instance with the specified name, duration, and
     * additional parameters.
     *
     * @param name           the name of the workout
     * @param duration       the duration of the workout in minutes
     * @param specificParams additional parameters specific to the type of workout
     * @return a new instance of Workout
     */
    public abstract Workout createWorkout(String name, int duration, Object ...specificParams);

    /**
     * Validates the common parameters for a workout.
     * <p>
     * Checks that the workout name is not null or empty, and that the duration is
     * positive.
     * If any validation fails, an {@link IllegalArgumentException} is thrown.
     *
     * @param name     the name of the workout; must not be null or empty
     * @param duration the duration of the workout in minutes; must be greater than
     *                 zero
     * @throws IllegalArgumentException if the name is not positive
     */
    protected void validateCommonParams(String name, int duration) {
        if (name == null || name.trim().isEmpty()) {
            LOGGER.warning("attemptt to create a workout with invalide name");
            throw new IllegalArgumentException("Workout name cannot be empty or null");
        }
        if (duration <= 0) {
            LOGGER.warning("attempt to create a workout with invalide duration");
            throw new IllegalArgumentException("Workout duration must be positive");
        }
    }

    
}