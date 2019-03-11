package actor;

import java.awt.Color;

/**
 *
 * @author spockm
 */
public class Boulder extends GameObject implements BlockedLocation
{
    
    public Boulder()
    {
        setColor(Color.DARK_GRAY);
    }
    public Boulder(Color initialColor)
    {
//        setColor(initialColor);
        setColor(Color.DARK_GRAY);
    }
    public Boulder(Boulder b)
    {
//        setLocation(b.getLocation());
        setColor(Color.BLACK);
    }
    
    
    
    @Override
    public String toString()
    {
        String result = "Boulder: ";
        return result;
    }
    
    @Override
    public GameObject getClone()
    {
        GameObject clone = new Boulder(this);
        return clone;
    }
}
