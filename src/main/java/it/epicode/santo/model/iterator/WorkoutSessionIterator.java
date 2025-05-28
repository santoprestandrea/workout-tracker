package it.epicode.santo.model.iterator;

import java.util.NoSuchElementException;

import it.epicode.santo.model.core.Workout;
import it.epicode.santo.model.core.WorkoutSession;
import it.epicode.santo.model.exceptions.InvalidWorkoutDataException;

/**
 * An iterator for traversing the workouts within a {@link WorkoutSession}.
 * <p>
 * This iterator provides methods to iterate through the workouts in a session,
 * check if more workouts are available, determine if the session is empty,
 * and reset the iteration.
 * </p>
 *
 * <p>
 * Example usage:
 * <pre>
 *     WorkoutSessionIterator iterator = new WorkoutSessionIterator(session);
 *     while (iterator.hasNext()) {
 *         Workout workout = iterator.next();
*     }
 * </pre>
 * </p>
 */
public class WorkoutSessionIterator implements WorkoutIterator{
    private WorkoutSession session;
    private int index = 0;

    public WorkoutSessionIterator(WorkoutSession session){
        // Costructor validation
        if(session == null){
            throw new InvalidWorkoutDataException("workout session can't be null");
        }
        this.session = session;
    }
    
    @Override
    public boolean hasNext(){
        return index < session.getSize();
    }

    @Override 
    public Workout next(){
        if(!hasNext()){
            throw new NoSuchElementException();
        }
        return session.getWorkoutAt(index++);
    }
    @Override
    public boolean isEmpty() {
        return session.getSize() == 0;
    }

    @Override
    public void reset() {
        index = 0;
    }
    
}
