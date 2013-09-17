package problems.rnrp;

import algorithms.random.RandomAlgorithm;

import java.util.Random;

public class RobustNRPRandomAlgorithm extends RandomAlgorithm{
   public RobustNextReleaseProblem robustNRP;
   private Random random;

   public RobustNRPRandomAlgorithm(RobustNextReleaseProblem robustNRP){
      super(1000 * robustNRP.getNumberOfRequirements());
      this.robustNRP = robustNRP;
      this.random = new Random();
   }

   public double calculateSolutionValue(int[] solution){
      return robustNRP.calculateSolutionValue(solution);
   }

   public boolean isSolutionBest(double solution1, double solution2){
      return robustNRP.isSolutionBest(solution1, solution2);
   }

   public boolean isSolutionValid(int[] solution){
      return robustNRP.isSolutionValid(solution);
   }

   //public int[] getRandomSolution(){
      //int[] randomSolution = new int[robustNRP.getNumberOfRequirements()];

      //for(int i = 0; i <= randomSolution.length - 1; i++){
         //randomSolution[i] = random.nextInt(2);
      //}

      //return randomSolution;
   //}

   public int[] getRandomSolution(){
      int[] randomSolution = new int[robustNRP.getNumberOfRequirements()];
      int amountOfNumbersToInsert = random.nextInt(randomSolution.length);

      if(amountOfNumbersToInsert <= randomSolution.length / 2){
         insertNumbers(amountOfNumbersToInsert, randomSolution, 1);
      }
      else{
         for(int i = 0; i <= randomSolution.length - 1; i++){
            randomSolution[i] = 1;
         }
         insertNumbers(randomSolution.length - amountOfNumbersToInsert, randomSolution, 0);
      }

      return randomSolution;
   }

   public void insertNumbers(int amountOfNumbersToInsert, int[] randomSolution, int numberToInsert){
      int positionToInsert = random.nextInt(randomSolution.length);

      for(int i = 0; i <= amountOfNumbersToInsert - 1; i++){
         while(randomSolution[positionToInsert] == numberToInsert){
            positionToInsert = random.nextInt(randomSolution.length);
         }

         randomSolution[positionToInsert] = numberToInsert;
      }
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
}
