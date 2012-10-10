package problems.jssp;

import algorithms.acs.ACS;
import algorithms.acs.ACSAnt;

/**
 * Class to implement the Ant Colony System algorithm to the Job Sequence Schedule Problem.
 *
 * @author Matheus Paixao
 */
public class JSSPACS extends ACS{
   JSSPProblem jsspProblem;

   /**
    * Method to create the JSSPACS object, receive the JSSPProblem object and
    * the number of iterations is passed to ACS constructor.
    *
    * @author Matheus Paixao
    * @param jsspProblem the JSSPProblem object
    * @param numberOfIterations number of iterations to be runned
    * @see AntQ constructor
    */
   public JSSPACS(JSSPProblem jsspProblem, int numberOfIterations){
      super(numberOfIterations);
      this.jsspProblem = jsspProblem;
   }

   /**
    * Method to compare if a solution value is better than another one.
    *
    * @author Matheus Paixao
    * @param iterationSolutionValue the fitness value of some solution
    * @param bestSolutionValue the best fitness value of an iteration
    * @return true if the first fitness value is best than the other one
    * @see isSolutionBest in JSSPProblem class
    */
   public boolean isSolutionBest(double iterationSolutionValue, double bestSolutionValue){
      return jsspProblem.isSolutionBest(iterationSolutionValue, bestSolutionValue);
   }

   public int getNumberOfNodes(){
      return jsspProblem.getNumberOfJobs();
   }

   /**
    * Method that implements the fitness function of JSSP problem.
    *
    * @author Matheus Paixao
    * @param solution the Integer array that corresponds to the solution founded by the algorithm
    * @return fitness value of the solution
    * @see calculateSolutionValue in JSSPProblem class
    */
   public double calculateSolutionValue(Integer[] solution){
      return jsspProblem.calculateSolutionValue(solution);
   }

   /**
    * Method to get the heuristic value of an edge.
    *
    * In JSSP as smaller the solution, higher is the heuristic value.
    * @author Matheus Paixao
    * @param node1 the first node of the edge
    * @param node2 the second node of the edge
    * @return the heuristic value of the edge composed by the two passed nodes
    * @see getCurrentAnt in ACS class
    * @see getJobSequence
    * @see calculateSolutionValue in JSSPProblem class
    */
   public double getHeuristicValue(int node1, int node2){
      Integer[] jobSequence = getJobSequence(node2, getCurrentAnt());

      return 1 / jsspProblem.calculateSolutionValue(jobSequence);
   }

   /**
    * Method to get the job sequence of an "in construction" solution, given the ant
    * and the possible next node to add.
    *
    * @author Matheus Paixao
    * @param possibleNextNode the possible next node to get the heuristic value
    * @param ant the ant that are building its solution
    * @return an array of int that represents the job sequence with the new node
    * @see getTour in ACSAnt class
    * @see getNumberOfJobs
    */
   private Integer[] getJobSequence(int possibleNextNode, ACSAnt ant){
      Integer[] tour = getCurrentAnt().getTour();
      Integer[] jobSequence = new Integer[getNumberOfJobs(tour) + 1];

      for(int i = 0; i <= jobSequence.length - 2; i++){
         jobSequence[i] = tour[i];
      }
      jobSequence[jobSequence.length - 1] = possibleNextNode;

      return jobSequence;
   }

   /**
    * Method to get the number of jobs of a solution.
    *
    * @author Matheus Paixao
    * @param solution the solution to count the number of jobs
    * @return the number of jobs of the solution
    */
   private int getNumberOfJobs(Integer[] solution){
      int numberOfJobs = 0;

      for(int i = 0; i <= solution.length - 1; i++){
         if(solution[i] != null){
            numberOfJobs++;
         }
      }

      return numberOfJobs;
   }

   public double getInitialPheromone(){
      return 0.01;
   }

   public int getNumberOfAnts(){
      return 10;
   }

   public double getRho(){
      return 0.1;
   }

   public double getAlpha(){
      return 0.1;
   }

   public double getBeta(){
      return 2;
   }

   public double getQ0(){
      return 0.9;
   }
}
