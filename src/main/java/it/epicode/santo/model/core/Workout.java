package it.epicode.santo.model.core;

/**
 * Represents a workout with a name and duration.
 * Implementing classes should provide details about the workout's name and its duration in minutes.
 */
public interface Workout {
    String getName();
    int getDuration();
    String getDetails(); 
}
