package it.epicode.santo.util;

import java.io.IOException;
import java.util.logging.FileHandler;
import java.util.logging.Handler;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.logging.SimpleFormatter;


/**
 * LoggerManager is a singleton class that manages the logger configuration
 * for the application.
 * It is responsible for initializing the logger and configuring it to write to
 * a file.
 */
public class LoggerManager {
    private static final Logger LOGGER = Logger.getLogger("WorkoutTrackerLogger");
    private static final String LOG_FILE = "workout_tracker.log";
    private static boolean initialized = false;

    // private constructor to prevent instantiation
    private LoggerManager() {
        // prevent instantiation
    }

    /**
     * Returns an instance of the logger configured to write to a file.
     * If the logger has not been initialized yet, it initializes it.
     *
     * @return Logger configured to write to a file.
     */
    public static Logger getLogger() {
        if (!initialized) {
            try {
                // remove eventual existing logger handers to avoid
                Handler[] handlers = LOGGER.getHandlers();
                for (Handler handler : handlers) {
                    LOGGER.removeHandler(handler);
                }

                // create file hander to write a file(append = true)
                FileHandler fileHandler = new FileHandler(LOG_FILE, true);
                fileHandler.setFormatter(new SimpleFormatter());
                LOGGER.addHandler(fileHandler);

                // Disable logging to the console
                LOGGER.setUseParentHandlers(false);

                LOGGER.setLevel(Level.INFO);
                initialized = true;
            } catch (IOException e) {
                throw new RuntimeException("Unable to initialize logger to file: " + e.getMessage(), e);
            }
        }
        return LOGGER;
    }

}