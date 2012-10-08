package problems.tsp;

import algorithms.random.RandomAlgorithm;
import instancereaders.TSPInstanceReader;

import java.io.File;

/**
 * Class to implement the RandomAlgorithm class to the Travel Salesman Problem.
 *
 * @author Matheus Paixao
 */
public class TSPRandomAlgorithm extends RandomAlgorithm{
   private TSPProblem tspProblem;

   /**
    * Method to create the TSPRandomAlgorithm object, receive the instance to read and
    * the number of iterations is passed to the RandomAlgorithm constructor.
    *
    * @author Matheus Paixao
    * @param instance the instance to read
    * @param numberOfIterations number of iterations to be runned
    * @see RandomAlgorithm constructor
    * @see getDistancesMatrix in TSPInstanceReader
    */
   public TSPRandomAlgorithm(TSPProblem tspProblem, int numberOfIterations){
      super(numberOfIterations);
      this.tspProblem = tspProblem;
   }

   public int getNumberOfNodes(){
      return tspProblem.getNumberOfCities();
   }

   /**
    * Method that implements the fitness function of TSP problem.
    *
    * @author Matheus Paixao
    * @param solution the array of int that corresponds to the solution founded by the algorithm
    * @return fitness value of the solution
    */
   public double calculateSolutionValue(Integer[] solution){
      return tspProblem.calculateSolutionValue(solution);
   }

   /**
    * Method to comprare if a solution value is better than another one.
    *
    * In TSP as smaller fitness value as better.
    * @author Matheus Paixao
    * @param iterationSolutionValue the fitness value of some solution
    * @param bestSolutionValue the best fitness value of an iteration
    * @return true if the first fitness value is best than the other one
    */
   public boolean isSolutionBest(double iterationSolutionValue, double bestSolutionValue){
      return tspProblem.isSolutionBest(iterationSolutionValue, bestSolutionValue);
   }

   /**
    * Method to test if the founded solution broke some restriction.
    *
    * In TSP there's no city sequence restriction.
    * @author Matheus Paixao
    */
   public boolean satisfyAllRestrictions(Integer[] solution){
      return true;
   }
}
