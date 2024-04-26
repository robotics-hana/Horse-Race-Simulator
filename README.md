# HorseRaceSimulator

Welcome to the Horse Racing Simulator project! This Java game allows users to customize horses that they can race on varying track lengths. Attributes were added to the horse to make the game more dynamic; confidence is the main feature that is used to increase the speed of the horse, however the faster (higher the confidence) the higher the likely the horse will fall. To increase user engagement the horses can have different equipment’s (such as carrots) and the user can chose the length of the race and number of horses.

Part 1 has basic horse object instance initialisation in the main method, the horse simulation is shown in the output. The user customization is limited to race length, a maximum of 3 horse objects, the symbol, a confidence of 0-1 and the name of the horse. Encapsulation is key to the design of the programme with emphasis on ensuring variable align with Java conventions.

Part 2 utilises Java swing to create a pop frame with the ability to as many horses the user wants with a customizable race length. The user is limited to each horse having independent names from each other, this allows user to choose from saved horses or to enter details of a new horse. The history shows all previous horses and their progress, this was achieved by storing and updating details of a horses wins and losses on a file. The leader board allows user to see the statistics of each horse (number of wins, losses and races).


## Getting Started

To get started with the Horse Racing Simulator, follow these steps:

1.	Download and Extract the Zip File: After receiving the zip file, users should download it and extract automatically or via a software to retrieve its contents to safe location.
2.	Open the Project in a Java Development Environment: Users should then open their preferred Java IDE, such as IntelliJ IDEA or VisualStudio.
3.	Import the Project: Within the Java IDE, users can import the project by selecting the option to import or open an existing project. Navigate to the saved location for the extracted the zip file and select the project directory.
4.	Build the Project: Once the project is imported into the Java IDE, users may need to build it to ensure that it can compile without errors, and the project is ready to run. This step can typically be performed by selecting the option to build or compile the project within the IDE. 
a.	‘ls’ can be used to check the paths available; ‘cd’ can be used to progress to the file, ‘javac’ can be used to compile in the terminal otherwise use the compile button on the Java IDE.
5.	Run the Project: After the project has been built successfully, users can run it by selecting the option to run the project within the IDE. This will execute the main method of the project, launching the Horse Racing Simulator application.

## Features

- **Start New Race:** Start a ‘new horse race’ by specifying the race length and number of horses.
- **View History:** View the history of past races, including details of each horse's past performance.
- **Leaderboard:** View leaderboard statistics, including the number of wins, falls, and total amount of races for each horse.

## Usage

1.	Launch Application: Run the application to access the title page pop up frame of the game.
2.	Title Page Options: Choose from options starting a new race, viewing race history, or checking the leaderboard. If it's your first time running the application, an error may occur as the file that stores the horse details hasn't been created yet. (This will be created after the first horse race started)
3.	Race Setup: Input the desired race length and number of horses. For each horse, decide whether to input a new horse's details or select a saved horse from the dropdown menu. Ensure each horse has a unique name and a confidence value between 0 and 1, otherwise a pop error will show up.
4.	Race Completion: After the race, review the race result and statistics displayed.
5.	Continued Updates: The leaderboard and race history will continue updating as long as the file storing this data remains intact. To erase all past runs delete the file.

