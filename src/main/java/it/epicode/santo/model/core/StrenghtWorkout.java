package it.epicode.santo.model.core;

/**
 * Represents a strength workout session with a specific name, duration, number of sets, and repetitions.
 * <p>
 * This class implements the {@link Workout} interface and is serializable.
 * It performs input validation in the constructor to ensure all parameters are valid.
 * </p>
 *
 * <p>
 * Example usage:
 * <pre>
 *     StrengthWorkout workout = new StrengthWorkout("Push Ups", 30, 4, 12);
 * </pre>
 * </p>
 */
public class StrenghtWorkout implements Workout {
    private String name;
    private int duration;
    private int sets;
    private int reps;

    public StrenghtWorkout(String name, int duration, int sets, int reps) {
        this.name = name;
        this.duration = duration;
        this.sets = sets;
        this.reps = reps;
    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public int getDuration() {
        return duration;
    }

    public int getSets() {
        return sets;
    }

    public int getReps() {
        return reps;
    }

    @Override
    public String toString() {
        return "StrengthWorkout{" +
                "name='" + name + '\'' +
                ", duration=" + duration +
                ", sets=" + sets +
                ", reps=" + reps +
                '}';
    }

    @Override
    public String getDetails() {
        return String.format("Strength Workout: %s | Duration: %d min | Sets: %d | Reps: %d", name, duration, sets,
                reps);
    }
}
