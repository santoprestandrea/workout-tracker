package it.epicode.santo.util;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

import org.json.JSONArray;
import org.json.JSONObject;
import org.json.JSONTokener;

import it.epicode.santo.model.core.CardioWorkout;
import it.epicode.santo.model.core.StrenghtWorkout;
import it.epicode.santo.model.core.Workout;
import it.epicode.santo.model.core.WorkoutProgram;
import it.epicode.santo.model.core.WorkoutSession;
import it.epicode.santo.model.exceptions.InvalidWorkoutDataException;
import it.epicode.santo.model.factory.CardioWorkoutCreator;
import it.epicode.santo.model.factory.StrengthWorkoutCreator;
import it.epicode.santo.model.factory.WorkoutCreator;
import it.epicode.santo.util.WorkoutFileHandler.WorkoutData;

public class WorkoutFileHandler {
    private static final Logger LOGGER = LoggerManager.getLogger();

    /**
     * Saves all data (workouts, sessions, programs) to a JSON file.
     */
    public void saveWorkouts(String fileName, List<Workout> workouts,
            List<WorkoutSession> sessions, List<WorkoutProgram> programs) {
        try {
            JSONObject root = new JSONObject();

            // Save individual workouts
            JSONArray workoutsArray = new JSONArray();
            for (Workout w : workouts) {
                workoutsArray.put(workoutToJSON(w));
            }
            root.put("workouts", workoutsArray);

            // Save sessions
            JSONArray sessionsArray = new JSONArray();
            for (WorkoutSession session : sessions) {
                JSONObject sessionObj = new JSONObject();
                sessionObj.put("name", session.getName());
                sessionObj.put("type", "session");

                JSONArray sessionWorkouts = new JSONArray();
                for (int i = 0; i < session.getSize(); i++) {
                    sessionWorkouts.put(workoutToJSON(session.getWorkoutAt(i)));
                }
                sessionObj.put("workouts", sessionWorkouts);
                sessionsArray.put(sessionObj);
            }
            root.put("sessions", sessionsArray);

            // Save programs
            JSONArray programsArray = new JSONArray();
            for (WorkoutProgram program : programs) {
                JSONObject programObj = new JSONObject();
                programObj.put("name", program.getName());
                programObj.put("type", "program");

                JSONArray programSchedule = new JSONArray();
                for (int i = 0; i < program.getSize(); i++) {
                    Workout w = program.getWorkoutAt(i);
                    if (w instanceof WorkoutSession) {
                        JSONObject ref = new JSONObject();
                        ref.put("type", "session_ref");
                        ref.put("name", w.getName());
                        programSchedule.put(ref);
                    } else {
                        programSchedule.put(workoutToJSON(w));
                    }
                }
                programObj.put("schedule", programSchedule);
                programsArray.put(programObj);
            }
            root.put("programs", programsArray);

            // write to file
            try (FileWriter file = new FileWriter(fileName)) {
                file.write(root.toString(2));
                LOGGER.info("save completed succssefully to: " + fileName);
            }
        } catch (IOException e) {
            LOGGER.severe("Error during saving: " + e.getMessage());
            throw new InvalidWorkoutDataException("Error during saving", e);
        }
    }

    /**
     * Loads workout data from a JSON file and populates a {@link WorkoutData} object.
     * <p>
     * The method reads the specified file, parses its JSON content, and loads individual workouts,
     * workout sessions, and workout programs into the returned {@code WorkoutData} instance.
     * <ul>
     *   <li>If the file contains a "workouts" array, each workout is parsed and added to the workouts list.</li>
     *   <li>If the file contains a "sessions" array, each session is parsed, its workouts are loaded, and the session is added to the sessions list.</li>
     *   <li>If the file contains a "programs" array, each program is parsed, its schedule is loaded (including references to sessions or individual workouts), and the program is added to the programs list.</li>
     * </ul>
     * If the file is not found, empty lists are returned. If an error occurs during parsing, an
     * {@link InvalidWorkoutDataException} is thrown.
     *
     * @param fileName the path to the JSON file containing the workout data
     * @return a {@link WorkoutData} object populated with workouts, sessions, and programs loaded from the file
     * @throws InvalidWorkoutDataException if an error occurs during loading or parsing the file
     */
    public  WorkoutData loadWorkouts(String fileName){
        WorkoutData data = new WorkoutData();
        try(FileReader reader = new FileReader(fileName))
        {
            JSONObject root = new JSONObject(new JSONTokener(reader));

            //load individual workouts
            if(root.has("workouts")){
                JSONArray workoutsArray = root.getJSONArray("workouts");
                for (int i = 0; i < workoutsArray.length(); i++) {
                    Workout workout = jsonToWorkout(workoutsArray.getJSONObject(i));
                    if (workout != null) {
                        data.workouts.add(workout);
                    }
                }
            }

            // Load sessions
            if (root.has("sessions")) {
                JSONArray sessionsArray = root.getJSONArray("sessions");
                for (int i = 0; i < sessionsArray.length(); i++) {
                    JSONObject sessionObj = sessionsArray.getJSONObject(i);
                    WorkoutSession session = new WorkoutSession(sessionObj.getString("name"));

                    JSONArray sessionWorkouts = sessionObj.getJSONArray("workouts");
                    for (int j = 0; j < sessionWorkouts.length(); j++) {
                        Workout workout = jsonToWorkout(sessionWorkouts.getJSONObject(j));
                        if (workout != null) {
                            session.addWorkout(workout);
                        }
                    }
                    data.sessions.add(session);
                }
            }

            // Load programs
            if (root.has("programs")) {
                JSONArray programsArray = root.getJSONArray("programs");
                for (int i = 0; i < programsArray.length(); i++) {
                    JSONObject programObj = programsArray.getJSONObject(i);
                    WorkoutProgram program = new WorkoutProgram(programObj.getString("name"));

                    JSONArray schedule = programObj.getJSONArray("schedule");
                    for (int j = 0; j < schedule.length(); j++) {
                        JSONObject item = schedule.getJSONObject(j);

                        if ("session_ref".equals(item.optString("type"))) {
                            // Trova la sessione per nome
                            String sessionName = item.getString("name");
                            WorkoutSession session = data.sessions.stream()
                                    .filter(s -> s.getName().equals(sessionName))
                                    .findFirst().orElse(null);
                            if (session != null) {
                                program.addWorkoutToSchedule(session);
                            }
                        } else {
                            // Workout singolo
                            Workout workout = jsonToWorkout(item);
                            if (workout != null) {
                                program.addWorkoutToSchedule(workout);
                            }
                        }
                    }
                    data.programs.add(program);
                }
            }
            
            LOGGER.info("load successfully complitely from file " + fileName);

        } catch (FileNotFoundException e) {
            LOGGER.warning("filenotfound:" + fileName + ". Empty list will be returned");
        }
        catch(Exception e){
            LOGGER.severe("Error during loading" + e.getMessage());
            throw new InvalidWorkoutDataException("Error during loading " + e);
        }
        return data;
    }

    /**
     * Converts a {@link Workout} object into a {@link JSONObject} representation.
     * The resulting JSON object contains the workout's name and duration.
     * If the workout is an instance of {@link StrengthWorkout}, additional fields
     * such as type ("strength"), sets, and reps are included.
     * If the workout is an instance of {@link CardioWorkout}, additional fields
     * such as type ("cardio") and distance in kilometers are included.
     *
     * @param w the {@link Workout} object to convert
     * @return a {@link JSONObject} representing the workout
     */
    private JSONObject workoutToJSON(Workout w) {
        JSONObject obj = new JSONObject();
        obj.put("name", w.getName());
        obj.put("duration", w.getDuration());
        if (w instanceof StrenghtWorkout) {
            StrenghtWorkout sw = (StrenghtWorkout) w;
            obj.put("type", "strength");
            obj.put("sets", sw.getSets());
            obj.put("reps", sw.getReps());
        } else if (w instanceof CardioWorkout) {
            CardioWorkout cw = (CardioWorkout) w;
            obj.put("type", "cardio");
            obj.put("distanceKm", cw.getDistanceKm());
        }
        return obj;
    }

    /**
     * Converts a JSONObject representing a workout into a Workout object.
     * <p>
     * The method parses the JSON object to extract workout details such as name,
     * duration, and type.
     * Depending on the "type" field ("strength" or "cardio"), it creates the
     * appropriate Workout
     * instance using the corresponding WorkoutCreator implementation.
     * </p>
     *
     * @param obj the JSONObject containing workout data
     * @return a Workout object created from the JSON data, or
     *         {@code null} if
     *         parsing fails
     */
    private Workout jsonToWorkout(JSONObject obj) {
        try {
            String name = obj.getString("name");
            int duration = obj.getInt("duration");
            String type = obj.getString("type");

            WorkoutCreator creator;
            if ("strength".equals(type)) {
                int sets = obj.getInt("sets");
                int reps = obj.getInt("reps");
                creator = new StrengthWorkoutCreator();
                return creator.createWorkout(name, duration, sets, reps);
            } else if ("cardio".equals(type)) {
                double distanceKm = obj.getDouble("distanceKm");
                creator = new CardioWorkoutCreator();
                return creator.createWorkout(name, duration, distanceKm);
            }
        } catch (Exception e) {
            LOGGER.warning("Error parsing workout: " + e.getMessage());
        }
        return null;
    }

    /**
     * A container class that holds collections of workout-related data.
     * <p>
     * This class encapsulates lists of {@link Workout}, {@link WorkoutSession},
     * and {@link WorkoutProgram} objects, providing a convenient structure
     * for managing and transferring grouped workout data.
     * </p>
     */
    public static class WorkoutData {
        public List<Workout> workouts = new ArrayList<>();
        public List<WorkoutSession> sessions = new ArrayList<>();
        public List<WorkoutProgram> programs = new ArrayList<>();
    }

}
