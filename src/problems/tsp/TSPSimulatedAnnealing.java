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
      return 0.9995;
   }

   protected int getNumberOfMarkovChains(){
      return 10;
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
      int[] neighbourSolution = new int[solution.length];
      int randomIndex1 = 0;
      int randomIndex2 = 0;
      int swapCityAux = 0;

      for(int i = 0; i <= neighbourSolution.length - 1; i++){
         neighbourSolution[i] = solution[i];
      }

      randomIndex1 = random.nextInt(neighbourSolution.length - 1);
      randomIndex2 = random.nextInt(neighbourSolution.length - 1);

      swapCityAux = neighbourSolution[randomIndex1];
      neighbourSolution[randomIndex1] = neighbourSolution[randomIndex2];
      neighbourSolution[randomIndex2] = swapCityAux;

      return neighbourSolution;
   }

   private void calculateInitialAndFinalTemperature(){
      this.initialTemperature = 1000;
      this.finalTemperature = 1;
   }

   protected double calculateSolutionValue(int[] solution){
      return tspProblem.calculateSolutionValue(solution);
   }
}
