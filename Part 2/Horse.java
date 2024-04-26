
class Horse {
    private char symbol;
    private String name;
    private double confidence;
    private String breed;
    private String equipment;
    private int distanceTravelled;
    private boolean fallen;
    private int wins;
    private int falls;
   
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
   
    public char getSymbol() {
    return symbol;
    }
   
    public String getName() {
    return name;
    }
   
    public double getConfidence() {
    return confidence;
    }
   
    public String getBreed() {
    return breed;
    }
   
    public String getEquipment() {
    return equipment;
    }
   
    public int getDistanceTravelled() {
    return distanceTravelled;
    }
   
    public boolean hasFallen() {
    return fallen;
    }
   
    public void moveForward() {
    distanceTravelled++;
    }
   
    public void fall() {
    fallen = true;
    }
   
    public void incrementWins() {
    wins++;
    }
   
    public void incrementFalls() {
    falls++;
    }
   
    public int getWins() {
    return wins;
    }
   
    public int getFalls() {
    return falls;
    }
   }
   