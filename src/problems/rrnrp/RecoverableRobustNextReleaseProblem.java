package problems.rrnrp;

import instancereaders.RobustNRPInstanceReader;

import java.io.File;

import java.util.Arrays;

/**
 * Class that describes the Robust Next Release Problem features.
 *
 * @author Matheus Paixao
 */
public class RecoverableRobustNextReleaseProblem{
   RobustNRPInstanceReader robustNRPInstanceReader;

   int numberOfRequirements;
   double[] requirementsValues;
   double[] requirementsCosts;
   double[] requirementsDeviances;
   double budget;
   int gamma;
   int recoveryParameter;

   /**
    * Method to create the RobustNextReleaseProblem object.
    *
    * @author Matheus Paixao
    * @param instance the problem's instance
    */
   public RecoverableRobustNextReleaseProblem(File instance, int gammaPercentage, int recoveryPercentage){
      robustNRPInstanceReader = new RobustNRPInstanceReader(instance);
      this.numberOfRequirements = robustNRPInstanceReader.getNumberOfRequirements();
      this.requirementsValues = robustNRPInstanceReader.getRequirementsValues();
      this.requirementsCosts = robustNRPInstanceReader.getRequirementsCosts();
      this.requirementsDeviances = robustNRPInstanceReader.getRequirementsDeviances();
      this.budget = getBudget(70);
      this.gamma = getGamma(gammaPercentage);
      this.recoveryParameter = getRecoveryParameter(recoveryPercentage);
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

   private int getGamma(int gammaPercentage){
      return (int) (((double) gammaPercentage / 100) * getNumberOfRequirements());
   }

   private int getRecoveryParameter(int recoveryPercentage){
      return (int) (((double) recoveryPercentage / 100) * gamma);
   }

   public boolean isSolutionValid(int[] solution){
      boolean result = false;

      if(getSolutionCost(solution) <= budget){
         result = true;
      }

      return result;
   }

   private double getSolutionCost(int[] solution){
      return getSolutionEstimatesCosts(solution) + getMaxDeviancesSubsetSum(solution) - getMinEstimatesSubsetSum(solution);
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

   private double getMinEstimatesSubsetSum(int[] solution){
      double minEstimatesSubsetSum = 0;
      double[] sortedSolutionCosts = getSolutionCosts(solution);

      Arrays.sort(sortedSolutionCosts);

      for(int i = 0; i <= sortedSolutionCosts.length - 1; i++){
         if(sortedSolutionCosts[i] != 0){
            for(int j = i; j <= (i + recoveryParameter) - 1; j++){
               minEstimatesSubsetSum += sortedSolutionCosts[j];
               if(j == sortedSolutionCosts.length - 1){
                  break;
               }
            }
            break;
         }
      }

      return minEstimatesSubsetSum;
   }

   private double[] getSolutionCosts(int[] solution){
      double[] solutionCosts = new double[solution.length];

      for(int i = 0; i <= solutionCosts.length - 1; i++){
         solutionCosts[i] = requirementsCosts[i] * solution[i];
      }

      return solutionCosts;
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
