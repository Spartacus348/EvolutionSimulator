//Based off of ActorWorld
//Has multiple examples of natural selection
//Setting 1: Each Evolver has a random chance of being removed each step;
//           the random chance is weighted based off of an internal "fitness"
//           value. Then each square is filled with a copy of a random neighbor
//           square, with a small amount of "fitness" variation.
//Setting 2: Each step removes the least fit 50% of the world. The world is then
//           filled back up the same way as Setting 1.
//Setting 3: Each step removes a random 50% of the population, weighted by the
//           "fitness" factor. Then the world is filled like Setting 1.
//Setting 4: Greater fitness isn't necessarily better. There are two troughs of
//           increased desireabiliy to select for. [Still doesn't behave as expected]
//"Fitness" is visibly visible on a red-blue scale.
//
//By Gabe McDonald
package info.gridworld.actor;

import info.gridworld.grid.Grid;
import info.gridworld.grid.Location;
import info.gridworld.world.World;

import java.util.ArrayList;
import java.util.Random;
import java.util.Scanner;
import java.io.PrintWriter;
import java.io.IOException;

/**
 * An <code>EvolutionWorld</code> is occupied by evolvers. <br />
 */

public class EvolutionWorld extends World<Evolver>
{
   int setting = 4; //setting number; 1, 2, 3, or 4(experimental)
   public int average = 0, minimum = 200000, maximum = 0; //for statistics
   Random gen = new Random();
   
   double a = 0, b = 0; //for setting 4 and making the math cleaner

   /**
    * Constructs an evolution world with a default grid.
    */
   public EvolutionWorld()
   {
      Scanner in = new Scanner(System.in);
      System.out.println("What Setting? (1,2,3)");
      setting = in.nextInt();
   }

   /**
    * Constructs an evolution world with a given grid.
    * @param grid the grid for this world.
    */
   public EvolutionWorld(Grid<Evolver> grid)
   {
      super(grid);
      Scanner in = new Scanner(System.in);
      System.out.println("What Setting? (1,2,3)");
      setting = in.nextInt();
   }

   public void show()
   {
      if (getMessage() == null)
      {
         String defaultMessage = "Click Step to instanciate a singe step of selection. Selection Run is recommended."; 
         setMessage(defaultMessage);
      }
      super.show();
   }

   public void step()
   {
      Grid<Evolver> gr = getGrid();
      ArrayList<Evolver> evolvers = new ArrayList<Evolver>();
      for (Location loc : gr.getOccupiedLocations())
         evolvers.add(gr.get(loc));
   
      for (Evolver e : evolvers)
      {
         // randomly select an evolver to remove; weight selection based on "fitness"
         if (e.getGrid() == gr)
            if(setting == 1){
               if(gen.nextInt(100 + e.getFitness()) > e.getFitness())
                  e.removeSelfFromGrid();
            } 
            //gets rid of all evolvers less fit than the average
            else if (setting == 2){
               if(e.getFitness() < average)
                  e.removeSelfFromGrid();
            }
            //on average each evolver has a 50% chance of dying; however, the individual number is dependent on fitness
            else if (setting ==3){
               if(gen.nextInt(Math.abs(maximum-minimum)) + minimum > e.getFitness())
                  e.removeSelfFromGrid();
            }
            /**There are two peaks for fitness, one of which is hard to reach but more effective
                  For this version, larger fitness wasn't necessarily better; there were two 
                  troughs of desireability centered around 91 and 321. 91 was less desireable
                  than 321, but no Evolvers would start near there. I wanted to see if some would make it.
                  However, its results don't quite make sense yet.
           */
            else if (setting == 4){
               a = e.getFitness()/100;
               b = 100 - 30 * Math.pow(Math.E , -1*(a-1.35)*(a-3.6)*(a-0.6)*(a-2.6));
               if(gen.nextInt(300) < b)
                  e.removeSelfFromGrid();
            }
      }
      //Sets up the statistics for each step
      average = 0;
      minimum = 20000;
      maximum = 0;
      for(Evolver e : evolvers){
         average += e.getFitness();
         if(minimum > e.getFitness()) minimum = e.getFitness();
         if(maximum < e.getFitness()) maximum = e.getFitness();
      }
      
      average = average / evolvers.size();
      if(setting == 0){
         for(Evolver e : evolvers)
            e.select(average);
      }
      
      int j = 0;
      for (int i = 0; i < getGrid().getNumCols(); i++){ //fills each empty spot with a new evolver based off of the surrounding squares
         for(  j = 0; j < getGrid().getNumRows(); j++){
            if(gr.get(gr.getLoc( i , j)) == null)
               try{ 
                  new Evolver(gr.get(gr.getOccupiedAdjacentLocations(gr.getLoc(i , j)).get(gen.nextInt(gr.getOccupiedAdjacentLocations(gr.getLoc(i , j)).size()))).getFitness() , 'a').putSelfInGrid(gr , gr.getLoc(i , j));
               } 
               catch (IllegalArgumentException e){
                  new Evolver(average ,'a').putSelfInGrid(gr , gr.getLoc(i,j));
               }   //Puts in a new Evolver based on a randomly selected neighboring Evolver.
         }
      }
      
      average = 0;
      minimum = 20000;
      maximum = 0;
      for(Evolver e : evolvers){
         average += e.getFitness();
         if(minimum > e.getFitness()) minimum = e.getFitness();
         if(maximum < e.getFitness()) maximum = e.getFitness();
      }
      average /= evolvers.size();
      setMessage("Average: " + average + "\t Minimum: " + minimum + "\t Maximum: " + maximum);
     //System.out.println(minimum + "\t" + average + "\t" + maximum);
   }

   /**
    * Adds an evolver to this world at a given location.
    * @param loc the location at which to add the actor
    * @param occupant the actor to add
    */
   public void add(Location loc, Evolver occupant)
   {
      occupant.putSelfInGrid(getGrid(), loc);
   }

   /**
    * Adds an occupant at a random location.
    * @param occupant the occupant to add
    */
   public void add(Evolver occupant)
   {
      Location loc = getRandomEmptyLocation();
      if (loc != null)
         add(loc, occupant);
   }

   /**
    * Removes an actor from this world.
    * @param loc the location from which to remove an actor
    * @return the removed actor, or null if there was no actor at the given
    * location.
    */
   public Evolver remove(Location loc)
   {
      Evolver occupant = getGrid().get(loc);
      if (occupant == null)
         return null;
      occupant.removeSelfFromGrid();
      return occupant;
   }
   
   //Outputs a text file representation of the world every "step" steps for "length" steps
   
   public void printLong(int length, int step) throws IOException{ 
      int c , r, a; //cols, rows, and then another iterator
      PrintWriter x;
      String out = "";
      for(int i = 1; i < length/step; i++){
         x = new PrintWriter(i + ".txt");
         out = "";
         for(c = 0; c < getGrid().getNumCols(); c++){
            for(r = 0; r < getGrid().getNumRows(); r++)
               out += getGrid().get(new Location(c , r)).getFitness() + "\t";
            out += "\n";
         }
         x.print(out);
         x.close();
         for(a = 0; a < step; a++)step();
      }
   }
   
   //Outputs a single text file of the frequency of each value as a snapshot every "steps" steps for "length" steps
   
   public void printLongStats(int length, int step) throws IOException{
      int c , r, //cols, rows
          a , val, maxVal = 0; //an iterator, a counter, and a cap for the counter
      PrintWriter x = new PrintWriter("Stats" + setting + ".txt");
      String out = "";
      for(int i = 1; i < length/step; i++){
         out = "";
         for(c = 0; c < getGrid().getNumCols(); c++)
            for(r = 0; r < getGrid().getNumRows(); r++)
               if(maxVal < getGrid().get(new Location(c , r)).getFitness())
                  maxVal = getGrid().get(new Location(c , r)).getFitness();
         for(a = 0; a <= maxVal; a++){
            val = 0;
            for(c = 0; c < getGrid().getNumCols(); c++)
               for(r = 0; r < getGrid().getNumCols(); r++)
                  if(getGrid().get(new Location(c , r)).getFitness() == a)
                     val++;
            x.print(val + "\t");
         }
         x.print("\n");
         for(a = 0; a < step; a++) step();
      }
      x.close();
   }
}