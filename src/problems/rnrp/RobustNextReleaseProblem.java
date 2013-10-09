package problems.rnrp;

import instancereaders.RobustNRPInstanceReader;

import java.io.File;

import java.util.Arrays;

/**
 * Class that describes the Robust Next Release Problem features.
 *
 * @author Matheus Paixao
 */
public class RobustNextReleaseProblem{
   RobustNRPInstanceReader robustNRPInstanceReader;

   int numberOfRequirements;
   double[] requirementsValues;
   double[] requirementsCosts;
   double[] requirementsDeviances;
   int[][] precedenceMatrix;
   double budget;
   int gamma;

   /**
    * Method to create the RobustNextReleaseProblem object.
    *
    * @author Matheus Paixao
    * @param instance the problem's instance
    */
   //public RobustNextReleaseProblem(File instance){
   public RobustNextReleaseProblem(File instance, int gammaPercentage){
      robustNRPInstanceReader = new RobustNRPInstanceReader(instance);
      this.numberOfRequirements = robustNRPInstanceReader.getNumberOfRequirements();
      this.requirementsValues = robustNRPInstanceReader.getRequirementsValues();
      this.requirementsCosts = robustNRPInstanceReader.getRequirementsCosts();
      this.requirementsDeviances = robustNRPInstanceReader.getRequirementsDeviances();
      this.precedenceMatrix = robustNRPInstanceReader.getPrecedenceMatrix();
      this.budget = getBudget(70);
      //this.gamma = getGamma();
      this.gamma = getGamma(gammaPercentage);
   }

   public int getNumberOfRequirements(){
      return this.numberOfRequirements;
   }

   private double getBudget(double totalCostPercentage){
      double percentage = totalCostPercentage / 100;

      return getTotalCostsSum() * percentage;
   }

   private double getTotalCostsSum(){
      double totalCostsSum = 0;

      for(int i = 0; i <= requirementsCosts.length - 1; i++){
         totalCostsSum += requirementsCosts[i];
      }

      return totalCostsSum;
   }

   //private int getGamma(){
   private int getGamma(int gammaPercentage){
      return (int) (((double) gammaPercentage / 100) * getNumberOfRequirements());
      //return (int) (((double) 0 / 100) * getNumberOfRequirements());
   }

   public boolean isSolutionValid(int[] solution){
      boolean result = false;

      if((getSolutionCost(solution) <= budget) && (arePrecedencesRespected(solution) == true)){
         result = true;
      }

      return result;
   }

   private double getSolutionCost(int[] solution){
      return getSolutionEstimatesCosts(solution) + getMaxDeviancesSubsetSum(solution);
   }

   private double getSolutionEstimatesCosts(int[] solution){
      double solutionEstimatesCosts = 0;

      for(int i = 0; i <= solution.length - 1; i++){
         solutionEstimatesCosts += solution[i] * requirementsCosts[i];
      }

      return solutionEstimatesCosts;
   }

   private double getMaxDeviancesSubsetSum(int[] solution){
      double maxDeviancesSubsetSum = 0;
      double[] solutionDeviances = getSolutionDeviances(solution);
      Arrays.sort(solutionDeviances);

      for(int i = solutionDeviances.length - 1; i >= (solutionDeviances.length - gamma); i--){
         maxDeviancesSubsetSum += solutionDeviances[i];
      }

      return maxDeviancesSubsetSum;
   }

   private double[] getSolutionDeviances(int[] solution) {
      double[] solutionDeviances = new double[solution.length];

      for(int i = 0; i <= solutionDeviances.length - 1; i++){
         solutionDeviances[i] = solution[i] * requirementsDeviances[i];
      }

      return solutionDeviances;
   }

   private boolean arePrecedencesRespected(int[] solution){
      boolean result = true;

      loop:
      for(int i = 0; i <= solution.length - 1; i++){
         if(solution[i] == 1){
            for(int j = 0; j <= solution.length - 1; j++){
               if(precedenceMatrix[i][j] == 1){
                  if(solution[j] == 0){
                     result = false;
                     break loop;
                  }
               }
            }
         }
      }

      return result;
   }

   public double calculateSolutionValue(int[] solution){
      double solutionValue = 0;

      for(int i = 0; i <= solution.length - 1; i++){
         solutionValue += solution[i] * requirementsValues[i];
      }

      return solutionValue;
   }

   public boolean isSolutionBest(double solutionValue1, double solutionValue2){
      boolean result = false;

      if(solutionValue1 > solutionValue2){
         result = true;
      }

      return result;
   }
}
