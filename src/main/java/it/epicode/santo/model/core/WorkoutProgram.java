package it.epicode.santo.model.core;

import java.util.ArrayList;
import java.util.List;

public class WorkoutProgram implements Workout {
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

}
