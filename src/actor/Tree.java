package actor;

import grid.Grid;
import grid.Location;
import java.awt.Color;

/**
 *
 * @author spockm
 */
public class Tree extends GameObject implements BlockedLocation
{
    private final Color DEFAULT_COLOR = new Color(30,180,30);
    private final int CHOP_AGE_LOSS = 20;
    private final int MAX_AGE = 2000;
    
    private int age;

    public Tree()
    {
        setColor(DEFAULT_COLOR);
        age = 0;
    }
    public Tree(Color initialColor)
    {
        setColor(initialColor);
        age = 0;
    }
    public Tree(Tree b)
    {
        setLocation(b.getLocation());
        age = b.getAge();
        setColor(b.getColor());
    }
    
    public int getAge()
    {
        return age;
    }
    
    public int getPointValue()
    {
        return (age/100);
    }
    
    public void act()
    {
        int numWater = this.numWaterOnBorders();

        while(numWater > 0) //Ages only by water.
        {
            age+=numWater;
            numWater--;
        }
        
        if(age > MAX_AGE)
        {   //change back into a Seed
            Grid<GameObject> gr = getGrid();
            Location loc = getLocation();
            Seed c = new Seed();
            this.removeSelfFromGrid();
            c.putSelfInGrid(gr, loc);
        }
    }

    public void chop()
    {
        age = age - CHOP_AGE_LOSS;
        if(age < 0)
            this.removeSelfFromGrid();
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
        String result = "Tree with age "+this.getAge();
        return result;
    }
    
    @Override
    public GameObject getClone()
    {
        GameObject clone = new Tree(this);
        return clone;
    }

    
}
