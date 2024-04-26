public class Horse {
    // Fields of class Horse
    private char symbol; // Symbol representing the horse
    private String name; // Name of the horse
    private double confidence; // Confidence level of the horse
    private int distanceTravelled; // Distance travelled by the horse
    private boolean fallen; // Indicates if the horse has fallen during the race

    // Static field to keep track of the number of Horse objects created
    private static int horseCount = 0;

    // Constructor of class Horse
    public Horse(char horseSymbol, String horseName, double horseConfidence) {
        // Check if the maximum number of Horse objects has been reached
        if (horseCount >= 3) {
            throw new IllegalStateException("Cannot create more than 3 Horse objects.");
        }

        // Initialize variables
        this.distanceTravelled = 0;
        this.fallen = false;
        this.name = horseName;

        // Set the symbol, default to '♘' if the given symbol is not valid (not visible)
        try {
            setSymbol(horseSymbol);
        } catch (IllegalArgumentException e) {
            System.err.println(e.getMessage());
            this.symbol = '♘';
        }

        // Set the confidence level, default to 0.5 if out of range
        try {
            setConfidence(horseConfidence);
        } catch (IllegalArgumentException e) {
            System.err.println(e.getMessage());
        }

        // Increment the horseCount to track the number of Horse objects created
        horseCount++;
    }

    // Increments the distance travelled by the horse
    public void moveForward() {
        distanceTravelled++;
    }

    // Sets the horse to fallen
    public void fall() {
        fallen = true;
    }

    // Resets the horse to start position by setting distance travelled to 0
    public void goBackToStart() {
        distanceTravelled = 0;
        fallen = false;
    }

    // Returns symbol of the horse
    public char getSymbol() {
        return symbol;
    }

    // Returns name of the horse
    public String getName() {
        return name;
    }

    // Returns confidence of the horse
    public double getConfidence() {
        return confidence;
    }

    // Returns distance travelled by horse
    public int getDistanceTravelled() {
        return distanceTravelled;
    }

    // Returns true if the horse has fallen
    public boolean hasFallen() {
        return fallen;
    }

    // Changes the confidence of the horse, if new confidence is out of 0-1 range it sets it 0.5
    public void setConfidence(double newConfidence) {
        if (0 < newConfidence && newConfidence < 1) {
            this.confidence = newConfidence;
        } else {
            this.confidence = 0.5;
            throw new IllegalArgumentException("Error: For horse: " + this.name + ", set confidence between 0-1.");
        }
    }

    // Checks that the symbol is visible (not whitespace)
    public void setSymbol(char horseSymbol) {
        // Check if the character is in the Unicode range of visible characters
        if ((horseSymbol >= '\u0021' && horseSymbol <= '\u007E')
                || (horseSymbol >= '\u00A1' && horseSymbol <= '\uFFFD')) {
            this.symbol = horseSymbol;
        } else {
            throw new IllegalArgumentException(
                    "Error: For horse: " + this.name + ", null is not acceptable as a symbol, it has been set to ♘ instead.");
        }
    }
}
