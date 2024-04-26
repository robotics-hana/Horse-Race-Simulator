import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.*;
import java.util.ArrayList;
import java.util.HashSet;

public class RaceGUI extends JFrame {

 private int raceLength;
 private int totalHorses;
 private int currentHorseIndex;
 private ArrayList<Horse> horses;
 private Timer timer;
 private JFrame raceFrame;
 private String horseDetailsFilePath = "horse_details.txt";

 // Define available horse breeds
 private String[] availableBreeds = {"Thoroughbred", "Quarter Horse", "Arabian", "Appaloosa"};

 public RaceGUI() {
 showTitlePage(); // Show the title page initially
 }

 private void showTitlePage() {
 setTitle("Horse Racing Simulator");
 setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
 setSize(400, 225); // Adjusted width to accommodate the new panel
 setLocationRelativeTo(null); // Center the window

 JPanel titlePanel = new JPanel();
 titlePanel.setLayout(new GridLayout(3, 1));

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

 JButton leaderboardButton = new JButton("View Leaderboard");
 leaderboardButton.addActionListener(new ActionListener() {
 @Override
 public void actionPerformed(ActionEvent e) {
 displayLeaderboard(); // Call the method to display the leaderboard

 }
 });
 titlePanel.add(leaderboardButton);

 add(titlePanel);
 setVisible(true);
 }

 private void displayLeaderboard() {
 String horseDetailsFilePath = "horse_details.txt"; // Specify the correct file path here
 
 System.out.println("Displaying leaderboard...");
 
 try (BufferedReader reader = new BufferedReader(new FileReader(horseDetailsFilePath))) {
 // Create column names
 String[] columnNames = {"Name", "Wins", "Falls", "Breed", "Confidence"};
 
 // Create data array
 Object[][] data = new Object[totalHorses][5];
 
 // Read horse details and populate data array
 String line;
 int rowIndex = 0;
 while ((line = reader.readLine()) != null) {
 if (line.startsWith("Name: ")) {
 data[rowIndex][0] = line.substring("Name: ".length());
 } else if (line.startsWith("Wins: ")) {
 data[rowIndex][1] = Integer.parseInt(line.substring("Wins: ".length()));
 } else if (line.startsWith("Falls: ")) {
 data[rowIndex][2] = Integer.parseInt(line.substring("Falls: ".length()));
 } else if (line.startsWith("Breed: ")) {
 data[rowIndex][3] = line.substring("Breed: ".length());
 } else if (line.startsWith("Confidence: ")) {
 data[rowIndex][4] = Double.parseDouble(line.substring("Confidence: ".length()));
 rowIndex++;
 }
 }
 
 // Create JTable with data and column names
 JTable table = new JTable(data, columnNames);
 table.setPreferredScrollableViewportSize(new Dimension(500, 200));
 table.setFillsViewportHeight(true);
 
 // Add table to a scroll pane
 JScrollPane scrollPane = new JScrollPane(table);
 
 // Display the table in a popup frame
 JFrame frame = new JFrame("Leaderboard");
 frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
 frame.add(scrollPane);
 frame.pack();
 frame.setLocationRelativeTo(null); // Center the frame
 frame.setVisible(true);
 } catch (IOException e) {
 e.printStackTrace();
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

 private void initializeRace(int raceLength, int numberOfHorses) {
 if (raceLength <= 0 || numberOfHorses <= 0) {
 JOptionPane.showMessageDialog(null, "Race length and number of horses must be greater than 0.");
 return;
 }
 this.raceLength = raceLength;
 this.totalHorses = numberOfHorses;
 this.currentHorseIndex = 0;
 this.horses = new ArrayList<>();

 initializeNextHorse(); // Start the process by initializing the first horse
 }
 
 private void initializeNextHorse() {
 if (currentHorseIndex < totalHorses) {
 boolean isNewHorse = JOptionPane.showConfirmDialog(null, "Would you like to add a new horse?", "New Horse or Saved Horse", JOptionPane.YES_NO_OPTION) == JOptionPane.YES_OPTION;

 if (isNewHorse) {
 openInputFrame(currentHorseIndex); // Open input frame for new horse
 } else {
 openSavedHorseDialog(currentHorseIndex); // Open dialog to choose from saved horses
 }
 } else {
 // All horses added, proceed to create race frame and start race
 if (allHorsesAdded()) {
 createRaceFrame();
 startRace();
 }
 saveHorseDetailsToFile(); // Save horse details to file after adding all horses
 }
 }

 private void openSavedHorseDialog(int horseIndex) {
 try (BufferedReader reader = new BufferedReader(new FileReader(horseDetailsFilePath))) {
 String line;
 DefaultComboBoxModel<String> comboBoxModel = new DefaultComboBoxModel<>();
 while ((line = reader.readLine()) != null) {
 if (line.startsWith("Name: ")) {
 String horseName = line.substring("Name: ".length());
 comboBoxModel.addElement(horseName);
 }
 }
 JComboBox<String> savedHorsesComboBox = new JComboBox<>(comboBoxModel);
 JOptionPane.showMessageDialog(null, savedHorsesComboBox, "Choose Horse " + (horseIndex + 1), JOptionPane.QUESTION_MESSAGE);
 String selectedHorseName = (String) savedHorsesComboBox.getSelectedItem();
 if (selectedHorseName != null && !horseExists(selectedHorseName)) {
 // Load details of the selected horse and add it to the race
 Horse selectedHorse = loadHorseDetails(selectedHorseName);
 if (selectedHorse != null) {
 horses.add(selectedHorse);
 currentHorseIndex++; // Move to the next horse
 initializeNextHorse(); // Initialize the next horse
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
 JOptionPane.showMessageDialog(null, "Failed to load details for horse: " + horseName, "Error", JOptionPane.ERROR_MESSAGE);
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
 JComboBox<String> breedComboBox = new JComboBox<>(availableBreeds); // Use JComboBox for breed selection
 JLabel equipmentLabel = new JLabel("Equipment:");
 JComboBox<String> equipmentComboBox = new JComboBox<>(new String[]{"Carrot", "Saddle", "Horseshoe"});
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
 double confidence = Double.parseDouble(confidenceField.getText());
 String selectedBreed = (String) breedComboBox.getSelectedItem(); // Get selected breed from JComboBox
 String selectedEquipment = (String) equipmentComboBox.getSelectedItem(); // Get selected equipment from JComboBox
 horses.add(new Horse(symbol, nameField.getText(), confidence, selectedBreed, selectedEquipment, 0, 0));
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

 JPanel racePanel = createRacePanel(); // Create race panel
 GridBagConstraints racePanelConstraints = new GridBagConstraints();
 racePanelConstraints.gridx = 0;
 racePanelConstraints.gridy = 1;
 raceFrame.add(racePanel, racePanelConstraints); // Add race panel to frame

 JPanel horseInfoPanel = createHorseInfoPanel(); // Create horse information panel
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
 racePanel.setLayout(new GridLayout(horses.size() + 2, raceLength + 3)); // Adjusted length

 // Calculate the length of the top and bottom edges
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
 lane.append(" ");
 }
 }
 // Add horse name and confidence at the end of the lane
 lane.append(" |");
 JPanel lanePanel = createPanelWithText(lane.toString());
 racePanel.add(lanePanel);
 }
 }

 // Print the bottom edge of the track
 JPanel bottomEdge = createPanelWithText("=".repeat(edgeLength)); // Updated length
 racePanel.add(bottomEdge);

 return racePanel;
 }

 private JPanel createHorseInfoPanel() {
 JPanel horseInfoPanel = new JPanel();
 horseInfoPanel.setLayout(new GridLayout(horses.size(), 1)); // One column for each horse

 // Add horse information labels
 for (Horse horse : horses) {
 if (horse != null) {
 JLabel horseInfoLabel = new JLabel(horse.getName() + " (Current confidence " + horse.getConfidence() + ")");
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
 updateWinsAndFalls(); // Update wins and falls
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

 private void updateRace() {
 raceFrame.getContentPane().removeAll(); // Clear the current content
 JPanel racePanel = createRacePanel(); // Recreate race panel with updated positions
 GridBagConstraints racePanelConstraints = new GridBagConstraints();
 racePanelConstraints.gridx = 0;
 racePanelConstraints.gridy = 1;
 raceFrame.add(racePanel, racePanelConstraints); // Add updated race panel to frame

 JPanel horseInfoPanel = createHorseInfoPanel(); // Recreate horse information panel
 GridBagConstraints horseInfoPanelConstraints = new GridBagConstraints();
 horseInfoPanelConstraints.gridx = 1;
 horseInfoPanelConstraints.gridy = 1;
 raceFrame.add(horseInfoPanel, horseInfoPanelConstraints); // Add updated horse information panel to frame

 raceFrame.revalidate(); // Revalidate the frame to update the layout
 raceFrame.repaint(); // Repaint the frame to reflect the changes
 }

 private boolean allHorsesFallen() {
 for (Horse horse : horses) {
 if (horse != null && !horse.hasFallen()) {
 return false;
 }
 }
 return true;
 }

 private boolean raceFinished() {
 for (Horse horse : horses) {
 if (horse != null && horse.getDistanceTravelled() >= raceLength) {
 return true;
 }
 }
 return false;
 }

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
 try (PrintWriter writer = new PrintWriter(new FileWriter(horseDetailsFilePath))) {
 for (Horse horse : horses) {
 if (horse != null) {
 writer.println("Name: " + horse.getName());
 writer.println("Symbol: " + horse.getSymbol());
 writer.println("Confidence: " + horse.getConfidence());
 writer.println("Breed: " + horse.getBreed());
 writer.println("Equipment: " + horse.getEquipment());
 writer.println("Wins: " + horse.getWins());
 writer.println("Falls: " + horse.getFalls());
 }
 }
 } catch (IOException e) {
 e.printStackTrace();
 }
 }

 public static void main(String[] args) {
 SwingUtilities.invokeLater(new Runnable() {
 @Override
 public void run() {
 new RaceGUI();
 }
 });
 }
}
