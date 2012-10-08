package problems.tsp;

import algorithms.acs.ACS;

import java.util.Random;

/**
 * Class to implement the Ant Colony System class to the Travel Salesman Problem.
 *
 * @author Matheus Paixao
 */
public class TSPACS extends ACS{
   private TSPProblem tspProblem;

   private double initialPheromone;

   /**
    * Method to create the TSPAntQ object, receive TSPProblem object and
    * the number of iterations is passed to AntQ constructor.
    *
    * @author Matheus Paixao
    * @param tspProblem the TSPProblem object
    * @param numberOfIterations number of iterations to be runned
    * @see ACS constructor
    * @see calculateInitialPheromone
    */
   public TSPACS(TSPProblem tspProblem, int numberOfIterations){
      super(numberOfIterations);
      this.tspProblem = tspProblem;
      this.initialPheromone = calculateInitialPheromone();
   }

   protected double getQ0(){
      return 0.9;
   }

   protected double getBeta(){
      return 2;
   }

   protected double getAlpha(){
      return 0.1;
   }

   protected double getRho(){
      return 0.1;
   }

   protected int getNumberOfNodes(){
      return tspProblem.getNumberOfCities();
   }

   protected int getNumberOfAnts(){
      return 10;
   }

   protected double getInitialPheromone(){
      return this.initialPheromone;
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
    * Method that implements the fitness function of TSP problem.
    *
    * @author Matheus Paixao
    * @param solution the array of int that corresponds to the solution to be calculated
    * @return fitness value of the solution
    */
   public double calculateSolutionValue(Integer[] solution){
      return tspProblem.calculateSolutionValue(solution);
   }

   /**
    * Method to get the initial pheromone value for all edges.
    *
    * The initial pheromone value is described in the ACS original paper.
    * Uses the nearest neighbour heurisitic.
    * @author Matheus Paixao
    * @return the initial pheromone value for all edges.
    * @see getNumberOfNodes
    * @see getNearestNeighbourSolutionValue
    */
   private double calculateInitialPheromone(){
      return Math.pow(getNumberOfNodes() * getNearestNeighbourSolutionValue(), -1);
   }

   /**
    * Method to get a TSP solution value using the nearest neighbour heuristic.
    *
    * @author Matheus Paixao
    * @return the solution value
    * @see initNearestNeighbourAlgorithm
    * @see getFirstNearestNeighbourCity
    * @see getNearestNeighbourSolutionValue
    * @see calculateSolutionValue in TSPProblem class
    */
   private double getNearestNeighbourSolutionValue(){
      Integer[] nearestNeighbourSolution = new Integer[getNumberOfNodes()];
      Integer[] citiesToVisitInNearestNeighbourSolution = new Integer[getNumberOfNodes()];
      int previousCity = 0;
      int nextCity = 0;

      initNearestNeighbourAlgorithm(nearestNeighbourSolution, citiesToVisitInNearestNeighbourSolution);

      nextCity = getFirstNearestNeighbourCity(citiesToVisitInNearestNeighbourSolution);
      nearestNeighbourSolution[0] = nextCity;
      citiesToVisitInNearestNeighbourSolution[nextCity] = null;

      for(int i = 1; i <= nearestNeighbourSolution.length - 1; i++){
         previousCity = i - 1;
         nextCity = getNextNearestNeighbourCity(previousCity, citiesToVisitInNearestNeighbourSolution);
         nearestNeighbourSolution[i] = nextCity;
         citiesToVisitInNearestNeighbourSolution[nextCity] = null;
      }

      return tspProblem.calculateSolutionValue(nearestNeighbourSolution);
   }

   /**
    * Method to init the nearest neighbour heuristic.
    *
    * @author Matheus Paixao
    * @param nearestNeighbourSolution the solution using the nearest neighbour heuristic
    * @param citiesToVisitInNearestNeighbourSolution the array used in nearest neighbour heurisitic to build the solution
    */
   private void initNearestNeighbourAlgorithm(Integer[] nearestNeighbourSolution, Integer[] citiesToVisitInNearestNeighbourSolution){
      for(int i = 0; i <= nearestNeighbourSolution.length - 1; i++){
         nearestNeighbourSolution[i] = 0;
         citiesToVisitInNearestNeighbourSolution[i] = i;
      }
   }

   /**
    * Method to randomly get the first city of the nearest neighbour algorithm.
    *
    * @author Matheus Paixao
    * @param citiesToVisitInNearestNeighbourSolution the array of cities to randomly get the first one
    * @return the first city of the nearest neighbour solution
    */
   private int getFirstNearestNeighbourCity(Integer[] citiesToVisitInNearestNeighbourSolution){
      Random random = new Random();
      int randomCityIndex = random.nextInt(getNumberOfNodes());
      int firstCity = citiesToVisitInNearestNeighbourSolution[randomCityIndex];

      return firstCity;
   }

   /**
    * Method to get the next node using nearest neighbour heuristic (greedy algorithm).
    *
    * @author Matheus Paixao
    * @param previousCity the last added city to the solution
    * @param citiesToVisitInNearestNeighbourSolution the cities to visit array to get the next city
    */
   private int getNextNearestNeighbourCity(int previousCity, Integer[] citiesToVisitInNearestNeighbourSolution){
      double[][] distancesMatrix = tspProblem.getDistancesMatrix();
      int nextNearestNeighbourCity = 0;
      double minDistance = 0;
      double distance = 0;

      for(int i = 0; i <= citiesToVisitInNearestNeighbourSolution.length - 1; i++){
         if(citiesToVisitInNearestNeighbourSolution[i] != null){
            distance = distancesMatrix[previousCity][i];
            if(minDistance == 0){
               minDistance = distance;
               nextNearestNeighbourCity = i;
            }
            else{
               if(distance < minDistance){
                  minDistance = distance;
                  nextNearestNeighbourCity = i;
               }
            }
         }
      }

      return nextNearestNeighbourCity;
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
   public double getHeuristicValue(int node1, int node2){
      double[][] distancesMatrix = tspProblem.getDistancesMatrix();
      return 1 / distancesMatrix[node1][node2];
   }
}
