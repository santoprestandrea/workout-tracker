package it.epicode.santo;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertSame;
import org.junit.Test;

import it.epicode.santo.controller.WorkoutManager;

public class WorkoutManagerSingletonTest {

    @Test
    public void testSingletonInstance() {
        WorkoutManager m1 = WorkoutManager.getInstance();
        WorkoutManager m2 = WorkoutManager.getInstance();
        assertSame(m1, m2);
    }

    @Test
    public void testSingletonInitialization() {
        WorkoutManager manager = WorkoutManager.getInstance();
        assertNotNull(manager);
    }

}
