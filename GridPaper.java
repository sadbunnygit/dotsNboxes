import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.*;

/**
 * GridPaper: Draws out the dots and boxes, and other info to help the user
 * has the boxes and the players
 * Where the gameplay happens
 * has all the game logic, how turns work and winning/losing
 * Listens to mouse clicks and movement
 *
 * @author (Danelle)
 * @version (Start: Nov 20 2023, Last Edited: Jan 11 2024)
 */
public class GridPaper extends JPanel implements MouseListener, MouseMotionListener
{
    //public static final ints
        //sizes
    public static final int HEIGHT = 480; 
    public static final int WIDTH = 640;
    public static final int GRIDSIZE = 360;
    public static final int GRIDX = 20; 
    public static final int GRIDY = HEIGHT-GRIDSIZE-40;
    public static final int DOTSIZE = 20;
        //maxes
    public static final int MAXPLAYERS = 8;
    public static final int MAXNAMESIZE = 3;
    
    //grid stuff
    int gridSize; 
    Block[][] blocks;
    //player stuff
    int numOfPlayers = 2; 
    Player[] players;
    int currentPlayer = 0; //player 0 is player 1
        
    Random rand;
    
    // constructor
    public GridPaper(int gridSize, int numOfPlayers)
    {
        rand = new Random();
        
        addMouseListener(this); //listen to the mouse
        addMouseMotionListener(this);//and its movement
        setPreferredSize( new Dimension(WIDTH,HEIGHT) );
        setBackground(Color.GRAY.brighter()); //light grey background
        
        //set up game
            //grid
        this.gridSize = gridSize;
        blocks = new Block[gridSize][gridSize];
        createGrid();
            //players
        this.numOfPlayers = numOfPlayers;
        players = new Player[numOfPlayers];
        currentPlayer = 0;
        createPlayers();
            
        repaint();
    }
    //---intilize arrays, methods that could just not be methods an in the constructor---
    private void createGrid()
    {
        int blockSize = GRIDSIZE/gridSize;
        for (int x = 0; x<gridSize; x++)
        {
            for (int y= 0; y<gridSize; y++)
                blocks[x][y] = new Block(GRIDX+(x*blockSize),GRIDY+(y*blockSize), blockSize);
        }
    }
    private void createPlayers()//make the player with their choseen colours
    {
        for (int p = 0; p<numOfPlayers; p++)
        {
            //choose the colour
            Color colour = chooseColour(p, "Player "+(p+1)+", choose your colour:");
            //make their name
            String name = getAmountOfChar(1,MAXNAMESIZE,"Player "+(p+1)+", enter your (1-"+MAXNAMESIZE+") initials:", p);
            //make the player with their choseen colour
            players[p] = new Player(colour, name);    
        }
    }
    
    //---Drawing and drawing methods--- 
    public void paintComponent(Graphics g)
    {
        super.paintComponent(g);
        /*# FOR DEV MODE*/  if (Driver.DEVMODE){System.out.println("DRWAING");}
        drawGrid(g);
        drawWhoseTurn(g,GRIDX+50,GRIDY-20,45);
        drawPlayerInfo(g, 40, GRIDY+40,GRIDX+GRIDSIZE+36);
    }
    //draw the grid (dots )
    private void drawGrid(Graphics g)
    {
        int blockSize = GRIDSIZE/gridSize;    
        //border                                                               
        g.setColor(Color.WHITE);                                           //roundnesss
        g.fillRoundRect(GRIDX-5,GRIDY-5,GRIDSIZE+DOTSIZE+10,GRIDSIZE+DOTSIZE+10,20,20);
        g.setColor(Color.BLACK);
        g.drawRoundRect(GRIDX-5,GRIDY-5,GRIDSIZE+DOTSIZE+10,GRIDSIZE+DOTSIZE+10,20,20);
        //boxes
        drawBoxes(g);
        //dots
        for (int x = GRIDX; x<=(GRIDSIZE+GRIDX); x+=blockSize)
        {
            for (int y = GRIDY; y<=(GRIDSIZE+GRIDY); y+=blockSize)
                g.fillOval(x,y, DOTSIZE,DOTSIZE);
                //g.fillOval(x+(DOTSIZE/4),y+(DOTSIZE/4), DOTSIZE/2,DOTSIZE/2);//dot looks smaller than is
        }
    }
    //draw the boxes
    private void drawBoxes(Graphics g)
    {
        for (int x = 0; x<gridSize; x++)
        {
            for (int y= 0; y<gridSize; y++)
                blocks[x][y].draw(g);
        }
    }
    //draw the player info
    private void drawPlayerInfo(Graphics g, int size, int y, int x)
    {
        //header
        g.setColor(Color.BLACK);
        g.setFont(new Font("Arial", Font.BOLD, size+2)); //Title is bigger
        g.drawString("Scores:", x, y);
        y+=size+2;
        //draw each player scores
        for (int p = 0; p<players.length;p++)
        {    
            g.setColor(players[p].getColour()); 
            if(p==currentPlayer)//if their turn bold and highlight, not not )
            {
                g.setColor(new Color(255 - players[p].getColour().getRed(), 255 - players[p].getColour().getGreen(), 255 - players[p].getColour().getBlue()));   //inverted colour so good contrast  
                g.fillRect(x,y-size+10,(size/2)*10,size-8); //highlight
                g.setColor(players[p].getColour());     
                g.setFont(new Font("Arial", Font.BOLD, size));
            }    
            else
                g.setFont(new Font("Arial", Font.PLAIN, size));
            g.drawString(players[p].getName()+": "+(players[p].getNumOwned()), x, y);
            y+=size; //increase y so the next player is below
        }
    }
    private void drawWhoseTurn(Graphics g, int size, int y, int x)
    {    
        g.setColor(players[currentPlayer].getColour());     
        g.setFont(new Font("sadbunny was here", Font.BOLD, size));
        g.drawString(players[currentPlayer].getName()+"'s turn!", x, y);        
    }
    //---get input methods---
    //get a string from an joptian pane, with a specific amount of characters
    private String getAmountOfChar(int min, int max, String prompt, int p)//returns a character string of a certain length range
    {
        String string = JOptionPane.showInputDialog(prompt);//it will add even more telling them its wrong every time so they get the message
        if (string == null)//cancel
        {
            Driver.deleteGameFrame();
            return "swh"; //sadbunny was here
        }
        for (int i = 0; i<string.length(); i++) //check each characeter
        {
            if (string.charAt(i)==' ')//if theres is a space
                return getAmountOfChar(min,max,prompt+" * no spaces pls",p); 
        }
        for (int i = 0; i<p; i++) //loop up to current chooser
        {
            if(players[i].getName().equals(string))
                return getAmountOfChar(min,max,prompt+" * Taken!, try again",p); 
        }
        return (string.length()>0&&string.length()<4)?string:getAmountOfChar(min,max,prompt+" *"+min+"-"+max+" characters, try again",p);//only returrn string if it's 2 characters
    }
    //get a colour from a colour chooser, checks if the chosen one is taken
    private Color chooseColour(int p, String prompt) 
    {
        Color colour = JColorChooser.showDialog(this, prompt,null);
        if (colour == null)
            colour = new Color(rand.nextInt(256), rand.nextInt(256), rand.nextInt(256));; //random colour for those who decieded to click x, so no cancel when picking colour!! this is an easter egg 
        // Check if taken colour
        for (int i = 0; i < p; i++) 
        {
            if (players[i].getColour().equals(colour)) 
                return chooseColour(p, prompt + " That is taken. Choose another.");
        }
        ///return their choice if not taken
        return colour;
    }
    
    //---Game functions---
    public void reset()
    {
        //reset each blcok
        for (int x = 0; x < gridSize; x++) 
        {
            for (int y = 0; y < gridSize; y++) 
                blocks[x][y].reset();
        }
        for (int p = 0; p < numOfPlayers; p++) 
            players[p].setNumOwned(0); // Reset player stats
        currentPlayer = 0; // Reset the current player
        repaint(); // Repaint the panel 
    }
    private void checkForWin()
    {
        for (Block[] row : blocks) 
        {
            for (Block b : row) 
            {
                for (BlockSide s : b.sides) 
                {
                    if (!s.isClosed()) 
                        return; // if any sides are open, no boxes are filled, exit the function
                }
            }
        }
        
        // if not returned already then there is a win, now display that and the final stats
        
        //find the highest score and if there is a tie
        int score; int maxScore = 0;
        boolean isTie = false;
        for (Player p : players) 
        {
            score = p.getNumOwned();
            if (score > maxScore)
            {
                maxScore = score;
                isTie = false; //reset tie if a new high score is found
            }
            else if (score == maxScore) 
                isTie = true; // same score means tie
        }
        //the coloured and formated text to appear
        String message = "sadbunny was here!!";
        if (isTie)
            message = "<html>" + "<h1 style='color: #6610F2;'>" + "We have a tie!"+"</h1>"; //when more than 1 winner
        else
            message = "<html>" + "<h1 style='color: #08cc1f;'>" + "Winner Winner Chicken Dinner!"+"</h1>"; //when 1 winner
        String losersMessage = "<h2 style='color: #da0712;'>Loser(s):</h3>"; //we want losers after so add after
        for (Player p : players) 
        {
            score = p.getNumOwned();
            if (score == maxScore) //if they have a winning score
                message += "<h3 style='color: rgb("+p.getColour().getRed()+", "+p.getColour().getGreen()+", "+p.getColour().getBlue()+");'> \t"+"  ★  " + p.getName() + " - Score: " + score+"</h3>";
            else 
                losersMessage += "<h4 style='color: rgb("+p.getColour().getRed()+", "+p.getColour().getGreen()+", "+p.getColour().getBlue()+");'> \t"+"  ➤  " + p.getName() + " - Score: " + score+"</h4>";
        }
        message += losersMessage;  //we want losers after so add after
        message +=  "<p><br>Do you want to play again?</p>" + "</html>";
        if(JOptionPane.YES_OPTION == JOptionPane.showOptionDialog(this, message, "Game Over", JOptionPane.YES_NO_OPTION, JOptionPane.INFORMATION_MESSAGE, null,null,null))
            reset();
        else//quit
        {
            this.setVisible(false);
            Driver.deleteGameFrame();
        }
    }
    
    //~~~~Mouse Listeners~~~~
    //Mouse Clicked 
    public void mouseClicked(MouseEvent me)
    {
        /*# FOR DEV MODE*/  if (Driver.DEVMODE){System.out.println("\n"+me.getX() + " CLICKED " + me.getX());}
        boolean nextPlayer = false;
        boolean closedTheBox = false;
        //Check what hitbox it is in
        //go through each block 
        for (int x = 0; x<gridSize; x++)
        {
            for (int y= 0; y<gridSize; y++)
            {
                //and the sides
                for(int i = 0; i < blocks[x][y].NSIDES; i++)
                {
                    if(blocks[x][y].sides[i].isIn(me.getX(),me.getY())&&!blocks[x][y].sides[i].isClosed())
                    {
                        /*# FOR DEV MODE*/  if (Driver.DEVMODE){System.out.println("IN HITBOX!");}
                        blocks[x][y].setSides(i,players[currentPlayer]);
                        //next player
                        nextPlayer = true;
                        if(blocks[x][y].isAllClosed())//meaning that they closed it, they get anotehrt nturn
                        {   
                            players[currentPlayer].setNumOwned(players[currentPlayer].getNumOwned()+1);//increaes how much they own
                            nextPlayer = false; //htey go again
                            closedTheBox = true; 
                        }
                    }
                }
            }
        }   
        if(nextPlayer&&!closedTheBox)
        {
            currentPlayer++;
            if(currentPlayer>=numOfPlayers)
                currentPlayer=0;
             /*# FOR DEV MODE*/  if (Driver.DEVMODE){System.out.println("Player: "+currentPlayer);}
        }
        repaint();
        //check for win everytime its updated
        checkForWin();
    }
    public void mousePressed(MouseEvent me){}
    public void mouseReleased(MouseEvent me){}
    public void mouseEntered(MouseEvent me){}
    public void mouseExited(MouseEvent me){}
    //motion
    public void mouseMoved(MouseEvent me) //this is for a hover effect to see where you are clicking
    {
        /*# FOR DEV MODE*/  if (Driver.DEVMODE){System.out.println("\n"+me.getX() + " MOVED " + me.getX());}
        //Check what hitbox it is in
        //go through each block 
        for (int x = 0; x<gridSize; x++)
        {
            for (int y= 0; y<gridSize; y++)
            {
                //and the sides
                for(int i = 0; i < blocks[x][y].NSIDES; i++)
                {
                    if(blocks[x][y].sides[i].isIn(me.getX(),me.getY())&&!blocks[x][y].sides[i].isClosed())
                    {
                        /*# FOR DEV MODE*/  if (Driver.DEVMODE){System.out.println("HOVERED IN HITBOX!");}
                        blocks[x][y].sides[i].setHovered(true);
                    }
                    else
                        blocks[x][y].sides[i].setHovered(false); //when not in
                }
            }
        }  
        repaint();
    }
    public void mouseDragged(MouseEvent me) {}
}
