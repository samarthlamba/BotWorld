package actor;

import java.awt.Color;

/**
 *
 * @author spockm
 */
public class Canal extends GameObject
{
    public Canal()
    {
        setColor(Color.BLUE);
    }
    public Canal(Color initialColor)
    {
        setColor(initialColor);
    }
    public Canal(Canal b)
    {
        setLocation(b.getLocation());
        setColor(Color.BLUE);
    }
    
    @Override
    public String toString()
    {
        String result = "River: ";
        return result;
    }
    
    @Override
    public GameObject getClone()
    {
        GameObject clone = new Canal(this);
        return clone;
    }
}
