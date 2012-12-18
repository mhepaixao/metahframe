package problems.tsp;

import algorithms.sa.SimulatedAnnealing;
import instancereaders.TSPInstanceReader;

import java.io.File;

import java.util.Random;
import java.util.ArrayList;

/**
 * Class to implement the SimulatedAnnealing class to the Travel Salesman Problem.
 *
 * @author Matheus Paixao
 */
public class TSPSimulatedAnnealing extends SimulatedAnnealing{
   private TSPProblem tspProblem;

   private int[] initialSolution;
   private double initialTemperature;
   private double finalTemperature;
   private int numberOfNeighbours;

   private Random random;

   /**
    * Method to create the TSPSimulatedAnnealing object, receive TSPProblem object and
    *
    * @author Matheus Paixao
    * @param tspProblem the TSPProblem object
    * @see SimulatedAnnealing constructor
    * @see generateInitialSolution
    * @see calculateInitialAndFinalTemperature
    */
   public TSPSimulatedAnnealing(TSPProblem tspProblem){
      super();
      this.tspProblem = tspProblem;
      this.random = new Random();

      this.initialSolution = generateInitialSolution();
      calculateInitialAndFinalTemperature();
   }

   protected double getInitialTemperature(){
      return this.initialTemperature;
   }

   protected double getFinalTemperature(){
      return this.finalTemperature;
   }

   protected double getAlpha(){
      return 0.995;
   }

   protected int getNumberOfMarkovChains(){
      return this.numberOfNeighbours;
   }

   protected int[] getInitialSolution(){
      return this.initialSolution;
   }

   /**
    * Method to generate a random initial solution.
    *
    * @author Matheus Paixao
    * @return the random initial solution 
    * @see getNumberOfCities in TSPProblem class
    * @see getRandomCity
    */
   private int[] generateInitialSolution(){
      int[] initialSolution = new int[tspProblem.getNumberOfCities()];
      ArrayList<Integer> listToGetRandomCities = new ArrayList<Integer>();

      for(int i = 0; i <= initialSolution.length - 1; i++){
         listToGetRandomCities.add(i);
      }

      for(int i = 0; i <= initialSolution.length - 1; i++){
         initialSolution[i] = getRandomCity(listToGetRandomCities);
      }

      return initialSolution;
   }

   /**
    * Method to get a random city from a list of possible next cities.
    *
    * @author Matheus Paixao
    * @param listToGetRandomCities the list to randomly choose the next city
    * @return a random city from the list
    */
   private int getRandomCity(ArrayList<Integer> listToGetRandomCities){
      int randomCity;
      int randomIndex = random.nextInt(listToGetRandomCities.size());

      randomCity = listToGetRandomCities.get(randomIndex);
      listToGetRandomCities.remove(randomIndex);
      return randomCity;
   }

   /**
    * Method to get a random neighbour solution.
    *
    * @author Matheus Paixao
    * @param solution the solution to get a neighbour
    * @see super charged getNeighbourSolution
    * @see a random neighbour solution
    */
   protected int[] getNeighbourSolution(int[] solution){
      int randomIndex1 = random.nextInt(solution.length - 1);
      int randomIndex2 = random.nextInt(solution.length - 1);

      return getNeighbourSolution(solution, randomIndex1, randomIndex2);
   }

   /**
    * Method to get a neighbour solution.
    *
    * A neighbour solution in TSP is made by swapping to cities.
    * @author Matheus Paixao
    * @param solution the solution to get the neighbours
    * @param index1 the index of the first city to swap
    * @param index2 the index of the second city to swap
    * @return the neighbour solution swapping the passed cities
    */
   private int[] getNeighbourSolution(int[] solution, int index1, int index2){
      int[] neighbourSolution = new int[solution.length];
      int swapCityAux = 0;

      for(int i = 0; i <= neighbourSolution.length - 1; i++){
         neighbourSolution[i] = solution[i];
      }

      swapCityAux = neighbourSolution[index1];
      neighbourSolution[index1] = neighbourSolution[index2];
      neighbourSolution[index2] = swapCityAux;

      return neighbourSolution;
   }

   /**
    * Method to calculate the initial and final temperature of the TSP.
    *
    * The initial temperature is the maximum solution value difference between two neighbours of the initial solution
    * The final temperature is the minimum solution value difference between two neighbours of the initial solution
    * @author Matheus Paixao
    * @see getAllNeighbours
    * @see getSolutionDifference
    */
   private void calculateInitialAndFinalTemperature(){
      double maxSolutionValueDifference = 0;
      double minSolutionValueDifference = 0;
      double solutionValueDifference = 0;
      ArrayList<int[]> neighbours = getAllNeighbours(getInitialSolution());

      for(int i = 0; i <= neighbours.size() - 1; i++){
         for(int j = 0; j <= neighbours.size() - 1; j++){
            if(i != j){
               solutionValueDifference = getSolutionDifference(neighbours.get(i), neighbours.get(j));

               if((maxSolutionValueDifference == 0) && (minSolutionValueDifference == 0)){
                  maxSolutionValueDifference = solutionValueDifference;
                  minSolutionValueDifference = solutionValueDifference;
               }

               if(solutionValueDifference > maxSolutionValueDifference){
                  maxSolutionValueDifference = solutionValueDifference;
               }
               else{
                  if((solutionValueDifference > 0) && (solutionValueDifference < minSolutionValueDifference)){
                     minSolutionValueDifference = solutionValueDifference;
                  }
               }
            }
         }
      }

      this.initialTemperature = maxSolutionValueDifference;
      this.finalTemperature = minSolutionValueDifference;
   }

   /**
    * Method to get the set of all neighbours of the passed solution.
    *
    * @author Matheus Paixao
    * @param solution the solution to get all neighbours
    * @return the set of all neighbours of the solution passed
    * @see getNeighbourSolution
    */
   private ArrayList<int[]> getAllNeighbours(int[] solution){
      ArrayList<int[]> neighbours = new ArrayList<int[]>();

      for(int i = 0; i <= solution.length - 1; i++){
         for(int j = solution.length - 1; j > i; j--){
            neighbours.add(getNeighbourSolution(solution, i, j));
         }
      }

      this.numberOfNeighbours = neighbours.size();
      return neighbours;
   }

   /**
    * Method to get the solution value difference between two solutions.
    *
    * @author Matheus Paixao
    * @param solution1
    * @param solution2
    * @return the solution value difference between the solutions passed
    * @see calculateSolutionValue
    */
   private double getSolutionDifference(int[] solution1, int[] solution2){
      double solutionDifference = 0;
      double solutionValue1 = calculateSolutionValue(solution1);
      double solutionValue2 = calculateSolutionValue(solution2);

      if(solutionValue1 >= solutionValue2){
         solutionDifference = solutionValue1 - solutionValue2;
      }
      else{
         solutionDifference = solutionValue2 - solutionValue1;
      }

      return solutionDifference;
   }

   /**
    * Method that implements the fitness function of TSP problem.
    *
    * @author Matheus Paixao
    * @param solution the array of int that corresponds to the solution to be calculated
    * @return fitness value of the solution
    */
   protected double calculateSolutionValue(int[] solution){
      return tspProblem.calculateSolutionValue(solution);
   }

   /**
    * Method to compare if a solution value is better than another one.
    *
    * @author Matheus Paixao
    * @param solutionValue1
    * @param solutionValue2
    * @return true if the first fitness value is best than the other one
    * @see isSolutionBest in TSPProblem class
    */
   protected boolean isSolutionBest(double solutionValue1, double solutionValue2){
      return tspProblem.isSolutionBest(solutionValue1, solutionValue2);
   }
}
