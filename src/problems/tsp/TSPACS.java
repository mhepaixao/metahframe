package problems.tsp;

import algorithms.acs.ACS;
import instancereaders.TSPInstanceReader;

import java.io.File;

public class TSPACS extends ACS{
   private TSPInstanceReader tspInstanceReader;

   private int numberOfCities;
   private double[][] distancesMatrix;
   private double initialPheromone;

   public TSPACS(File instance, int numberOfIterations){
      super(numberOfIterations);
      this.tspInstanceReader = new TSPInstanceReader(instance);
      this.distancesMatrix = tspInstanceReader.getDistancesMatrix();
      this.numberOfCities = distancesMatrix.length; 
      //this.initialPheromone = calculateInitialPheromone();
   }

   protected int getNumberOfNodes(){
      return this.numberOfCities;
   }
}
