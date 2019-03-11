package actor;

import grid.Location;
import java.awt.Color;

/**
 *
 * @author spockm
 */
public class DryCanal extends GameObject 
{
    public static Color DEFAULT_COLOR = new Color(160,82,45);
    
    private int distanceToLake = 44;
    
    public DryCanal()
    {
        setColor(DEFAULT_COLOR);
    }
    public DryCanal(DryCanal b)
    {
        setLocation(b.getLocation());
        setColor(DEFAULT_COLOR);
    }
    public int getDistanceToLake() 
    {
        return distanceToLake;
    }
    
    public void activate(int dist)
    {
        if(dist < distanceToLake)
        {   
            distanceToLake = dist+1;
            for(int d=0; d<360; d+=90)
            {
                Location next = getLocation().getAdjacentLocation(d);
                if(getGrid().isValid(next))
                {
                    if(getGrid().get(next) instanceof DryCanal)
                    {
                        DryCanal dc = (DryCanal)getGrid().get(next);
                        dc.activate(distanceToLake);
                    }
                }   
            }
        }
    }

    @Override
    public String toString()
    {
        String result = "DryRiver: ";
        return result;
    }
    
    @Override
    public GameObject getClone()
    {
        GameObject clone = new DryCanal(this);
        return clone;
    }
    
}
