//Stalactite.java: By Gabe.
//                My first step into using Gridworld as a heatmap.
//                Every "step" causes the formation of stalactites via
//                increasing the probability of the zone "dropping" in 
//                proportion to how far it and its surroundings have dropped.
//                While I don't remember exactly how much I started with, I 
//                distinctly remember there being pseudocode for this in my
//                AP CS book, so much of this program is not mine originally.
//***************************************************************************

package info.gridworld.actor;
import info.gridworld.grid.*;

import java.awt.Color;

import java.util.Random;

public class Stalactite extends Flower{
   
   private Color DEFAULT_COLOR = new Color (255 ,255, 255);
   private double DARKENING_FACTOR = 0.01;
    
   private int depth = 0;
    
   private Random gen = new Random();
    
   public void act(){
      Stalactite stal;
      Location loc = getLocation();
      Grid gr = getGrid();
   
      int totalDepth = 0;
      
      for(int i = 0; i < 8; i++){
         try{
            stal = (Stalactite) gr.get(loc.getAdjacentLocation(45*i));
            totalDepth += stal.getDepth();
         } 
         catch (IllegalArgumentException e){
         }
      }
      if(gen.nextInt(200 + (totalDepth + depth)/9) <= (totalDepth + depth)/9){
         depth++;
         Color c = getColor();
         int red = (int) (c.getRed()*(1-DARKENING_FACTOR));
         int green = (int) (c.getGreen()*(1-DARKENING_FACTOR));
         int blue = (int) (c.getBlue()*(1 - DARKENING_FACTOR));
      
         setColor(new Color(red, green, blue));
      }
   }

    
   public int getDepth(){
      return depth;
   }
}