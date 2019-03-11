package brain;

import actor.BotBrain;

/**
 *
 * @author spockm
 */
public class CrazyCanals extends BotBrain
{        
    public int chooseAction()
    {
        //Choose a random number 0, 90, 180 or 270...
        int randomDirection = (int)(Math.random()*4)*90;
        return randomDirection + BUILD_CANAL;
    }
}
