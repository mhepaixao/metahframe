package problems.tsp;

import algorithms.ga.GeneticAlgorithm;
import instancereaders.TSPInstanceReader;

import java.io.File;

import java.util.ArrayList;
import java.util.Random;

/**
 * Class to implement the GeneticAlgorithm class to the Travel Salesman Problem.
 *
 * @author Matheus Paixao
 */
public class TSPGeneticAlgorithm extends GeneticAlgorithm{
   private TSPProblem tspProblem;

   private Random random;

   private int populationSize;

   public TSPGeneticAlgorithm(TSPProblem tspProblem, int numberOfIterations){
      super(numberOfIterations);
      this.tspProblem = tspProblem;
      this.random = new Random();
      this.populationSize = 20;
   }

   protected double getCrossoverProbability(){
      return 0.8;
   }

   protected double getMutationProbability(){
      return 0.01;
   }

   protected int[][] getInitialPopulation(){
      int[][] initialPopulation = new int[populationSize][];

      for(int i = 0; i <= initialPopulation.length - 1; i++){
         initialPopulation[i] = getRandomSolution();
      }

      return initialPopulation;
   }

   /**
    * Method to generate a random solution.
    *
    * @author Matheus Paixao
    * @return a random solution 
    * @see getNumberOfCities in TSPProblem class
    * @see getRandomCity
    */
   private int[] getRandomSolution(){
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
}
