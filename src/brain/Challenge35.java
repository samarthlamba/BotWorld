/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package brain;

import actor.BlockedLocation;
import actor.Bot;
import actor.BotBrain;
import java.util.ArrayList;
import actor.Boulder;
import actor.Canal;
import actor.Crop;
import actor.DryCanal;
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
public class Challenge35 extends BotBrain {

    private int counter = 0;
    private Location cutOffNext = new Location(-999, 999);
    private Location cultivate = new Location(-999, 999);
    private Location firstTree = new Location(-999, 999);
    private Location secondTree = new Location(-999, 999);
    private Location cutOff = new Location(-999, 999);
    public int step = 1;
    private int count = 1;
    private int cutOffStep = 0;
    private int cutOffCounter = 0;
    private int numberOfMoves = 0;
     private int cultivateMoveCounter = 0;

//work on equation to decide to get nearest or get most points farm...
    //if seed is stuck ignore it. 
    @Override
    public int chooseAction() {
        
          int move = 0;
        Location goal = new Location(0, 0);
        
        if (getMoveNumber()==500)
        {
           cutOffNext = new Location(-999, 999);
           cultivateMoveCounter = 0;
                    step = 1;
                    cutOff = new Location(-999, 999);
                    cutOffStep = 0;
                    goal = new Location(0, 0);
                    count = 1;
                    counter = 0;

                    cutOffNext = new Location(-999, 999);
                    cultivate = new Location(-999, 999);
                    firstTree = new Location(-999, 999);
                    secondTree = new Location(-999, 999);
                   
                    
                   
                    
                    cutOffCounter = 0;
                    numberOfMoves = 0;
        }
      

        GameObject[][] arena = this.getArena();
        Location seed = findSeed();
        Location crop = findCropLeastDist();
        Location farm = farmBest();
        
        System.out.println ("Steps = " + step);
        if (getLocation().equals(cultivate)) cultivateMoveCounter++;
        System.out.println("cultivatemove counter " + cultivateMoveCounter);
                

        if (step == 1 && getNumSeeds() > 1) {
            Location clearing = area(7, 5);

            if (clearing.isValidLocation() && counter == 0 && step ==1 &&!getLocation().equals(clearing)){
                goal = clearing;
                System.out.println("clearing   " + goal);
                move = AStar(getLocation(), goal, 1);
               return move;
            }

            if (clearing.isValidLocation() && ((clearing.getRow() == getLocation().getRow()) && (clearing.getCol() == getLocation().getCol()))) {

                step = step + 1;
            }
        }
        if (step != 1 && counter <= 16) {
            counter = counter + 1;
            System.out.println("buildingOrchard");
            move = buildOrchard();
            System.out.println(counter);

            return move;

        }

        //goes to lake and connect the dry canals to a water supply
        //reminder: 10 age per turn, use to find when to cutoff
        if (counter >= 17 && counter < 7000) {

            if (builtOrchard()) {
                System.out.println("forcedLake");
                goal = forcedLake();

                if (besideLakeNoCanal() && !cultivate.equals(getLocation()) && !cutOff.isValidLocation()) {
                    count++;
                    cutOff = getLocation();

                    System.out.println("BesideLake + cultivate... " + cultivate + "cutOff..." + cutOff);

                }
                if (cultivate.isValidLocation() && count > 1) {
                    goal = cultivate;
                }
                if (cultivate.equals(getLocation())) {
                    count++;

                }
                if (cutOff.equals(getLocation()) && cutOffCounter == 0) {
                    goal = cultivate;
                    System.out.println("At CutOFF");

                    cutOffStep = 10;
                    cutOffCounter++;
                    System.out.println("cutOffStep = " + cutOffStep);

                    return (AStar(getLocation(), goal, 1) + 3000);
                }

                if (!cutOff.isValidLocation() && count == 1) {
                    System.out.println(count + "== count" + "goal +" + goal);

                    return (AStar(getLocation(), goal, 1) + 3000);
                }
                if (cultivate.equals(getLocation()) && lastMinute(numberOfMoves)) {
                    System.out.println("5000");
                    return 5000;
                }
                if (!cultivate.equals(getLocation()) && lastMinute(numberOfMoves)) {
                    System.out.println("cutOffStep = " + cutOffStep);
                    if (cutOffStep == 10) {
                        System.out.println("found CutOffnext");
                        cutOffNext = getLocation();
                        cutOffStep = 1000;
                    }
                    System.out.println("dont drop canal_____" + goal);
                    numberOfMoves++;
                    return (AStar(getLocation(), goal, 1));
                }
 if (cutOff.isValidLocation() && !lastLastMinute(numberOfMoves) && !getLocation().equals(cutOffNext) && !getLocation().equals(cutOff)) {
 goal = cutOffNext;
                    System.out.println("cutting off next location. + " + goal);
                    return (AStar(getLocation(), goal,2 ));
 }
                if (cutOff.isValidLocation() && !lastMinute(numberOfMoves) && !getLocation().equals(cutOffNext) && !getLocation().equals(cutOff)) {

                    goal = cutOffNext;
                    System.out.println("cutting off next location. + " + goal);
                    return (AStar(getLocation(), goal, 1));

                }
                if (getLocation().equals(cutOffNext)) {
                    goal = cutOff;
                    count = 1;
                    System.out.println("cutOff location " + goal);
                    return (AStar(getLocation(), goal, 2));

                }
                if (getLocation().equals(cutOff)) {
System.out.println("ReachedEnd");
                    cutOffNext = new Location(-999, 999);
                    step = 1;
                    cutOff = new Location(-999, 999);
                    cutOffStep = 0;
                    goal = new Location(0, 0);
                    count = 1;
                    counter = 0;

                    cutOffNext = new Location(-999, 999);
                    cultivate = new Location(-999, 999);
                    firstTree = new Location(-999, 999);
                    secondTree = new Location(-999, 999);
                   
                    
                   
                    
                    cutOffCounter = 0;
                    numberOfMoves = 0;

                    return (AStar(getLocation(), goal, 1));
                    

                }
            }
        }
         cutOffNext = new Location(-999, 999);
                    step = 1;
                    cutOff = new Location(-999, 999);
                    cutOffStep = 0;
                    goal = new Location(0, 0);
                    count = 1;
                    counter = 0;

                    cutOffNext = new Location(-999, 999);
                    cultivate = new Location(-999, 999);
                    firstTree = new Location(-999, 999);
                    secondTree = new Location(-999, 999);
                   
                    
                   
                    
                    cutOffCounter = 0;
                    numberOfMoves = 0;
        goal = new Location(0, 0);
        return (AStar(getLocation(), goal, 1));
    
    }
    //finds the closest seed to the bot
    public Location findSeed() {
        GameObject[][] arena = this.getArena();

        int lowest = 999;
        Location bestLoc = new Location(-999, -999);
        for (int row = 0; row < 18; row++) {
            for (int col = 0; col < 18; col++) {

                if (arena[row][col] instanceof Seed) {
                    Location checking = new Location(row, col);
                    if ((getLocation()).distanceTo(new Location(row, col)) < lowest && (AStar(getLocation(), checking, 1)) != 987654) {
                        bestLoc = new Location(row, col);
                        lowest = (getLocation()).distanceTo(new Location(row, col));

                    }
                }
            }
        }
        return bestLoc;
    }

    private int buildOrchard() {

        if (counter == 2) {
            return 3090;
        }
        if (counter == 3) {
            return 3090;
        }
        if (counter == 4) {
            return 3090;
        }
        if (counter == 5) {
            return 3090;
        }
        if (counter == 6) {
            return 3180;
        }

        if (counter == 7) {
            return 3270;
        }
        if (counter == 8) {
            firstTree = getLocation();
            return 2270;
        }
        if (counter == 9) {
            return 3270;
        }
        if (counter == 10) {
            secondTree = getLocation();
            return 2270;
        }
        if (counter == 11) {
            counter = counter + 1;
            return 3180;

        }

        if (counter == 12) {
            return 3180;
        }
        if (counter == 13) {
            return 3090;
        }
        if (counter == 14) {
            return 3090;
        }
        if (counter == 15) {
            cultivate = getLocation();
            return 90;
        }
        if (counter == 16) {
            return 3090;
        }
        if (counter == 17) {
            return 3090;
        }

        return 999;
    }
    //returns true when bot HAS to move and go to the cutoff point

    public boolean lastMinute(int getNumOfMoves) {
        GameObject[][] arena = this.getArena();
        Tree tree1 = null;
        Tree tree2 = null;
        int moves = 100;
        GameObject trees1 = arena[firstTree.getRow()][firstTree.getCol()];
        GameObject trees2 = arena[secondTree.getRow()][secondTree.getCol()];

        if (arena[firstTree.getRow()][firstTree.getCol()] instanceof Tree) {
            tree1 = (Tree) trees1;
        }
        if (arena[secondTree.getRow()][secondTree.getCol()] instanceof Tree) {
            tree2 = (Tree) trees2;
        }
if (arena[firstTree.getRow()][firstTree.getCol()] instanceof Tree)
        moves = (int) Math.floor((2000 - tree1.getAge()) / 10);
else 
    moves= 10000;
        System.out.println("moves = " + moves);
        Location myLocation = getLocation();
        int distance = getNumOfMoves;
        System.out.println("distance =   " + distance);
        if (moves > (distance + 8)) {
            return true;
        }
        
            return false;
        
    
    }
    //same as above but smaller leeway
    public boolean lastLastMinute(int getNumOfMoves) {
        GameObject[][] arena = this.getArena();
        Tree tree1 = null;
        Tree tree2 = null;
        int moves = 100;
        GameObject trees1 = arena[firstTree.getRow()][firstTree.getCol()];
        GameObject trees2 = arena[secondTree.getRow()][secondTree.getCol()];

        if (arena[firstTree.getRow()][firstTree.getCol()] instanceof Tree) {
            tree1 = (Tree) trees1;
        }
        if (arena[secondTree.getRow()][secondTree.getCol()] instanceof Tree) {
            tree2 = (Tree) trees2;
        }
if (arena[firstTree.getRow()][firstTree.getCol()] instanceof Tree)
        moves = (int) Math.floor((2000 - tree1.getAge()) / 10);
else 
    moves= 10000;
        System.out.println("moves = " + moves);
        Location myLocation = getLocation();
        int distance = getNumOfMoves;
        System.out.println("distance =   " + distance);
        if (moves > (distance + 4)) {
            return true;
        }
        
            return false;
        
    
    }

    public boolean builtOrchard() {
        if (counter > 16) {
            return true;
        } else {
            return false;
        }
    }
    /* public boolean besideTreeN() {
     int count = 0;
     GameObject[][] arena = this.getArena();
     for (int direction = 0; direction < 360; direction += 90) {
     Location myLoc = new Location(getRow(), getCol());
     Location adjacent = myLoc.getAdjacentLocation(direction);
     if (adjacent.isValidLocation()) {
     if (arena[adjacent.getRow()][adjacent.getCol()] instanceof Tree) {
     count++;
     }
     }
     }
     if (count > 0) {
     return true;
     } else {
     return false;
     }
     }*/

    public boolean reachedForcedLake(Location goal) {
        if (getLocation().equals(goal)) {
            return true;
        } else {
            return false;
        }
    }
    /* public boolean besideTreeWithInput(Location target) {
     int count = 0;
     GameObject[][] arena = this.getArena();
     for (int direction = 0; direction < 360; direction += 90) {

     if (target.isValidLocation()) {
     if (arena[target.getRow()][target.getCol()] instanceof Tree) {
     count++;
     }
     }
     }
     if (count > 0) {
     return true;
     } else {
     return false;
     }
     }*/

    public Location findCropLeastDist() {
        GameObject[][] arena = this.getArena();
        int highestPoint = 0;
        int lowest = 999;
        Location bestLoc = new Location(-999, -999);
        for (int row = 0; row < 18; row++) {
            for (int col = 0; col < 18; col++) {

                if (arena[row][col] instanceof Crop) {
                    Location checking = new Location(row, col);
                    if ((getLocation()).distanceTo(new Location(row, col)) < lowest && AStar(getLocation(), checking, 1) != 987654) {
                        bestLoc = new Location(row, col);
                        lowest = (getLocation()).distanceTo(new Location(row, col));
                        highestPoint = ((Crop) arena[row][col]).getPointValue();
                    } else if (((getLocation()).distanceTo(new Location(row, col)) == lowest && AStar(getLocation(), checking, 1) != 987654)) //mess with this slightly
                    {
                        if (((Crop) arena[row][col]).getPointValue() >= highestPoint) {
                            bestLoc = new Location(row, col);
                            lowest = (getLocation()).distanceTo(new Location(row, col));
                            highestPoint = ((Crop) arena[row][col]).getPointValue();
                        }
                    }
                }
            }
        }
        return bestLoc;
    }

    public Location farmBest() {
        GameObject[][] arena = this.getArena();
        double distance = 999;
        Location bestLoc = new Location(-999, -999);
        for (int row = 0; row < 18; row++) {
            for (int col = 0; col < 18; col++) {
                for (int direction = 0; direction < 360; direction += 90) {
                    Location myLoc = getLocation();
                    Location current = new Location(row, col);
                    Location adjacent = current.getAdjacentLocation(direction);
                    if (adjacent.isValidLocation()) {
                        if ((arena[adjacent.getRow()][adjacent.getCol()] instanceof Lake || arena[adjacent.getRow()][adjacent.getCol()] instanceof Canal) && ((getLocation()).distanceTo(new Location(row, col)) < distance) && !(arena[current.getRow()][current.getCol()] instanceof Farm) && !(arena[current.getRow()][current.getCol()] instanceof Tree) && !(arena[current.getRow()][current.getCol()] instanceof Lake) && !(arena[current.getRow()][current.getCol()] instanceof Canal) && !(arena[current.getRow()][current.getCol()] instanceof Boulder && (AStar(getLocation(), current, 1)) != 987654)) {
                            if (!current.equals(myLoc)) {
                                bestLoc = new Location(current.getRow(), current.getCol());
                                distance = (getLocation()).distanceTo(new Location(row, col));
                                break;
                            }

                        } else if (((getLocation()).distanceTo(new Location(row, col)) < distance) && !(arena[current.getRow()][current.getCol()] instanceof Farm) && !(arena[current.getRow()][current.getCol()] instanceof Tree) && !(arena[current.getRow()][current.getCol()] instanceof Lake) && !(arena[current.getRow()][current.getCol()] instanceof Canal) && !(arena[current.getRow()][current.getCol()] instanceof Boulder && (AStar(getLocation(), current, 1)) != 987654)) {
                            if (!current.equals(myLoc)) {
                                bestLoc = new Location(current.getRow(), current.getCol());
                                distance = (getLocation()).distanceTo(new Location(row, col));
                                break;
                            }

                        }
                    }
                }

            }

        }
        return bestLoc;
    }
    /* public Location area()
     {
     GameObject[][] arena = this.getArena();
     double distance = 999;
     Location bestLoc = new Location(-999,-999);
     Location checkLoc = new Location(-999,-999);
     for(int row=0; row < 18; row++){
     for(int col = 0; col < 13; col++){
     for(int direction=0; direction<360; direction+=90)
                    
     {
     Location myLoc = getLocation();
     Location checking = new Location(row, col);
     Location adjacent = checking.getAdjacentLocation(90);
     if (!(arena[checking.getRow()][checking.getCol()] instanceof Lake || arena[checking.getRow()][checking.getCol()] instanceof Canal) && !(arena[checking.getRow()][checking.getCol()] instanceof Boulder) && checking.isValidLocation())
     {
     System.out.println(1);
                        
     if (!(arena[adjacent.getRow()][adjacent.getCol()] instanceof Lake) && !(arena[adjacent.getRow()][adjacent.getCol()] instanceof Canal) && !(arena[adjacent.getRow()][adjacent.getCol()] instanceof Boulder) && adjacent.isValidLocation())
     {
     System.out.println(2);
     adjacent = adjacent.getAdjacentLocation(90);
     if (!(arena[adjacent.getRow()][adjacent.getCol()] instanceof Lake) && !(arena[adjacent.getRow()][adjacent.getCol()] instanceof Canal) && !(arena[adjacent.getRow()][adjacent.getCol()] instanceof Boulder) && adjacent.isValidLocation())
     {
     System.out.println(3);
     adjacent = adjacent.getAdjacentLocation(90);
                        
     if (!(arena[adjacent.getRow()][adjacent.getCol()] instanceof Lake) && !(arena[adjacent.getRow()][adjacent.getCol()] instanceof Canal) && !(arena[adjacent.getRow()][adjacent.getCol()] instanceof Boulder) && adjacent.isValidLocation())
     {
     System.out.println(4);
     adjacent = adjacent.getAdjacentLocation(90);
                        
                            
     if (!(arena[adjacent.getRow()][adjacent.getCol()] instanceof Lake) && !(arena[adjacent.getRow()][adjacent.getCol()] instanceof Canal) && !(arena[adjacent.getRow()][adjacent.getCol()] instanceof Boulder) && adjacent.isValidLocation())
     { 
     System.out.println(5);
     if (getLocation().distanceTo(checking) < distance)
     {
     bestLoc = checking;
     System.out.println(bestLoc);
     }
     }
     }
     }
     }
     }
     }
     }
     }
     System.out.println(bestLoc);
        
     return bestLoc;
     } */

    //finds a clearing that is empty.
    private Location area(int leftright, int updown) {
        GameObject[][] arena = getArena();
        int distance = 999;
        Location bestLoc = new Location(-999, 999);

        for (int row = 0; row < 19 - updown; row++) {
            for (int col = 0; col < 19 - leftright; col++) {
                boolean empty = true;
                for (int r = row; r < row + updown; r++) {
                    for (int c = col; c < col + leftright; c++) {
                        Location current = new Location(r, c);
                        if ((arena[current.getRow()][current.getCol()] instanceof Boulder || arena[current.getRow()][current.getCol()] instanceof Canal || arena[current.getRow()][current.getCol()] instanceof DryCanal) || arena[current.getRow()][current.getCol()] instanceof Lake) {
                            empty = false;
                        }
                    }
                }
                if (empty == true && ((getLocation()).distanceTo(new Location(row + 1, col + 1)) < distance)) {
                    distance = ((getLocation()).distanceTo(new Location(row + 1, col + 1)));
                    bestLoc = new Location(row + 1, col + 1);

                }
            }
        }
        System.out.println(bestLoc);
        return bestLoc;
    }

    /*  public double getDiagonalDistance(Location start, Location end) {
     double diagonal = ((start.getRow() - end.getRow()) * (start.getRow() - end.getRow())) + ((start.getCol() - end.getCol()) * (start.getCol() - end.getCol()));
     double diagonalSqrt = Math.sqrt(diagonal);
     return diagonalSqrt;
     }*/
    public Location forcedLake() {
        GameObject[][] arena = this.getArena();

        int lowest = 999;
        Location bestLoc = new Location(-999, -999);

        for (int row = 1; row < 17; row++) {
            for (int col = 1; col < 17; col++) {
                Location checkLoc = new Location(row, col);

                for (int direction = 0; direction < 360; direction += 90) {
                    Location adjacent = checkLoc.getAdjacentLocation(direction);
                    if (arena[adjacent.getRow()][adjacent.getCol()] instanceof Lake) {

                        if ((getLocation()).distanceTo(new Location(row, col)) < lowest) {
                            bestLoc = new Location(row, col);
                            lowest = (getLocation()).distanceTo(new Location(row, col));

                        }

                    }
                }
            }
        }
        return bestLoc;
    }

    public int besideLakeCounter() {
        int count = 0;
        GameObject[][] arena = this.getArena();
        for (int direction = 0; direction < 360; direction += 90) {
            Location myLoc = new Location(getRow(), getCol());
            Location adjacent = myLoc.getAdjacentLocation(direction);
            if (adjacent.isValidLocation()) {
                if (arena[adjacent.getRow()][adjacent.getCol()] instanceof Lake || arena[adjacent.getRow()][adjacent.getCol()] instanceof Canal) {
                    count++;
                }
            }
        }
        return count;
    }

    //finds a water body (lake)
    public boolean besideLake() {
        GameObject[][] arena = this.getArena();
        for (int direction = 0; direction < 360; direction += 90) {
            Location myLoc = new Location(getRow(), getCol());
            Location adjacent = myLoc.getAdjacentLocation(direction);
            if (isLocationOnGrid(adjacent)) {
                if (arena[adjacent.getRow()][adjacent.getCol()] instanceof Lake || arena[adjacent.getRow()][adjacent.getCol()] instanceof Canal) {
                    return true;
                }
            }
        }
        return false;
    }

    public boolean besideLakeNoCanal() {
        GameObject[][] arena = this.getArena();
        for (int direction = 0; direction < 360; direction += 90) {
            Location myLoc = new Location(getRow(), getCol());
            Location adjacent = myLoc.getAdjacentLocation(direction);
            if (isLocationOnGrid(adjacent)) {
                if (arena[adjacent.getRow()][adjacent.getCol()] instanceof Lake) {
                    return true;
                }
            }
        }
        return false;
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

    public boolean isLocationOnGrid(Location loc) {
        GameObject[][] arena = getArena();
        return loc.getCol() >= 0 && loc.getCol() < arena.length
                && loc.getRow() >= 0 && loc.getRow() < arena[0].length;
    }

    public int goToLocation(Location loc) {
        int direction = 0;
        Location myLoc = new Location(getRow(), getCol());
        if (!besideLake()) {
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

        if (besideLake()) {

            direction = (int) (Math.random() * 4) * 90;
            if (besideLake()) {

                direction = direction + 2000;
            }

        }

        return direction;
    }

//got the code from a mixture of Youtube CodeCompile and The Coding Train, raywenderlich a* blog, Stanford Amit CS course, and wikipedia pseudocode. 
    ///introduces variables. Sets goals and start position. G is zero currently (no moves made). Adds the current position to close as you don't need to study it anymore.
    //
    private int AStar(Location start, Location goal, int Canal) {
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

            if (current.equals(goalNode)) {
                return start.getDirectionToward(pathMaker(current, startNode).get(0));
            }

            if (obstacles(current, Canal)) {
                open.remove(current);
                current.setG(9999999);
                continue;
            }

            open.remove(current);
            closed.add(current);

            for (int i = 0; i < 360; i = i + 90) {
                Node neighbor = current.getAdjacentNode(i);

                if (closed.contains(neighbor) || !current.getAdjacentLocation(i).isValidLocation() || obstacles(neighbor, Canal)) {
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
        if (open.isEmpty() && !current.equals(goal)) {
            return 987654;
        } else {
            System.out.println(current.getF(arena));
            return 180;
        }
    }

    private ArrayList<Node> pathMaker(Node current, Node start) {
        ArrayList<Node> path = new ArrayList<>();
        while (!current.equals(start)) {
            path.add(0, current);
            current = current.getParent();
        }

        return path;
    }

    public boolean canReach(Node current, ArrayList<Node> open, Location goal) {
        if (open.isEmpty() && !current.equals(goal)) {
            return false;
        } else {
            return true;
        }
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

    private boolean obstacles(Location x, int Canal) {
        GameObject[][] arena = this.getArena();
        if (x.isValidLocation()) {
            if (Canal == 1) {
                if (arena[x.getRow()][x.getCol()] instanceof Boulder || arena[x.getRow()][x.getCol()] instanceof DryCanal || arena[x.getRow()][x.getCol()] instanceof Lake || arena[x.getRow()][x.getCol()] instanceof Tree || arena[x.getRow()][x.getCol()] instanceof Canal) {
                    return true;
                }
            }
            if (Canal == 2) {
                if (arena[x.getRow()][x.getCol()] instanceof Boulder || arena[x.getRow()][x.getCol()] instanceof Lake || arena[x.getRow()][x.getCol()] instanceof Tree || arena[x.getRow()][x.getCol()] instanceof Farm) {
                    return true;
                }
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
            if ((new Location(this.getRow(), this.getCol()).isValidLocation())) {
                if (arena[this.getRow()][this.getCol()] instanceof Farm) {
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
            H = H + this.distanceTo(goal);
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
