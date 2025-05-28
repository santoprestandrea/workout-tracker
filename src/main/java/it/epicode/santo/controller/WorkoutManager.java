package it.epicode.santo.controller;

import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

import it.epicode.santo.model.core.Workout;
import it.epicode.santo.model.core.WorkoutProgram;
import it.epicode.santo.model.core.WorkoutSession;
import it.epicode.santo.model.exceptions.InvalidWorkoutDataException;
import it.epicode.santo.model.factory.CardioWorkoutCreator;
import it.epicode.santo.model.factory.StrengthWorkoutCreator;
import it.epicode.santo.model.iterator.WorkoutIterator;
import it.epicode.santo.util.LoggerManager;
import it.epicode.santo.util.WorkoutFileHandler;

public class WorkoutManager {
    private static final Logger LOGGER = LoggerManager.getLogger();

    // collections to handle data
    private List<Workout> allWorkouts;
    private List<WorkoutSession> sessions;
    private List<WorkoutProgram> programs;

    // Factory to create workouts
    private StrengthWorkoutCreator strengthFactory;
    private CardioWorkoutCreator cardioFactory;

    // File handler for persistency
    private WorkoutFileHandler fileHandler;

    // Singleton pattern
    private static WorkoutManager instance;

    private WorkoutManager() {
        this.allWorkouts = new ArrayList<>();
        this.sessions = new ArrayList<>();
        this.programs = new ArrayList<>();
        this.strengthFactory = new StrengthWorkoutCreator();
        this.cardioFactory = new CardioWorkoutCreator();
        this.fileHandler = new WorkoutFileHandler();
        LOGGER.info("WorkoutManager inizializzato");
    }

    // === WORKOUT CREATION ===

    public Workout createStrengthWorkout(String name, int duration, int sets, int reps)
            throws InvalidWorkoutDataException {
        try {
            Workout workout = strengthFactory.createWorkout(name, duration, sets, reps);
            allWorkouts.add(workout);
            LOGGER.info("Create StrengthWorkout: " + workout.getName());
            return workout;
        } catch (InvalidWorkoutDataException e) {
            LOGGER.warning("Error during creation of StrengthWorkout: " + e.getMessage());
            throw e;
        }
    }

    public Workout createCardioWorkout(String name, int duration, double distanceKm)
            throws InvalidWorkoutDataException {
        try {
            Workout workout = cardioFactory.createWorkout(name, duration, distanceKm);
            allWorkouts.add(workout);
            LOGGER.info("Create CardioWorkout: " + workout.getName());
            return workout;
        } catch (InvalidWorkoutDataException e) {
            LOGGER.warning("Error during creation of CardioWorkout: " + e.getMessage());
            throw e;
        }
    }

    // === SESSIONS HANDLER ===

    public WorkoutSession createSession(String name) throws InvalidWorkoutDataException {
        try {
            WorkoutSession session = new WorkoutSession(name);
            sessions.add(session);
            LOGGER.info("Creation WorkoutSession: " + session.getName());
            return session;
        } catch (Exception e) {
            LOGGER.warning("Error during the creation of WorkoutSession: " + e.getMessage());
            throw new InvalidWorkoutDataException("Impossible create session: " + e.getMessage(), e);
        }
    }

    public void addWorkoutToSession(WorkoutSession session, Workout workout) {
        if (session == null || workout == null) {
            throw new IllegalArgumentException("Session and workout can't be null");
        }
        session.addWorkout(workout);
        LOGGER.info("Add workout '" + workout.getName() + "' to session '" + session.getName() + "'");
    }

    // === PROGRAMS HANDLER ===

    public WorkoutProgram createProgram(String name) throws InvalidWorkoutDataException {
        try {
            WorkoutProgram program = new WorkoutProgram(name);
            programs.add(program);
            LOGGER.info("Create WorkoutProgram: " + program.getName());
            return program;
        } catch (Exception e) {
            LOGGER.warning("Error nella creation of WorkoutProgram: " + e.getMessage());
            throw new InvalidWorkoutDataException("Impossible to create a programma: " + e.getMessage(), e);
        }
    }

    public void addWorkoutToProgram(WorkoutProgram program, Workout workout) {
        if (program == null || workout == null) {
            throw new IllegalArgumentException("Program e workout can't be null");
        }
        program.addWorkoutToSchedule(workout);
        LOGGER.info("Add workout '" + workout.getName() + "' to program '" + program.getName() + "'");
    }

    // === SEARCH OPERATIONS ===

    public List<Workout> getAllWorkouts() {
        return new ArrayList<>(allWorkouts);
    }

    public List<WorkoutSession> getAllSessions() {
        return new ArrayList<>(sessions);
    }

    public List<WorkoutProgram> getAllPrograms() {
        return new ArrayList<>(programs);
    }

    public Workout findWorkoutByName(String name) {
        if (name == null || name.trim().isEmpty()) {
            return null;
        }
        return allWorkouts.stream()
                .filter(w -> w.getName().equalsIgnoreCase(name.trim()))
                .findFirst()
                .orElse(null);
    }

    public WorkoutSession findSessionByName(String name) {
        if (name == null || name.trim().isEmpty()) {
            return null;
        }
        return sessions.stream()
                .filter(s -> s.getName().equalsIgnoreCase(name.trim()))
                .findFirst()
                .orElse(null);
    }

    public WorkoutProgram findProgramByName(String name) {
        if (name == null || name.trim().isEmpty()) {
            return null;
        }
        return programs.stream()
                .filter(p -> p.getName().equalsIgnoreCase(name.trim()))
                .findFirst()
                .orElse(null);
    }

    // === REMOVAL OPERATIONS ===

    public boolean removeWorkout(Workout workout) {
        if (workout == null)
            return false;

        boolean removed = allWorkouts.remove(workout);
        if (removed) {
            LOGGER.info("Removed workout: " + workout.getName());
        }
        return removed;
    }

    public boolean removeSession(WorkoutSession session) {
        if (session == null)
            return false;

        boolean removed = sessions.remove(session);
        if (removed) {
            LOGGER.info("Removed session: " + session.getName());
        }
        return removed;
    }

    public boolean removeProgram(WorkoutProgram program) {
        if (program == null)
            return false;

        boolean removed = programs.remove(program);
        if (removed) {
            LOGGER.info("Removed program: " + program.getName());
        }
        return removed;
    }

    // === STATISTICS ===


    public int getTotalWorkouts() {
        return allWorkouts.size();
    }

    public int getTotalSessions() {
        return sessions.size();
    }

    public int getTotalPrograms() {
        return programs.size();
    }

    public int getTotalDurationMinutes() {
        return allWorkouts.stream()
                .mapToInt(Workout::getDuration)
                .sum();
    }

    // === PERSISTENCY ===

    public void saveToFile(String filename) throws Exception {
        try {
            fileHandler.saveWorkouts(filename, allWorkouts, sessions, programs);
            LOGGER.info("Data loaded into file: " + filename);
        } catch (Exception e) {
            LOGGER.severe("Error during loading: " + e.getMessage());
            throw e;
        }
    }

    public void loadFromFile(String filename) throws Exception {
        try {
            // Load all data from file
            WorkoutFileHandler.WorkoutData data = fileHandler.loadWorkouts(filename);

            // Update internal lists
            this.allWorkouts.clear();
            this.allWorkouts.addAll(data.workouts);

            this.sessions.clear();
            this.sessions.addAll(data.sessions);

            this.programs.clear();
            this.programs.addAll(data.programs);

            LOGGER.info("Data loaded from file: " + filename);
        } catch (Exception e) {
            LOGGER.severe("Error during loading: " + e.getMessage());
            throw e;
        }
    }

    // === UTILITY FOR ITERAZIONE ===

    public void printAllWorkouts() {
        LOGGER.info("=== ALL WORKOUTS ===");
        for (Workout workout : allWorkouts) {
            LOGGER.info(workout.toString());
        }
    }

    public void printSessionDetails(WorkoutSession session) {
        if (session == null)
            return;

        LOGGER.info("=== SESSION DETAILS: " + session.getName() + " ===");
        WorkoutIterator iterator = session.createIterator();
        while (iterator.hasNext()) {
            Workout workout = iterator.next();
            LOGGER.info("  - " + workout.toString());
        }
        LOGGER.info("Total duratino of session: " + session.getDuration() + " minutes");
    }

}
