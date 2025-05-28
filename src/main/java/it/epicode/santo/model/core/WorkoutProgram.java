package it.epicode.santo.model.core;

import java.util.ArrayList;
import java.util.List;

import it.epicode.santo.model.iterator.WorkoutCollection;
import it.epicode.santo.model.iterator.WorkoutIterator;
import it.epicode.santo.model.iterator.WorkoutProgramIterator;

// Composite: WorkoutProgram
/**
 * Represents a workout program that consists of a schedule of workouts.
 * A WorkoutProgram can contain individual workouts or workout sessions,
 * and provides methods to manage and iterate over its schedule.
 * <p>
 * Implements the {@link Workout}, {@link WorkoutCollection}, and
 * {@link Serializable} interfaces.
 * </p>
 *
 * <p>
 * Example usage:
 * 
 * <pre>
 *     WorkoutProgram program = new WorkoutProgram("Full Body Routine");
*     program.addWorkoutToSchedule(new WorkoutSession(...));
 *     int totalDuration = program.getDuration();
 * </pre>
 * </p>
 *
 */
public class WorkoutProgram implements Workout, WorkoutCollection {
    private String name;
    private List<Workout> schedule;

    public WorkoutProgram(String name) {
        this.name = name;
        this.schedule = new ArrayList<>();
    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public int getDuration() {
        return schedule.stream().mapToInt(Workout::getDuration).sum();
    }

    @Override
    public String toString() {
        return "WorkoutProgram{" +
                "name='" + name + '\'' +
                ", schedule=" + schedule +
                '}';
    }

    @Override
    public String getDetails() {
        StringBuilder details = new StringBuilder("Workout Program: " + name + "\n");
        for (Workout workout : schedule) {
            details.append(workout.getDetails()).append("\n");
        }
        return details.toString();
    }

    @Override
    public int getSize() {
        int size = schedule.size();
        return size;
    }

    @Override
    public Workout getWorkoutAt(int index) {
        if (index < 0 || index >= schedule.size()) {
            throw new IndexOutOfBoundsException("Index out of bounds" + index);
        }
        return schedule.get(index);
    }

    @Override
    public WorkoutIterator createIterator() {
        return new WorkoutProgramIterator(this);
    }
}
