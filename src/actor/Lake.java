package actor;

import grid.Location;
import java.awt.Color;

/**
 *
 * @author spockm
 */
public class Lake extends GameObject implements BlockedLocation
{
    public Lake()
    {
        setColor(Color.BLUE);
    }
    public Lake(Lake b)
    {
        setLocation(b.getLocation());
        setColor(Color.BLUE);
    }
    
    public void activate(int dist)
    {
        for(int d=0; d<360; d+=90)
        {
            Location next = getLocation().getAdjacentLocation(d);
            if(getGrid().isValid(next))
            {
                if(getGrid().get(next) instanceof DryCanal)
                {
                    DryCanal dc = (DryCanal)getGrid().get(next);
                    dc.activate(0);
                }
            }   
        }
    }

    @Override
    public String toString()
    {
        String result = "Lake: ";
        return result;
    }
    
    @Override
    public GameObject getClone()
    {
        GameObject clone = new Lake(this);
        return clone;
    }
}
