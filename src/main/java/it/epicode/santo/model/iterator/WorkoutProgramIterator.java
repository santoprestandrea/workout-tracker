package it.epicode.santo.model.iterator;

import it.epicode.santo.model.core.Workout;
import it.epicode.santo.model.core.WorkoutProgram;
import it.epicode.santo.model.exceptions.InvalidWorkoutDataException;

public class WorkoutProgramIterator implements WorkoutIterator {
    private WorkoutProgram program;
    private int index = 0;

    public WorkoutProgramIterator(WorkoutProgram program) {
        if (program == null) {
            throw new InvalidWorkoutDataException("workout session can't be null");
        }
        this.program = program;
    }

    @Override
    public boolean hasNext() {
        return index < program.getSize();
    }

    @Override
    public Workout next() {
        if (!hasNext()) {
            throw new java.util.NoSuchElementException();
        }
        return program.getWorkoutAt(index++);
    }

    @Override
    public boolean isEmpty() {
        return program.getSize() == 0;
    }

    @Override
    public void reset() {
        index = 0;
    }

}
