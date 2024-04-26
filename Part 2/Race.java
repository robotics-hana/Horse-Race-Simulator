
/**
 * @author Hana
 * @version 1.0
 */
// Imports
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;

// Class race implents java swing and the horse race simulation 
public class Race extends JFrame {

    private int raceLength;
    private int totalHorses;
    private int currentHorseIndex;
    private ArrayList<Horse> horses;
    private Timer timer;
    private JFrame raceFrame;
    private String horseDetailsFilePath = "horse_details.txt";

    // Define available horse breeds 4 to chose from
    private String[] availableBreeds = { "Thoroughbred", "Quarter Horse", "Arabian", "Appaloosa" };

    public Race() {
        showTitlePage(); // Show the title page initially
    }

    private void showTitlePage() {
        setTitle("Horse Racing Simulator");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(400, 225); // Adjusted width to accommodate the new panel
        setLocationRelativeTo(null); // Center the window

        JPanel titlePanel = new JPanel();
        titlePanel.setLayout(new GridLayout(4, 1)); // Increased the grid layout toa llow for new button

        JLabel titleLabel = new JLabel("Welcome to Horse Racing Simulator!");
        titleLabel.setHorizontalAlignment(SwingConstants.CENTER);
        titlePanel.add(titleLabel);

        JButton newRaceButton = new JButton("Start New Race");
        newRaceButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                initializeMainGUI();
            }
        });
        titlePanel.add(newRaceButton);

        JButton leaderboardButton = new JButton("View History");
        leaderboardButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                HistoryDisplay historyDisplay = new HistoryDisplay(); // Changed class name to "HistoryDisplay"
                historyDisplay.displayHistory();
            }
        });
        titlePanel.add(leaderboardButton);

        JButton statisticsButton = new JButton("Leaderboard"); // Added new button for leaderboard
        statisticsButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                StatisticsDisplay statisticsDisplay = new StatisticsDisplay(); // Changed class name to "HistoryDisplay"
                statisticsDisplay.displayStatistics();
            }
        });
        titlePanel.add(statisticsButton);

        add(titlePanel);
        setVisible(true);
    }

    // Frame used to show the leaderboard and horse statistics
    public class StatisticsDisplay {

        private String horseDetailsFilePath = "horse_details.txt";

        public void displayStatistics() {
            System.out.println("Displaying leaderboard statistics...");

            // Map to store max wins for each horse
            Map<String, Integer> maxWinsMap = new HashMap<>();
            // Map to store max falls for each horse
            Map<String, Integer> maxFallsMap = new HashMap<>();
            // Map to store breed for each horse
            Map<String, String> breedMap = new HashMap<>();
            // Map to store accessory for each horse
            Map<String, String> accessoryMap = new HashMap<>();

            try (BufferedReader reader = new BufferedReader(new FileReader(horseDetailsFilePath))) {
                String line;
                String currentHorseName = null;
                int maxWins = 0;
                int maxFalls = 0;
                String breed = null;
                String accessory = null;

                while ((line = reader.readLine()) != null) {
                    if (line.startsWith("Name: ")) {
                        // Update max wins, max falls, breed, and accessory
                        if (currentHorseName != null) {
                            maxWinsMap.put(currentHorseName, maxWins);
                            maxFallsMap.put(currentHorseName, maxFalls);
                            breedMap.put(currentHorseName, breed);
                            accessoryMap.put(currentHorseName, accessory);
                        }
                        // Reset values for the new horse
                        currentHorseName = line.substring("Name: ".length());
                        maxWins = 0;
                        maxFalls = 0;
                        breed = null;
                        accessory = null;
                    } else if (line.startsWith("Wins: ")) {
                        int wins = Integer.parseInt(line.substring("Wins: ".length()));
                        maxWins = Math.max(maxWins, wins);
                    } else if (line.startsWith("Falls: ")) {
                        int falls = Integer.parseInt(line.substring("Falls: ".length()));
                        maxFalls = Math.max(maxFalls, falls);
                    } else if (line.startsWith("Breed: ")) {
                        breed = line.substring("Breed: ".length());
                    } else if (line.startsWith("Equipment: ")) {
                        accessory = line.substring("Equipment: ".length());
                    }
                }

                // Update max wins, max falls, breed, and accessory for the last horse in the
                // file
                if (currentHorseName != null) {
                    maxWinsMap.put(currentHorseName, maxWins);
                    maxFallsMap.put(currentHorseName, maxFalls);
                    breedMap.put(currentHorseName, breed);
                    accessoryMap.put(currentHorseName, accessory);
                }
            } catch (IOException | NumberFormatException e) {
                e.printStackTrace();
                JOptionPane.showMessageDialog(null, "Error reading file or parsing data.", "Error",
                        JOptionPane.ERROR_MESSAGE);
                return;
            }

            // Create column names including breed and accessory
            String[] columnNames = { "Name", "Wins", "Falls", "Breed", "Accessory", "Total races" };

            // Create table model with specified column names
            DefaultTableModel model = new DefaultTableModel(columnNames, 0);

            // Populate the table model with horse statistics
            for (Map.Entry<String, Integer> entry : maxWinsMap.entrySet()) {
                String horseName = entry.getKey();
                int maxWins = entry.getValue();
                int maxFalls = maxFallsMap.get(horseName); // Get max falls for the horse
                String breed = breedMap.get(horseName); // Get breed for the horse
                String accessory = accessoryMap.get(horseName); // Get accessory for the horse
                int races = maxWins + maxFalls; // Calculate total races

                model.addRow(new Object[] { horseName, maxWins, maxFalls, breed, accessory, races });
            }

            // Create JTable with model
            JTable table = new JTable(model);

            // Create JScrollPane and add table to it
            JScrollPane scrollPane = new JScrollPane(table);

            // Create JFrame and add scrollPane to it
            JFrame frame = new JFrame("Leaderboard");
            frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
            frame.add(scrollPane);
            frame.pack();
            frame.setLocationRelativeTo(null); // Center the frame
            frame.setVisible(true);
        }
    }

    // This method is used to configure a new pop up frame to show the history of
    // each race
    public class HistoryDisplay {

        private void displayHistory() {
            String horseDetailsFilePath = "horse_details.txt";

            System.out.println("Displaying history...");

            try (BufferedReader reader = new BufferedReader(new FileReader(horseDetailsFilePath))) {
                // Create column names
                String[] columnNames = { "Name", "Wins", "Falls", "Breed", "Confidence" };

                // Create table model with specified column names
                DefaultTableModel model = new DefaultTableModel(columnNames, 0);

                Map<String, Integer> maxWinsMap = new HashMap<>(); // Map to store max wins for each horse
                Map<String, Integer> maxFallsMap = new HashMap<>(); // Map to store max falls for each horse

                String name = null;
                int wins = 0;
                int falls = 0;
                String breed = null;
                double confidence = 0.0;

                String line;
                while ((line = reader.readLine()) != null) {
                    if (line.startsWith("Name: ")) {
                        if (name != null) { // Add previous horse's details
                            model.addRow(new Object[] { name, maxWinsMap.getOrDefault(name, 0),
                                    maxFallsMap.getOrDefault(name, 0), breed, confidence });
                        }
                        // Start collecting details of a new horse
                        name = line.substring("Name: ".length());
                        wins = 0; // Reset wins for the new horse
                        falls = 0; // Reset falls for the new horse
                    } else if (line.startsWith("Wins: ")) {
                        wins = Math.max(wins, Integer.parseInt(line.substring("Wins: ".length())));
                        maxWinsMap.put(name, wins);
                    } else if (line.startsWith("Falls: ")) {
                        falls = Math.max(falls, Integer.parseInt(line.substring("Falls: ".length())));
                        maxFallsMap.put(name, falls);
                    } else if (line.startsWith("Breed: ")) {
                        breed = line.substring("Breed: ".length());
                    } else if (line.startsWith("Confidence: ")) {
                        confidence = Double.parseDouble(line.substring("Confidence: ".length()));
                    }
                }
                // Add the last horse's details
                if (name != null) {
                    model.addRow(new Object[] { name, maxWinsMap.getOrDefault(name, 0),
                            maxFallsMap.getOrDefault(name, 0), breed, confidence });
                }

                // Create JTable with model
                JTable table = new JTable(model);

                // Create JScrollPane and add table to it
                JScrollPane scrollPane = new JScrollPane(table);

                // Create JFrame and add scrollPane to it
                JFrame frame = new JFrame("History");
                frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
                frame.add(scrollPane);
                frame.pack();
                frame.setLocationRelativeTo(null); // Center the frame
                frame.setVisible(true);
            } catch (IOException | NumberFormatException e) {
                e.printStackTrace();
                JOptionPane.showMessageDialog(null, "Error reading file or parsing data.", "Error",
                        JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    private void initializeMainGUI() {
        getContentPane().removeAll(); // Clear the current content
        setTitle("Horse Racing Simulator");
        setSize(400, 225); // Adjusted width to accommodate the new panel
        setLocationRelativeTo(null); // Center the window

        JPanel mainPanel = new JPanel();
        mainPanel.setLayout(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.gridx = 0;
        gbc.gridy = 0;

        JLabel raceLengthLabel = new JLabel("Race Length:");
        JTextField raceLengthField = new JTextField(10); // Increased size to accommodate 10 characters
        mainPanel.add(raceLengthLabel, gbc);
        gbc.gridx = 1;
        mainPanel.add(raceLengthField, gbc);

        gbc.gridx = 0;
        gbc.gridy = 1;
        JLabel numberOfHorsesLabel = new JLabel("Number of Horses:");
        JTextField numberOfHorsesField = new JTextField(10); // Increased size to accommodate 10 characters
        mainPanel.add(numberOfHorsesLabel, gbc);
        gbc.gridx = 1;
        mainPanel.add(numberOfHorsesField, gbc);

        gbc.gridx = 0;
        gbc.gridy = 2;
        gbc.gridwidth = 2;
        JButton startButton = new JButton("Start Race");
        startButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                int raceLength = Integer.parseInt(raceLengthField.getText());
                int numberOfHorses = Integer.parseInt(numberOfHorsesField.getText());
                initializeRace(raceLength, numberOfHorses);
            }
        });
        mainPanel.add(startButton, gbc);

        add(mainPanel);
        revalidate();
        repaint();
    }

    // Ensure the configurations for the start of race is met
    private void initializeRace(int raceLength, int numberOfHorses) {
        if (raceLength <= 0 || numberOfHorses <= 0) {
            JOptionPane.showMessageDialog(null, "Race length and number of horses must be greater than 0.");
            return;
        }
        this.raceLength = raceLength;
        this.totalHorses = numberOfHorses;
        this.currentHorseIndex = 0;
        this.horses = new ArrayList<>();

        initializeNextHorse(); // Start the process by providing details for the first horse
    }

    private void initializeNextHorse() {
        if (currentHorseIndex < totalHorses) {
            boolean isNewHorse = JOptionPane.showConfirmDialog(null, "Would you like to add a new horse?",
                    "New Horse or Saved Horse", JOptionPane.YES_NO_OPTION) == JOptionPane.YES_OPTION;

            if (isNewHorse) {
                openInputFrame(currentHorseIndex);
            } else {
                openSavedHorseDialog(currentHorseIndex); // Choose from saved horses
            }
        } else {
            // All horses added, proceed to create race frame and start race
            if (allHorsesAdded()) {
                createRaceFrame();
                startRace();
            }
            saveHorseDetailsToFile(); // Save horse details to file after addig all horses
        }
    }

    private void openSavedHorseDialog(int horseIndex) {
        try (BufferedReader reader = new BufferedReader(new FileReader(horseDetailsFilePath))) {
            String line;
            DefaultComboBoxModel<String> comboBoxModel = new DefaultComboBoxModel<>();
            HashSet<String> uniqueHorseNames = new HashSet<>(); // store unique horse names
            while ((line = reader.readLine()) != null) {
                if (line.startsWith("Name: ")) {
                    String horseName = line.substring("Name: ".length());
                    if (!uniqueHorseNames.contains(horseName)) { // add only if the name is unique
                        uniqueHorseNames.add(horseName);
                        comboBoxModel.addElement(horseName);
                    }
                }
            }
            JComboBox<String> savedHorsesComboBox = new JComboBox<>(comboBoxModel);
            JOptionPane.showMessageDialog(null, savedHorsesComboBox, "Choose Horse " + (horseIndex + 1),
                    JOptionPane.QUESTION_MESSAGE);
            String selectedHorseName = (String) savedHorsesComboBox.getSelectedItem();
            if (selectedHorseName != null && !horseExists(selectedHorseName)) {
                // Load details of the selected horse and add it to the race
                Horse selectedHorse = loadHorseDetails(selectedHorseName);
                if (selectedHorse != null) {
                    horses.add(selectedHorse);
                    currentHorseIndex++; // Move to the next horse
                    initializeNextHorse();
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private boolean horseExists(String horseName) {
        for (Horse horse : horses) {
            if (horse != null && horse.getName().equals(horseName)) {
                return true;
            }
        }
        return false;
    }

    // load horse details from the horse detail files
    private Horse loadHorseDetails(String horseName) {
        try (BufferedReader reader = new BufferedReader(new FileReader(horseDetailsFilePath))) {
            String line;
            char symbol = '\u0000';
            double confidence = 0.0;
            String breed = null;
            String equipment = null;
            int wins = 0;
            int falls = 0;
            boolean foundHorse = false;
            while ((line = reader.readLine()) != null) {
                if (line.startsWith("Name: ") && line.substring("Name: ".length()).equals(horseName)) {
                    foundHorse = true;
                } else if (foundHorse) {
                    if (line.startsWith("Symbol: ")) {
                        symbol = line.charAt("Symbol: ".length());
                    } else if (line.startsWith("Confidence: ")) {
                        confidence = Double.parseDouble(line.substring("Confidence: ".length()));
                    } else if (line.startsWith("Breed: ")) {
                        breed = line.substring("Breed: ".length());
                    } else if (line.startsWith("Equipment: ")) {
                        equipment = line.substring("Equipment: ".length());
                    } else if (line.startsWith("Wins: ")) {
                        wins = Integer.parseInt(line.substring("Wins: ".length()));
                    } else if (line.startsWith("Falls: ")) {
                        falls = Integer.parseInt(line.substring("Falls: ".length()));
                    }
                }
            }
            if (foundHorse) {
                return new Horse(symbol, horseName, confidence, breed, equipment, wins, falls);
            } else {
                JOptionPane.showMessageDialog(null, "Failed to load details for horse: " + horseName, "Error",
                        JOptionPane.ERROR_MESSAGE); // error message configured
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    private void openInputFrame(int horseIndex) {
        JFrame inputFrame = new JFrame("Enter Details for Horse " + (horseIndex + 1));
        inputFrame.setSize(400, 300); // Set the size here
        JPanel inputPanel = new JPanel(new GridLayout(0, 2));
        JLabel nameLabel = new JLabel("Name:");
        JTextField nameField = new JTextField();
        JLabel symbolLabel = new JLabel("Symbol:");
        JTextField symbolField = new JTextField();
        JLabel confidenceLabel = new JLabel("Confidence:");
        JTextField confidenceField = new JTextField();
        JLabel breedLabel = new JLabel("Breed:");
        JComboBox<String> breedComboBox = new JComboBox<>(availableBreeds);
        JLabel equipmentLabel = new JLabel("Equipment:");
        JComboBox<String> equipmentComboBox = new JComboBox<>(new String[] { "Carrot", "Saddle", "Horseshoe" });
        JButton submitButton = new JButton("Submit");

        inputPanel.add(nameLabel);
        inputPanel.add(nameField);
        inputPanel.add(symbolLabel);
        inputPanel.add(symbolField);
        inputPanel.add(confidenceLabel);
        inputPanel.add(confidenceField);
        inputPanel.add(breedLabel);
        inputPanel.add(breedComboBox);
        inputPanel.add(equipmentLabel);
        inputPanel.add(equipmentComboBox);
        inputPanel.add(submitButton);

        submitButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                char symbol = symbolField.getText().charAt(0);
                String name = nameField.getText();
                if (nameExistsInFile(name)) {
                    JOptionPane.showMessageDialog(null, "A horse with the name \"" + name + "\" already exists.",
                            "Duplicate Name", JOptionPane.ERROR_MESSAGE); // error message for horse name
                    return;
                }
                double confidence;
                try {
                    confidence = Double.parseDouble(confidenceField.getText());
                    if (confidence < 0.1 || confidence > 1.0) {
                        JOptionPane.showMessageDialog(null, "Confidence must be between 0.1 and 1.0", "Invalid Input",
                                JOptionPane.ERROR_MESSAGE);// error message for confidence, not in range
                        return;
                    }
                } catch (NumberFormatException ex) {
                    JOptionPane.showMessageDialog(null, "Confidence must be a number", "Invalid Input",
                            JOptionPane.ERROR_MESSAGE); // error message for confidence, not number
                    return;
                }
                String selectedBreed = (String) breedComboBox.getSelectedItem();
                String selectedEquipment = (String) equipmentComboBox.getSelectedItem();
                horses.add(new Horse(symbol, name, confidence, selectedBreed, selectedEquipment, 0, 0));
                inputFrame.dispose();

                // Initialize the next horse
                currentHorseIndex++;
                initializeNextHorse();
            }
        });

        inputFrame.add(inputPanel);
        inputFrame.setLocationRelativeTo(null);
        inputFrame.setVisible(true); // Set visible after setting size
    }

    // check if the horse name already exist in the file
    private boolean nameExistsInFile(String name) {
        try (BufferedReader reader = new BufferedReader(new FileReader(horseDetailsFilePath))) {
            String line;
            while ((line = reader.readLine()) != null) {
                if (line.startsWith("Name: ") && line.substring("Name: ".length()).equals(name)) {
                    return true;
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return false;
    }

    private boolean allHorsesAdded() {
        return horses.size() == totalHorses;
    }

    private void createRaceFrame() {
        getContentPane().removeAll(); // Clear the current content
        raceFrame = new JFrame("Race Track");
        raceFrame.setLayout(new GridBagLayout()); // Use GridBagLayout for centering
        raceFrame.setSize(400, 225); // Adjusted height to accommodate the new panel
        raceFrame.setLocationRelativeTo(null); // Center the window

        JPanel titlePanel = createTitlePanel(); // Create title panel
        GridBagConstraints titlePanelConstraints = new GridBagConstraints();
        titlePanelConstraints.gridx = 0;
        titlePanelConstraints.gridy = 0;
        raceFrame.add(titlePanel, titlePanelConstraints); // Add title panel to frame

        JPanel racePanel = createRacePanel(); // creating new race panel
        GridBagConstraints racePanelConstraints = new GridBagConstraints();
        racePanelConstraints.gridx = 0;
        racePanelConstraints.gridy = 1;
        raceFrame.add(racePanel, racePanelConstraints); // add race panel to frame

        JPanel horseInfoPanel = createHorseInfoPanel(); // ceate horse information panel
        GridBagConstraints horseInfoPanelConstraints = new GridBagConstraints();
        horseInfoPanelConstraints.gridx = 1;
        horseInfoPanelConstraints.gridy = 1;
        raceFrame.add(horseInfoPanel, horseInfoPanelConstraints); // Add horse information panel to frame

        raceFrame.setVisible(true);
    }

    private JPanel createTitlePanel() {
        JPanel titlePanel = new JPanel();
        JLabel titleLabel = new JLabel("The race has just begun!");
        titlePanel.add(titleLabel);
        return titlePanel;
    }

    private JPanel createRacePanel() {
        JPanel racePanel = new JPanel();
        racePanel.setLayout(new GridLayout(horses.size() + 2, raceLength + 3)); // Change length

        // Calculate the length of the top and bottom edges take into account the |
        // symbol
        int edgeLength = raceLength + 3;

        // Print the top edge of the track
        JPanel topEdge = createPanelWithText("=".repeat(edgeLength)); // Updated length
        racePanel.add(topEdge);

        // Print the race lanes and horse positions
        for (Horse horse : horses) {
            if (horse != null) {
                StringBuilder lane = new StringBuilder("| ");
                for (int i = 0; i < edgeLength; i++) { // Updated length
                    if (i == horse.getDistanceTravelled()) {
                        lane.append(horse.hasFallen() ? "X" : horse.getSymbol());
                    } else {
                        lane.append("  ");
                    }
                }
                // Add horse name and confidence at the end of the lane
                lane.append(" |");
                JPanel lanePanel = createPanelWithText(lane.toString());
                racePanel.add(lanePanel);
            }
        }

        // Bottom row to track
        JPanel bottomEdge = createPanelWithText("=".repeat(edgeLength)); // new length
        racePanel.add(bottomEdge);

        return racePanel;
    }

    private JPanel createHorseInfoPanel() {
        JPanel horseInfoPanel = new JPanel();
        horseInfoPanel.setLayout(new GridLayout(horses.size(), 1)); // One column for each horse

        // Add horse information labels
        for (Horse horse : horses) {
            if (horse != null) {
                JLabel horseInfoLabel = new JLabel(
                        horse.getName() + " (Current confidence " + horse.getConfidence() + ")");
                horseInfoPanel.add(horseInfoLabel);
            }
        }

        return horseInfoPanel;
    }

    private JPanel createPanelWithText(String text) {
        JPanel panel = new JPanel();
        JLabel label = new JLabel(text);
        panel.add(label);
        return panel;
    }

    private void startRace() {
        timer = new Timer(200, new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                moveHorses();
                updateRace();
                if (allHorsesFallen() || raceFinished()) {
                    ((Timer) e.getSource()).stop();
                    displayRaceResult(); // Display race result
                    updateWinsAndFalls(); // Update wins and falls when the race is finished
                    saveHorseDetailsToFile(); // Save horse details to file
                    showTitlePage(); // Show the title page when the race is over
                }
            }
        });
        timer.start();
    }

    private void moveHorses() {
        for (Horse horse : horses) {
            if (horse != null && !horse.hasFallen()) {
                // Move each horse based on its confidence
                if (Math.random() < horse.getConfidence()) {
                    horse.moveForward();
                }
                // The probability of a horse falling increases with the race's progress
                if (Math.random() < (double) horse.getDistanceTravelled() / raceLength) {
                    horse.fall();
                }
            }
        }
    }

    // update
    private void updateRace() {
        raceFrame.getContentPane().removeAll(); // Clear the current content
        JPanel racePanel = createRacePanel(); // create race panel with updated positions
        GridBagConstraints racePanelConstraints = new GridBagConstraints();
        racePanelConstraints.gridx = 0;
        racePanelConstraints.gridy = 1;
        raceFrame.add(racePanel, racePanelConstraints); // Add updated race panel to frame

        JPanel horseInfoPanel = createHorseInfoPanel();
        GridBagConstraints horseInfoPanelConstraints = new GridBagConstraints();
        horseInfoPanelConstraints.gridx = 1;
        horseInfoPanelConstraints.gridy = 1;
        raceFrame.add(horseInfoPanel, horseInfoPanelConstraints); // Add updated horse information panel to frame

        raceFrame.revalidate(); // check frame to update the layout
        raceFrame.repaint(); // update frame to add changes
    }

    // check if all races have fallen
    private boolean allHorsesFallen() {
        for (Horse horse : horses) {
            if (horse != null && !horse.hasFallen()) {
                return false;
            }
        }
        return true;
    }

    // set race to finished
    private boolean raceFinished() {
        for (Horse horse : horses) {
            if (horse != null && horse.getDistanceTravelled() >= raceLength) {
                return true;
            }
        }
        return false;
    }

    // pop up to show end result of race
    private void displayRaceResult() {
        StringBuilder resultMessage = new StringBuilder("Race Result:\n");
        for (Horse horse : horses) {
            if (horse != null) {
                if (horse.hasFallen()) {
                    resultMessage.append(horse.getName()).append(" has fallen!\n");
                } else {
                    resultMessage.append(horse.getName()).append(" finished the race!\n");
                }
            }
        }
        JOptionPane.showMessageDialog(null, resultMessage.toString(), "Race Result", JOptionPane.INFORMATION_MESSAGE);
    }

    // used to update and increment wins or losses of horses
    private void updateWinsAndFalls() {
        for (Horse horse : horses) {
            if (horse != null) {
                if (horse.hasFallen()) {
                    horse.incrementFalls();
                } else {
                    horse.incrementWins();
                }
            }
        }
    }

    private void saveHorseDetailsToFile() {
        // Create a HashSet to store the names of the horses that have been already
        // created
        HashSet<String> existingHorses = new HashSet<>();

        // Read existing horse names from the file
        try (BufferedReader reader = new BufferedReader(new FileReader(horseDetailsFilePath))) {
            String line;
            while ((line = reader.readLine()) != null) {
                if (line.startsWith("Name: ")) {
                    String horseName = line.substring("Name: ".length());
                    existingHorses.add(horseName);
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        try (PrintWriter writer = new PrintWriter(new FileWriter(horseDetailsFilePath, true))) {
            for (Horse horse : horses) {
                if (horse != null) {
                    writer.println("Name: " + horse.getName());
                    writer.println("Symbol: " + horse.getSymbol());
                    writer.println("Confidence: " + horse.getConfidence());
                    writer.println("Breed: " + horse.getBreed());
                    writer.println("Equipment: " + horse.getEquipment());
                    writer.println("Wins: " + horse.getWins());
                    writer.println("Falls: " + horse.getFalls());

                    // Add the horse name to the set of existing horses
                    existingHorses.add(horse.getName());
                }

            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        // Set the default font for the UI
        setUIFont(new Font("Arial", Font.PLAIN, 13));
        // Set the background color for the UI
        UIManager.put("Panel.background", new Color(255, 209, 220));

        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                new Race();
            }
        });
    }

    // Method to set the default font for the UI components
    public static void setUIFont(Font font) {
        UIDefaults defaults = UIManager.getDefaults();
        for (Object key : defaults.keySet()) {
            if (defaults.get(key) instanceof Font) {
                defaults.put(key, font);
            }
        }
    }
}
