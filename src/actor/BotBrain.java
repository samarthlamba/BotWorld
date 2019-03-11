package actor;

/**
 * @author Spock
 */
import java.awt.Color;

/**
 * A BotBrain is the 'brain' for a Bot in the BotWorld.  Each time that 
 a Bot is given its turn to act, it asks its BotBrain how it should act.  The
 Bot also provides the BotBrain with all of the sensor information it has.  
 </br>
 It is important to note that even though a BotBrain has a Location, 
 changing that location does not actually move the Rat (or the BotBrain.)  
 It is only changing where you think you are, but really you're only fooling 
 yourself.  The same is true about the other parameters a BotBrain has.  
 </br>
 * While this structure is intended to keep you from 'cheating' the game.  
 * I'm sure some of you will find a way around it.  Therefore, it is a rule 
 * that any attempts to work around the rules will eliminate you from all 
 * competitions.  (Although I would like to get feedback so that I can 
 * improve how the game works.)  
 */
public abstract class BotBrain 
{   
    //CONSTANTS for possible moves.

    /**
     * The value to return to not move during a turn. 
     */
    public static final int REST = -1;

    public static final int MOVE_NORTH_LEAVE_FARM = 0;
    public static final int MOVE_EAST_LEAVE_FARM = 90;
    public static final int MOVE_SOUTH_LEAVE_FARM = 180;
    public static final int MOVE_WEST_LEAVE_FARM = 270;
    public static final int NORTH = 0;
    public static final int EAST = 90;
    public static final int SOUTH = 180;
    public static final int WEST = 270;

    public static final int LEAVE_BOULDER = 1000;
    public static final int MOVE_NORTH_LEAVE_BOULDER = 1000;
    public static final int MOVE_EAST_LEAVE_BOULDER = 1090;
    public static final int MOVE_SOUTH_LEAVE_BOULDER = 1180;
    public static final int MOVE_WEST_LEAVE_BOULDER = 1270;

    public static final int PLANT_SEED = 2000;
    public static final int MOVE_NORTH_PLANT_SEED = 2000;
    public static final int MOVE_EAST_PLANT_SEED = 2090;
    public static final int MOVE_SOUTH_PLANT_SEED = 2180;
    public static final int MOVE_WEST_PLANT_SEED = 2270;

    public static final int BUILD_CANAL = 3000;
    public static final int MOVE_NORTH_BUILD_CANAL = 3000;
    public static final int MOVE_EAST_BUILD_CANAL = 3090;
    public static final int MOVE_SOUTH_BUILD_CANAL = 3180;
    public static final int MOVE_WEST_BUILD_CANAL = 3270;

    public static final int CHOP = 4000;        

    public static final int CULTIVATE = 5000;        
    
    //Instance Variables-----------------------------
    
    //What you know...
    private int row;
    private int col;  
    private String name;  
    private Color color;    

    //What you see...
    private GameObject[][] theArena;    
    private int numSeeds;
    
    //Match information...
    private int moveNumber; 
    private int score;
    private int bestScore;
    private int roundNumber; 
    private int roundsWon;   
    
    //Constructor=============================================================
    /**
     * Constructs a new BotBrain.
     * The name of the BotBrain will be 'default' until it is changed.
     */
    public BotBrain()
    {
        name = "default";
    }
    /**
     * Chooses the action for this BotBrain. </br>
     * @return the selected action (as an integer) 
     */
    public abstract int chooseAction();
//    {
//        // Every BotBrain that extends this class MUST override this method.
//    }
    
    /**
     * This method should include code that will initialize this BotBrain 
        at the start of a new round.  
     */
    public void initForRound()
    {
        // Every BotBrain that extends this class could override this method.
        /* empty */
    }
    
    //Accessors ----------------------------------------------
    //What you know...
    public int getCol() { return col; }
    public int getRow() { return row; }
    public String getName() { return name; } 
    public Color getColor() { return color; }
    public int getNumSeeds() { return numSeeds; }
    
    //What you see...
    public GameObject[][] getArena() { return theArena; }
    
    //Match information...
    public int getMoveNumber() { return moveNumber; }
    public int getRoundNumber() { return roundNumber; }
    public int getScore() { return score; }
    public int getBestScore() { return bestScore; }
    public int getRoundsWon() { return roundsWon; }


    public void setName(String in) { name = in; }
    
    /*
    The modifier methods listed below are used by the RatBots engine to 
    give 'vision' to your BotBrain each turn.  If you change their values, 
    you are not really changing the game, you're just lying to yourself.  
    */
    public void setLocation(int c, int r) { col = c; row = r; }
    public void setArena(GameObject[][] arena) 
    {
        theArena = arena;
    }
    
    public void setColor(Color c) { color = c; }
    public void setNumSeeds(int in) { numSeeds = in; }
    public void setMoveNumber(int in) { moveNumber = in; }
    public void setRoundNumber(int in) { roundNumber = in; }
    public void setScore(int in) { score = in; }
    public void setBestScore(int in) { bestScore = in; }
    public void setRoundsWon(int in) { roundsWon = in; }           
        
    
    
}
