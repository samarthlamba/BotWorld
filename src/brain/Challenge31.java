/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package brain;

import actor.BotBrain;
import java.util.ArrayList;
import java.util.Collections;
import actor.Boulder;
import actor.Canal;
import actor.Crop;
import actor.Farm;
import actor.GameObject;
import actor.Lake;
import actor.Seed;
import actor.Tree;
import grid.Location;

/**
 *
 * @author 18lambas
 */
public class Challenge31 extends BotBrain {

    @Override
    public int chooseAction() {
        int move = 0;
        Location goal = new Location(0, 0);
        GameObject[][] arena = this.getArena();
     Location seed = findSeed();
    // Location lake = besideLakeN();
     Location farm = findCropLeastDist();
         if(seed.isValidLocation()){
            goal = seed;
                 move = AStar(getLocation(), goal);
         }
       /* else if (lake.isValidLocation()){
             goal = lake;
             System.out.println("LAKE VALUE:" + goal);
             move = AStar(getLocation(), goal);
         }*/
         else if(farm.isValidLocation()){
            goal = farm;
                 move = AStar(getLocation(), goal);
         }
         if(besideLakeN() > 1 && getNumSeeds() > 0){
          
            move = move + 2000;
        } else if(besideLakeN() > 0 )
                {
        
            move = move + 3000;
        }
         
             return move;
    
    }
           
    
        public Location findSeed()
    {
        GameObject[][] arena = this.getArena();
     
        int lowest = 999;
        Location bestLoc = new Location(-999,-999);
        for(int row=0; row < 18; row++){
            for(int col = 0; col < 18; col++){
                
                if(arena[row][col] instanceof Seed ){
                  
                    if((getLocation()).distanceTo(new Location(row, col)) < lowest){
                        bestLoc = new Location(row, col);
                        lowest = (getLocation()).distanceTo(new Location(row, col));
                   
                   
                        
                    }
                }
            }
        }
        return bestLoc;
    }
        
        public Location findCropLeastDist()
    {
        GameObject[][] arena = this.getArena();
        int highestPoint = 0;
        int lowest = 999;
        Location bestLoc = new Location(-999,-999);
        for(int row=0; row < 18; row++){
            for(int col = 0; col < 18; col++){
                
                if(arena[row][col] instanceof Crop )
                {
                    
                    if((getLocation()).distanceTo(new Location(row, col)) < lowest){
                        bestLoc = new Location(row, col);
                        lowest= (getLocation()).distanceTo(new Location(row, col));
                        highestPoint = ((Crop)arena[row][col]).getPointValue();
                       } 
                    else if(((getLocation()).distanceTo(new Location(row, col)) == lowest))  //mess with this slightly
                    {
                        if(((Crop)arena[row][col]).getPointValue() >= highestPoint){
                            bestLoc = new Location(row, col);
                            lowest = (getLocation()).distanceTo(new Location(row, col));
                            highestPoint = ((Crop)arena[row][col]).getPointValue();
                        }
                    }
                }
            }
        }
        return bestLoc;
    }
      public int besideLakeN()
    {
        int count = 0;
        GameObject[][] arena = this.getArena();
        for(int direction=0; direction<360; direction+=90)
        {
            Location myLoc = new Location(getRow(), getCol());
            Location adjacent = myLoc.getAdjacentLocation(direction);
            if(adjacent.isValidLocation())
                if(arena[adjacent.getRow()][adjacent.getCol()] instanceof Lake || arena[adjacent.getRow()][adjacent.getCol()] instanceof Canal)
                    count++;
        }
        return count;     
    }
/* public Location besideLakeN()//work on this
    {
     
            Location bestLoc = new Location(-999,-999);
        GameObject[][] arena = this.getArena();
           int last = 0;
           
         for(int row=0; row < 18; row++)
         {
            for(int col = 0; col < 18; col++)
            {
                int now = 0;
        for(int direction=0; direction<360; direction+=90)
        {
           
            Location myLoc = new Location(getRow(), getCol());
            if (myLoc.isValidLocation())
                    {
            Location adjacent = myLoc.getAdjacentLocation(direction);
            if(arena[adjacent.getRow()][adjacent.getCol()] instanceof Lake || arena[adjacent.getRow()][adjacent.getCol()] instanceof Canal)
                    now = now + 1;
        }
            if (now> last)
            {//add a distance thing here 
            bestLoc = myLoc;
            last = now;
            }
            }
            
        }
         }
         System.out.println (bestLoc);
           return bestLoc;
    }*/
     public boolean besideLake()
    {
        GameObject[][] arena = this.getArena();
        for(int direction=0; direction<360; direction+=90)
        {
            Location myLoc = new Location(getRow(), getCol());
            Location adjacent = myLoc.getAdjacentLocation(direction);
            if(isLocationOnGrid(adjacent))
                if(arena[adjacent.getRow()][adjacent.getCol()] instanceof Lake)
                    return true;
        }
        return false;        
    }
     public boolean isLocationOnGrid(Location loc)
    {
        GameObject[][] arena = getArena();
        return loc.getCol() >= 0 && loc.getCol() < arena.length &&
                loc.getRow() >= 0 && loc.getRow() < arena[0].length;
    }

         public int goToLocation(Location loc)
    {
        int direction = 0;
        Location myLoc = new Location(getRow(), getCol());
if (!besideLake())
{
        if (loc.getRow() < myLoc.getRow()) {
            direction = 0;
        } else if (loc.getRow() > myLoc.getRow()) {
            direction = 180;
        } else if (loc.getCol() < myLoc.getCol()) {
            direction = 270;
        } else {
            direction = 90;
        }
}

if (besideLake())
{
   
     direction= (int)(Math.random()*4)*90;
    if (besideLake())
        {
            
            direction= direction +2000;
        }



    
}

return direction;
    }



//got the code from a mixture of Youtube CodeCompile and The Coding Train, raywenderlich a* blog, Stanford Amit CS course, and wikipedia pseudocode. 

   ///introduces variables. Sets goals and start position. G is zero currently (no moves made). Adds the current position to close as you don't need to study it anymore.
    //
    private int AStar(Location start, Location goal) {
      GameObject[][] arena = this.getArena();
        ArrayList<Node> open = new ArrayList<>();
        ArrayList<Node> closed = new ArrayList<>();
        Node goalNode = new Node(goal.getRow(), goal.getCol());
   
        Node startNode = new Node(start.getRow(), start.getCol());
        
             Node current = new Node(start.getRow(), start.getCol());

        current.setG(0);
        closed.add(current);

        for (int i = 0; i < 360; i = i + 90) {
            Node neighbor = current.getAdjacentNode(i);
            neighbor.setGoal(goal);
            neighbor.setParent(current);
            open.add(neighbor);
        }

      while (!open.isEmpty()) {
            current = open.get(lowestNodeIndex(open));
        
            if (current.equals(goalNode))
            {
                return start.getDirectionToward(pathMaker(current, startNode).get(0));
            }

            if (obstacles(current)) {
                open.remove(current);
                current.setG(999);
                continue;
            }

            open.remove(current);
            closed.add(current);

            for (int i = 0; i < 360; i = i + 90) {
                Node neighbor = current.getAdjacentNode(i);

                if (closed.contains(neighbor) || !current.getAdjacentLocation(i).isValidLocation() || obstacles(neighbor)) {
                    neighbor.setG(99999999);
                    continue;
                }

                int G = current.getG() + 1;

                if (!open.contains(neighbor)) {
                    open.add(neighbor);
                } else if (G >= neighbor.getG()) {
                    continue;
                }
                
                neighbor.setParent(current);
                neighbor.setGoal(goal);
            }
        }
        return 180; 
    }

    private ArrayList<Node> pathMaker(Node current, Node start) {
        ArrayList<Node> path = new ArrayList<>();
        while (!current.equals(start)) {
            path.add(0, current);
            current = current.getParent();
        }
       
        return path;
    }
   private int lowestNodeIndex(ArrayList<Node> open) {
        GameObject[][] arena = this.getArena();
        int lowestIndex = 0;
        for (Node x : open) {
            if (x.getF(arena) < open.get(lowestIndex).getF(arena)) {
                lowestIndex = open.indexOf(x);
            }
        }
        return lowestIndex;
    }
   
    private boolean obstacles(Location x) {
           GameObject[][] arena = this.getArena();
        if (x.isValidLocation()) {
            if (arena[x.getRow()][x.getCol()] instanceof Boulder || arena[x.getRow()][x.getCol()] instanceof Lake || arena[x.getRow()][x.getCol()] instanceof Tree || arena[x.getRow()][x.getCol()] instanceof Canal) {
                return true;
            }
        }
        return false;
    }
    
 



class Node extends Location {

    private int G; //counter for number of moves
    private int H; //estimator
    public Node parent; //last node
    
    public Node(int r, int c) {
        super(r, c);
        parent = null;
        H = 0;
        G = 0;
    }

public int getF(GameObject[][] arena) {
        if((new Location(this.getRow(),this.getCol()).isValidLocation())){
            if(arena[this.getRow()][this.getCol()] instanceof Farm){
                       }
        }
        
        return G + H;
    }  

    public int getG() {
        return G;
    } //cost to start

    public int getH() {
        return H;
    }   //cost to end

   
    
    public Node getParent() {
        return parent;
    }

    public void setG(int g) {
        G = G + g;
    }

    public void setGoal(Location goal) {
        H = H+ this.distanceTo(goal);
    }

    public void setParent(Node x) {
        parent = x;
        G = G + x.getG() + 1;
    }
   

    public Node getAdjacentNode(int dir) {
        Location temp = new Location(this.getRow(), this.getCol());
        temp = temp.getAdjacentLocation(dir);
        Node next = new Node(temp.getRow(), temp.getCol());
        return next;
    }
    }

    @Override
    public boolean equals(Object other) {
        if (!(other instanceof Node)) {
            return false;
        }

        Node otherLoc = (Node) other;
        return getRow() == otherLoc.getRow() && getCol() == otherLoc.getCol();
    }

  
  public Location getLocation() {
        return new Location(getRow(), getCol());
    }

}
