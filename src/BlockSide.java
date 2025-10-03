import java.awt.*;


/**
 * Blockside: subclass of hitbox
 * the sides of a block, will be the colour of the player who clicked
 * each block has 4 of these
 * can be closed or opened
 * has a hover effect
 * 
 * @author (Danelle)
 * @version (Start: Nov 29 2023, Last Edited: Nov 29 2023)
 */
public class BlockSide extends Hitbox
{
    //instance varibeles
    private Color colour;
    private boolean closed = false;
    private boolean hovered = false;
    
    //constructor
    public BlockSide(int x, int y, int width, int height)
    {
        super(x,y,width,height);
        colour = Color.WHITE; //default colour
    }
    
    //getter / setter
    public void setColour(Color newColour) 
    {   
        this.colour = newColour;
        /*# FOR DEV MODE*/  if (Driver.DEVMODE){System.out.println("colour changed: "+colour);}
    }
    public boolean isClosed(){return closed;}
    public void setClosed(boolean closed){this.closed = closed;}
    public boolean isHovered(){return hovered;}
    public void setHovered(boolean hovered){this.hovered = hovered;}
    
    //override the parent method
    @Override
    public void draw(Graphics g)
    {
        g.setColor(colour);
        if(closed) //draw only when closed
        {
            //sizing to make it look cooler
            if (width>height)
                g.fillRect(x-(GridPaper.DOTSIZE/2),y+(GridPaper.DOTSIZE/4), width+GridPaper.DOTSIZE, height/2);
            if (width<height)
                g.fillRect(x+(GridPaper.DOTSIZE/4),y-(GridPaper.DOTSIZE/2), width/2, height+GridPaper.DOTSIZE);
        }

        g.setColor(Color.BLACK); //reset colour
        if(hovered)
            g.drawRect(x,y, width, height);;
    }
    
    public void reset()
    {
        closed = false;
        colour = Color.WHITE;
    }
}
