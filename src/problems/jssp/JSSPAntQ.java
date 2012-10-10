package problems.jssp;

import algorithms.antq.AntQ;
import algorithms.antq.Ant;
import util.Node;
import util.Edge;

/**
 * Class to implement the AntQ algorithm to the Job Sequence Schedule Problem.
 *
 * @author Matheus Paixao
 */
public class JSSPAntQ extends AntQ{
   JSSPProblem jsspProblem;

   /**
    * Method to create the JSSPAntQ object, receive the JSSPProblem object and
    * the number of iterations is passed to AntQ constructor.
    *
    * @author Matheus Paixao
    * @param jsspProblem the JSSPProblem object
    * @param numberOfIterations number of iterations to be runned
    * @see AntQ constructor
    */
   public JSSPAntQ(JSSPProblem jsspProblem, int numberOfIterations){
      super(numberOfIterations);
      this.jsspProblem = jsspProblem;
   }

   public int getNumberOfNodes(){
      return jsspProblem.getNumberOfJobs(); //in JSSP the jobs are represented by the nodes
   }

   public double getInitialPheromone(){
      return 0.01; //this initial pheromone value was founded empirically
   }

   /**
    * Method to get the heuristic value of an edge.
    *
    * In JSSP as smaller the solution, higher is the heuristic value.
    * @author Matheus Paixao
    * @param node1 the first node of the edge
    * @param node2 the second node of the edge
    * @return the heuristic value of the edge composed by the two passed nodes
    * @see getCurrentAnt
    * @see getJobSequence
    * @see calculateSolutionValue in JSSPProblem class
    */
   public double getHeuristicValue(Node node1, Node node2){
      Integer[] jobSequence = getJobSequence(node1, node2, getCurrentAnt());
      
      return 1 / jsspProblem.calculateSolutionValue(jobSequence);
   }

   /**
    * Method that implements the fitness function of JSSP problem.
    *
    * @author Matheus Paixao
    * @param solution the array of edges that corresponds to the solution founded by the algorithm
    * @return fitness value of the solution
    * @see calculateSolutionValue in JSSPProblem class
    */
   public double calculateSolutionValue(Edge[] solution){
      Integer[] jobSequence = new Integer[solution.length];

      for(int i = 0; i <= jobSequence.length - 1; i++){
         jobSequence[i] = solution[i].getNode1().getIndex();
      }

      return jsspProblem.calculateSolutionValue(jobSequence);
   }

   /**
    * Method to get the job sequence of an "in construction" solution, given the ant
    * and the next edge to add.
    *
    * @author Matheus Paixao
    * @param node1 the first node of the next edge
    * @param node2 the second node of the next edge
    * @param ant the ant that are building its solution
    * @return an array of int that represents the job sequence with the new edge
    * @see getTour in Ant class
    * @see getNumberOfJobs
    */
   private Integer[] getJobSequence(Node node1, Node node2, Ant ant){
      Integer[] jobSequence;

      Edge[] solution = ant.getTour();
      jobSequence = new Integer[getNumberOfJobs(solution) + 2]; //there is the next edge to add

      if(solution[0] == null){
         jobSequence[0] = node1.getIndex();
         jobSequence[1] = node2.getIndex();
      }
      else{
         for(int i = 0; i <= solution.length - 1; i++){
            if(solution[i] != null){
               jobSequence[i] = solution[i].getNode1().getIndex();
               jobSequence[i+1] = solution[i].getNode2().getIndex();
            }
         }
         jobSequence[jobSequence.length - 1] = node2.getIndex();
      }

      return jobSequence;
   }

   /**
    * Method to get the number of jobs of a solution.
    *
    * @author Matheus Paixao
    * @param solution the solution to count the number of jobs
    * @return the number of jobs of the solution
    */
   private int getNumberOfJobs(Edge[] solution){
      int numberOfJobs = 0;

      for(int i = 0; i <= solution.length - 1; i++){
         if(solution[i] != null){
            numberOfJobs++;
         }
      }

      return numberOfJobs;
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
}
