package actor;

import java.awt.Color;

/**
 *
 * @author spockm
 */
public class Seed extends GameObject 
{
    private final Color DEFAULT_COLOR = null;//new Color(160,82,45);
    

    public Seed()
    {
        setColor(DEFAULT_COLOR);
    }
    public Seed(Seed b)
    {
        setLocation(b.getLocation());
        setColor(DEFAULT_COLOR);
    }    

    @Override
    public String toString()
    {
        String result = "Seed ";
        return result;
    }
    
    @Override
    public GameObject getClone()
    {
        GameObject clone = new Seed(this);
        return clone;
    }


}
