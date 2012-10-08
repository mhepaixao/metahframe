package problems.tsp;

import instancereaders.TSPInstanceReader;

import java.io.File;

public class TSPProblem{
   private TSPInstanceReader tspInstanceReader;

   private int numberOfCities;
   private double[][] distancesMatrix;

   public TSPProblem(File instance){
      this.tspInstanceReader = new TSPInstanceReader(instance);
      this.distancesMatrix = tspInstanceReader.getDistancesMatrix();
      this.numberOfCities = distancesMatrix.length; 
   }

   public double[][] getDistancesMatrix(){
      return this.distancesMatrix;
   }

   public int getNumberOfCities(){
      return this.distancesMatrix.length;
   }

   public boolean isSolutionBest(double iterationSolutionValue, double bestSolutionValue){
      boolean result = false;

      if(iterationSolutionValue < bestSolutionValue){
         result = true;
      }

      return result;
   }

   /**
    * Method that implements the fitness function of TSP problem.
    *
    * @author Matheus Paixao
    * @param solution the array of int that corresponds to the solution to be calculated
    * @return fitness value of the solution
    */
   public double calculateSolutionValue(Integer[] solution){
      double solutionValue = 0;

      for(int i = 0; i <= solution.length - 2; i++){
         solutionValue += distancesMatrix[solution[i]][solution[i + 1]];
      }
      solutionValue += distancesMatrix[solution[solution.length - 1]][solution[0]];

      return solutionValue;
   }
}
