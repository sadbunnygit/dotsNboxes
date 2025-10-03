import java.awt.*;

/**
 * Hitbox: **not used but a subclass made 
 * checks if something is within it
 * drawn only in devmode
 * 
 * @author (Danelle)
 * @version (Start: Nov 22 2023, Last Edited: Nov 29 2023)
 */
public class Hitbox
{
    protected int x;
    protected int y;
    protected int width;
    protected int height;
    
    //constructor
    public Hitbox(int x, int y, int width, int height)
    {
        this.x = x;
        this.y = y;
        this.width = width;
        this.height = height;
    }
    //getter and setter
    public int getX() {return x;}
    public int getY() {return y;}
    public void setX(int x) {this.x = x;}
    public void setY(int y) {this.y = y;}
    
    public void draw(Graphics g)
    {
        //inside
        /*# FOR DEV MODE*/
        if(Driver.DEVMODE)
        {
            g.setColor(Color.GREEN);
            g.drawRect(x,y,width,height);
            g.setColor(Color.BLACK); //reset colour
        }
    }

    //checks if a point is within the boudaries of the hitbox
    public boolean isIn(int x, int y)
    {
        if((x>=this.x)&&(x<=this.x+width)&&(y>=this.y)&&(y<=this.y+height))
            return true;
        else
            return false;
    }
}
