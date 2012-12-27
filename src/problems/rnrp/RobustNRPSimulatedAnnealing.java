package problems.rnrp;

import algorithms.sa.SimulatedAnnealing;

import java.util.Random;

/**
 * Class to implement the SimulatedAnnealing class to the Robust Next Release Problem.
 *
 * @author Matheus Paixao
 */
public class RobustNRPSimulatedAnnealing extends SimulatedAnnealing{
   RobustNextReleaseProblem robustNRP;

   int[] initialSolution;

   Random random;

   public RobustNRPSimulatedAnnealing(RobustNextReleaseProblem robustNRP){
      this.random = new Random();

      this.robustNRP = robustNRP;
      this.initialSolution = generateInitialSolution();
   }

   protected double getInitialTemperature(){
      return 1000;
   }

   protected double getFinalTemperature(){
      return 0.1;
   }

   protected double getAlpha(){
      return 0.995;
   }

   protected int getNumberOfMarkovChains(){
      return robustNRP.getNumberOfRequirements();
   }

   protected int[] getInitialSolution(){
      return this.initialSolution;
   }

   protected int[] getNeighbourSolution(int[] solution){
      return null;
   }

   protected double calculateSolutionValue(int[] solution){
      return 0;
   }

   protected boolean isSolutionBest(double solutionValue1, double solutionValue2){
      return false;
   }

   private int[] generateInitialSolution(){
      int[] initialSolution = getRandomSolution();

      while(robustNRP.isSolutionValid(initialSolution) == false){
         initialSolution = getRandomSolution();
      }

      return initialSolution;
   }

   private int[] getRandomSolution(){
      int[] randomSolution = new int[robustNRP.getNumberOfRequirements()];

      for(int i = 0; i <= randomSolution.length - 1; i++){
         randomSolution[i] = random.nextInt(2);
      }

      return randomSolution;
   }
}
