package problems.tsp;

import algorithms.ga.GeneticAlgorithm;
import instancereaders.TSPInstanceReader;

import java.io.File;

import java.util.Arrays;
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
      this.populationSize = 10;
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

   protected int[][] getParents(int[][] population){
      int[][] parents = new int[2][population[0].length];

      for(int i = 0; i <= parents.length - 1; i++){
         parents[i] = getIndividual(population);
      }

      return parents;
   }

   /**
    * Method to get an individual using the roullete selection method.
    *
    * @author Matheus Paixao
    * @return the individual selected using the roullete selection method
    * @see getProbabilities
    * @see getRouletteValue
    */
   private int[] getIndividual(int[][] population){
      int[] individual = null;
      double rouletteValue = 0;
      double[] probabilities = getIndividualsProbabilities(population);

      rouletteValue = getRouletteValue(probabilities);

      for(int i = 0; i <= probabilities.length - 1; i++){
         if(rouletteValue == probabilities[i]){
            individual = population[i];
            break;
         }
      }

      return individual;
   }

   /**
    * Method to calculate the probability of each individual
    *
    * @author Matheus Paixao
    * @return an array containing the selection's probability of each individual
    */
   private double[] getIndividualsProbabilities(int[][] population){
      double[] probabilities = new double[population.length];
      double[] individualsSelectionValues = getIndividualsSelectionValues(population);
      double individualsSelectionValuesSum = 0;

      for(int i = 0; i <= individualsSelectionValues.length - 1; i++){
         individualsSelectionValuesSum += individualsSelectionValues[i];
      }

      for(int i = 0; i <= probabilities.length - 1; i++){
         probabilities[i] = individualsSelectionValues[i] / individualsSelectionValuesSum;
      }

      return probabilities;
   }

   private double[] getIndividualsSelectionValues(int[][] population){
      double[] individualsSelectionValues = new double[population.length];
      double[] individualsSolutionValues = new double[population.length];
      double maxIndividualSolutionValue = 0;

      for(int i = 0; i <= individualsSolutionValues.length - 1; i++){
         individualsSolutionValues[i] = tspProblem.calculateSolutionValue(population[i]);
         if(individualsSolutionValues[i] > maxIndividualSolutionValue){
            maxIndividualSolutionValue = individualsSolutionValues[i];
         }
      }

      for(int i = 0; i <= individualsSelectionValues.length - 1; i++){
         individualsSelectionValues[i] = maxIndividualSolutionValue - individualsSolutionValues[i];
      }

      return individualsSelectionValues;
   }

   /**
    * Method to get the value of the probability selected by the roulette.
    *
    * Higher the probability of a node, higher the chance to be choosen by the roulette.
    * For more information search for "roulette selection method".
    * @author Matheus Paixao
    * @param probabilities an array containing the probabilities for roulette selection.
    * @return the probability value choosen by the roulette.
    * @see getRandomNumber
    */
   private double getRouletteValue(double[] probabilities){
      double[] rouletteProbabilities = new double[probabilities.length];
      double neddle = 0;
      double neddleChecker = 0;
      double rouletteValue = 0;

      for(int i = 0; i <= rouletteProbabilities.length - 1; i++){
         rouletteProbabilities[i] = probabilities[i];
      }

      Arrays.sort(rouletteProbabilities);

      neddle = random.nextDouble();

      for(int i = 0; i <= rouletteProbabilities.length - 1; i++){
         neddleChecker += rouletteProbabilities[i];
         if(neddleChecker >= neddle){
            rouletteValue = rouletteProbabilities[i];
            break;
         }
      }

      return rouletteValue;
   }
}
