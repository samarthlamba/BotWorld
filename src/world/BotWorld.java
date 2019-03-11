 
package world; 

import actor.Bot;
import actor.BotBrain;
import actor.Canal;
import actor.DryCanal;
import actor.GameObject;
import actor.Lake;
import actor.Seed;
import grid.Grid;
import grid.Location;
import grid.RatBotsGrid;
import gui.RatBotsArena;
import java.util.ArrayList;
import java.util.Random;

/**
 * A RatBotWorld is full of RatBotActors used in the game RatBots.  
 * @author Spock
 */
public class BotWorld extends ActorWorld
{
    /**
     * The number of moves in a round of RatBots competition.
     */
    public static final int NUM_MOVES_IN_ROUND = 500;
    /**
     * The number of rounds in a match of RatBots competition.  
     */
    public static final int NUM_ROUNDS_IN_MATCH = 100;
    
    public static final int MAX_AVG_MSEC = 25;

    private static final String DEFAULT_MESSAGE = "RatBots is awesome.";
    private static Random randy = new Random();

    private static int moveNum = 1;
    private static int roundNum = 1;
    
    private boolean roundRobin = false;
    private int rr1=0; 
    private int rr2=0;
    private boolean matchReady = false;    
    
    
    private RatBotsArena arena = new RatBotsArena();
    
    private ArrayList<Bot> ratsInMaze = new ArrayList<Bot>();
    private ArrayList<Bot> allrats = new ArrayList<Bot>();
    
    /**
     * Constructs a BotBrain world with a default grid.
     */
    public BotWorld()
    {
        initializeGridForRound();
        initializeMatch();
    }

    /**
     * Constructs a BotBrain world with a given grid.
     * @param grid the grid for this world.
     */
    public BotWorld(Grid<GameObject> grid)
    {
        super(grid);
        initializeGridForRound();
        initializeMatch();
    }

    /**
     * gets the Arena used in this World.
     * @return the Arena
     */
    public RatBotsArena getArena() { return arena; }
    /**
     * Gets the current move number in the round being played.  
     * @return the move number in the current round.
     */
    public static int getMoveNum() { return moveNum; }
    /**
     * Gets the current round number in the match being played.  
     * @return the round number in the current match.
     */
    public static int getRoundNum() { return roundNum; }
    
    public void startRoundRobin()
    {
        if(allrats.size() > 0)
        {
            roundRobin = true;
            System.out.println("Beginning Round-Robin for "+allrats.size()+" rats.");
            initializeMatch();
        }
    }
    
    
    public void initializeMatch()
    {
//        System.out.println("INITIALIZING MATCH");
        moveNum = 1;
        roundNum = 1;
        matchReady = true;
        
        if(roundRobin)
        {
            initializeRoundRobinMatch();
        }
        initializeGridForRound();
    }
    
    private void initializeRoundRobinMatch()
    {
        if(ratsInMaze.size() > 1)
        {
            System.out.println(ratsInMaze.get(0).getRatBot().getName()+","+ratsInMaze.get(0).getRoundsWon()
                    +","+  ratsInMaze.get(1).getRatBot().getName()+","+ratsInMaze.get(1).getRoundsWon() );
//                        +"      time(sec)="+(int)((System.currentTimeMillis() - matchstart)/1000));
            if(ratsInMaze.get(0).getRoundsWon()+ratsInMaze.get(1).getRoundsWon()<100)
                System.out.println("Incomplete match...???");
            //score match
            if(ratsInMaze.get(0).getRoundsWon()>ratsInMaze.get(1).getRoundsWon())
            {
                ratsInMaze.get(0).increaseMatchesWon();
                ratsInMaze.get(1).increaseMatchesLost();
            }
            if(ratsInMaze.get(0).getRoundsWon()==ratsInMaze.get(1).getRoundsWon())
            {
                ratsInMaze.get(0).increaseMatchesTied();
                ratsInMaze.get(1).increaseMatchesTied();
            }
            if(ratsInMaze.get(0).getRoundsWon()<ratsInMaze.get(1).getRoundsWon())
            {
                ratsInMaze.get(1).increaseMatchesWon();
                ratsInMaze.get(0).increaseMatchesLost();
            }
            ratsInMaze.get(0).setRoundsWon(0);
            ratsInMaze.get(1).setRoundsWon(0);

        }
        ratsInMaze.clear(); 
        rr2++;
        if(rr2==allrats.size())
        {
            rr1++;
            rr2=rr1+1;
            if(rr1==allrats.size()-1)
            {
                for(Bot x : allrats)
                {
                    System.out.println(x.getRatBot().getName()+
                            ",  TP=,"+x.getTotalScore() +
                            ",  w=,"+x.getMatchesWon()+
                            ",  t=,"+x.getMatchesTied()+
                            ",  l=,"+x.getMatchesLost()                              
                            );
                }
                System.out.println("TOURNEY COMPLETE");
                roundRobin = false;
                return;
            }
        }
        ratsInMaze.add(allrats.get(rr1));
        ratsInMaze.add(allrats.get(rr2));
        
    }
    
    public void clearAllRats()
    {
        ratsInMaze.clear();
        initializeGridForRound();
    }
    
    public void zeroScoreAllRats()
    {
        for(Bot b : ratsInMaze)
        {
            b.clearScores();
        }
    }
    /**
     * Initialize the arena and each of the RatBots for a round of competition.
     */
    public final void initializeGridForRound()
    {
        clearAllObjectsFromGrid();
        arena.initializeArena(this);

        for(Bot r : ratsInMaze) 
        {
            r.initialize();
//            System.out.println("INIT in BotWorld");
            r.putSelfInGrid(getGrid(), getRandomEmptyCenterLocation());
            r.setDirection(getRandomDirection());
        }
        
        moveNum = 1; 
    }
    /**
     * Clears the Arena in preparation of starting a new round. 
     * @return an ArrayList of the Rats in the arena.
     */
    public void clearAllObjectsFromGrid()
    {
        RatBotsGrid<GameObject> gr = (RatBotsGrid<GameObject>)this.getGrid();
        for(int x=0; x<gr.getNumCols(); x++)
        {
            for(int y=0; y<gr.getNumRows(); y++)
            {
                Location loc = new Location(y,x);
                GameObject a = gr.get(loc);
                if(a != null)
                {
                    a.removeSelfFromGrid();
                }
            }
        }
    }
    /**
     * Scores the results from a round of competition.
     */
    public void scoreRound()
    {
        
        int max = calcMaxScore();
        for(Bot r : ratsInMaze)
        {
            if(r.getScore() == max && max > 0)
                r.increaseRoundsWon();
        }
        roundNum++;
        setMessage("Starting round #"+roundNum);
    }
    public int calcMaxScore() 
    {
        int maxScore = -5000;
        for(Bot r : ratsInMaze)
        {
            if(r.getScore() > maxScore)
                maxScore = r.getScore();
        }
        return maxScore;
    }

    
    //inheirits javadoc comment from world.
    @Override
    public void show()
    {
        if (getMessage() == null)
            setMessage(DEFAULT_MESSAGE);
        super.show();

    }

    //inheirits javadoc comment from world.
    @Override
    public void step()
    {
        Grid<GameObject> gr = getGrid();
        updateWaterways();
        //This only applies for the FIRST call to this method.  ?????
        if(!matchReady)
            initializeMatch();
        
        //Get all the Actors in the Grid----------------------
        ArrayList<GameObject> actors = new ArrayList<GameObject>();
        // Look at all grid locations.
        for (int r = 0; r < gr.getNumRows(); r++)
        {
            for (int c = 0; c < gr.getNumCols(); c++)
            {
                // If there's an object at this location, put it in the array.
                Location loc = new Location(r, c);
                if (gr.get(loc) != null) 
                    actors.add(gr.get(loc));
            }
        }
        
        //--------------Shuffle their order----------------------
        if(actors.size() > 1)
        {
            //shuffle their order for acting.
            for(int z=0;z<actors.size()*2;z++)
            {
                //Pick a random one.
                int from = randy.nextInt(actors.size());
                //Swap it to the front.
                GameObject a = actors.get(from);
                GameObject b = actors.get(0);
                actors.set(from,b);
                actors.set(0,a);              
            }
        }

        //-------------Have them each act()--------------------------
        int numPowerUps = 0; //going to count the total number of PowerUps too... 
        for (GameObject a : actors)
        {
            if(a instanceof Seed)
                numPowerUps++;
            if (a.getGrid() == gr)
            {
                if(a instanceof Bot)
                {
                    Bot b = (Bot)a;
                    if(b.getAvgMSecUsed() < MAX_AVG_MSEC)
                    {
                        long startTime = System.currentTimeMillis();
                        a.act();
                        long finishTime = System.currentTimeMillis();
//                        System.out.println(finishTime+"-"+startTime);
                        b.increaseMSecUsed((int)(finishTime-startTime));
                    }
                    else
                    {
                        System.out.println("Skipped a turn due to SLOW TIME ");
                    }
                }
                else
                    a.act();
            }
        }
        if(((RatBotsGrid)gr).isMessageWaiting())
            setMessage(((RatBotsGrid)gr).getMessage());
        
        //*** UPDATE THE SCORES ***
        
        //--------ADD A POWERUP--------------------------------------
        if(numPowerUps < 2 && Math.random()<.01 && RatBotsArena.getPlayMode() >= RatBotsArena.SANDBOX)
            addRandomSeed();
                
        //--------------Is round/match over?-------------------------
        moveNum++;
        if(moveNum > NUM_MOVES_IN_ROUND)
        {
            scoreRound();
            initializeGridForRound();
            
//            moveNum = 1;
        }
        if(roundNum > NUM_ROUNDS_IN_MATCH && roundRobin) 
        {
            initializeMatch();
            return;
        }         
                
    }
    
    private void updateWaterways()
    {
        //Make all Canals dry...
        for(int r=0; r<18; r++)
            for(int c=0; c<18; c++)
            {
                if(getGrid().get(new Location(r,c)) instanceof Canal)
                {
                    new DryCanal().putSelfInGrid(getGrid(), new Location(r,c));
                }
            }
        
        //Activate starting at each Lake
        for(int r=0; r<18; r++)
            for(int c=0; c<18; c++)
            {
                if(getGrid().get(new Location(r,c)) instanceof Lake)
                {
                    Lake lake = (Lake)(getGrid().get(new Location(r,c)));
                    lake.activate(0);
                }
            }

        //Each activated DryCanal (dist < 40) becomes Canal
        for(int r=0; r<18; r++)
            for(int c=0; c<18; c++)
            {
                if(getGrid().get(new Location(r,c)) instanceof DryCanal)
                {
                    DryCanal dc = (DryCanal)(getGrid().get(new Location(r,c)));
                    if(dc.getDistanceToLake() < 40)
                        new Canal().putSelfInGrid(getGrid(), new Location(r,c));
                }
            }
    }
    
    
    
    /**
     * Add a new BotBrain to the arena. 
     * @param bot the BotBrain to be added.  
     */
    public void add(BotBrain bot)
    {
        Bot newRat = new Bot(bot);
//        Location inCenter = this.getRandomEmptyCenterLocation();
        
        ratsInMaze.add(newRat);
        initializeGridForRound();
    }    
    public void addToAllRats(BotBrain bot)
    {
        Bot newRat = new Bot(bot);
        allrats.add(newRat);        
    }
    public void addFromAllRats(int num)
    {
        if(num<allrats.size())
        {
            try 
            {
                Class c = allrats.get(num).getRatBot().getClass();
                BotBrain r = (BotBrain)c.newInstance();
                String name = c.getName();
                if(name.contains("."))
                {
                    String names[] = name.split("\\.");
                    name = names[names.length-1];
                }

                r.setName(name);
                add(r);
            } 
            catch (InstantiationException ex) {  } //oh well...
            catch (IllegalAccessException ex) {  } //oh well...
        }
            
    }
    public void resetRatsInMaze()
    {
        for(Bot r : ratsInMaze)
            r.clearScores();
    }
    /**
     * Gets one of the 4 possible directions.
     * @return a random direction.
     */
    public int getRandomDirection()
    {
        return randy.nextInt(4)*90;
    }
    /**
     * Gets an empty Location in the center room.  
     * @return a random empty Location from the center room.  
     */
    public Location getRandomEmptyCenterLocation()
    {
        Grid<GameObject> grid = getGrid();
        int rows = grid.getNumRows();
        int cols = grid.getNumCols();
        int centerRow = rows/2; 
        int centerCol = cols/2;
        // get all valid empty locations and pick one at random
        ArrayList<Location> emptyLocs = new ArrayList<Location>();
        for(int row=centerRow-1; row<centerRow+2; row++)
        {
            for(int col=centerCol-1; col<centerCol+2; col++)
            {
                Location loc = new Location(row,col);
                if(grid.get(loc)==null)
                {
                    emptyLocs.add(loc);
                }
            }
        }
        
        if (emptyLocs.isEmpty()) //Go wider (24 places!)
        {
            for(int row=centerRow-2; row<centerRow+3; row++)
            {
                for(int col=centerCol-2; col<centerCol+3; col++)
                {
                    Location loc = new Location(row,col);
                    if(grid.get(loc)==null)
                    {
                        emptyLocs.add(loc);
                    }
                }
            }
        }
        if (emptyLocs.isEmpty()) //Go wider (48 places!)
        {
            for(int row=centerRow-3; row<centerRow+4; row++)
            {
                for(int col=centerCol-3; col<centerCol+4; col++)
                {
                    Location loc = new Location(row,col);
                    if(grid.get(loc)==null)
                    {
                        emptyLocs.add(loc);
                    }
                }
            }
        }

        if (emptyLocs.isEmpty())
        {
            System.out.println("WARNING: could not find an empty non-center location!!! ");
            return new Location(15,15);
        }
        int r = randy.nextInt(emptyLocs.size());
        return emptyLocs.get(r);       

    }
     
    public Location getEmptyRandomPrizeLocation()
    {
        Location loc;
        Grid<GameObject> grid = getGrid();
        int rows = grid.getNumRows();
        int cols = grid.getNumCols();
        
        int loopCount = 0;
        do
        {
            loopCount++;
            loc = new Location(randy.nextInt(rows),randy.nextInt(cols));
        } while( (isNearRat(loc) || !grid.isValid(loc) || grid.get(loc) != null) && loopCount<100);
        
        if(loopCount==100) return null; //no space found!
        
        return loc;
    }
    
    public boolean isNearRat(Location loc)
    {
        int PRIZE_PROXIMITY = 10;
        RatBotsGrid grid = (RatBotsGrid)this.getGrid();
        ArrayList<Bot> allRats = grid.getAllRats();
        for(Bot r : allRats)
        {
            if(loc.distanceTo(r.getLocation()) < PRIZE_PROXIMITY)
                return true;
        }
        
        return false;
    }
    
    
    
//    public Location getRandomEmptySuperPrizeLocation()
//    {
//        ArrayList<Location> prizeSpots = getSuperPrizeLocations();
//        for(int q=0;q<10;q++)
//        {
//            Location loc = prizeSpots.get(randy.nextInt(prizeSpots.size()));
//            if(getGrid().get(loc) == null || getGrid().get(loc) instanceof Marker)
//                return loc;
//        }
//        return null;
//    }
        
//    public boolean isInRoom(Location loc)
//    {
//        for(Location prize : getSuperPrizeLocations())
//        {
//            if(loc.distanceTo(prize) <= 2)
//                return true;
//        }
//        return false;
//    }
    
//    public void addPrize()
//    {
//        RatBotsGrid gr = (RatBotsGrid)this.getGrid();
//        Location p = gr.getEmptyRandomPrizeLocation();
//        if(p != null)
//            new Prize(0,Prize.DEFAULT_VALUE).putSelfInGrid(getGrid(), p);                
//    }
    
    public void addRandomSeed()
    {
        Location loc = getEmptyRandomPrizeLocation();
        if(loc != null)
            new Seed().putSelfInGrid(getGrid(), loc);
    }
    
}
