package it.epicode.santo.model.core;

public class StrenghtWorkout implements Workout {
    private String name;
    private int duration;
    private int sets;
    private int reps;

    public StrenghtWorkout(String name, int duration, int sets, int reps) {
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
        if (sets <= 0) {
            /*
             * TODO lancia eccezione personalizzata
             */
        }
        if (reps <= 0) {
            /*
             * TODO lancia eccezione personalizzata
             */
        }

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
