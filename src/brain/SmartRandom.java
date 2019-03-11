/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package brain;

import actor.BlockedLocation;
import actor.BotBrain;
import grid.Location;

/**
 * SmartRandom is like RandomFarmer except that if it choose 
 * random move than is not legal, it will choose again.  
 * @author spockm
 */
public class SmartRandom extends BotBrain
{        
    public int chooseAction()
    {
        //Choose a random number 0, 90, 180 or 270...
        //These correspond to moving and leaving a Farm.  
        int randomDirection = (int)(Math.random()*4)*90;
           
        int loopCount = 0;
        //If I can't move the direction chosen, choose again! 
        while(!canMove(randomDirection) && loopCount < 10)
        {
            randomDirection = (int)(Math.random()*4)*90;
            loopCount++; 
            /*The loop counter prevents against an eternal loop 
                    if I've been blocked in. */
        }    
        
        return randomDirection;
    }
    
    public boolean canMove(int direction)
    {
        Location myLoc = new Location(getRow(), getCol());
        Location next = myLoc.getAdjacentLocation(direction);
        if(!next.isValidLocation())
            return false;
        if(getArena()[next.getRow()][next.getCol()] instanceof BlockedLocation)
            return false;
        
        return true;
    }
}
