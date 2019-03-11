package gui;

import actor.Boulder;
import actor.Crop;
import actor.DryCanal;
import actor.Farm;
import actor.Lake;
import actor.Canal;
import actor.GameObject;
import actor.Seed;
import actor.Tree;
import grid.Grid;
import grid.Location;
import grid.RatBotsGrid;
import java.awt.Color;
import java.util.ArrayList;
import java.util.Random;
import world.World;

/**
 * The Arena class includes all of the methods needed to setup the arena 
 * according to the rules of the game.  
 * @author Spock
 */
public class RatBotsArena 
{
    /**
     * The size of a side of the central starting room in the arena. 
     */
    private Random randy = new Random();
    
    public static final int CHALLENGE_1 = 1; //Get the single Crop
    public static final int CHALLENGE_2 = 2; //Plant Seed by Lake
    public static final int CHALLENGE_3 = 3; //Connect Tree to Water with Canals
    public static final int SANDBOX = 4; //SandBox Mode, 1 of everything to start
    public static final int START = 5;
    public static final int NORMAL = 6;
    
    private static int playMode = START; 
    public void setPlayMode(int in) 
    { 
        playMode = in;
    }
    public static int getPlayMode()
    {
        return playMode;
    }
    
    /**
     * Toggles whether the grid will include Blocks or not.  
     * This is an option in the Arena menu.
     */
//    public void toggleShowBlocks(World world) 
//    { 
//        withBlocks = ! withBlocks; 
//    }

    /**
     * Initializes the Arena based on the selected rules.  
     * @param world the world that the Arena is within
     */
    public void initializeArena(World world)
    {
        if(playMode == NORMAL)
        {
            addLakes(world);
            addBoulderRanges(world);
            clearHighways(world);
        }
        else if(playMode == CHALLENGE_1)
        {   //Single Crop to get
            RatBotsGrid grid = (RatBotsGrid)world.getGrid();
            Location start = world.getRandomEmptyLocation();
            new Crop().putSelfInGrid(grid, start);
        }
        else if(playMode == CHALLENGE_2)
        {   //Plant a Seed by a Lake
            addLakes(world);            
        }
        else if(playMode == CHALLENGE_3)
        {   //Connect a Lake to a Tree by Canals
            addLakes(world);
            addBoulderRanges(world);  
            clearHighways(world);
            RatBotsGrid grid = (RatBotsGrid)world.getGrid();
            new Tree(new Color(239, 16, 5)).putSelfInGrid(grid, grid.getRandomEmptyTreeLocation());
            //have each Bot add a single Tree
        }
        else if(playMode == SANDBOX)
        {
            addOneOfEverything(world);            
        }
        else if(playMode == START)
        {
            addOneOfEverything(world);            
//            playMode = NORMAL;
        }
    }
    
    private void addLakes(World world)
    {        
        addLargeLake(world, 5);
        addLargeLake(world, 4);
        addLargeLake(world, 3);
        int numExtraLakes = (int)(Math.random()*5)+1;
        for(int z=0; z<numExtraLakes; z++)
            addLargeLake(world, (int)(Math.random()*8));
    }
    private void addBoulderRanges(World world)
    {
        addBoulderRange(world, 4);
        int numExtraBoulderRanges = (int)(Math.random()*3);
        for(int z=0; z<numExtraBoulderRanges; z++)
            addBoulderRange(world, (int)(Math.random()*8));
    }

    private void addLargeLake(World world, int size)
    {
        RatBotsGrid grid = (RatBotsGrid)world.getGrid();
        Location start = getRandomNonHighwayLocation(world);
        int randomDirection = (int)(Math.random()*4)*90;
        for(int z=0; z<size; z++)
        {
            if(Math.random()>.6) randomDirection += Location.LEFT;
            else if(Math.random()>.6) randomDirection += Location.RIGHT;
            start = start.getAdjacentLocation(randomDirection);
            if(grid.isValid(start))
                new Lake().putSelfInGrid(grid, start);
        }
    }
    
    private void addBoulderRange(World world, int size)
    {
        RatBotsGrid grid = (RatBotsGrid)world.getGrid();
        Location start = getRandomNonHighwayLocation(world);
        int randomDirection = (int)(Math.random()*4)*90;
        for(int z=0; z<size; z++)
        {
            int travelDirection = randomDirection;
            if(Math.random()>.85) travelDirection += Location.LEFT;
            else if(Math.random()>.85) travelDirection += Location.RIGHT;
            start = start.getAdjacentLocation(travelDirection);
            if(grid.isValid(start))
                new Boulder().putSelfInGrid(grid, start);
        }
    }
    
    private void addOneOfEverything(World world)
    {
        RatBotsGrid grid = (RatBotsGrid)world.getGrid();
        Seed seed = new Seed();
        seed.putSelfInGrid(grid, new Location(0,0));
        Tree tree = new Tree();
        tree.putSelfInGrid(grid, new Location(0,1));
        Canal river = new Canal();
        river.putSelfInGrid(grid, new Location(0,2));
        DryCanal canal = new DryCanal();
        canal.putSelfInGrid(grid, new Location(0,3));
        Crop crop = new Crop();
        crop.putSelfInGrid(grid, new Location(0,4));
        Farm farm = new Farm();
        farm.putSelfInGrid(grid, new Location(0,5));
        Lake lake = new Lake();
        lake.putSelfInGrid(grid, new Location(0,6));
        Boulder b = new Boulder();
        b.putSelfInGrid(grid, new Location(0,7));
    }
    
    private void clearHighways(World world)
    {
        int[] highways = {0,8,9,17};
        Grid gr = world.getGrid();

        for(int z=0; z<18; z++)
        {
            for(int q : highways)
            {
                GameObject g = (GameObject)gr.get(new Location(q,z));
                if (g != null)
                    g.removeSelfFromGrid();
                g = (GameObject)gr.get(new Location(z,q));
                if (g != null)
                    g.removeSelfFromGrid();
            }
        }
    }
    
    private Location getRandomNonHighwayLocation(World world)
    {
        Grid gr = world.getGrid();

        ArrayList<Location> emptyLocs = new ArrayList<Location>();
        //Ignore the ouside rim...
        for (int i = 1; i < 17; i++)
            for (int j = 1; j < 17; j++)
            {
                if(i!=8 && i!=9 && j!=8 && j!=9)
                {
                    Location loc = new Location(i, j);
                    if (gr.isValid(loc) && gr.get(loc) == null)
                        emptyLocs.add(loc);
                }
            }
            if (emptyLocs.size() == 0)
                return null;
            
            int r = randy.nextInt(emptyLocs.size());
            return emptyLocs.get(r);
    }
    
}
