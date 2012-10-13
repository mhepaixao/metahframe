package problems.srpp;

import algorithms.random.RandomAlgorithm;
import util.Node;

import java.util.ArrayList;

/**
 * Class to implement the Random Algorithm to the Software Requirement Priorization Problem.
 *
 * This problem is modeled in the paper:
 * "Aplicando o Algoritmo Ant-Q na Priorização de Requisitos de Software com Precedência",
 * published at WESB 2012
 * @author Matheus Paixao
 */
public class SRPPRandomAlgorithm extends RandomAlgorithm{
   SRPPProblem srppProblem;

   /**
    * Method to create the SRPPRandomAlgorithm object, receive the SRPPProblem object and
    * the number of iterations is passed to the RandomAlgorithm constructor.
    *
    * @author Matheus Paixao
    * @param srppProblem the SRPPProblem object
    * @param numberOfIterations number of iterations to be runned
    * @see RandomAlgorithm constructor
    */
   public SRPPRandomAlgorithm(SRPPProblem srppProblem, int numberOfIterations){
      super(numberOfIterations);
      this.srppProblem = srppProblem;
   }

   public int getNumberOfNodes(){
      return srppProblem.getNumberOfRequirements();
   }

   /**
    * Method that implements the fitness function of SRRP problem.
    *
    * @author Matheus Paixao
    * @param solution the Integer array that corresponds to the solution founded by the algorithm
    * @return fitness value of the solution
    * @see calculateSolutionValue in SRPPProblem class
    */
   public double calculateSolutionValue(Integer[] solution){
      return srppProblem.calculateSolutionValue(solution);
   }

   /**
    * Method to compare if a solution value is better than another one.
    *
    * @author Matheus Paixao
    * @param iterationSolutionValue the fitness value of some solution
    * @param bestSolutionValue the best fitness value of an iteration
    * @return true if the first fitness value is best than the other one
    * @see isSolutionBest in SRPPProblem class
    */
   public boolean isSolutionBest(double iterationSolutionValue, double bestSolutionValue){
      return srppProblem.isSolutionBest(iterationSolutionValue, bestSolutionValue);
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
   public boolean satisfyAllRestrictions(Integer[] solution){
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
      int[][] precedencesMatrix =  srppProblem.getPrecedencesMatrix();
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
      int[][] precedencesMatrix = srppProblem.getPrecedencesMatrix();
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
   private int getRequirementPosition(Node requirement, Integer[] solution){
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
