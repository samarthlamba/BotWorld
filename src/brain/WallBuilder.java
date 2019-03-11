package brain;  //packages are used to organize code in Java.  

import actor.BotBrain;  //Since BotBrain is in a different package, it needs to be imported.  

/**
 * This is the second demo Bot for BotWorld 2018.  
 * It leaves a Boulder every fifth step each round.  
 * @author spockm
 */
public class WallBuilder extends BotBrain
{
    int moveCounter = 0;
     
    public int chooseAction()
    {
        moveCounter++;
        int randomDirection = (int)(Math.random()*4)*90;
        if(moveCounter % 5 == 0) 
            return randomDirection + 1000; //Leave a Boulder!!!
        else 
            return randomDirection;
    }
    
    /**
     * This method will be called at the start of every new round.  
     * It allows the moveCounter to be reset to zero.  
     */
    public void initForRound()
    {
        moveCounter = 0;
    }
}
