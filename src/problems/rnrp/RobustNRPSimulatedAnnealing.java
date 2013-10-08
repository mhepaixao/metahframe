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

   int numberOfRequirementsToChangeInNeighbourSolution;
   int numberOfFitnessEvaluations;

   Random random;

   public RobustNRPSimulatedAnnealing(RobustNextReleaseProblem robustNRP, int numberOfFitnessEvaluations){
      this.random = new Random();

      this.robustNRP = robustNRP;
      this.numberOfFitnessEvaluations = numberOfFitnessEvaluations;
      this.numberOfRequirementsToChangeInNeighbourSolution = 1;
   }

   protected double getInitialTemperature(){
      return 50;
   }

   protected double getFinalTemperature(){
      return Math.pow(getAlpha(), numberOfFitnessEvaluations) * getInitialTemperature();
   }

   protected double getAlpha(){
      return 1 - ((1.0 / robustNRP.getNumberOfRequirements()) / 100);
   }

   protected int getNumberOfMarkovChains(){
      return robustNRP.getNumberOfRequirements();
   }

   public int[] getInitialSolution(){
      int[] initialSolution = new int[robustNRP.getNumberOfRequirements()];
      int randomRequirement = 0;

      while(robustNRP.isSolutionValid(initialSolution) == true){
         randomRequirement = random.nextInt(initialSolution.length);
         initialSolution[randomRequirement] = 1;
      }
      initialSolution[randomRequirement] = 0;

      return initialSolution;
   }

   protected int[] getNeighbourSolution(int[] solution){
      int[] neighbourSolution = getNeighbourSolution(solution, numberOfRequirementsToChangeInNeighbourSolution);

      if(robustNRP.isSolutionValid(neighbourSolution) == false){
         repairSolution(neighbourSolution);
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

   public void repairSolution(int[] solution){
      removeRandomRequirement(solution);

      if(robustNRP.isSolutionValid(solution) == false){
         repairSolution(solution);
      }
   }

   private void removeRandomRequirement(int[] solution){
      boolean removeFlag = false;
      int randomRequirementToRemove = 0;

      while(removeFlag == false){
         randomRequirementToRemove = random.nextInt(solution.length);
         if(solution[randomRequirementToRemove] == 1){
            solution[randomRequirementToRemove] = 0;
            removeFlag = true;
         }
      }
   }

   protected double calculateSolutionValue(int[] solution){
      return robustNRP.calculateSolutionValue(solution);
   }

   protected boolean isSolutionBest(double solutionValue1, double solutionValue2){
      return robustNRP.isSolutionBest(solutionValue1, solutionValue2);
   }
}
