package problems.tsp;

import algorithms.sa.SimulatedAnnealing;
import instancereaders.TSPInstanceReader;

import java.io.File;

import java.util.Random;
import java.util.ArrayList;

public class TSPSimulatedAnnealing extends SimulatedAnnealing{
   private TSPProblem tspProblem;

   private int[] initialSolution;
   private double initialTemperature;
   private double finalTemperature;
   private int numberOfNeighbours;

   private Random random;

   public TSPSimulatedAnnealing(TSPProblem tspProblem, int numberOfIterations){
      super(numberOfIterations);
      this.tspProblem = tspProblem;
      this.random = new Random();

      this.initialSolution = generateInitialSolution();
      calculateInitialAndFinalTemperature();
   }

   protected double getInitialTemperature(){
      return this.initialTemperature;
   }

   protected double getFinalTemperature(){
      return this.finalTemperature;
   }

   protected double getAlpha(){
      return 0.995;
   }

   protected int getNumberOfMarkovChains(){
      return this.numberOfNeighbours;
   }

   protected int[] getInitialSolution(){
      return this.initialSolution;
   }

   private int[] generateInitialSolution(){
      int[] initialSolution = new int[tspProblem.getNumberOfCities()];
      ArrayList<Integer> listToGetRandomCities = new ArrayList<Integer>();

      for(int i = 0; i <= initialSolution.length - 1; i++){
         listToGetRandomCities.add(i);
      }

      for(int i = 0; i <= initialSolution.length - 1; i++){
         initialSolution[i] = getRandomCity(listToGetRandomCities);
      }

      return initialSolution;
   }

   private int getRandomCity(ArrayList<Integer> listToGetRandomCities){
      int randomCity;
      int randomIndex = random.nextInt(listToGetRandomCities.size());

      randomCity = listToGetRandomCities.get(randomIndex);
      listToGetRandomCities.remove(randomIndex);
      return randomCity;
   }

   protected int[] getNeighbourSolution(int[] solution){
      int randomIndex1 = random.nextInt(solution.length - 1);
      int randomIndex2 = random.nextInt(solution.length - 1);

      return getNeighbourSolution(solution, randomIndex1, randomIndex2);
   }

   private int[] getNeighbourSolution(int[] solution, int index1, int index2){
      int[] neighbourSolution = new int[solution.length];
      int swapCityAux = 0;

      for(int i = 0; i <= neighbourSolution.length - 1; i++){
         neighbourSolution[i] = solution[i];
      }

      swapCityAux = neighbourSolution[index1];
      neighbourSolution[index1] = neighbourSolution[index2];
      neighbourSolution[index2] = swapCityAux;

      return neighbourSolution;
   }

   private void calculateInitialAndFinalTemperature(){
      double maxSolutionValueDifference = 0;
      double minSolutionValueDifference = 0;
      double solutionValueDifference = 0;
      ArrayList<int[]> neighbours = getAllNeighbours(getInitialSolution());

      for(int i = 0; i <= neighbours.size() - 1; i++){
         for(int j = 0; j <= neighbours.size() - 1; j++){
            if(i != j){
               solutionValueDifference = getSolutionDifference(neighbours.get(i), neighbours.get(j));

               if((maxSolutionValueDifference == 0) && (minSolutionValueDifference == 0)){
                  maxSolutionValueDifference = solutionValueDifference;
                  minSolutionValueDifference = solutionValueDifference;
               }

               if(solutionValueDifference > maxSolutionValueDifference){
                  maxSolutionValueDifference = solutionValueDifference;
               }
               else{
                  if((solutionValueDifference > 0) && (solutionValueDifference < minSolutionValueDifference)){
                     minSolutionValueDifference = solutionValueDifference;
                  }
               }
            }
         }
      }

      this.initialTemperature = maxSolutionValueDifference;
      this.finalTemperature = minSolutionValueDifference;
   }

   private ArrayList<int[]> getAllNeighbours(int[] solution){
      ArrayList<int[]> neighbours = new ArrayList<int[]>();

      for(int i = 0; i <= solution.length - 1; i++){
         for(int j = solution.length - 1; j > i; j--){
            neighbours.add(getNeighbourSolution(solution, i, j));
         }
      }

      this.numberOfNeighbours = neighbours.size();
      return neighbours;
   }

   private double getSolutionDifference(int[] solution1, int[] solution2){
      double solutionDifference = 0;
      double solutionValue1 = calculateSolutionValue(solution1);
      double solutionValue2 = calculateSolutionValue(solution2);

      if(solutionValue1 >= solutionValue2){
         solutionDifference = solutionValue1 - solutionValue2;
      }
      else{
         solutionDifference = solutionValue2 - solutionValue1;
      }

      return solutionDifference;
   }

   protected double calculateSolutionValue(int[] solution){
      return tspProblem.calculateSolutionValue(solution);
   }
}
