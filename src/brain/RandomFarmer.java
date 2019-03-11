package brain;

import actor.BotBrain;
import actor.Canal;
import actor.Farm;
import actor.GameObject;
import actor.Lake;
import grid.Location;
/**
 * @author Spock
 * RandomFarmer chooses a random move each turn.
 */
public class RandomFarmer extends BotBrain
{        
    public int chooseAction()
    {
        //Choose a random number 0, 90, 180 or 270...
        //These correspond to moving and leaving a Farm.  
        int randomDirection = (int)(Math.random()*4)*90;
       Location farm = farmBest();
        return randomDirection;
    }

    public Location farmBest()
        {
            GameObject[][] arena = this.getArena();
        double distance = 999;
        Location bestLoc = new Location(-999,-999);
        for(int row=0; row < 18; row++){
            for(int col = 0; col < 18; col++){
                 for(int direction=0; direction<360; direction+=90)
                 {
                  
                     Location current = new Location(row, col);
            Location adjacent = current.getAdjacentLocation(direction);
            if(adjacent.isValidLocation())
                if((arena[adjacent.getRow()][adjacent.getCol()] instanceof Lake || arena[adjacent.getRow()][adjacent.getCol()] instanceof Canal) && ((getLocation()).distanceTo(new Location(row, col)) < distance) && !(arena[current.getRow()][current.getCol()] instanceof Farm ))
                { 
                    bestLoc = new Location(current.getRow(), current.getCol());
              distance= (getLocation()).distanceTo(new Location(row, col));
              System.out.println("he;;o,   " + bestLoc);
           break;
            }
                }
                     
                 }
            }
        
             System.out.println("he;;o,2222   " + bestLoc);
          return bestLoc;
        }
           public Location getLocation() {
        return new Location(getRow(), getCol());
    }

}


    

