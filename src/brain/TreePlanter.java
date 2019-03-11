package brain;

import actor.BotBrain;
import actor.GameObject;
import actor.Lake;
import grid.Location;

/**
 * Tree Planter checks to see if it is beside a Lake - if so, it plants a Tree.
 * @author Spock
 */
public class TreePlanter extends BotBrain
{
    public int chooseAction()
    {
        int randomDirection = (int)(Math.random()*4)*90;
        if(besideLake()) 
            return randomDirection + 2000; //Leave a Seed. 
        else 
            return randomDirection;
    }
    /*
    * This method checks if the Bot is currently beside a Lake.  
    */
    public boolean besideLake()
    {
        GameObject[][] arena = this.getArena();
        for(int direction=0; direction<360; direction+=90)
        {
            Location myLoc = new Location(getRow(), getCol());
            Location adjacent = myLoc.getAdjacentLocation(direction);
            if(isLocationOnGrid(adjacent))
                if(arena[adjacent.getRow()][adjacent.getCol()] instanceof Lake)
                    return true;
        }
        return false;        
    }
    /*
     * This is a useful method to keep you from getting indexOutOfBounds errors.
     */
    public boolean isLocationOnGrid(Location loc)
    {
        GameObject[][] arena = getArena();
        return loc.getCol() >= 0 && loc.getCol() < arena.length &&
                loc.getRow() >= 0 && loc.getRow() < arena[0].length;
    }
}
