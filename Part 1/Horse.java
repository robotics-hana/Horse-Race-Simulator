
/**
 * Write a description of class Horse here.
 * 
 * @author hana
 * @version (1)
 */

public class Horse
{
    // Static field to keep track of the number of Horse objects created
    private static int horseCount = 0;    
    
      
    //Constructor of class Horse
    /**
     * Constructor for objects of class Horse
     */
    public Horse(char horseSymbol, String horseName, double horseConfidence)
    {
               if (horseCount >= 3) {
            throw new IllegalStateException("Cannot create more than 3 Horse objects.");
        }
        // Increment the horseCount to track the number of Horse objects created
        horseCount++;

        // Initialize variables
        this.distanceTravelled = 0;
        this.fallen = false;
        this.name = horseName;

        // Set the symbol, default to '♘' if the given symbol is not valid (not visible)
        try {
            isCharacterVisible(horseSymbol);
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

    }
    
    
     // Sets the horse to fallen
    public void fall() {
        fallen = true;
    }

    
   // Returns confidence of the horse
    public double getConfidence() {
        return confidence;
    }
    
    // Returns distance travelled by horse
    public int getDistanceTravelled() {
        return distanceTravelled;
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
    
    // Returns true if the horse has fallen
    public boolean hasFallen() {
        return fallen;
    }

    // Increments the distance travelled by the horse
    public void moveForward() {
        distanceTravelled++;
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
    public void setSymbol(char newSymbol) {
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
