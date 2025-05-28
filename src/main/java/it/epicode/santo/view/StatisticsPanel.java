package it.epicode.santo.view;

import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;

import javax.swing.JLabel;
import javax.swing.JPanel;

import it.epicode.santo.controller.WorkoutManager;

public class StatisticsPanel extends JPanel {

    private final WorkoutManager workoutManager;
    private JLabel totalWorkoutsLabel;
    private JLabel totalSessionsLabel;
    private JLabel totalProgramsLabel;
    private JLabel totalDurationLabel;

    /**
     * Constructs the statistics panel.
     * @param workoutManager the manager providing workout data
     */
    public StatisticsPanel(WorkoutManager workoutManager) {
        this.workoutManager = workoutManager;
        initializeUI(); // Set up the user interface
        refreshData();  // Populate the labels with data
    }

    /**
     * Initializes the user interface components and layout.
     */
    private void initializeUI() {
        setLayout(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10); // Padding between components
        gbc.anchor = GridBagConstraints.WEST;    // Align components to the left

        // Title label for the statistics panel
        JLabel titleLabel = new JLabel("General Statistics");
        titleLabel.setFont(new Font("Arial", Font.BOLD, 20));
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.gridwidth = 2;
        add(titleLabel, gbc);

        gbc.gridwidth = 1;
        gbc.gridy++;

        // Label for total number of workouts
        totalWorkoutsLabel = new JLabel();
        gbc.gridx = 0;
        add(new JLabel("Total number of workouts:"), gbc);
        gbc.gridx = 1;
        add(totalWorkoutsLabel, gbc);

        gbc.gridy++;
        gbc.gridx = 0;
        // Label for total number of sessions
        totalSessionsLabel = new JLabel();
        add(new JLabel("Total number of sessions:"), gbc);
        gbc.gridx = 1;
        add(totalSessionsLabel, gbc);

        gbc.gridy++;
        gbc.gridx = 0;
        // Label for total number of programs
        totalProgramsLabel = new JLabel();
        add(new JLabel("Total number of programs:"), gbc);
        gbc.gridx = 1;
        add(totalProgramsLabel, gbc);

        gbc.gridy++;
        gbc.gridx = 0;
        // Label for total workout duration in minutes
        totalDurationLabel = new JLabel();
        add(new JLabel("Total workout duration (min):"), gbc);
        gbc.gridx = 1;
        add(totalDurationLabel, gbc);
    }

    /**
     * Refreshes the data displayed in the statistics labels.
     */
    public void refreshData() {
        // Update each label with the latest statistics from the manager
        totalWorkoutsLabel.setText(String.valueOf(workoutManager.getTotalWorkouts()));
        totalSessionsLabel.setText(String.valueOf(workoutManager.getTotalSessions()));
        totalProgramsLabel.setText(String.valueOf(workoutManager.getTotalPrograms()));
        totalDurationLabel.setText(String.valueOf(workoutManager.getTotalDurationMinutes()));
    }
}