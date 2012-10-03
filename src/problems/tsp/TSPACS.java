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
      return Math.pow(getNumberOfNodes() * getNearestNeighbourSolution(), -1);
   }

   private double getNearestNeighbourSolution(){
      int[] nearestNeighbourSolution = new int[getNumberOfNodes()];
      Integer[] citiesToVisit = new Integer[getNumberOfNodes()];

      for(int i = 0; i <= nearestNeighbourSolution.length - 1; i++){
         nearestNeighbourSolution[i] = 0;
         citiesToVisit[i] = i;
      }

      nearestNeighbourSolution[0] = getFirstNearestNeighbourCity(citiesToVisit);
      for(int i = 1; i <= nearestNeighbourSolution.length - 1; i++){
         nearestNeighbourSolution[i] = getNextNearestNeighbourCity(i - 1, citiesToVisit);
      }

      return calculateSolutionValue(nearestNeighbourSolution);
   }

   private int getFirstNearestNeighbourCity(Integer[] citiesToVisit){
      Random random = new Random();
      int firstCity = random.nextInt(getNumberOfNodes());
      citiesToVisit[firstCity] = null;
      return firstCity;
   }

   private int getNextNearestNeighbourCity(int lastAddedCity, Integer[] citiesToVisit){
      int nextNearestNeighbourCity = 0;
      double minDistance = 0;

      for(int i = 0; i <= citiesToVisit.length - 1; i++){
         if(citiesToVisit[i] != null){
            if(minDistance == 0){
               minDistance = distancesMatrix[lastAddedCity][i];
               nextNearestNeighbourCity = i;
            }
            else{
               if(distancesMatrix[lastAddedCity][i] < minDistance){
                  minDistance = distancesMatrix[lastAddedCity][i];
                  nextNearestNeighbourCity = i;
               }
            }
         }
      }

      citiesToVisit[nextNearestNeighbourCity] = null;
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
