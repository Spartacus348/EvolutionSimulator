//DarwinWorld.java by Gabe McDonald
//Runs the EvolutionWorld:
//Sets up a 128x128 grid of Evolvers with randomly distributed fitness values between 0 and MAX_FITNESS
//Iterates the world one step, then returns the statistics
//Iterates the world 4001 steps, returning a count of how many times each value appears in the grid every 20 steps
//Puts the counts in a text file
//Returns the final statistics for the EvolutionWorld.
//Note: When compiling, you may get an error saying that this program uses unchecked operations. I believe it is due to the file 
//      reader, though I am not sure. The program has always run fine for me.
//Other Note: If you want to see a visual representation of the data, Go into the folder this file is in and choose the most recent
//            "Stats1.txt" file, or whatever version number you ran, open it in Excel, select the entire sheet, and view it as a contour map.
//            The x-values are the fitness number, the y-values are the generation numbers (Divided by 20), and the color values are their rate of incidence.

import info.gridworld.actor.*;
import info.gridworld.grid.*;
import java.io.IOException;

public class DarwinWorld{
   public static void main (String[] args) throws IOException{
      int j = 0;
      int i = 0;
      EvolutionWorld evw = new EvolutionWorld(new BoundedGrid(128 , 128));
      
      final int MAX_FITNESS = 100;
      
      for(i = 0; i < evw.getGrid().getNumCols(); i++)
         for(j = 0; j < evw.getGrid().getNumRows(); j++)
            evw.add(new Location(i , j) , new Evolver(MAX_FITNESS));
      evw.step();      
      System.out.println("First Maximum: " + evw.maximum + " \tAverage: " + evw.average + "\tMinimum: " + evw.minimum);
      //evw.show(); //Will open a window showing the world. However, due to a coloring error I can't seem to fix, the colors are
                    //stuck in the maximum position. You can watch the stats tick up in real time, though.
      print(evw);
      evw.printLongStats(4001, 20);
      System.out.println("\n\n");
      print(evw);
      System.out.println("End Maximum: " + evw.maximum + " \tAverage: " + evw.average + "\tMinimum: " + evw.minimum);
   }
     
   public static void print(EvolutionWorld evw){
      int i = 0, j = 0;
      for(i = 0; i < evw.getGrid().getNumCols(); i++){
         for(j = 0; j < evw.getGrid().getNumRows(); j++)
            System.out.print(evw.getGrid().get(evw.getGrid().getLoc( i , j)).getFitness() +  "\t");
         System.out.println("");
      }
   }
}
