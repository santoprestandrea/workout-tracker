package it.epicode.santo.model.iterator;

import it.epicode.santo.model.core.Workout;

/**
 * Represents a collection of Workout objects that can be iterated over.
 * Provides methods to create an iterator, retrieve the size of the collection,
 * and access a Workout at a specific index.
 */
public interface WorkoutCollection {
    WorkoutIterator createIterator();

    int getSize();

    Workout getWorkoutAt(int index);

}
