package actor;

import java.awt.Color;

/**
 *
 * @author spockm
 */
public class Crop extends GameObject 
{
    private final Color DEFAULT_COLOR = Color.YELLOW;
    private final int DEFAULT_VALUE = 4;
    
    private int value;

    public Crop()
    {
        setColor(DEFAULT_COLOR);
        value = DEFAULT_VALUE;
    }
    public Crop(Color initialColor, int numWaterOnBorder)
    {
        setColor(initialColor);
        value = DEFAULT_VALUE*(numWaterOnBorder+1)*(numWaterOnBorder+1);
    }
    public Crop(Crop b)
    {
        setLocation(b.getLocation());
        setColor(DEFAULT_COLOR);
        value = b.getPointValue();
    }
    
    public int getPointValue()
    {
        return value;
    }
    
    
    

    @Override
    public String toString()
    {
        String result = "Crop: worth "+getPointValue()+" points";
        return result;
    }
    
    @Override
    public GameObject getClone()
    {
        GameObject clone = new Crop(this);
        return clone;
    }

}
