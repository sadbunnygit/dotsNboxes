import java.awt.*;


/**
 * Block: the boxes that players are trying to get
 * has 4 sides (BlockSide) and once these are all closed the block/box gets claimed by the player who closed it 
 * Once claimed, will be the colour and show the name of the owner
 * the game grid paper has a 2d array of these 
 * 
 * @author (Danelle)
 * @version (Start: Nov 20 2023, Last Edited: Jan 11 2024)
 */
public class Block
{
    //instance variables
    private int x;
    private int y;
    private static int size;
    
    //hitboxs
    BlockSide[] sides; //and their finals
    static final int NSIDES = 4; //number of hitboxes
    private static final int N = 0; // north or top
    private static final int S = 1; // south or bottom
    private static final int E = 2; // east or right
    private static final int W = 3; // west or left
    //who is the owner of this block
    private Player owner;
    
    //constructor
    public Block(int x, int y, int size)
    {
        this.x = x;
        this.y = y;
        this.size = size;
        //create Hitboxes
        sides = new BlockSide[NSIDES];
        //hort
        sides[N] = new BlockSide(x+GridPaper.DOTSIZE,y,size-GridPaper.DOTSIZE,GridPaper.DOTSIZE);
        sides[S] = new BlockSide(x+GridPaper.DOTSIZE,y+size,size-GridPaper.DOTSIZE,GridPaper.DOTSIZE);
        //vert
        sides[E] = new BlockSide(x+size,y+GridPaper.DOTSIZE,GridPaper.DOTSIZE,size-GridPaper.DOTSIZE);
        sides[W] = new BlockSide(x,y+GridPaper.DOTSIZE,GridPaper.DOTSIZE,size-GridPaper.DOTSIZE);
    }
    
    //getter and setter
    public int getX() {return x;}
    public int getY() {return y;}
    public void setX(int x) {this.x = x;}
    public void setY(int y) {this.y = y;}

    public boolean isAllClosed() //check if all sides are closed
    {
        for (BlockSide s : sides)
        {
            if(!s.isClosed())
                return false;
        }
        return true;    
    }
    
    public void setSides(int which, Player player) //set a side to whoever clicked it
    {
        sides[which].setColour(player.getColour());
        sides[which].setClosed(true);
        if (isAllClosed())
            owner = player;
        /*# FOR DEV MODE*/  if (Driver.DEVMODE){System.out.println("side set!");}
    }
    
    public void draw(Graphics g)
    {
        //fill in when all sides closed
        if (isAllClosed())
        {
            Graphics2D g2d = (Graphics2D) g;
            g2d.setColor(owner.getColour());
            int circleSize = size-GridPaper.DOTSIZE;
            g2d.fillOval(x+GridPaper.DOTSIZE,y+GridPaper.DOTSIZE,circleSize,circleSize);
            //name label
            g2d.setColor(new Color(255 - owner.getColour().getRed(), 255 - owner.getColour().getGreen(), 255 - owner.getColour().getBlue()));   //inverted colour so good contrast  
            int fontSize = circleSize/GridPaper.MAXNAMESIZE;
            g2d.setFont(new Font(Font.MONOSPACED, Font.PLAIN, fontSize)); 
            FontMetrics metrics = g2d.getFontMetrics();
            int xCentered = x+GridPaper.DOTSIZE+(circleSize/2)-((metrics.stringWidth(owner.getName()))/2); //center the string
            int yCentered = y+GridPaper.DOTSIZE+(circleSize/2)+((metrics.getHeight())/2);
            g.drawString(owner.getName(),xCentered, yCentered);
            g.setColor(Color.BLACK); //reset colour
        } 
        //draw the sides
        for(int i = 0; i < NSIDES; i++)
            sides[i].draw(g);
    }
    //reset owner and the sides
    public void reset()
    {
        owner = null;
        for (BlockSide s : sides)
            s.reset(); 
    }
}
