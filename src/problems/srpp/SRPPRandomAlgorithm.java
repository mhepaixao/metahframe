package problems.srpp;

import algorithms.random.RandomAlgorithm;
import instancereaders.SRPPInstanceReader;
import util.Node;

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

   /**
    * Method to create the SRPPRandomAlgorithm object, receive the instance to read and
    * the number of iterations is passed to the RandomAlgorithm constructor.
    *
    * @author Matheus Paixao
    * @param instance the instance to read
    * @param numberOfIterations number of iterations to be runned
    * @see RandomAlgorithm constructor
    * @see getObjectiveValues in SRPPInstanceReader
    * @see getPrecedencesMatrix in SRPPInstanceReader
    */
   public SRPPRandomAlgorithm(File instance, int numberOfIterations){
      super(numberOfIterations);
      srppInstanceReader = new SRPPInstanceReader(instance);
      objectivesValues = srppInstanceReader.getObjectiveValues();
      this.precedencesMatrix = srppInstanceReader.getPrecedencesMatrix();
      this.numberOfRequirements = objectivesValues[0].length;
   }

   public int getNumberOfNodes(){
      return this.numberOfRequirements;
   }

   /**
    * Method that implements the fitness function of SRRP problem.
    *
    * This fitness function is described in the paper.
    * @author Matheus Paixao
    * @param solution the array of edges that corresponds to the solution founded by the algorithm
    * @return fitness value of the solution
    * @see getObjectivesSum
    */
   public double calculateSolutionValue(int[] solution){
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
   private double getObjectivesSum(int requirement){
      double objectivesSum = 0;

      for(int i = 0; i <= objectivesValues.length - 1; i++){
         objectivesSum += objectivesValues[i][requirement];
      }

      return objectivesSum;
   }

   /**
    * Method to compare if a solution value is better than another one.
    *
    * In SRPP as bigger fitness value as better.
    * @author Matheus Paixao
    * @param iterationSolutionValue the fitness value of some solution
    * @param bestSolutionValue the best fitness value of an iteration
    * @return true if the first fitness value is best than the other one
    */
   public boolean isSolutionBest(double iterationSolutionValue, double bestSolutionValue){
      boolean result = false;

      if(iterationSolutionValue > bestSolutionValue){
         result = true;
      }

      return result;
   }

   /**
    * Method to know if a solution broke some restriction.
    *
    * In SRPP a requirement cannot be placed before its predecessors.
    * @author Matheus Paixao
    * @param solution the solution to test if it broke some restriction
    * @return true if the solution is OK, false if broke some restriction
    * @see hasPredecessor
    * @see getPredecessors
    * @see getRequirementPosition
    */
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

   /**
    * Method to get all predecessors of a requirement.
    *
    * @author Matheus Paixao
    * @param requirement the requirement to get its predecessors
    * @return list containing all predecessors of the requirement
    */
   private ArrayList<Node> getPredecessors(int requirement){
      ArrayList<Node> predecessors = new ArrayList<Node>();

      for(int i = 0; i <= precedencesMatrix[requirement].length - 1; i++){
         if(precedencesMatrix[requirement][i] == 1){
            predecessors.add(new Node(i));
         }
      }

      return predecessors;
   }

   /**
    * Method to get the position of a requirement in a given solution.
    *
    * @author Matheus Paixao
    * @param requirement the requirement to get the position
    * @param solution the solution where the requirement is
    * @return the position of the requirement in the solution
    */
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
