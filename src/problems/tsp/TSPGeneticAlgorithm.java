package problems.tsp;

import algorithms.ga.GeneticAlgorithm;
import instancereaders.TSPInstanceReader;

import java.io.File;

import java.util.Arrays;
import java.util.Collections;
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
      this.populationSize = 100;
   }

   protected double getCrossoverProbability(){
      return 0.8;
   }

   protected double getMutationProbability(){
      return 0.01;
   }

   protected int getNumberOfEliteIndividuals(){
      return (int) (0.2*populationSize);
   }

   protected boolean isMinimizationProblem(){
      return true;
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

   protected int[][] getParents(int[][] population, double[] individualsSolutionValues){
      int[][] parents = new int[2][population[0].length];

      for(int i = 0; i <= parents.length - 1; i++){
         parents[i] = getIndividual(population, individualsSolutionValues);
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
   private int[] getIndividual(int[][] population, double[] individualsSolutionValues){
      int[] individual = null;
      double rouletteValue = 0;
      double[] probabilities = getIndividualsProbabilities(population, individualsSolutionValues);

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
   private double[] getIndividualsProbabilities(int[][] population, double[] individualsSolutionValues){
      double[] probabilities = new double[population.length];
      double probabilityValue = 0;
      double[] individualsSelectionValues = getIndividualsSelectionValues(population, individualsSolutionValues);
      double individualsSelectionValuesSum = getIndividualsSelectionValuesSum(individualsSelectionValues);

      for(int i = 0; i <= probabilities.length - 1; i++){
         probabilityValue = individualsSelectionValues[i] / individualsSelectionValuesSum;
         if(Double.isNaN(probabilityValue)){
            probabilityValue = 0;
         }

         probabilities[i] = probabilityValue;
      }

      return probabilities;
   }

   private double[] getIndividualsSelectionValues(int[][] population, double[] individualsSolutionValues){
      double[] individualsSelectionValues = new double[population.length];
      double maxIndividualSolutionValue = getMaxIndividualSolutionValue(individualsSolutionValues);

      for(int i = 0; i <= individualsSelectionValues.length - 1; i++){
         individualsSelectionValues[i] = maxIndividualSolutionValue - individualsSolutionValues[i];
      }

      return individualsSelectionValues;
   }

   private double getIndividualsSelectionValuesSum(double[] individualsSelectionValues){
      double individualsSelectionValuesSum = 0;

      for(int i = 0; i <= individualsSelectionValues.length - 1; i++){
         individualsSelectionValuesSum += individualsSelectionValues[i];
      }

      return individualsSelectionValuesSum;
   }

   private double getMaxIndividualSolutionValue(double[] individualsSolutionValues){
      double maxIndividualSolutionValue = 0;

      for(int i = 0; i <= individualsSolutionValues.length - 1; i++){
         if(individualsSolutionValues[i] > maxIndividualSolutionValue){
            maxIndividualSolutionValue = individualsSolutionValues[i];
         }
      }

      return maxIndividualSolutionValue;
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

   protected int[][] getChildsByCrossover(int[][] parents){
      return getChildsByCycleCrossover(parents);
   }

   private int[][] getChildsByCycleCrossover(int[][] parents){
      int childs[][] = new int[parents.length][parents[0].length];
      ArrayList<ArrayList<Integer>> cycles = getCycles(parents);
      ArrayList<Integer> cycle = null;
      
      for(int i = 0; i <= cycles.size() - 1; i++){
         cycle = cycles.get(i);
         if(i % 2 == 0){
            for(int j = 0; j <= cycle.size() - 1; j++){
               childs[0][cycle.get(j)] = parents[0][cycle.get(j)];
               childs[1][cycle.get(j)] = parents[1][cycle.get(j)];
            }
         }
         else{
            for(int j = 0; j <= cycle.size() - 1; j++){
               childs[0][cycle.get(j)] = parents[1][cycle.get(j)];
               childs[1][cycle.get(j)] = parents[0][cycle.get(j)];
            }
         }
      }

      return childs;
   }

   private ArrayList<ArrayList<Integer>> getCycles(int[][] parents){
      ArrayList<ArrayList<Integer>> cycles = new ArrayList<ArrayList<Integer>>();

      for(int i = 0; i <= parents[0].length - 1; i++){
         if(isCityInCycles(i, cycles) == false){
            cycles.add(getCycle(i, parents));
         }
      }

      return cycles;
   }

   private boolean isCityInCycles(int cityIndex, ArrayList<ArrayList<Integer>> cycles){
      boolean result = false;

      if(cycles.size() != 0){
         outerloop:
         for(int i = 0; i <= cycles.size() - 1; i++){
            for(int j = 0; j <= cycles.get(i).size() - 1; j++){
               if(cycles.get(i).get(j) == cityIndex){
                  result = true;
                  break outerloop;
               }
            }
         }
      }

      return result;
   }

   private ArrayList<Integer> getCycle(int startIndex, int[][] parents){
      ArrayList<Integer> cycle = new ArrayList<Integer>();
      int nextIndex = parents[0].length;
      cycle.add(startIndex);

      while(nextIndex != startIndex){
         nextIndex = getNextIndex(cycle.get(cycle.size() - 1), parents);
         if(nextIndex != startIndex){
            cycle.add(nextIndex);
         }
      }

      Collections.sort(cycle);
      return cycle;
   }

   private int getNextIndex(int lastIndex, int[][] parents){
      int nextIndex = 0;
      int lastIndexValue = parents[0][lastIndex];

      for(int i = 0; i <= parents[1].length - 1; i++){
         if(parents[1][i] == lastIndexValue){
            nextIndex = i;
            break;
         }
      }

      return nextIndex;
   }

   protected void mutate(int[] individual, int indexToMutate){
      int randomCityToSwap = random.nextInt(individual.length);
      int cityAux = individual[indexToMutate];
      individual[indexToMutate] = individual[randomCityToSwap];
      individual[randomCityToSwap] = cityAux;
   }

   protected double calculateSolutionValue(int[] individual){
      return tspProblem.calculateSolutionValue(individual);
   }

   protected boolean isSolutionBetter(double solutionValue1, double solutionValue2){
      return tspProblem.isSolutionBest(solutionValue1, solutionValue2);
   }
}
