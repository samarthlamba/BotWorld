package actor;

import grid.Grid;
import grid.Location;
import java.awt.Color;

/**
 *
 * @author spockm
 */
public class Farm extends GameObject 
{
    private final Color DEFAULT_COLOR = Color.WHITE;
    private final int GROWTH_TIME = 100;
    
    private int timeUntilCrop;

    public Farm()
    {
        setColor(DEFAULT_COLOR);
        timeUntilCrop = GROWTH_TIME;
    }
    public Farm(Color initialColor)
    {
        setColor(initialColor);
        timeUntilCrop = GROWTH_TIME;
    }
    public Farm(Farm b)
    {
        setLocation(b.getLocation());
        timeUntilCrop = b.getTimeUntilCrop();
        setColor(DEFAULT_COLOR);
    }
    
    public int getTimeUntilCrop()
    {
        return timeUntilCrop;
    }
    
    public void act()
    {
        int numWater = this.numWaterOnBorders();
        //Decrease time until Crop
        timeUntilCrop--;
        if(numWater > 0) //Grows twice as fast near water.  ????
            timeUntilCrop--;
        //Convert to Crop when aged enough.  
        if(timeUntilCrop <= 0)
        {
            Grid<GameObject> gr = getGrid();
            Location loc = getLocation();
            Crop c = new Crop(getColor(),numWater);
            this.removeSelfFromGrid();
            c.putSelfInGrid(gr, loc);
        }
    }
    
    private int numWaterOnBorders()
    {
        int count = 0;
        Grid<GameObject> gr = getGrid();
        for(int dir=0; dir<360; dir+=90)
        {    
            Location neighborLoc = this.getLocation().getAdjacentLocation(dir);
            if(gr.isValid(neighborLoc))
            {
                GameObject neighbor = gr.get(neighborLoc);
                if (neighbor instanceof Lake || neighbor instanceof Canal)
                    count++;
            }
        }
        return count;
    }
    

    @Override
    public String toString()
    {
        String result = "Farm: "+this.getTimeUntilCrop()+" steps until Crop grows.";
        return result;
    }
    
    @Override
    public GameObject getClone()
    {
        GameObject clone = new Farm(this);
        return clone;
    }

    
}
