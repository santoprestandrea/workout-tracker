package it.epicode.santo.model.factory;

import it.epicode.santo.model.core.StrenghtWorkout;
import it.epicode.santo.model.core.Workout;
import it.epicode.santo.model.exceptions.InvalidWorkoutDataException;

/**
 * Factory class for creating instances of {@link StrengthWorkout}.
 * <p>
 * This class extends {@link WorkoutCreator} and implements the
 * {@link #createWorkout(String, int, Object...)} method to create
 * a StrengthWorkout with the specified parameters.
 * </p>
 */
public class StreghtWorkoutCreator extends WorkoutCreator{
    @Override
    public Workout createWorkout(String name, int duration, Object...specificParams){
        try {
            if(specificParams.length == 2 && specificParams[0] instanceof Integer && specificParams[1] instanceof Integer){
                int sets = (Integer) specificParams[0];
                int reps = (Integer) specificParams[1];

                // Input validation
                validateCommonParams(name, duration);
                if(sets <= 0 || reps <=0){
                    throw new IllegalArgumentException("sets and reps must be positive");
                }

                // call StrghtWorkout constractor 
                return new StrenghtWorkout(name, duration, sets, reps);
            }
            throw new IllegalArgumentException("Specific params for StrenghtWorkout must be two intengers (sets,reps) ");
        } catch (IllegalArgumentException e) {
            throw new InvalidWorkoutDataException("Impossible to create StreghtWorkout: " + e.getMessage(), e);
        } catch(Exception e){
            throw new InvalidWorkoutDataException("Generic error during StreghtWorkout creation: " + e.getMessage(), e);
        }
    }
    
}
