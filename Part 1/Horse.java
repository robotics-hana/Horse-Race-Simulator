
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
    }
    
    
    //Other methods of class Horse
    public void fall()
    {
        
    }
    
    public double getConfidence()
    {
        
    }
    
    public int getDistanceTravelled()
    {
        
    }
    
    public String getName()
    {
        
    }
    
    public char getSymbol()
    {
        
    }
    
    public void goBackToStart()
    {
        
    }
    
    public boolean hasFallen()
    {
        
    }

    public void moveForward()
    {
        
    }

    public void setConfidence(double newConfidence)
    {
        
    }
    
    public void setSymbol(char newSymbol)
    {
        
    }
    
}
