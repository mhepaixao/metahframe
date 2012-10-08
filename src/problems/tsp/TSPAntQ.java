package problems.tsp;

import algorithms.antq.AntQ;
import util.Node;
import util.Edge;

/**
 * Class to implement the AntQ class to the Travel Salesman Problem.
 *
 * @author Matheus Paixao
 */
public class TSPAntQ extends AntQ{
   private TSPProblem tspProblem;

   private double initialPheromone;

   /**
    * Method to create the TSPAntQ object, receive TSPProblem object and
    * the number of iterations is passed to AntQ constructor.
    *
    * @author Matheus Paixao
    * @param tspProblem the TSPProblem object
    * @param numberOfIterations number of iterations to be runned
    * @see AntQ constructor
    * @see calculateInitialPheromone
    */
   public TSPAntQ(TSPProblem tspProblem, int numberOfIterations){
      super(numberOfIterations);
      this.tspProblem = tspProblem;
      this.initialPheromone = calculateInitialPheromone();
   }

   public int getNumberOfNodes(){
      return tspProblem.getNumberOfCities();
   }

   public double getInitialPheromone(){
      return this.initialPheromone;
   }

   /**
    * Method that implements the fitness function of TSP problem.
    *
    * @author Matheus Paixao
    * @param solution the array of edges that corresponds to the solution founded by the algorithm
    * @return fitness value of the solution
    * @see getIntegerSolutionArray
    * @see calculateSolutionValue in TSPProblem class
    */
   public double calculateSolutionValue(Edge[] solution){
      return tspProblem.calculateSolutionValue(getIntegerSolutionArray(solution));
   }

   /**
    * Method to get an Integer solution array given an Edge solution array.
    *
    * @author Matheus Paixao
    * @param solution the Edge solution array
    * @return the Integer solution array
    */
   private Integer[] getIntegerSolutionArray(Edge[] solution){
      Integer[] integerSolutionArray = new Integer[solution.length];

      for(int i = 0; i <= integerSolutionArray.length - 1; i++){
         integerSolutionArray[i] = solution[i].getNode1().getIndex();
      }

      return integerSolutionArray;
   }

   /**
    * Method to compare if a solution value is better than another one.
    *
    * @author Matheus Paixao
    * @param iterationSolutionValue the fitness value of some solution
    * @param bestSolutionValue the best fitness value of an iteration
    * @return true if the first fitness value is best than the other one
    * @see isSolutionBest in TSPProblem class
    */
   public boolean isSolutionBest(double iterationSolutionValue, double bestSolutionValue){
      return tspProblem.isSolutionBest(iterationSolutionValue, bestSolutionValue);
   }

   /**
    * Method to get the initial pheromone value for all edges.
    *
    * The initial pheromone value is composed with the distances average and the number of nodes.
    * @author Matheus Paixao
    * @return the initial pheromone value for all edges.
    * @see getNumberOfEdges
    */
   public double calculateInitialPheromone(){
      double[][] distancesMatrix = tspProblem.getDistancesMatrix();
      double sumOfDistances = 0;
      double distancesAverage = 0;

      for(int i = 0; i <= distancesMatrix.length - 1; i++){
         for(int j = 0; j <= distancesMatrix[0].length - 1; j++){
            sumOfDistances += distancesMatrix[i][j];
         }
      }
      distancesAverage = sumOfDistances / getNumberOfEdges();
      
      return 1 / (distancesAverage * distancesMatrix.length);
   }

   /**
    * Method to get the number of existing edges.
    *
    * When 'i' is equal to 'j' there is no edge.
    * @author Matheus Paixao
    * @return the number of existing edges.
    */
   private int getNumberOfEdges(){
      double[][] distancesMatrix = tspProblem.getDistancesMatrix();
      int numberOfEdges = 0;

      for(int i = 0; i <= distancesMatrix.length - 1; i++){
         for(int j = 0; j <= distancesMatrix[0].length - 1; j++){
            if(i != j){
               numberOfEdges++;
            }
         }
      }

      return numberOfEdges;
   }

   /**
    * Method to get the heuristic value of an edge.
    *
    * In TSP as smaller the distance, higher is the heuristic value.
    * @author Matheus Paixao
    * @param node1 the first node of the edge
    * @param node2 the second node of the edge
    * @return the heuristic value of the edge composed by the two passed nodes
    */
   public double getHeuristicValue(Node node1, Node node2){
      double[][] distancesMatrix = tspProblem.getDistancesMatrix();
      return 1 / distancesMatrix[node1.getIndex()][node2.getIndex()];
   }
}
