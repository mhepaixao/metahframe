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
   int[] requirementsCosts;
   int[] requirementsDeviances;
   double budget;
   int gamma;

   /**
    * Method to create the RobustNextReleaseProblem object.
    *
    * @author Matheus Paixao
    * @param instance the problem's instance
    */
   public RobustNextReleaseProblem(File instance){
      robustNRPInstanceReader = new RobustNRPInstanceReader(instance);
      this.numberOfRequirements = robustNRPInstanceReader.getNumberOfRequirements();
      this.requirementsValues = robustNRPInstanceReader.getRequirementsValues();
      this.requirementsCosts = robustNRPInstanceReader.getRequirementsCosts();
      this.requirementsDeviances = robustNRPInstanceReader.getRequirementsDeviances();
      this.budget = getBudget(50);
      this.gamma = getGamma(10);
   }

   public int getNumberOfRequirements(){
      return this.numberOfRequirements;
   }

   private double getBudget(double totalCostPercentage){
      double percentage = totalCostPercentage / 100;

      return getTotalCostsSum() * percentage;
   }

   private int getGamma(double estimatesMistakesPercentage){
      double percentage = estimatesMistakesPercentage / 100;
      double gamma = percentage * getNumberOfRequirements();

      if(gamma - ((int) gamma) != 0){
         gamma = ((int) gamma) + 1;
      }

      return (int) gamma;
   }

   private double getTotalCostsSum(){
      double totalCostsSum = 0;

      for(int i = 0; i <= requirementsCosts.length - 1; i++){
         totalCostsSum += requirementsCosts[i];
      }

      return totalCostsSum;
   }

   public boolean isSolutionValid(int[] solution){
      boolean result = false;

      if(getSolutionCost(solution) <= budget){
         result = true;
      }

      return result;
   }

   private double getSolutionCost(int[] solution){
      double solutionCost = 0;

      solutionCost = getSolutionEstimatesCosts(solution) + getMaxCostsDeviancesSubsetSum(solution);

      return solutionCost;
   }

   private double getSolutionEstimatesCosts(int[] solution){
      double solutionEstimatesCosts = 0;

      for(int i = 0; i <= solution.length - 1; i++){
         solutionEstimatesCosts += solution[i] * requirementsCosts[i];
      }

      return solutionEstimatesCosts;
   }

   private double getMaxCostsDeviancesSubsetSum(int[] solution){
      double maxCostsDeviancesSubsetSum = 0;
      double[] costsDeviances = getCostsDeviances(solution);

      Arrays.sort(costsDeviances);

      for(int i = costsDeviances.length - 1; i >= (costsDeviances.length - gamma); i--){
         maxCostsDeviancesSubsetSum += costsDeviances[i];
      }

      return maxCostsDeviancesSubsetSum;
   }

   private double[] getCostsDeviances(int[] solution){
      double[] costsDeviances = new double[getNumberOfRequirements()];

      for(int i = 0; i <= costsDeviances.length - 1; i++){
         costsDeviances[i] = solution[i] * (requirementsCosts[i] + requirementsDeviances[i]);
      }

      return costsDeviances;
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
