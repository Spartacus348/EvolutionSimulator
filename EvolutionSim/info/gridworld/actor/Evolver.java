//Gabe's Work
//Evolver.java
//This is the base unit for evolution in the EvolutionWorld.It doesn't do much except adapt its hue depending on it's fitness value.
//It's fitness value is just an int, where the higher the int, the more "fit" it is.
//Fitness is assigned one of two ways, explained below.
package info.gridworld.actor;

import java.util.Random;
import java.awt.Color;

import info.gridworld.grid.Grid;
import info.gridworld.grid.Location;


public class Evolver {
   public int fitness , intelligence = 0; //Intelligence was going to be a second characteristic that could also induce fitness.
                                          //I wanted to see if I could play the two off of each other somehow. This was never implemented.
   int direction = 0;
   Grid grid;
   Location location;
   
   Random gen = new Random();             /**Evolvers inherit their fitness from one of their neighboring Evolvers, 
                                             but with a small amount of randomness included to induce evolution **/
   Color color = new Color( 0 , 0 , 0);
   
   public Evolver(int i){                 //This is only called when the program fills the world with evolvers, allowing a uniform distribution.
      fitness = gen.nextInt(i);
      setColor((int)(255*Math.max((1 - fitness / 500) , 0)) , 0 , (int)(255*Math.min((fitness / 500) , 1)));   //Just sets the color according to the fitness value.
   }
   
   public Evolver(int i , char a){        //This is called while evolution is being simulated.
      double x = Math.pow((gen.nextInt(410)-205)/100 , 1/3);
      fitness = (int) Math.abs(i + x * Math.pow( Math.E , Math.pow( x , 6 ) / 2 ) / 1.6);
      setColor((int)(255*Math.max((1 - fitness / 500), 0)) , 0 , (int)(255*Math.min(((fitness / 500)),1)));
   }
   
   public void setColor(int red, int green, int blue){
      color = new Color(red, green, blue);
   }
   
   public int getFitness(){
      return fitness;
   }
      
   public void select( int bleh){   //This is just a dummy method. Other version of Evolver did things when selected, but this didn't.
   }
   
   /**Everything below here is copied from Actor.java. Most of my classes are extensions of Actor (or an extension of an extension),
            But these weren't similar enough. **/
   
   public Color getColor()
   {
      return color;
   }

   public void setColor(Color newColor)
   {
      color = newColor;
   }

   public int getDirection()
   {
      return direction;
   }

   public void setDirection(int newDirection)
   {
      direction = newDirection % Location.FULL_CIRCLE;
      if (direction < 0)
         direction += Location.FULL_CIRCLE;
   }

   public Grid<Evolver> getGrid()
   {
      return grid;
   }

   public Location getLocation()
   {
      return location;
   }

   public void putSelfInGrid(Grid<Evolver> gr, Location loc)
   {
      //if (gr != null)
        // throw new IllegalStateException(
          //          "This actor is already contained in a grid.");
   
      Evolver evolver = gr.get(loc);
      if (evolver != null)
         evolver.removeSelfFromGrid();
      gr.put(loc, this);
      grid = gr;
      location = loc;
   }

   public void removeSelfFromGrid()
   {
      if (grid == null)
         throw new IllegalStateException(
                    "This actor is not contained in a grid.");
      if (grid.get(location) != this)
         throw new IllegalStateException(
                    "The grid contains a different actor at location "
                            + location + ".");
   
      grid.remove(location);
      grid = null;
      location = null;
   } 
}
