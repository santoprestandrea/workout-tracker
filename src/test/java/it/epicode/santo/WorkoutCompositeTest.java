package it.epicode.santo;

import static org.junit.Assert.assertEquals;
import org.junit.Test;

import it.epicode.santo.model.core.CardioWorkout;
import it.epicode.santo.model.core.StrengthWorkout;
import it.epicode.santo.model.core.WorkoutProgram;
import it.epicode.santo.model.core.WorkoutSession;

public class WorkoutCompositeTest {

    @Test
    public void testSessionTotalDuration() {
        WorkoutSession session = new WorkoutSession("Sessione");
        session.addWorkout(new StrengthWorkout("Panca", 30, 4, 10));
        session.addWorkout(new CardioWorkout("Corsa", 20, 3.0));
        assertEquals(50, session.getDuration());
    }

    @Test
    public void testProgramTotalDuration() {
        WorkoutSession session = new WorkoutSession("Sessione");
        session.addWorkout(new StrengthWorkout("Panca", 30, 4, 10));
        session.addWorkout(new CardioWorkout("Corsa", 20, 3.0));

        WorkoutProgram program = new WorkoutProgram("Programma");
        program.addWorkoutToSchedule(session);
        program.addWorkoutToSchedule(new StrengthWorkout("Squat", 40, 5, 8));

        // 30+20 dalla sessione + 40 dallo squat
        assertEquals(90, program.getDuration());
    }

    @Test
    public void testRemoveWorkoutFromSession() {
        WorkoutSession session = new WorkoutSession("Sessione");
        StrengthWorkout w = new StrengthWorkout("Panca", 30, 4, 10);
        session.addWorkout(w);
        assertEquals(30, session.getDuration());
        session.removeWorkout(w);
        assertEquals(0, session.getDuration());
    }
}
