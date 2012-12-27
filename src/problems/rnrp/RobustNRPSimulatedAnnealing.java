package problems.rnrp;

import algorithms.sa.SimulatedAnnealing;

/**
 * Class to implement the SimulatedAnnealing class to the Robust Next Release Problem.
 *
 * @author Matheus Paixao
 */
public class RobustNRPSimulatedAnnealing extends SimulatedAnnealing{
   RobustNextReleaseProblem robustNRP;

   public RobustNRPSimulatedAnnealing(RobustNextReleaseProblem robustNRP){
      this.robustNRP = robustNRP;
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
      return null;
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
}
