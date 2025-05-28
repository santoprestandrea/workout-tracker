package it.epicode.santo.model.factory;

import java.util.logging.Logger;

import it.epicode.santo.model.core.StrenghtWorkout;
import it.epicode.santo.model.core.Workout;
import it.epicode.santo.model.exceptions.InvalidWorkoutDataException;
import it.epicode.santo.util.LoggerManager;

/**
 * Factory class for creating instances of {@link StrengthWorkout}.
 * <p>
 * This class extends {@link WorkoutCreator} and implements the
 * {@link #createWorkout(String, int, Object...)} method to create
 * a StrengthWorkout with the specified parameters.
 * </p>
 */
public class StreghtWorkoutCreator extends WorkoutCreator {
    private static final Logger LOGGER = LoggerManager.getLogger();

    @Override
    public Workout createWorkout(String name, int duration, Object... specificParams) {
        try {
            if (specificParams.length == 2 && specificParams[0] instanceof Integer
                    && specificParams[1] instanceof Integer) {
                int sets = (Integer) specificParams[0];
                int reps = (Integer) specificParams[1];

                // Input validation
                validateCommonParams(name, duration);
                if (sets <= 0 || reps <= 0) {
                    LOGGER.warning(
                            "attempt to create streght workout with invalid sets and/or reps: " + sets + " " + reps);
                    throw new IllegalArgumentException("sets and reps must be positive");
                }

                // call StrghtWorkout constractor
                return new StrenghtWorkout(name, duration, sets, reps);
            }
            throw new IllegalArgumentException(
                    "Specific params for StrenghtWorkout must be two intengers (sets,reps) ");
        } catch (IllegalArgumentException e) {
            throw new InvalidWorkoutDataException("Impossible to create StreghtWorkout: " + e.getMessage(), e);
        } catch (Exception e) {
            throw new InvalidWorkoutDataException("Generic error during StreghtWorkout creation: " + e.getMessage(), e);
        }
    }

}
