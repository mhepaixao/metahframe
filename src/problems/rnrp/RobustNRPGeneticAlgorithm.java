package problems.rnrp;

import algorithms.ga.GeneticAlgorithm;

import java.util.Random;
import java.util.Arrays;

/**
 * Class to implement the SimulatedAnnealing class to the Robust Next Release Problem.
 *
 * @author Matheus Paixao
 */
public class RobustNRPGeneticAlgorithm extends GeneticAlgorithm{

   RobustNextReleaseProblem robustNRP;

   Random random;

   public RobustNRPGeneticAlgorithm(RobustNextReleaseProblem robustNRP, int numberOfIterations){
      super(numberOfIterations);
      this.robustNRP = robustNRP;
      this.random = new Random();
   }

   protected double getCrossoverProbability(){
      return 0.9;
   }

   protected double getMutationProbability(){
      double numberOfRequirements = (double) robustNRP.getNumberOfRequirements();
      return 1 / (10 * numberOfRequirements);
   }

   protected int getNumberOfEliteIndividuals(){
      return 0;
   }

   protected int[][] getInitialPopulation(){
      int[][] initialPopulation = new int[robustNRP.getNumberOfRequirements()][robustNRP.getNumberOfRequirements()];

      for(int i = 0; i <= initialPopulation.length - 1; i++){
         initialPopulation[i] = getRandomIndividual(initialPopulation[0].length, i);
      }

      return initialPopulation;
   }

   private int[] getRandomIndividual(int numberOfRequirements, int requirementToBeIncluded){
      int[] randomIndividual = new int[numberOfRequirements];

      for(int i = 0; i <= randomIndividual.length - 1; i++){
         randomIndividual[i] = random.nextInt(2);
      }
      randomIndividual[requirementToBeIncluded] = 1;

      return randomIndividual;
   }

   protected int[][] getParents(int[][] population, double[] individualsSolutionValues){
      return getParentsByRoulleteSelection(population, individualsSolutionValues);
   }

   private int[][] getParentsByRoulleteSelection(int[][] population, double[] individualsSolutionValues){
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
      double individualsSolutionValuesSum = getIndividualsSolutionValuesSum(individualsSolutionValues);

      for(int i = 0; i <= probabilities.length - 1; i++){
         probabilityValue = individualsSolutionValues[i] / individualsSolutionValuesSum;
         if(Double.isNaN(probabilityValue)){
            probabilityValue = 0;
         }

         probabilities[i] = probabilityValue;
      }

      return probabilities;
   }

   private double getIndividualsSolutionValuesSum(double[] individualsSolutionValues){
      double individualsSolutionValuesSum = 0;

      for(int i = 0; i <= individualsSolutionValues.length - 1; i++){
         individualsSolutionValuesSum += individualsSolutionValues[i];
      }

      return individualsSolutionValuesSum;
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
      return null;
   }

   protected void mutate(int[] individual, int indexToMutate){
   }

   protected double calculateSolutionValue(int[] individual){
      return robustNRP.calculateSolutionValue(individual);
   }

   protected boolean isSolutionBetter(double solutionValue1, double solutionValue2){
      return false;
   }

   protected boolean isMinimizationProblem(){
      return false;
   }
}
