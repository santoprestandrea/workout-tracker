package it.epicode.santo.view;

import javax.swing.*;

import java.awt.*;
import java.util.List;

import it.epicode.santo.controller.WorkoutManager;
import it.epicode.santo.model.core.Workout;
import it.epicode.santo.model.core.WorkoutProgram;
import it.epicode.santo.model.core.WorkoutSession;

public class SessionManagementPanel extends JPanel {

    private final WorkoutManager workoutManager;
    private final MainWindow mainWindow;

    private DefaultListModel<String> sessionListModel;
    private JList<String> sessionList;
    private JTextArea sessionDetails;

    private DefaultListModel<String> programListModel;
    private JList<String> programList;
    private JTextArea programDetails;

    public SessionManagementPanel(WorkoutManager workoutManager, MainWindow mainWindow) {
        this.workoutManager = workoutManager;
        this.mainWindow = mainWindow;
        initializeUI();
    }

    private void initializeUI() {
        setLayout(new BorderLayout());

        // --- Sessions ---
        sessionListModel = new DefaultListModel<>();
        sessionList = new JList<>(sessionListModel);
        sessionList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        JScrollPane sessionScroll = new JScrollPane(sessionList);

        sessionDetails = new JTextArea();
        sessionDetails.setEditable(false);
        sessionDetails.setFont(new Font("Monospaced", Font.PLAIN, 15));
        JScrollPane sessionDetailsScroll = new JScrollPane(sessionDetails);

        JSplitPane sessionSplit = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT, sessionScroll, sessionDetailsScroll);
        sessionSplit.setDividerLocation(200);

        // --- Programs ---
        programListModel = new DefaultListModel<>();
        programList = new JList<>(programListModel);
        programList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        JScrollPane programScroll = new JScrollPane(programList);

        programDetails = new JTextArea();
        programDetails.setEditable(false);
        programDetails.setFont(new Font("Monospaced", Font.PLAIN, 15));
        JScrollPane programDetailsScroll = new JScrollPane(programDetails);

        JSplitPane programSplit = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT, programScroll, programDetailsScroll);
        programSplit.setDividerLocation(200);

        // --- Tabbed pane for Sessions and Programs ---
        JTabbedPane tabbedPane = new JTabbedPane();
        JPanel sessionPanel = new JPanel(new BorderLayout());
        sessionPanel.add(sessionSplit, BorderLayout.CENTER);

        JButton addSessionBtn = new JButton("Add Session");
        addSessionBtn.addActionListener(e -> openAddSessionDialog());
        JPanel sessionBtnPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        sessionBtnPanel.add(addSessionBtn);
        sessionPanel.add(sessionBtnPanel, BorderLayout.SOUTH);

        JPanel programPanel = new JPanel(new BorderLayout());
        programPanel.add(programSplit, BorderLayout.CENTER);

        JButton addProgramBtn = new JButton("Add Program");
        addProgramBtn.addActionListener(e -> openAddProgramDialog());
        JPanel programBtnPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        programBtnPanel.add(addProgramBtn);
        programPanel.add(programBtnPanel, BorderLayout.SOUTH);

        tabbedPane.addTab("Sessions", sessionPanel);
        tabbedPane.addTab("Programs", programPanel);

        add(tabbedPane, BorderLayout.CENTER);

        // Listener to show details
        sessionList.addListSelectionListener(e -> showSessionDetails());
        programList.addListSelectionListener(e -> showProgramDetails());

        refreshData();
    }

    public void refreshData() {
        // Sessions
        sessionListModel.clear();
        List<WorkoutSession> sessions = workoutManager.getAllSessions();
        for (WorkoutSession session : sessions) {
            sessionListModel.addElement(session.getName());
        }
        sessionDetails.setText("");

        // Programs
        programListModel.clear();
        List<WorkoutProgram> programs = workoutManager.getAllPrograms();
        for (WorkoutProgram program : programs) {
            programListModel.addElement(program.getName());
        }
        programDetails.setText("");
    }

    private void showSessionDetails() {
        int idx = sessionList.getSelectedIndex();
        if (idx < 0) {
            sessionDetails.setText("");
            return;
        }
        WorkoutSession session = workoutManager.getAllSessions().get(idx);
        StringBuilder sb = new StringBuilder();
        sb.append("Session: ").append(session.getName()).append("\n");
        sb.append("Total duration: ").append(session.getDuration()).append(" min\n");
        sb.append("Workouts in session:\n");
        int i = 1;
        for (Workout w : session.getWorkouts()) {
            sb.append("  ").append(i++).append(". ").append(w.getName())
                    .append(" (").append(w.getDuration()).append(" min)\n");
        }
        sessionDetails.setText(sb.toString());
    }

    private void showProgramDetails() {
        int idx = programList.getSelectedIndex();
        if (idx < 0) {
            programDetails.setText("");
            return;
        }
        WorkoutProgram program = workoutManager.getAllPrograms().get(idx);
        StringBuilder sb = new StringBuilder();
        sb.append("Program: ").append(program.getName()).append("\n");
        sb.append("Total duration: ").append(program.getDuration()).append(" min\n");
        sb.append("Content:\n");
        int i = 1;
        for (Workout w : program.getSchedule()) {
            sb.append("  ").append(i++).append(". ").append(w.getName())
                    .append(" (").append(w.getDuration()).append(" min)");
            if (w instanceof WorkoutSession) {
                sb.append(" [Session]");
            }
            sb.append("\n");
        }
        programDetails.setText(sb.toString());
    }

    // --- Dialog to add a session ---
    private void openAddSessionDialog() {
        JDialog dialog = new JDialog(SwingUtilities.getWindowAncestor(this), "New Session",
                Dialog.ModalityType.APPLICATION_MODAL);
        dialog.setSize(400, 400);
        dialog.setLocationRelativeTo(this);
        dialog.setLayout(new BorderLayout());

        JTextField nameField = new JTextField(20);

        DefaultListModel<String> selectedWorkoutsModel = new DefaultListModel<>();
        JList<String> selectedWorkoutsList = new JList<>(selectedWorkoutsModel);

        JButton addWorkoutBtn = new JButton("Add Workout");
        addWorkoutBtn.addActionListener(e -> {
            List<Workout> allWorkouts = workoutManager.getAllWorkouts();
            if (allWorkouts.isEmpty()) {
                JOptionPane.showMessageDialog(dialog, "No workouts available.", "Warning",
                        JOptionPane.WARNING_MESSAGE);
                return;
            }
            String[] workoutNames = allWorkouts.stream().map(Workout::getName).toArray(String[]::new);
            String selected = (String) JOptionPane.showInputDialog(
                    dialog,
                    "Select a workout to add:",
                    "Add Workout",
                    JOptionPane.PLAIN_MESSAGE,
                    null,
                    workoutNames,
                    workoutNames[0]);
            if (selected != null && !selectedWorkoutsModel.contains(selected)) {
                selectedWorkoutsModel.addElement(selected);
            }
        });

        JButton createBtn = new JButton("Create Session");
        createBtn.addActionListener(e -> {
            String name = nameField.getText().trim();
            if (name.isEmpty()) {
                JOptionPane.showMessageDialog(dialog, "Enter a name for the session.", "Error",
                        JOptionPane.ERROR_MESSAGE);
                return;
            }
            try {
                WorkoutSession session = workoutManager.createSession(name);
                for (int i = 0; i < selectedWorkoutsModel.size(); i++) {
                    Workout w = workoutManager.findWorkoutByName(selectedWorkoutsModel.get(i));
                    if (w != null)
                        session.addWorkout(w);
                }
                dialog.dispose();
                refreshData();
                mainWindow.refreshAllPanels();
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(dialog, "Error creating session:\n" + ex.getMessage(),
                        "Error", JOptionPane.ERROR_MESSAGE);
            }
        });

        JPanel topPanel = new JPanel(new BorderLayout());
        topPanel.add(new JLabel("Session name:"), BorderLayout.WEST);
        topPanel.add(nameField, BorderLayout.CENTER);

        JPanel centerPanel = new JPanel(new BorderLayout());
        centerPanel.add(new JLabel("Selected workouts:"), BorderLayout.NORTH);
        centerPanel.add(new JScrollPane(selectedWorkoutsList), BorderLayout.CENTER);

        JPanel btnPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        btnPanel.add(addWorkoutBtn);
        btnPanel.add(createBtn);

        dialog.add(topPanel, BorderLayout.NORTH);
        dialog.add(centerPanel, BorderLayout.CENTER);
        dialog.add(btnPanel, BorderLayout.SOUTH);

        dialog.setVisible(true);
    }

    // --- Dialog to add a program ---
    private void openAddProgramDialog() {
        JDialog dialog = new JDialog(SwingUtilities.getWindowAncestor(this), "New Program",
                Dialog.ModalityType.APPLICATION_MODAL);
        dialog.setSize(500, 450);
        dialog.setLocationRelativeTo(this);
        dialog.setLayout(new BorderLayout());

        JTextField nameField = new JTextField(20);

        DefaultListModel<String> scheduleModel = new DefaultListModel<>();
        JList<String> scheduleList = new JList<>(scheduleModel);

        JButton addSessionBtn = new JButton("Add Session");
        addSessionBtn.addActionListener(e -> {
            List<WorkoutSession> allSessions = workoutManager.getAllSessions();
            if (allSessions.isEmpty()) {
                JOptionPane.showMessageDialog(dialog, "No sessions available.", "Warning",
                        JOptionPane.WARNING_MESSAGE);
                return;
            }
            String[] sessionNames = allSessions.stream().map(WorkoutSession::getName).toArray(String[]::new);
            String selected = (String) JOptionPane.showInputDialog(
                    dialog,
                    "Select a session to add:",
                    "Add Session",
                    JOptionPane.PLAIN_MESSAGE,
                    null,
                    sessionNames,
                    sessionNames[0]);
            if (selected != null) {
                scheduleModel.addElement("[Session] " + selected);
            }
        });

        JButton addWorkoutBtn = new JButton("Add Single Workout");
        addWorkoutBtn.addActionListener(e -> {
            List<Workout> allWorkouts = workoutManager.getAllWorkouts();
            if (allWorkouts.isEmpty()) {
                JOptionPane.showMessageDialog(dialog, "No workouts available.", "Warning",
                        JOptionPane.WARNING_MESSAGE);
                return;
            }
            String[] workoutNames = allWorkouts.stream().map(Workout::getName).toArray(String[]::new);
            String selected = (String) JOptionPane.showInputDialog(
                    dialog,
                    "Select a workout to add:",
                    "Add Workout",
                    JOptionPane.PLAIN_MESSAGE,
                    null,
                    workoutNames,
                    workoutNames[0]);
            if (selected != null) {
                scheduleModel.addElement(selected);
            }
        });

        JButton createBtn = new JButton("Create Program");
        createBtn.addActionListener(e -> {
            String name = nameField.getText().trim();
            if (name.isEmpty()) {
                JOptionPane.showMessageDialog(dialog, "Enter a name for the program.", "Error",
                        JOptionPane.ERROR_MESSAGE);
                return;
            }
            try {
                WorkoutProgram program = workoutManager.createProgram(name);
                for (int i = 0; i < scheduleModel.size(); i++) {
                    String entry = scheduleModel.get(i);
                    if (entry.startsWith("[Session] ")) {
                        String sessionName = entry.replace("[Session] ", "");
                        WorkoutSession session = workoutManager.findSessionByName(sessionName);
                        if (session != null)
                            program.addWorkoutToSchedule(session);
                    } else {
                        Workout w = workoutManager.findWorkoutByName(entry);
                        if (w != null)
                            program.addWorkoutToSchedule(w);
                    }
                }
                dialog.dispose();
                refreshData();
                mainWindow.refreshAllPanels();
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(dialog, "Error creating program:\n" + ex.getMessage(),
                        "Error", JOptionPane.ERROR_MESSAGE);
            }
        });

        JPanel topPanel = new JPanel(new BorderLayout());
        topPanel.add(new JLabel("Program name:"), BorderLayout.WEST);
        topPanel.add(nameField, BorderLayout.CENTER);

        JPanel centerPanel = new JPanel(new BorderLayout());
        centerPanel.add(new JLabel("Program content:"), BorderLayout.NORTH);
        centerPanel.add(new JScrollPane(scheduleList), BorderLayout.CENTER);

        JPanel btnPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        btnPanel.add(addSessionBtn);
        btnPanel.add(addWorkoutBtn);
        btnPanel.add(createBtn);

        dialog.add(topPanel, BorderLayout.NORTH);
        dialog.add(centerPanel, BorderLayout.CENTER);
        dialog.add(btnPanel, BorderLayout.SOUTH);

        dialog.setVisible(true);
    }
}
