package it.epicode.santo.model.core;

public class CardioWorkout implements Workout {
    private String name;
    private int duration; // minutes
    private double distanceKm;

    public CardioWorkout(String name, int duration, double distanceKm) {
        // input validation
        if (name == null || name.trim().isEmpty()) {
            /*
             * TODO lancia eccezione personalizzata
             */
        }
        if (duration <= 0) {
            /*
             * TODO lancia eccezione personalizzata
             */
        }
        if (distanceKm <= 0) {
            /*
             * TODO lancia eccezione personalizzata
             */
        }

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
