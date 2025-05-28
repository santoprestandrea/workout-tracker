package it.epicode.santo;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import org.junit.Test;

import it.epicode.santo.model.core.CardioWorkout;
import it.epicode.santo.model.core.StrengthWorkout;
import it.epicode.santo.model.core.Workout;
import it.epicode.santo.model.exceptions.InvalidWorkoutDataException;
import it.epicode.santo.model.factory.CardioWorkoutCreator;
import it.epicode.santo.model.factory.StrengthWorkoutCreator;
import it.epicode.santo.model.factory.WorkoutCreator;

public class WorkoutFactoryTest {

    @Test
    public void testStrengthWorkoutCreator_ValidParams() {
        WorkoutCreator creator = new StrengthWorkoutCreator();
        Workout workout = creator.createWorkout("Bench Press", 40, 4, 12);

        assertTrue(workout instanceof StrengthWorkout);
        assertEquals("Bench Press", workout.getName());
        assertEquals(40, workout.getDuration());
        assertEquals(4, ((StrengthWorkout) workout).getSets());
        assertEquals(12, ((StrengthWorkout) workout).getReps());
    }

    @Test
    public void testCardioWorkoutCreator_ValidParams() {
        WorkoutCreator creator = new CardioWorkoutCreator();
        Workout workout = creator.createWorkout("Running", 30, 5.0);

        assertTrue(workout instanceof CardioWorkout);
        assertEquals("Running", workout.getName());
        assertEquals(30, workout.getDuration());
        assertEquals(5.0, ((CardioWorkout) workout).getDistanceKm(), 0.01);
    }

    @Test(expected = InvalidWorkoutDataException.class)
    public void testStrengthWorkoutCreator_InvalidName() {
        WorkoutCreator creator = new StrengthWorkoutCreator();
        creator.createWorkout("", 40, 4, 12);
    }

    @Test(expected = InvalidWorkoutDataException.class)
    public void testStrengthWorkoutCreator_InvalidDuration() {
        WorkoutCreator creator = new StrengthWorkoutCreator();
        creator.createWorkout("Bench Press", 0, 4, 12);
    }

    @Test(expected = InvalidWorkoutDataException.class)
    public void testStrengthWorkoutCreator_InvalidSets() {
        WorkoutCreator creator = new StrengthWorkoutCreator();
        creator.createWorkout("Bench Press", 40, 0, 12);
    }

    @Test(expected = InvalidWorkoutDataException.class)
    public void testStrengthWorkoutCreator_InvalidReps() {
        WorkoutCreator creator = new StrengthWorkoutCreator();
        creator.createWorkout("Bench Press", 40, 4, 0);
    }

    @Test(expected = InvalidWorkoutDataException.class)
    public void testStrengthWorkoutCreator_MissingParams() {
        WorkoutCreator creator = new StrengthWorkoutCreator();
        creator.createWorkout("Bench Press", 40); // Missing sets and reps
    }

    @Test(expected = InvalidWorkoutDataException.class)
    public void testCardioWorkoutCreator_InvalidName() {
        WorkoutCreator creator = new CardioWorkoutCreator();
        creator.createWorkout("", 30, 5.0);
    }

    @Test(expected = InvalidWorkoutDataException.class)
    public void testCardioWorkoutCreator_InvalidDuration() {
        WorkoutCreator creator = new CardioWorkoutCreator();
        creator.createWorkout("Running", -10, 5.0);
    }

    @Test(expected = InvalidWorkoutDataException.class)
    public void testCardioWorkoutCreator_InvalidDistance() {
        WorkoutCreator creator = new CardioWorkoutCreator();
        creator.createWorkout("Running", 30, -2.0);
    }

    @Test(expected = InvalidWorkoutDataException.class)
    public void testCardioWorkoutCreator_MissingDistanceParam() {
        WorkoutCreator creator = new CardioWorkoutCreator();
        creator.createWorkout("Running", 30); // Missing distance
    }

    @Test(expected = InvalidWorkoutDataException.class)
    public void testCardioWorkoutCreator_MissingDurationParam() {
        WorkoutCreator creator = new CardioWorkoutCreator();
        creator.createWorkout("Running", 0, 5.0); // Missing duration
    }

}