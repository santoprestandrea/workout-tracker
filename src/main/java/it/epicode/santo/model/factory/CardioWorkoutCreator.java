package it.epicode.santo.model.factory;

import it.epicode.santo.model.core.CardioWorkout;
import it.epicode.santo.model.core.Workout;

public class CardioWorkoutCreator extends WorkoutCreator {
    @Override
    public Workout createWorkout(String name, int duration, Object ...specificParams){
        try {
            if(specificParams.length == 1 && specificParams[0] instanceof Double){
                double distanceKm = (Double) specificParams[0];

                // input validation
                validateCommonParams(name, duration);
                if(distanceKm <= 0){
                    throw new IllegalArgumentException("Distance must be positive");
                }

                // call CardioWorkout constructor
                return new CardioWorkout(name, duration, distanceKm);
            }
            throw new IllegalArgumentException("Specific param for CardioWorkout must be two a double (distanceKm) ");
        } catch (IllegalArgumentException e) {
            //TODO
        } catch(Exception e){
            //TODO
        }
        
        
    }
    
}
