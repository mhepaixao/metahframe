package problems.rnrp;

import algorithms.ga.GeneticAlgorithm;

import java.util.Random;

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
      return null;
   }

   protected int[][] getChildsByCrossover(int[][] parents){
      return null;
   }

   protected void mutate(int[] individual, int indexToMutate){
   }

   protected double calculateSolutionValue(int[] individual){
      return 0;
   }

   protected boolean isSolutionBetter(double solutionValue1, double solutionValue2){
      return false;
   }

   protected boolean isMinimizationProblem(){
      return false;
   }
}
