package problems.tsp;

import algorithms.acs.ACS;
import instancereaders.TSPInstanceReader;

import java.io.File;

import java.util.Random;

public class TSPACS extends ACS{
   private TSPInstanceReader tspInstanceReader;

   private int numberOfCities;
   private double[][] distancesMatrix;
   private double initialPheromone;

   public TSPACS(File instance, int numberOfIterations){
      super(numberOfIterations);
      this.tspInstanceReader = new TSPInstanceReader(instance);
      this.distancesMatrix = tspInstanceReader.getDistancesMatrix();
      this.numberOfCities = distancesMatrix.length; 
      this.initialPheromone = calculateInitialPheromone();
   }

   protected int getNumberOfNodes(){
      return this.numberOfCities;
   }

   protected int getNumberOfAnts(){
      return 10;
   }

   protected double getInitialPheromone(){
      return this.initialPheromone;
   }

   private double calculateInitialPheromone(){
      return Math.pow(getNumberOfNodes() * getNearestNeighbourSolutionValue(), -1);
   }

   private double getNearestNeighbourSolutionValue(){
      int[] nearestNeighbourSolution = new int[getNumberOfNodes()];
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

      return calculateSolutionValue(nearestNeighbourSolution);
   }

   private void initNearestNeighbourAlgorithm(int[] nearestNeighbourSolution, Integer[] citiesToVisitInNearestNeighbourSolution){
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
    * Method that implements the fitness function of TSP problem.
    *
    * @author Matheus Paixao
    * @param solution the array of int that corresponds to the solution to be calculated
    * @return fitness value of the solution
    */
   public double calculateSolutionValue(int[] solution){
      double solutionValue = 0;

      for(int i = 0; i <= solution.length - 2; i++){
         solutionValue += distancesMatrix[solution[i]][solution[i + 1]];
      }
      solutionValue += distancesMatrix[solution[solution.length - 1]][solution[0]];

      return solutionValue;
   }
}
