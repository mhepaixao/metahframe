package problems.tsp;

import instancereaders.TSPInstanceReader;

import java.io.File;

/**
 * Class that describes the Travel Salesman Problem features.
 *
 * @author Matheus Paixao
 */
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

   /**
    * Method to comprare if a solution value is better than another one.
    *
    * In TSP as smaller fitness value as better.
    * @author Matheus Paixao
    * @param solution1
    * @param solution2
    * @return true if the first fitness value is best than the other one
    */
   public boolean isSolutionBest(double solution1, double solution2){
      boolean result = false;

      if(solution1 < solution2){
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
