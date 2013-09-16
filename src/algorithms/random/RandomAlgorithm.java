package algorithms.random;

import algorithms.Algorithm;

import java.util.Collections;
import java.util.ArrayList;
import java.util.Random;

/**
 * Class that implements a random search algorithm.
 *
 * dynamicListOfNodes is an ArrayList of integers that represents the nodes.
 * @author Matheus Paixao
 */
public abstract class RandomAlgorithm implements Algorithm{
   private int numberOfIterations;
   private double totalTime;

   public abstract int[] getRandomSolution();
   public abstract void repairSolution(int[] solution);
   public abstract double calculateSolutionValue(int[] solution); //fitness function value
   public abstract boolean isSolutionBest(double iterationSolutionValue, double bestSolutionValue); //depends on a max or min problem
   public abstract boolean isSolutionValid(int[] solution); //if the founded solution broke some restriction
   
   /**
    * Method to create an RandomAlgorithm object passing the number of iterations
    * that it will run.
    *
    * @author Matheus Paixao
    * @see setTotalTime
    */
   public RandomAlgorithm(int numberOfIterations){
      this.numberOfIterations = numberOfIterations;
      setTotalTime(0);
   }

   private int getNumberOfIterations(){
      return this.numberOfIterations;
   }

   public double getTotalTime(){
      return this.totalTime;
   }

   private void setTotalTime(double totalTime){
      this.totalTime = totalTime;
   }

   /**
    * Method to get the solution of the algorithm and to set the total time spended.
    *
    * @author Matheus Paixao
    * @return solution founded by the algorithm
    * @see loadDynamicListOfNodes
    * @see getNumberOfIterations
    * @see getIterationSolution
    * @see calculateSolutionValue
    * @see isSolutionBest
    */
   public double getSolution(){
      double initialTime = 0;
      double finalTime = 0;

      int[] iterationSolution = null;
      double iterationSolutionValue = 0;
      int[] bestSolution = null;
      double bestSolutionValue = 0;

      int iterationsCounter = 0;

      initialTime = System.currentTimeMillis();
      while(iterationsCounter <= getNumberOfIterations() - 1){
         iterationSolution = getRandomSolution();

         if(isSolutionValid(iterationSolution) == false){
            repairSolution(iterationSolution);
         }

         iterationSolutionValue = calculateSolutionValue(iterationSolution);

         if(bestSolution != null){
            if(isSolutionBest(iterationSolutionValue, bestSolutionValue) == true){
               //System.out.println("found best solution");
               bestSolution = iterationSolution;
               bestSolutionValue = iterationSolutionValue;
            }
         }
         else{
            bestSolution = iterationSolution;
            bestSolutionValue = iterationSolutionValue;
         }

         iterationsCounter++;
      }
      finalTime = System.currentTimeMillis();

      setTotalTime(finalTime - initialTime);

      return bestSolutionValue;
   }
}
