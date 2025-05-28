package it.epicode.santo.model.core;

import java.util.ArrayList;
import java.util.List;

import it.epicode.santo.model.iterator.WorkoutCollection;
import it.epicode.santo.model.iterator.WorkoutIterator;
import it.epicode.santo.model.iterator.WorkoutSessionIterator;

// Composite: WorkoutSession
/**
 * Represents a workout session that can contain multiple {@link Workout} components.
 * Implements the Composite design pattern, allowing a session to be treated as a single workout
 * or as a collection of workouts.
 * <p>
 * Provides methods to add and remove workouts, retrieve session details, and iterate over the contained workouts.
 * The total duration of the session is the sum of the durations of all contained workouts.
 * </p>
 *
 * <p>
 * This class is serializable.
 * </p>
 *
 */
public class WorkoutSession implements Workout, WorkoutCollection {
    private String name;
    private List<Workout> workouts;

    public WorkoutSession(String name) {
        this.name = name;
        this.workouts = new ArrayList<>();
    }

    public void addWorkout(Workout workout) {
        workouts.add(workout);
    }

    public void removeWorkout(Workout workout) {
        workouts.remove(workout);
    }

    @Override
    public String getName() {
        return name;
    }

    /**
     * Calculates and returns the total duration of the workout session.
     * The total duration is computed as the sum of the durations of all workouts
     * included in this session.
     *
     * @return the total duration of the session in minutes
     */
    @Override
    public int getDuration() {
        return workouts.stream().mapToInt(Workout::getDuration).sum();
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("WorkoutSession{")
                .append("name='").append(name).append('\'')
                .append(", workouts=").append(workouts)
                .append('}');
        return sb.toString();
    }

    @Override
    public String getDetails() {
        StringBuilder details = new StringBuilder("Workout Session: " + name + "\n");
        for (Workout workout : workouts) {
            details.append(workout.getDetails()).append("\n");
        }
        return details.toString();
    }

    @Override
    public int getSize() {
        int size = workouts.size();
        return size;
    }

    @Override
    public Workout getWorkoutAt(int index){
        if(index < 0 || index >= workouts.size()){
            throw new IndexOutOfBoundsException("Index out of bounds:" + index);
        }
        Workout workout = workouts.get(index);
        return workout;
    }

    @Override
    public WorkoutIterator createIterator(){
        return new WorkoutSessionIterator(this);
    }
}
