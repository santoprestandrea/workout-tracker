package it.epicode.santo.model.iterator;

import it.epicode.santo.model.core.Workout;

public interface WorkoutIterator {
    boolean hasNext();

    Workout next();

    boolean isEmpty();

    void reset();
}
