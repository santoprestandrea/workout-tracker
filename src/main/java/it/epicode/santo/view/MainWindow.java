package it.epicode.santo.view;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Font;
import java.util.logging.Logger;

import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.Icon;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTabbedPane;
import javax.swing.KeyStroke;
import javax.swing.SwingUtilities;
import javax.swing.UIManager;
import javax.swing.border.EmptyBorder;

import it.epicode.santo.controller.WorkoutManager;
import it.epicode.santo.util.LoggerManager;

/**
 * MainWindow is the main JFrame of the application.
 * It manages the main UI, including tabs, menu bar, and status bar.
 */
public class MainWindow extends JFrame {
    // Logger for logging messages and errors
    private static final Logger LOGGER = LoggerManager.getLogger();

    // Reference to the singleton WorkoutManager (handles business logic/data)
    private WorkoutManager workoutManager;
    // Tabbed pane for switching between main panels
    private JTabbedPane tabbedPane;

    // Main panels for each section of the application
    private WorkoutCreationPanel creationPanel;
    private WorkoutListPanel listPanel;
    private SessionManagementPanel sessionPanel;
    private StatisticsPanel statsPanel;

    /**
     * Constructor: initializes the main window and its components.
     */
    public MainWindow() {
        this.workoutManager = WorkoutManager.getInstance(); // Get the singleton instance
        initializeUI(); // Set up the UI components
        setupMenuBar(); // Set up the menu bar
        LOGGER.info("MainWindow initialized");
    }

    /**
     * Initializes the main UI layout and components.
     */
    private void initializeUI() {
        setTitle("Workout Tracker - Santo"); // Set window title
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE); // Exit app on close
        setSize(1000, 700); // Set window size
        setLocationRelativeTo(null); // Center the window on screen

        // Set modern system look and feel for the UI
        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (Exception e) {
            LOGGER.warning("Unable to set system Look and Feel");
        }

        // Use BorderLayout for main window
        setLayout(new BorderLayout());

        // Add welcome panel at the top (NORTH)
        add(createWelcomePanel(), BorderLayout.NORTH);

        // Create and add the tabbed pane at the center
        createTabbedPane();
        add(tabbedPane, BorderLayout.CENTER);

        // Add status bar at the bottom (SOUTH)
        add(createStatusBar(), BorderLayout.SOUTH);
    }

    /**
     * Creates the welcome panel displayed at the top of the window.
     * 
     * @return JPanel with title and subtitle.
     */
    private JPanel createWelcomePanel() {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBorder(new EmptyBorder(10, 15, 10, 15)); // Padding
        panel.setBackground(new Color(52, 73, 94)); // Dark blue background

        // Main title label
        JLabel titleLabel = new JLabel("Workout Tracker");
        titleLabel.setFont(new Font("Arial", Font.BOLD, 35));
        titleLabel.setForeground(Color.WHITE);

        // Subtitle label
        JLabel subtitleLabel = new JLabel("Manage your workouts professionally");
        subtitleLabel.setFont(new Font("Arial", Font.PLAIN, 20));
        subtitleLabel.setForeground(new Color(189, 195, 199));

        // Panel for text (transparent)
        JPanel textPanel = new JPanel(new BorderLayout());
        textPanel.setOpaque(false);
        textPanel.add(titleLabel, BorderLayout.NORTH);
        textPanel.add(subtitleLabel, BorderLayout.CENTER);

        // Add text panel to the left side of the welcome panel
        panel.add(textPanel, BorderLayout.WEST);

        return panel;
    }

    /**
     * Creates the main tabbed pane and adds all main panels as tabs.
     */
    private void createTabbedPane() {
        tabbedPane = new JTabbedPane(JTabbedPane.TOP);
        tabbedPane.setFont(new Font("Arial", Font.PLAIN, 20));

        // Tab 1: Workout creation panel
        creationPanel = new WorkoutCreationPanel(workoutManager, this);
        tabbedPane.addTab("New Workout", createTabIcon("create"), creationPanel, "Create new workouts");

        // Tab 2: Workout list panel
        listPanel = new WorkoutListPanel(workoutManager);
        tabbedPane.addTab("My workouts", createTabIcon("list"), listPanel, "View and manage workouts");

        // Tab 3: Session and program management panel
        sessionPanel = new SessionManagementPanel(workoutManager, this);
        tabbedPane.addTab("Sessions & programs", createTabIcon("session"), sessionPanel,
                "Manage sessions and programs");

        // Tab 4: Statistics panel
        statsPanel = new StatisticsPanel(workoutManager);
        tabbedPane.addTab("Statistics", createTabIcon("stats"), statsPanel, "View statistics and progress");
    }

    /**
     * Creates an icon for a tab.
     * 
     * @param type The type of tab (not used yet).
     * @return Icon for the tab (currently null).
     */
    private Icon createTabIcon(String type) {
        // Currently returns null, can be extended to provide custom icons
        return null;
    }

    /**
     * Creates the status bar displayed at the bottom of the window.
     * 
     * @return JPanel representing the status bar.
     */
    private JPanel createStatusBar() {
        JPanel statusBar = new JPanel(new BorderLayout());
        statusBar.setBorder(BorderFactory.createLoweredBevelBorder());
        statusBar.setPreferredSize(new Dimension(getWidth(), 25)); // Fixed height

        // Status message label (left side)
        JLabel statusLabel = new JLabel(" Workout Tracker v1.0");
        statusLabel.setFont(new Font("Arial", Font.PLAIN, 15));

        // Counts label (right side)
        JLabel countLabel = new JLabel("Workout: 0 | Sessioni: 0 | Programmi: 0 ");
        countLabel.setFont(new Font("Arial", Font.PLAIN, 15));

        statusBar.add(statusLabel, BorderLayout.WEST);
        statusBar.add(countLabel, BorderLayout.EAST);

        return statusBar;
    }

    /**
     * Sets up the menu bar with File, View, and Help menus.
     */
    private void setupMenuBar() {
        JMenuBar menuBar = new JMenuBar();

        // === File Menu ===
        JMenu fileMenu = new JMenu("File");
        fileMenu.setMnemonic('F'); // Alt+F shortcut

        // "New" menu item
        JMenuItem newFileItem = new JMenuItem("New");
        newFileItem.setAccelerator(KeyStroke.getKeyStroke("ctrl N"));
        newFileItem.addActionListener(e -> newFile());

        // "Open" menu item
        JMenuItem openItem = new JMenuItem("Open...");
        openItem.setAccelerator(KeyStroke.getKeyStroke("ctrl O"));
        openItem.addActionListener(e -> openFile());

        // "Save" menu item
        JMenuItem saveItem = new JMenuItem("Save");
        saveItem.setAccelerator(KeyStroke.getKeyStroke("ctrl S"));
        saveItem.addActionListener(e -> saveFile());

        // "Save As" menu item
        JMenuItem saveAsItem = new JMenuItem("Save As...");
        saveAsItem.addActionListener(e -> saveFileAs());

        // "Exit" menu item
        JMenuItem exitItem = new JMenuItem("Exit");
        exitItem.setAccelerator(KeyStroke.getKeyStroke("ctrl Q"));
        exitItem.addActionListener(e -> exitApplication());

        // Add items to File menu
        fileMenu.add(newFileItem);
        fileMenu.add(openItem);
        fileMenu.addSeparator();
        fileMenu.add(saveItem);
        fileMenu.add(saveAsItem);
        fileMenu.addSeparator();
        fileMenu.add(exitItem);

        // === View Menu ===
        JMenu viewMenu = new JMenu("View");
        viewMenu.setMnemonic('V'); // Alt+V shortcut

        // "Refresh" menu item
        JMenuItem refreshItem = new JMenuItem("Refresh");
        refreshItem.setAccelerator(KeyStroke.getKeyStroke("F5"));
        refreshItem.addActionListener(e -> refreshAllPanels());

        viewMenu.add(refreshItem);

        // === Help Menu ===
        JMenu helpMenu = new JMenu("Help");
        helpMenu.setMnemonic('A'); // Alt+A shortcut

        // "About" menu item
        JMenuItem aboutItem = new JMenuItem("Information...");
        aboutItem.addActionListener(e -> showAbout());

        helpMenu.add(aboutItem);

        // Add menus to the menu bar
        menuBar.add(fileMenu);
        menuBar.add(viewMenu);
        menuBar.add(Box.createHorizontalGlue()); // Pushes Help menu to the right
        menuBar.add(helpMenu);

        setJMenuBar(menuBar); // Set the menu bar for the window
    }

    // === MENU ACTION METHODS ===

    /**
     * Handles the "New File" action.
     * Prompts the user to save, then resets data and refreshes panels.
     */
    private void newFile() {
        int option = JOptionPane.showConfirmDialog(
                this,
                "Do you want to save the current data before creating a new file?",
                "New File",
                JOptionPane.YES_NO_CANCEL_OPTION);

        if (option == JOptionPane.YES_OPTION) {
            saveFile(); // Save current data
        } else if (option == JOptionPane.CANCEL_OPTION) {
            return; // Cancelled by user
        }

        // Reset all data (to be implemented in WorkoutManager)
        // workoutManager.clearAll();
        refreshAllPanels();
        updateStatusBar();

        JOptionPane.showMessageDialog(this, "New file created!", "Success", JOptionPane.INFORMATION_MESSAGE);
    }

    /**
     * Handles the "Open File" action.
     * Lets the user select a JSON file and loads data from it.
     */
    private void openFile() {
        JFileChooser fileChooser = new JFileChooser();
        fileChooser.setFileFilter(new javax.swing.filechooser.FileNameExtensionFilter("File JSON", "json"));

        int result = fileChooser.showOpenDialog(this);
        if (result == JFileChooser.APPROVE_OPTION) {
            try {
                String filename = fileChooser.getSelectedFile().getAbsolutePath();
                workoutManager.loadFromFile(filename); // Load data
                refreshAllPanels();
                updateStatusBar();
                JOptionPane.showMessageDialog(this, "File successfully loaded!", "Successo",
                        JOptionPane.INFORMATION_MESSAGE);
            } catch (Exception e) {
                JOptionPane.showMessageDialog(this, "Error loading file:\n" + e.getMessage(),
                        "Error", JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    /**
     * Handles the "Save File" action.
     * Lets the user choose a file and saves data to it in JSON format.
     */
    private void saveFile() {
        JFileChooser fileChooser = new JFileChooser();
        fileChooser.setFileFilter(new javax.swing.filechooser.FileNameExtensionFilter("File JSON", "json"));
        fileChooser.setSelectedFile(new java.io.File("workout_data.json"));

        int result = fileChooser.showSaveDialog(this);
        if (result == JFileChooser.APPROVE_OPTION) {
            try {
                String filename = fileChooser.getSelectedFile().getAbsolutePath();
                if (!filename.endsWith(".json")) {
                    filename += ".json"; // Ensure .json extension
                }
                workoutManager.saveToFile(filename); // Save data
                JOptionPane.showMessageDialog(this, "File successfully loaded!", "Success",
                        JOptionPane.INFORMATION_MESSAGE);
            } catch (Exception e) {
                JOptionPane.showMessageDialog(this, "Error saving file:\n" + e.getMessage(),
                        "Error", JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    /**
     * Handles the "Save As" action.
     * Currently behaves the same as saveFile().
     */
    private void saveFileAs() {
        saveFile(); // For now, same as save
    }

    /**
     * Handles the "Exit" action.
     * Prompts the user to save before exiting.
     */
    private void exitApplication() {
        int option = JOptionPane.showConfirmDialog(
                this,
                "Do you want to save your data before exiting?",
                "Exit Confirmation",
                JOptionPane.YES_NO_CANCEL_OPTION);

        if (option == JOptionPane.YES_OPTION) {
            saveFile(); // Save before exit
        } else if (option == JOptionPane.CANCEL_OPTION) {
            return; // Cancelled by user
        }

        System.exit(0); // Exit application
    }

    /**
     * Shows the "About" dialog with application info.
     */
    private void showAbout() {
        String message = "Workout Tracker v1.0\n\n" +
                "Developed by Santo\n" +
                "An application to manage workouts, sessions, and training programs.\n\n" +
                "Technologies used:\n" +
                "• Java Swing for the graphical interface\n" +
                "• Design Patterns (Factory, Composite, Iterator, Singleton)\n" +
                "• MVC Architecture\n" +
                "• Data persistence in JSON format";

        JOptionPane.showMessageDialog(this, message, "About", JOptionPane.INFORMATION_MESSAGE);
    }

    // === UTILITY METHODS ===

    /**
     * Refreshes all main panels (list, session, statistics) and updates the status
     * bar.
     */
    public void refreshAllPanels() {
        if (listPanel != null)
            listPanel.refreshData();
        if (sessionPanel != null)
            sessionPanel.refreshData();
        if (statsPanel != null)
            statsPanel.refreshData();
        updateStatusBar();
    }

    /**
     * Updates the status bar with the current counts of workouts, sessions, and
     * programs.
     */
    private void updateStatusBar() {
        // Get the status bar panel (assumed to be the third component in the content
        // pane)
        Component[] components = ((JPanel) getContentPane().getComponent(2)).getComponents(); // Status bar
        if (components.length > 1 && components[1] instanceof JLabel) {
            JLabel countLabel = (JLabel) components[1];
            countLabel.setText(String.format("Workout: %d | Sessions: %d | Programs: %d ",
                    workoutManager.getTotalWorkouts(),
                    workoutManager.getTotalSessions(),
                    workoutManager.getTotalPrograms()));
        }
    }

    /**
     * Switches to the tab at the given index.
     * 
     * @param tabIndex Index of the tab to select.
     */
    public void switchToTab(int tabIndex) {
        if (tabIndex >= 0 && tabIndex < tabbedPane.getTabCount()) {
            tabbedPane.setSelectedIndex(tabIndex);
        }
    }

    /**
     * Main method for testing the window.
     * Launches the application in the Swing event dispatch thread.
     */
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            try {
                new MainWindow().setVisible(true);
            } catch (Exception e) {
                e.printStackTrace();
            }
        });
    }
}