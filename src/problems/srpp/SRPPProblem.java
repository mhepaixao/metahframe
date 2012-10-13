package problems.srpp;

import instancereaders.SRPPInstanceReader;

import java.io.File;

/**
 * Class that describes the Software Requirements Priorization Problem features.
 *
 * This problem is modeled in the paper:
 * "Aplicando o Algoritmo Ant-Q na Priorização de Requisitos de Software com Precedência",
 * published at WESB 2012
 * @author Matheus Paixao
 */
public class SRPPProblem{
   SRPPInstanceReader srppInstanceReader;

   double[][] objectivesValues; //value of each objective for each requirement
   int numberOfRequirements;
   int numberOfClients;
   int[][] precedencesMatrix;

   /**
    * Method to create the SRPPProblem object.
    *
    * @author Matheus Paixao
    * @param instance the problem's instance
    * @see SRPPInstanceReader constructor
    * @see getObjectiveValues in srppInstanceReader class
    * @see getNumberOfRequirements in srppInstanceReader class
    * @see getNumberOfClients in srppInstanceReader class
    * @see getPrecedencesMatrix in srppInstanceReader class
    */
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

   /**
    * Method to comprare if a solution value is better than another one.
    *
    * In SRPP as bigger fitness value as better.
    * @author Matheus Paixao
    * @param solution1
    * @param solution2
    * @return true if the first fitness value is better than the other one
    */
   public boolean isSolutionBest(double solution1, double solution2){
      boolean result = false;

      if(solution1 > solution2){
         result = true;
      }

      return result;
   }

   /**
    * Method that implements the fitness function of SRPP problem.
    *
    * This fitness function is described in the paper.
    * @author Matheus Paixao
    * @param solution the Integer array that corresponds to the solution to be calculated
    * @return fitness value of the solution
    */
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
