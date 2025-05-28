package it.epicode.santo.model.factory;

import java.util.logging.Logger;

import it.epicode.santo.model.core.CardioWorkout;
import it.epicode.santo.model.core.Workout;
import it.epicode.santo.model.exceptions.InvalidWorkoutDataException;
import it.epicode.santo.util.LoggerManager;

/**
 * Factory class for creating instances of {@link CardioWorkout}.
 * <p>
 * This class extends {@link WorkoutCreator} and implements the factory method
 * to instantiate {@link CardioWorkout} objects with validated parameters.
 * </p>
 *
 * <p>
 * The {@code createWorkout} method expects the following parameters:
 * <ul>
 * <li>{@code name} - the name of the workout (must not be null or empty)</li>
 * <li>{@code duration} - the duration of the workout in minutes (must be
 * positive)</li>
 * *
 * <li>{@code specificParams} - expects a single {@code Double} value
 * representing the distance in kilometers (must be positive)</li>
 * </ul>
 * </p>
 * <p>
 * If the parameters are invalid, an {@link InvalidWorkoutDataException} is
 * thrown.
 * </p>
 *
 */
public class CardioWorkoutCreator extends WorkoutCreator {
    private static final Logger LOGGER = LoggerManager.getLogger();
    
    @Override
    public Workout createWorkout(String name, int duration, Object... specificParams) {
        try {
            if (specificParams.length == 1 && specificParams[0] instanceof Double) {
                double distanceKm = (Double) specificParams[0];

                // input validation
                validateCommonParams(name, duration);
                if (distanceKm <= 0) {
                    LOGGER.warning("attempt to create cardioworkout with negative distance: " + distanceKm);
                    throw new IllegalArgumentException("Distance must be positive");
                }

                // call CardioWorkout constructor
                return new CardioWorkout(name, duration, distanceKm);
            }
            throw new IllegalArgumentException("Specific param for CardioWorkout must be two a double (distanceKm) ");
        } catch (IllegalArgumentException e) {
            throw new InvalidWorkoutDataException("Impossible to create CardioWorkout: " + e.getMessage(), e);
        } catch (Exception e) {
            throw new InvalidWorkoutDataException("Generic error during CardioWorkout creation: " + e.getMessage(), e);
        }

    }

}
