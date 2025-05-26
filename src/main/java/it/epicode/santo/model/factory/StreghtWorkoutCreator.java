package it.epicode.santo.model.factory;

import it.epicode.santo.model.core.StrenghtWorkout;
import it.epicode.santo.model.core.Workout;

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
            // TODO
        } catch(Exception e){
            //TODO 
        }
    }
    
}
