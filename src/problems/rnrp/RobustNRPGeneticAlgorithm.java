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

   protected int[][] getInitialPopulation(){
      return null;
   }

   protected double getCrossoverProbability(){
      return 0.9;
   }

   protected double getMutationProbability(){
      return 0;
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

   protected int getNumberOfEliteIndividuals(){
      return 0;
   }

   protected boolean isMinimizationProblem(){
      return false;
   }
}
