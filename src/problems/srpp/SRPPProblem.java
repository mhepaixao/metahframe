package problems.srpp;

import instancereaders.SRPPInstanceReader;

import java.io.File;

public class SRPPProblem{
   SRPPInstanceReader srppInstanceReader;

   double[][] objectivesValues; //value of each objective for each requirement
   int numberOfRequirements;
   int numberOfClients;
   int[][] precedencesMatrix;

   public SRPPProblem(File instance){
      srppInstanceReader = new SRPPInstanceReader(instance);
      objectivesValues = srppInstanceReader.getObjectiveValues();
      this.numberOfRequirements = srppInstanceReader.getNumberOfRequirements();
      this.numberOfClients = srppInstanceReader.getNumberOfClients();
      this.precedencesMatrix = srppInstanceReader.getPrecedencesMatrix();
   }

   public int getNumberOfRequirements(){
      return this.numberOfRequirements;
   }

   public int[][] getPrecedencesMatrix(){
      return this.precedencesMatrix;
   }

   public boolean isSolutionBest(double solution1, double solution2){
      boolean result = false;

      if(solution1 > solution2){
         result = true;
      }

      return result;
   }

   public double calculateSolutionValue(Integer[] solution){
      double solutionValue = 0;

      for(int i = 0; i <= solution.length - 1; i++){
         solutionValue += (solution.length - i) * getObjectivesSum(solution[i]);
      }

      return solutionValue;
   }

   /**
    * Method to get the sum of all objectives values for a requirement.
    *
    * @author Matheus Paixao
    * @param requirement the requirement to know the objectives sum
    * @return the sum of all objectives values for the requirement
    */
   public double getObjectivesSum(Integer requirement){
      double objectivesSum = 0;

      for(int i = 0; i <= objectivesValues.length - 1; i++){
         objectivesSum += objectivesValues[i][requirement];
      }

      return objectivesSum;
   }

   public int getNumberOfClients(){
      return this.numberOfClients;
   }

   public int getNumberOfRequirementsWithNoPrecedence(){
      return srppInstanceReader.getNumberOfRequirementsWithNoPrecedence();
   }
}
