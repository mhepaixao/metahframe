package problems.tsp;

import algorithms.acs.ACS;
import instancereaders.TSPInstanceReader;

import java.io.File;

import java.util.Random;

public class TSPACS extends ACS{
   private TSPProblem tspProblem;

   private double initialPheromone;

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

   private double calculateInitialPheromone(){
      return Math.pow(getNumberOfNodes() * getNearestNeighbourSolutionValue(), -1);
   }

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

   private void initNearestNeighbourAlgorithm(Integer[] nearestNeighbourSolution, Integer[] citiesToVisitInNearestNeighbourSolution){
      for(int i = 0; i <= nearestNeighbourSolution.length - 1; i++){
         nearestNeighbourSolution[i] = 0;
         citiesToVisitInNearestNeighbourSolution[i] = i;
      }
   }

   private int getFirstNearestNeighbourCity(Integer[] citiesToVisitInNearestNeighbourSolution){
      Random random = new Random();
      int randomCityIndex = random.nextInt(getNumberOfNodes());
      int firstCity = citiesToVisitInNearestNeighbourSolution[randomCityIndex];

      return firstCity;
   }

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

   public double getHeuristicValue(int node1, int node2){
      double[][] distancesMatrix = tspProblem.getDistancesMatrix();
      return 1 / distancesMatrix[node1][node2];
   }
}
