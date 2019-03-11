package actor;

import grid.Location;
import grid.ScoringNotification;
import gui.RatBotsArena;
import gui.RatBotsColorAssigner;
import java.awt.Color;
import java.util.ArrayList;
import world.BotWorld;
/**
 * Rats are the competitors in the RatBots game.  Every Bot has a BotBrain 
 that acts as its 'brain' by making decisions for its actions.  
 * @author Spock
 */
public class Bot extends GameObject implements BlockedLocation
{       
    /**
     * Each Bot has a BotBrain as it's 'brain'.  The Bot 'tells' the BotBrain 
 what it 'sees' and the BotBrain makes the decision on how to act.  
     */
    private BotBrain botBrain; 
    
//    private int extraNumbers;
    private int numSeeds;
    private final int STARTER_SEEDS = 2;
        
    private int score;
    private int roundsWon;
    private int matchesWon;
    private int matchesTied;
    private int matchesLost;
    private int totalScore = 0;
    
    private int mostRecentChoice = 0;
    
    private int mSecUsed = 0;
    
    /**
     * Constructs a red Rat without a BotBrain.  
     */
    public Bot()
    {
//        botBrain = new BotBrain();
        setColor(Color.RED);
        clearScores();
        numSeeds = STARTER_SEEDS;
    }
    /**
     * Constructs a Rat with the given color and given BotBrain for its 'brain'.
     * All rats have a BotBrain that chooses their action each turn.  
     * @param rb the BotBrain that makes decisions for this Rat.
     */
    public Bot(BotBrain rb)
    {
        botBrain = rb;
        setColor(RatBotsColorAssigner.getAssignedColor());
        clearScores();        
        numSeeds = STARTER_SEEDS;
//        if(RatBotsArena.getPlayMode() == RatBotsArena.CHALLENGE_3)
//        {
//            Tree tree = new Tree(getColor());
//            Location treeLoc = getGrid().getRandomEmptyTreeLocation();
//            if(treeLoc != null)
//                tree.putSelfInGrid(getGrid(), treeLoc);
//        }
    }
    /**
     * Constructs a copy of this Rat (that does not include the BotBrain.)
     * @param in the Rat being copied.
     */
    public Bot(Bot in)
    {
        super(in);
        setLocation(in.getLocation());
        setColor(in.getColor());
        score = in.getScore();
        numSeeds = in.getNumSeeds();

    }
    
    public void setColor(Color in)
    {
        super.setColor(in);
        if(botBrain != null)
            botBrain.setColor(in);
    }
    
    public int getNumSeeds() { return numSeeds; }

    
    public int getMostRecentChoice() { return mostRecentChoice; }
    
    
    /**
     * Overrides the <code>act</code> method in the <code>GameObject</code> 
 class to act based on what the BotBrain decides.
     */
    @Override
    public void act()
    {
        //Ask the BotBrain what to do. 
        giveDataToBotBrain();
//        System.out.println("starting: "+botBrain.getName()+botBrain.getMoveNumber());
        int choice = BotBrain.REST;
        if(botBrain != null)
            choice = botBrain.chooseAction();
//        System.out.println("done: "+botBrain.getName()+botBrain.getMoveNumber());
        mostRecentChoice = choice;
        
        turn((choice%1000)); //Turn to face the direction of the choice.
        if( (choice%1000)%90 != 0) choice = -1; //An invalid choice.  
        
        if(choice < 0)
        {
            //REST
        }
        else if(choice/1000 == 4) 
        {   
            activateChopper();
        }
        else if(choice/1000 == 5) 
        {   
            cultivate();
        }
        else if(canMove())
        {
            Location previousLoc = getLocation();
            move();
            if(choice/1000 == 0 && RatBotsArena.getPlayMode() >= RatBotsArena.SANDBOX) 
                leaveFarm(previousLoc); //0,90,180,270 = MOVE (and leave Farm)
            else if(choice/1000 == 1 && RatBotsArena.getPlayMode() >= RatBotsArena.SANDBOX) 
                leaveBoulder(previousLoc); //1000,1090,1180,1270 = MOVE and leave Boulder
            else if(choice/1000 == 2 && RatBotsArena.getPlayMode()%2 == 0) 
                leaveSeed(previousLoc); //2000,2090,2180,2270 = MOVE and leave Tree
            else if(choice/1000 == 3 && RatBotsArena.getPlayMode() >= RatBotsArena.CHALLENGE_3) 
                leaveCanal(previousLoc); //3000,3090,3180,3270 = MOVE and leave Canal                       
        }
        
        scoreTrees();
    } //end of act() method
    
    /**
     * Turns the Bot
     * @param newDirection the direction to turn to.   
     */
    public void turn(int newDirection)
    {
        setDirection(newDirection);
    }
    
    private Location nextLocation()
    {
        return getLocation().getAdjacentLocation(getDirection());
    }
    
    private boolean nextLocationValid()
    {
        return getGrid().isValid(nextLocation());
    }
        
    private boolean canMove()
    {
        return canMove(getLocation(), getDirection());
    }
   
    private boolean canMove(Location loc, int dir)
    {
        Location next = loc.getAdjacentLocation(dir);
        if(!getGrid().isValid(next)) 
            return false;
        //Make sure destination is not occupied by a Blocked Location
        //Bots, Boulders, Lakes, Rivers and Trees are Blocked Locations.  
        if(getGrid().get(next) instanceof BlockedLocation )
            return false;
        
        return true;
    }
    
    /**
     * Moves the Bot forward, putting a Farm into the location it previously
     * occupied.
     */
    public void move()
    {        
        if(canMove())
        {
            Location old = getLocation();
            Location next = old.getAdjacentLocation(getDirection());

            if(getGrid().get(next) instanceof Crop)
            {
                Crop c = (Crop)(getGrid().get(next));
                this.addToScore(c.getPointValue());
                ScoringNotification sn = new ScoringNotification(getColor(),next,c.getPointValue());
                getGrid().addScoringNotification(sn);
            }
            if(getGrid().get(next) instanceof Seed)
            {
                numSeeds++;
            }
            if(getGrid().get(next) instanceof Canal)
            {
//                updateWaterways();
                if(score < 600)
                    this.score /=2; //Lose half your points.
                else
                    this.score -= 300;
            }
                    
            moveTo(next);
        }
    }
    
    private void leaveFarm(Location loc)
    {
            //Leave a Farm behind
            new Farm(getColor()).putSelfInGrid(getGrid(), loc);
    }
    private void leaveBoulder(Location loc)
    {
            //Leave a Boulder behind
            new Boulder(getColor()).putSelfInGrid(getGrid(), loc);
    }
    private void leaveCanal(Location loc)
    {
            //Leave a Boulder behind
            new DryCanal().putSelfInGrid(getGrid(), loc);
//            updateWaterways();
//            checkForNeighboringWater(loc);
    }
    private void leaveSeed(Location loc)
    {
            //Leave a Seed behind
        if(numSeeds > 0)
        {
            new Tree(getColor()).putSelfInGrid(getGrid(), loc);
            numSeeds--;
        }
    }
    
    
    
    private void activateChopper()
    { //This chops all adjacent Trees and Crops as well as other Bots!!  
        for(int d=0; d<360; d+=90)
        {
            turn(d);
            if(nextLocationValid() && getGrid().get(nextLocation()) instanceof Tree)
            {
                Tree tree = (Tree)(getGrid().get(nextLocation()));
                tree.chop();
            } 
            if(nextLocationValid() && getGrid().get(nextLocation()) instanceof Crop)
            {
                leaveFarm(nextLocation());
            }
            if(nextLocationValid() && getGrid().get(nextLocation()) instanceof Bot)
            {
                //Not sure yet.  Perhaps remove points???
            }
        }
    }
    
    private void cultivate()
    {
        for(int d=0; d<360; d+=45)
        {
            turn(d);
            if(nextLocationValid())
            {
                GameObject g = getGrid().get(nextLocation());
                if(g instanceof Tree || g instanceof Crop)
                {
                    g.act(); 
                }
                //Needed to do this two separate times because
                //Trees could change to Seeds after one act().
                g = getGrid().get(nextLocation());
                if(g instanceof Tree || g instanceof Crop)
                {
                    g.act(); 
                }
            }
        }
    }
        
    
//    private void checkForNeighboringWater(Location loc)
//    {
//        boolean changed = false;
//        if(loc == null) return;
//        GameObject here = getGrid().get(loc);
//        if(here instanceof DryCanal)
//        {
//            ArrayList<GameObject> neighbors = getGrid().getNeighborsSquare(loc);
//            for(GameObject obj : neighbors)
//            {
//                if (obj instanceof Lake || obj instanceof Canal)
//                {
//                    Canal r = new Canal();
//                    here.removeSelfFromGrid();
//                    r.putSelfInGrid(getGrid(), loc);
//                    changed = true;
//                    break;
//                }
//            }
//            if(changed)
//                for(GameObject obj : neighbors)
//                {
//                    if(obj instanceof DryCanal)
//                        checkForNeighboringWater(obj.getLocation());
//                }
//        }
//    }
//    
//    private void updateWaterways()
//    {
//        //Make all Canals dry...
//        for(int r=0; r<18; r++)
//            for(int c=0; c<18; c++)
//            {
//                if(getGrid().get(new Location(r,c)) instanceof Canal)
//                {
////                    Canal canal = (Canal)(getGrid().get(new Location(r,c)));
////                    canal.removeSelfFromGrid();
//                    new DryCanal().putSelfInGrid(getGrid(), new Location(r,c));
//                }
//            }
//        
//        //Activate starting at each Lake
//        for(int r=0; r<18; r++)
//            for(int c=0; c<18; c++)
//            {
//                if(getGrid().get(new Location(r,c)) instanceof Lake)
//                {
//                    Lake lake = (Lake)(getGrid().get(new Location(r,c)));
//                    lake.activate(0);
//                }
//            }
//
//        //Each activated DryCanal (dist < 40) becomes Canal
//        for(int r=0; r<18; r++)
//            for(int c=0; c<18; c++)
//            {
//                if(getGrid().get(new Location(r,c)) instanceof DryCanal)
//                {
//                    DryCanal dc = (DryCanal)(getGrid().get(new Location(r,c)));
//                    if(dc.getDistanceToLake() < 40)
//                        new Canal().putSelfInGrid(getGrid(), new Location(r,c));
//                }
//            }
//        
//    }
        
    private void scoreTrees()
    {
        ArrayList<Location> places = getGrid().getOccupiedLocations();
        for(Location place : places)
        {
            if(getGrid().get(place) instanceof Tree)
            {
                Tree tree = (Tree)(getGrid().get(place));
                if(tree.getColor().equals(getColor()))
                {
                    this.score += tree.getPointValue();
                    if(tree.getPointValue() > 0)
                    {
//                        System.out.println("Tree scored "+tree.getPointValue()+" for "+tree.getColor());
                        ScoringNotification sn = new ScoringNotification(getColor(),place,tree.getPointValue());
                        getGrid().addScoringNotification(sn);
                    }
                }
            }
        }
    }

    @Override
    public String toString()
    {
        return "Rat: "+botBrain.getName();
    }

    /**
     * Updates the most recent data (location, grid and status)
 information to the BotBrain.  This allows the BotBrain to make a decision
 based on current data every turn.  
     */
    private final void giveDataToBotBrain()
    {
        //score, energy, col, row, myStuff ================
        botBrain.setScore(score);
        botBrain.setLocation(getLocation().getCol(), getLocation().getRow());
        botBrain.setNumSeeds(numSeeds);
        //match stuff: bestScore, roundsWon ===================
        botBrain.setBestScore(this.calculateBestScore());
        botBrain.setRoundsWon(this.getRoundsWon());
        //world stuff: moveNumber, roundNumber ================
        botBrain.setMoveNumber(BotWorld.getMoveNum());
        botBrain.setRoundNumber(BotWorld.getRoundNum());

        //theArena!============================================
        int numRows = getGrid().getNumRows();
        int numCols = getGrid().getNumCols();
        GameObject[][] theArena = new GameObject[numRows][numCols];
        for(int row=0; row<numRows; row++)
        {
            for(int col=0; col<numCols; col++)
            {
                GameObject a = getGrid().get(new Location(row, col));
                if(a != null)
                    theArena[row][col] = a.getClone();
                //Might need to do each with instanceof here...
                
            }
        }
        botBrain.setArena(theArena);
            
        
    } //end of giveDataToRatBot() -----------------------------
    
//    public int myScore()
//    {
//        int score =0;
//        return score;
//    }
    
    private int calculateBestScore()
    {
        int bestScore = getScore();
        ArrayList<Bot> rats = getGrid().getAllRats();
        for(Bot r : rats)
        {
            if(r.getScore() > bestScore)
            {
                bestScore = r.getScore();
            }
        }  
        return bestScore;
    }
        
    /**
     * Accessor method to get the BotBrain from a Bot.
     * @return the BotBrain 'brain' of this BotBrain.
     */
    public BotBrain getRatBot()
    {
        return botBrain;
    }

    /**
     * Gets the current score (from this round).  
     * @return the score
     */
    public int getScore() { return score; }
    /**
     * Sets the current score of this Bot.
     * @param in the score
     */
    public void setScore(int in) { score = in; }
    /**
     * Adds the given amount to score of this Bot.  
     * @param add the amount to add
     */
    public void addToScore(int add) { score += add; }
    /**
     * Gets the total points scored over all rounds in this match for this Bot.
     * @return the total score
     */
    public int getTotalScore() { return totalScore; }

    /**
     * Gets the number of rounds won by this Bot in the match.
     * @return the rounds won
     */
    public int getRoundsWon() { return roundsWon; }
        
    /**
     * Sets the number of rounds won by this Bot in the match.
     * @param in the rounds won
     */
    public void setRoundsWon(int in) { roundsWon = in; }
    /**
     * Increases the number of rounds won in this match by this Bot by one.
     */
    public void increaseRoundsWon() { roundsWon++; }

    public int getAvgMSecUsed() { return mSecUsed/BotWorld.getMoveNum(); }
    public void increaseMSecUsed(int in) { mSecUsed += in; }
    
    // These methods are used for the RoundRobin tourney.
    public int getMatchesWon() { return matchesWon; }
    public int getMatchesTied() { return matchesTied; }
    public int getMatchesLost() { return matchesLost; }
    public void increaseMatchesWon() { matchesWon++; }
    public void increaseMatchesTied() { matchesTied++; }
    public void increaseMatchesLost() { matchesLost++; }
     /**
     * Initializes this Bot for a new round.  
     */
    public final void initialize()
    {
//        System.out.println("Bot:initialize()");
        totalScore += score;
        score = 0;
        mSecUsed = 0;
        this.numSeeds = this.STARTER_SEEDS;
        botBrain.initForRound();
    }
    
    public void clearScores()
    {
        score = 0;
        roundsWon = 0;
        matchesWon = 0;
        matchesTied = 0;
        matchesLost = 0;
        totalScore = 0;
    }

    @Override
    public GameObject getClone()
    {
        GameObject clone = new Bot(this);
        return clone;
    }
    
    
}