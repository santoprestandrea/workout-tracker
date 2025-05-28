package it.epicode.santo.view;

import java.awt.BorderLayout;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;

import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;

import it.epicode.santo.controller.WorkoutManager;
import it.epicode.santo.model.exceptions.InvalidWorkoutDataException;

/**
 * Panel for creating a new workout.
 * Allows the user to input workout details and add them to the system.
 */
public class WorkoutCreationPanel extends JPanel {

    private final WorkoutManager workoutManager;
    private final MainWindow mainWindow;

    /**
     * Constructor for WorkoutCreationPanel.
     * 
     * @param workoutManager the controller managing workout creation logic
     * @param mainWindow     the main application window, used for refreshing views
     */
    public WorkoutCreationPanel(WorkoutManager workoutManager, MainWindow mainWindow) {
        this.workoutManager = workoutManager;
        this.mainWindow = mainWindow;
        initializeUI();
    }

    /**
     * Initializes the user interface components and layout.
     */
    private void initializeUI() {
        // Use BorderLayout for the main panel
        setLayout(new BorderLayout());

        // Panel for the form fields
        JPanel formPanel = new JPanel();
        formPanel.setLayout(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5); // Padding between components
        gbc.fill = GridBagConstraints.HORIZONTAL;

        // Workout type label and combo box
        JLabel typeLabel = new JLabel("Workout type:");
        String[] types = { "Strength", "Cardio" };
        JComboBox<String> typeCombo = new JComboBox<>(types);

        // Workout name label and text field
        JLabel nameLabel = new JLabel("Name:");
        JTextField nameField = new JTextField(15);

        // Workout duration label and text field (in minutes)
        JLabel durationLabel = new JLabel("Duration (minutes):");
        JTextField durationField = new JTextField(5);

        // Strength workout: sets and reps labels and fields
        JLabel setsLabel = new JLabel("Sets:");
        JTextField setsField = new JTextField(5);

        JLabel repsLabel = new JLabel("Reps:");
        JTextField repsField = new JTextField(5);

        // Cardio workout: distance label and field (in kilometers)
        JLabel distanceLabel = new JLabel("Distance (km):");
        JTextField distanceField = new JTextField(5);

        // Add components to the form panel using GridBagLayout
        gbc.gridx = 0;
        gbc.gridy = 0;
        formPanel.add(typeLabel, gbc);
        gbc.gridx = 1;
        gbc.gridy = 0;
        formPanel.add(typeCombo, gbc);

        gbc.gridx = 0;
        gbc.gridy = 1;
        formPanel.add(nameLabel, gbc);
        gbc.gridx = 1;
        gbc.gridy = 1;
        formPanel.add(nameField, gbc);

        gbc.gridx = 0;
        gbc.gridy = 2;
        formPanel.add(durationLabel, gbc);
        gbc.gridx = 1;
        gbc.gridy = 2;
        formPanel.add(durationField, gbc);

        gbc.gridx = 0;
        gbc.gridy = 3;
        formPanel.add(setsLabel, gbc);
        gbc.gridx = 1;
        gbc.gridy = 3;
        formPanel.add(setsField, gbc);

        gbc.gridx = 0;
        gbc.gridy = 4;
        formPanel.add(repsLabel, gbc);
        gbc.gridx = 1;
        gbc.gridy = 4;
        formPanel.add(repsField, gbc);

        gbc.gridx = 0;
        gbc.gridy = 5;
        formPanel.add(distanceLabel, gbc);
        gbc.gridx = 1;
        gbc.gridy = 5;
        formPanel.add(distanceField, gbc);

        // Show/hide fields depending on the selected workout type
        typeCombo.addActionListener(e -> {
            boolean isStrength = typeCombo.getSelectedItem().equals("Strength");
            // Show sets and reps for strength workouts
            setsLabel.setVisible(isStrength);
            setsField.setVisible(isStrength);
            repsLabel.setVisible(isStrength);
            repsField.setVisible(isStrength);
            // Show distance for cardio workouts
            distanceLabel.setVisible(!isStrength);
            distanceField.setVisible(!isStrength);
        });
        // Set initial visibility for fields (default: Strength)
        typeCombo.setSelectedIndex(0);
        setsLabel.setVisible(true);
        setsField.setVisible(true);
        repsLabel.setVisible(true);
        repsField.setVisible(true);
        distanceLabel.setVisible(false);
        distanceField.setVisible(false);

        // Button to add the workout
        JButton addButton = new JButton("Add Workout");
        addButton.addActionListener(e -> {
            // Get user input from fields
            String type = (String) typeCombo.getSelectedItem();
            String name = nameField.getText().trim();
            String durationStr = durationField.getText().trim();

            try {
                // Parse duration as integer
                int duration = Integer.parseInt(durationStr);
                if ("Strength".equals(type)) {
                    // For strength workouts, parse sets and reps
                    int sets = Integer.parseInt(setsField.getText().trim());
                    int reps = Integer.parseInt(repsField.getText().trim());
                    // Create strength workout via the manager
                    workoutManager.createStrengthWorkout(name, duration, sets, reps);
                } else {
                    // For cardio workouts, parse distance
                    double distance = Double.parseDouble(distanceField.getText().trim());
                    // Create cardio workout via the manager
                    workoutManager.createCardioWorkout(name, duration, distance);
                }
                // Show success message
                JOptionPane.showMessageDialog(this, "Workout added successfully!", "Success",
                        JOptionPane.INFORMATION_MESSAGE);
                // Clear all input fields
                nameField.setText("");
                durationField.setText("");
                setsField.setText("");
                repsField.setText("");
                distanceField.setText("");
                // Refresh the main window panels to show the new workout
                mainWindow.refreshAllPanels();
            } catch (NumberFormatException ex) {
                // Show error if numeric fields are invalid
                JOptionPane.showMessageDialog(this,
                        "Please enter valid numeric values for duration, sets, reps, or distance.", "Error",
                        JOptionPane.ERROR_MESSAGE);
            } catch (InvalidWorkoutDataException ex) {
                // Show error if workout data is invalid (custom exception)
                JOptionPane.showMessageDialog(this, ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
            }
        });

        // Panel for the add button
        JPanel buttonPanel = new JPanel();
        buttonPanel.add(addButton);

        // Add form and button panels to the main panel
        add(formPanel, BorderLayout.CENTER);
        add(buttonPanel, BorderLayout.SOUTH);
    }
}


