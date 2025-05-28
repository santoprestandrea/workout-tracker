package it.epicode.santo.view;

import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.util.List;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.ListSelectionModel;
import javax.swing.table.DefaultTableModel;

import it.epicode.santo.controller.WorkoutManager;
import it.epicode.santo.model.core.CardioWorkout;
import it.epicode.santo.model.core.StrengthWorkout;
import it.epicode.santo.model.core.Workout;

/**
 * Panel that displays a list of workouts in a table and provides controls
 * to refresh, delete, and load workouts from file.
 */
public class WorkoutListPanel extends JPanel {
    private WorkoutManager manager; // Reference to the workout manager/controller
    private JTable workoutTable; // Table to display workouts
    private DefaultTableModel tableModel; // Model for the table data
    private JButton refreshButton; // Button to refresh the workout list
    private JButton deleteButton; // Button to delete the selected workout
    private JButton loadButton; // Button to load workouts from file

    // Listener for workout selection events
    private WorkoutSelectionListener selectionListener;

    /**
     * Interface for listening to workout selection events.
     */
    public interface WorkoutSelectionListener {
        void onWorkoutSelected(Workout workout);
    }

    /**
     * Constructs the WorkoutListPanel with the given WorkoutManager.
     * 
     * @param manager the workout manager/controller
     */
    public WorkoutListPanel(WorkoutManager manager) {
        this.manager = manager;
        initComponents(); // Initialize UI components
        loadWorkouts(); // Load workouts into the table
    }

    /**
     * Initializes the UI components and layout of the panel.
     */
    private void initComponents() {
        setLayout(new BorderLayout());
        setBorder(BorderFactory.createTitledBorder("Workout List"));

        // Define column names for the table
        String[] columnNames = { "Name", "Type", "Duration (min)", "Details" };
        // Create a non-editable table model
        tableModel = new DefaultTableModel(columnNames, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false; // Make table cells non-editable
            }
        };

        // Create the table and set selection mode to single row
        workoutTable = new JTable(tableModel);
        workoutTable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        // Add a listener to handle row selection changes
        workoutTable.getSelectionModel().addListSelectionListener(e -> {
            if (!e.getValueIsAdjusting()) {
                handleWorkoutSelection();
            }
        });

        // Add the table to a scroll pane and add it to the center of the panel
        JScrollPane scrollPane = new JScrollPane(workoutTable);
        add(scrollPane, BorderLayout.CENTER);

        // Create a panel for the action buttons
        JPanel buttonPanel = new JPanel(new FlowLayout());

        // Create and configure the refresh button
        refreshButton = new JButton("Refresh");
        refreshButton.addActionListener(this::handleRefresh);

        // Create and configure the delete button (initially disabled)
        deleteButton = new JButton("Delete");
        deleteButton.addActionListener(this::handleDelete);
        deleteButton.setEnabled(false);

        // Create and configure the load button
        loadButton = new JButton("Load");
        loadButton.addActionListener(this::handleLoad);

        // Add buttons to the button panel
        buttonPanel.add(refreshButton);
        buttonPanel.add(deleteButton);
        buttonPanel.add(loadButton);

        // Add the button panel to the bottom of the main panel
        add(buttonPanel, BorderLayout.SOUTH);
    }

    /**
     * Loads all workouts from the manager and displays them in the table.
     * Clears the table before loading new data.
     */
    public void loadWorkouts() {
        tableModel.setRowCount(0); // Clear the table

        List<Workout> workouts = manager.getAllWorkouts(); // Retrieve all workouts
        for (Workout workout : workouts) {
            String type = getWorkoutType(workout); // Get workout type as string
            String details = getWorkoutDetails(workout); // Get workout details

            Object[] row = {
                    workout.getName(),
                    type,
                    workout.getDuration(),
                    details
            };
            tableModel.addRow(row); // Add row to the table
        }
    }

    /**
     * Returns the type of the workout as a string.
     * 
     * @param workout the workout object
     * @return "Strength" for StrengthWorkout, "Cardio" for CardioWorkout, "Unknown"
     *         otherwise
     */
    private String getWorkoutType(Workout workout) {
        if (workout instanceof StrengthWorkout) {
            return "Strength";
        } else if (workout instanceof CardioWorkout) {
            return "Cardio";
        }
        return "Unknown";
    }

    /**
     * Returns a string with details specific to the workout type.
     * 
     * @param workout the workout object
     * @return details string (e.g., "3 sets x 10 reps" or "5.0 km")
     */
    private String getWorkoutDetails(Workout workout) {
        if (workout instanceof StrengthWorkout) {
            StrengthWorkout sw = (StrengthWorkout) workout;
            return sw.getSets() + " sets x " + sw.getReps() + " reps";
        } else if (workout instanceof CardioWorkout) {
            CardioWorkout cw = (CardioWorkout) workout;
            return cw.getDistanceKm() + " km";
        }
        return "";
    }

    /**
     * Handles the refresh button click event.
     * Reloads the workouts and shows a confirmation dialog.
     * 
     * @param e the action event
     */
    private void handleRefresh(ActionEvent e) {
        loadWorkouts();
        JOptionPane.showMessageDialog(this, "List refreshed!");
    }

    /**
     * Handles the delete button click event.
     * Prompts the user for confirmation and deletes the selected workout if
     * confirmed.
     * 
     * @param e the action event
     */
    private void handleDelete(ActionEvent e) {
        int selectedRow = workoutTable.getSelectedRow();
        if (selectedRow == -1)
            return; // No row selected

        String workoutName = (String) tableModel.getValueAt(selectedRow, 0);
        Workout workout = manager.findWorkoutByName(workoutName);

        if (workout != null) {
            int result = JOptionPane.showConfirmDialog(
                    this,
                    "Are you sure you want to delete the workout '" + workoutName + "'?",
                    "Confirm Deletion",
                    JOptionPane.YES_NO_OPTION);

            if (result == JOptionPane.YES_OPTION) {
                boolean removed = manager.removeWorkout(workout);
                if (removed) {
                    loadWorkouts();
                    JOptionPane.showMessageDialog(this, "Workout deleted successfully!");
                } else {
                    JOptionPane.showMessageDialog(this, "Error deleting workout.",
                            "Error", JOptionPane.ERROR_MESSAGE);
                }
            }
        }
    }

    /**
     * Handles the load button click event.
     * Loads workouts from the "workouts.json" file and updates the table.
     * 
     * @param e the action event
     */
    private void handleLoad(ActionEvent e) {
        String filename = "workouts.json";
        try {
            manager.loadFromFile(filename);
            loadWorkouts();
            JOptionPane.showMessageDialog(this, "Workouts loaded from file 'workouts.json' successfully!", "Success",
                    JOptionPane.INFORMATION_MESSAGE);
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "Error loading file 'workouts.json':\n" + ex.getMessage(),
                    "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    /**
     * Handles workout selection changes in the table.
     * Enables or disables the delete button based on selection.
     */
    private void handleWorkoutSelection() {
        boolean hasSelection = workoutTable.getSelectedRow() != -1;
        deleteButton.setEnabled(hasSelection);
    }

    /**
     * Returns the currently selected workout in the table, or null if none is
     * selected.
     * 
     * @return the selected Workout object, or null
     */
    private Workout getSelectedWorkout() {
        int selectedRow = workoutTable.getSelectedRow();
        if (selectedRow == -1)
            return null;

        String workoutName = (String) tableModel.getValueAt(selectedRow, 0);
        return manager.findWorkoutByName(workoutName);
    }

    /**
     * Sets the listener for workout selection events.
     * 
     * @param listener the listener to set
     */
    public void setSelectionListener(WorkoutSelectionListener listener) {
        this.selectionListener = listener;
    }

    /**
     * Clears the current selection in the table.
     */
    public void clearSelection() {
        workoutTable.clearSelection();
    }

    /**
     * Refreshes the workout data in the table.
     */
    public void refreshData() {
        loadWorkouts();
    }
}

