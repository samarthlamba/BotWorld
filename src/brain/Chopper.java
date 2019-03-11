package brain;

import actor.BotBrain;
import actor.Tree;
import grid.Location;

/**
 * This Bot looks for Trees and goes to chop them!
 * However, Chopper is REALLY BAD at getting around obstacles.  
 * @author spockm
 */
public class Chopper extends BotBrain
{
    public int chooseAction()
    {
        //Set up a variable for the location of a Tree
        Location treeLoc = new Location(0,0); //default to 0,0 as a backup.
        
        //Look through the arena for a Tree
        for(int row=0; row<getArena().length; row++)
        {
            for(int col=0; col<getArena()[0].length; col++)
            {
                if(getArena()[row][col] instanceof Tree)
                {   //When you find a Tree, remember it's location.  
                    treeLoc = new Location(row,col);
                }
            }
        }
        
        //Either chop or move to the Tree
        Location myLocation = new Location(getRow(), getCol());
        if(myLocation.distanceTo(treeLoc) == 1)
            return CHOP;
        else
            return goToLocation(treeLoc);       
    }
    
    /**
     * This method attempts to choose a good direction to move to get
     * to a goal Location, however it is REALLY BAD at getting around
     * obstacles!! It doesn't even consider them, so it tries to run
     * through them.  (Which doesn't work!) 
     * @param loc - the space you are trying to get to.  
     * @return the direction to move to get there.  
     */
    public int goToLocation(Location loc)
    {
        if(loc.getRow() > this.getRow())
            return SOUTH;
        if(loc.getRow() < this.getRow())
            return NORTH;
        if(loc.getCol() > this.getCol())
            return EAST;
        if(loc.getCol() < this.getCol())
            return WEST;
        return REST; //Must be there!!!
    }
}
