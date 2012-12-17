package problems.tsp;

import algorithms.sa.SimulatedAnnealing;
import instancereaders.TSPInstanceReader;

import java.io.File;

import java.util.Random;
import java.util.ArrayList;

public class TSPSimulatedAnnealing extends SimulatedAnnealing{
   private TSPProblem tspProblem;

   private Random random;

   public TSPSimulatedAnnealing(TSPProblem tspProblem, int numberOfIterations){
      super(numberOfIterations);
      this.tspProblem = tspProblem;
      this.random = new Random();
   }

   protected double getInitialTemperature(){
      return 1000;
   }

   protected double getFinalTemperature(){
      return 1;
   }

   protected double getAlpha(){
      return 0.9995;
   }

   protected int getNumberOfMarkovChains(){
      return 10;
   }

   protected int[] getInitialSolution(){
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

   protected double calculateSolutionValue(int[] solution){
      Integer[] parsedSolution = new Integer[solution.length];

      for(int i = 0; i <= parsedSolution.length - 1; i++){
         parsedSolution[i] = new Integer(solution[i]);
      }

      return tspProblem.calculateSolutionValue(parsedSolution);
   }
}
