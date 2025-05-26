package it.epicode.santo.model.exceptions;
/**
 * Exception thrown to indicate that invalid data was provided for a workout.
 * <p>
 * This exception is typically used to signal that the input data for creating or updating
 * a workout does not meet the required validation criteria.
 * </p>
 */
public class InvalidWorkoutDataException extends RuntimeException{
    /**
     * Constructs a new InvalidWorkoutDataException with the specified detail
     * message.
     *
     * @param message the detail message explaining the reason for the exception
     */
    public InvalidWorkoutDataException(String message){
        super(message);
    }
    /**
     * Constructs a new InvalidWorkoutDataException with the specified detail
     * message and cause.
     *
     * @param message the detail message explaining the reason for the exception
     * @param cause   the cause of the exception (which is saved for later retrieval
     *                by the {@link #getCause()} method)
     */
    public InvalidWorkoutDataException(String message, Throwable cause) {
        super(message, cause);
    }
}
