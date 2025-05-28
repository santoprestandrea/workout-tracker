package it.epicode.santo;

import java.util.Arrays;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import org.junit.Test;

import it.epicode.santo.model.core.CardioWorkout;
import it.epicode.santo.model.core.StrengthWorkout;
import it.epicode.santo.model.core.Workout;
import it.epicode.santo.model.core.WorkoutProgram;
import it.epicode.santo.model.core.WorkoutSession;
import it.epicode.santo.util.WorkoutFileHandler.WorkoutData;

public class WorkoutDTOTest {

    @Test
    public void testWorkoutDataContainer() {
        Workout w = new StrengthWorkout("Panca", 30, 4, 10);
        WorkoutSession s = new WorkoutSession("Sessione");
        WorkoutProgram p = new WorkoutProgram("Programma");

        WorkoutData data = new WorkoutData();
        data.workouts.add(w);
        data.sessions.add(s);
        data.programs.add(p);

        assertEquals(Arrays.asList(w), data.workouts);
        assertEquals(Arrays.asList(s), data.sessions);
        assertEquals(Arrays.asList(p), data.programs);
    }

    @Test
    public void testWorkoutDataContainerEmpty() {
        WorkoutData data = new WorkoutData();
        assertTrue(data.workouts.isEmpty());
        assertTrue(data.sessions.isEmpty());
        assertTrue(data.programs.isEmpty());
    }

    @Test
    public void testWorkoutDataContainerAddMultiple() {
        Workout w1 = new StrengthWorkout("Panca", 30, 4, 10);
        Workout w2 = new CardioWorkout("Corsa", 20, 5.0);
        WorkoutSession s = new WorkoutSession("Sessione");
        WorkoutProgram p = new WorkoutProgram("Programma");

        WorkoutData data = new WorkoutData();
        data.workouts.addAll(Arrays.asList(w1, w2));
        data.sessions.add(s);
        data.programs.add(p);

        assertEquals(2, data.workouts.size());
        assertEquals(1, data.sessions.size());
        assertEquals(1, data.programs.size());
    }
}