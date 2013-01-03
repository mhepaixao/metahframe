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
   int numberOfRequirementsToChangeInNeighbourSolution;

   Random random;

   public RobustNRPSimulatedAnnealing(RobustNextReleaseProblem robustNRP){
      this.random = new Random();

      this.robustNRP = robustNRP;
      this.initialSolution = generateInitialSolution();
      this.numberOfRequirementsToChangeInNeighbourSolution = 1;
   }

   protected double getInitialTemperature(){
      return 100;
   }

   protected double getFinalTemperature(){
      return 0.001;
   }

   protected double getAlpha(){
      return 0.9995;
   }

   protected int getNumberOfMarkovChains(){
      return robustNRP.getNumberOfRequirements();
   }

   protected int[] getInitialSolution(){
      return this.initialSolution;
   }

   protected int[] getNeighbourSolution(int[] solution){
      int[] neighbourSolution = getNeighbourSolution(solution, numberOfRequirementsToChangeInNeighbourSolution);

      while(robustNRP.isSolutionValid(neighbourSolution) == false){
         neighbourSolution = getNeighbourSolution(solution, numberOfRequirementsToChangeInNeighbourSolution);
      }

      return neighbourSolution;
   }

   private int[] getNeighbourSolution(int[] solution, int numberOfRequirementsToChangeInNeighbourSolution){
      int[] neighbourSolution = new int[solution.length];
      int indexToChange = 0;

      for(int i = 0; i <= solution.length - 1; i++){
         neighbourSolution[i] = solution[i];
      }

      for(int i = 0; i <= numberOfRequirementsToChangeInNeighbourSolution - 1; i++){
         indexToChange = random.nextInt(neighbourSolution.length);
         if(neighbourSolution[indexToChange] == 0){
            neighbourSolution[indexToChange] = 1;
         }
         else if(neighbourSolution[indexToChange] == 1){
            neighbourSolution[indexToChange] = 0;
         }
      }

      return neighbourSolution;
   }

   protected double calculateSolutionValue(int[] solution){
      return robustNRP.calculateSolutionValue(solution);
   }

   protected boolean isSolutionBest(double solutionValue1, double solutionValue2){
      return robustNRP.isSolutionBest(solutionValue1, solutionValue2);
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
