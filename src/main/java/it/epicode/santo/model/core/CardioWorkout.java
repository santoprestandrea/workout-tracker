package it.epicode.santo.model.core;

/**
 * Represents a cardio workout session, including its name, duration, and distance.
 * Implements the {@link Workout} interface.
 * <p>
 * This class validates input data to ensure that the workout name is not empty,
 * the duration is positive, and the distance is non-negative.
 * </p>
 *
 * <p>
 * Example usage:
 * <pre>
 *     CardioWorkout run = new CardioWorkout("Morning Run", 30, 5.0);
 * </pre>
 * </p>
 *
 * @see Workout
 * 
 */
public class CardioWorkout implements Workout {
    private String name;
    private int duration; // minutes
    private double distanceKm;

    public CardioWorkout(String name, int duration, double distanceKm) {
        this.name = name;
        this.duration = duration;
        this.distanceKm = distanceKm;
    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public int getDuration() {
        return duration;
    }

    public double getDistanceKm() {
        return distanceKm;
    }

    @Override
    public String toString() {
        return "CardioWorkout{" +
                "name='" + name + '\'' +
                ", duration=" + duration +
                ", distanceKm=" + distanceKm +
                '}';
    }

    @Override
    public String getDetails() {
        return String.format("Cardio Workout: %s | Duration: %d min | Distance: %.2f km", name, duration, distanceKm);
    }

}
