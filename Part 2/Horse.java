
class Horse {

    // Horse object instances
    private char symbol;
    private String name;
    private double confidence;
    private String breed;
    private String equipment;
    private int distanceTravelled;
    private boolean fallen;
    private int wins;
    private int falls;

    //Horse object constructor
    public Horse(char symbol, String name, double confidence, String breed, String equipment, int wins, int falls) {
        this.symbol = symbol;
        this.name = name;
        this.confidence = confidence;
        this.breed = breed;
        this.equipment = equipment;
        this.distanceTravelled = 0;
        this.fallen = false;
        this.wins = wins;
        this.falls = falls;
    }

    //returns the symbol
    public char getSymbol() {
        return symbol;
    }

    //returns the name
    public String getName() {
        return name;
    }

    //returns the confidence
    public double getConfidence() {
        return confidence;
    }

    //returns the breed of horse
    public String getBreed() {
        return breed;
    }

    //returns the equipment
    public String getEquipment() {
        return equipment;
    }

    //returns the distance travelled
    public int getDistanceTravelled() {
        return distanceTravelled;
    }

    //returns true if horse has fallen
    public boolean hasFallen() {
        return fallen;
    }

    //increments the distance travelled
    public void moveForward() {
        distanceTravelled++;
    }

    //sets fallen to true
    public void fall() {
        fallen = true;
    }

    //increments the wins
    public void incrementWins() {
        wins++;
    }

    //increments the falls
    public void incrementFalls() {
        falls++;
    }

    //returns amount of wins
    public int getWins() {
        return wins;
    }

    //returns amount of falls
    public int getFalls() {
        return falls;
    }

}
