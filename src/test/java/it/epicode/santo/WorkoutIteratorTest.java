package it.epicode.santo;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import org.junit.Test;

import it.epicode.santo.model.core.CardioWorkout;
import it.epicode.santo.model.core.StrengthWorkout;
import it.epicode.santo.model.core.WorkoutProgram;
import it.epicode.santo.model.core.WorkoutSession;
import it.epicode.santo.model.iterator.WorkoutIterator;

public class WorkoutIteratorTest {

    @Test
    public void testWorkoutSessionIterator() {
        WorkoutSession session = new WorkoutSession("Sessione Test");
        StrengthWorkout w1 = new StrengthWorkout("Panca", 45, 4, 10);
        CardioWorkout w2 = new CardioWorkout("Corsa", 30, 5.5);

        session.addWorkout(w1);
        session.addWorkout(w2);

        WorkoutIterator iterator = session.createIterator();

        assertFalse(iterator.isEmpty());
        assertTrue(iterator.hasNext());
        assertEquals(w1, iterator.next());
        assertTrue(iterator.hasNext());
        assertEquals(w2, iterator.next());
        assertFalse(iterator.hasNext());

        iterator.reset();
        assertTrue(iterator.hasNext());
        assertEquals(w1, iterator.next());
    }

    @Test
    public void testWorkoutProgramIterator() {
        WorkoutSession session = new WorkoutSession("Sessione Programma");
        StrengthWorkout w1 = new StrengthWorkout("Squat", 40, 5, 8);
        CardioWorkout w2 = new CardioWorkout("Bici", 60, 20);

        session.addWorkout(w1);
        session.addWorkout(w2);

        WorkoutProgram program = new WorkoutProgram("Programma Test");
        program.addWorkoutToSchedule(session);
        StrengthWorkout w3 = new StrengthWorkout("Push-up", 20, 3, 15);
        program.addWorkoutToSchedule(w3);

        WorkoutIterator iterator = program.createIterator();

        assertFalse(iterator.isEmpty());
        assertTrue(iterator.hasNext());
        assertEquals(session, iterator.next());
        assertTrue(iterator.hasNext());
        assertEquals(w3, iterator.next());
        assertFalse(iterator.hasNext());

        iterator.reset();
        assertTrue(iterator.hasNext());
        assertEquals(session, iterator.next());
    }

    @Test
    public void testEmptySessionIterator() {
        WorkoutSession session = new WorkoutSession("Vuota");
        WorkoutIterator iterator = session.createIterator();

        assertTrue(iterator.isEmpty());
        assertFalse(iterator.hasNext());
    }

    @Test
    public void testEmptyProgramIterator() {
        WorkoutProgram program = new WorkoutProgram("Vuoto");
        WorkoutIterator iterator = program.createIterator();

        assertTrue(iterator.isEmpty());
        assertFalse(iterator.hasNext());
    }
}