import java.awt.*;

/**
 * Player: Holds what colour they are, their name and the amount of boxes owned
 *
 * @author (Danelle)
 * @version (Start: Nov 28 2023, Last Edit: Dec 1 2023)
 */
public class Player
{
    // instance variables
    private Color colour;
    private int numOwned;
    private String name;

    //contructer
    public Player(Color colour, String name)
    {
        this.colour = colour;
        this.name = name;
        numOwned = 0;
    }
    
    //getter and setter    
    public Color getColour() {return colour;}
    public void setColour(Color colour) {this.colour = colour;}
    public int getNumOwned() {return numOwned;}
    public void setNumOwned(int numOwned) {this.numOwned = numOwned;}
    public String getName() {return name;}
    public void setName(String name) {this.name = name;}
}

