import java.io.File;

import java.util.ArrayList;

/**
 * Class to implement the RandomAlgorithm class to the Software Requirement Priorization Problem.
 *
 * This problem is modeled in the paper:
 * "Aplicando o Algoritmo Ant-Q na Priorização de Requisitos de Software com Precedência",
 * published at WESB 2012
 * @author Matheus Paixao
 */
public class SRPPRandomAlgorithm extends RandomAlgorithm{
   SRPPInstanceReader srppInstanceReader;

   double[][] objectivesValues;
   int numberOfRequirements;
   int[][] precedencesMatrix;

   public SRPPRandomAlgorithm(File instance, int numberOfIterations){
      super(numberOfIterations);
      srppInstanceReader = new SRPPInstanceReader(instance);
      objectivesValues = srppInstanceReader.getObjectiveValues();
      this.precedencesMatrix = srppInstanceReader.getPrecedencesMatrix();
      this.numberOfRequirements = objectivesValues[0].length;
   }

   public double calculateSolutionValue(int[] solution){
      double solutionValue = 0;

      for(int i = 0; i <= solution.length - 1; i++){
         solutionValue += (solution.length - i) * getObjectivesSum(solution[i]);
      }

      return solutionValue;
   }

   private double getObjectivesSum(int requirement){
      double objectivesSum = 0;

      for(int i = 0; i <= objectivesValues.length - 1; i++){
         objectivesSum += objectivesValues[i][requirement];
      }

      return objectivesSum;
   }

   public int getNumberOfNodes(){
      return this.numberOfRequirements;
   }

   public boolean isSolutionBest(double iterationSolutionValue, double bestSolutionValue){
      boolean result = false;

      if(iterationSolutionValue > bestSolutionValue){
         result = true;
      }

      return result;
   }

   public boolean satisfyAllRestrictions(int[] solution){
      boolean result = true;
      ArrayList<Node> predecessors = null;
      int predecessorPosition = 0;

      for(int i = 0; i < solution.length - 1; i++){
         if(hasPredecessor(solution[i])){
            predecessors = getPredecessors(solution[i]);
            for(Node node : predecessors){
               predecessorPosition = getRequirementPosition(node, solution);
               if(i < predecessorPosition){
                  result = false;
                  break;
               }
            }
         }
      }

      return result;
   }

   /**
    * Method to know if a requirement has some predecessor or don't.
    *
    * @author Matheus Paixao
    * @param requirement the requirement to test the predecessors
    * @return true if the requirement has some predecessor, false if don't
    */
   private boolean hasPredecessor(int requirement){
      boolean result = false;

      for(int i = 0; i <= precedencesMatrix[requirement].length - 1; i++){
         if(precedencesMatrix[requirement][i] == 1){
            result = true;
            break;
         }
      }

      return result;
   }

   private ArrayList<Node> getPredecessors(int requirement){
      ArrayList<Node> predecessors = new ArrayList<Node>();

      for(int i = 0; i <= precedencesMatrix[requirement].length - 1; i++){
         if(precedencesMatrix[requirement][i] == 1){
            predecessors.add(new Node(i));
         }
      }

      return predecessors;
   }

   private int getRequirementPosition(Node requirement, int[] solution){
      int requirementPosition = 0;

      for(int i = 0; i <= solution.length - 1; i++){
         if(requirement.getIndex() == solution[i]){
            requirementPosition = i;
            break;
         }
      }

      return requirementPosition;
   }
}
