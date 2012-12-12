package problems.tsp;

import algorithms.sa.SimulatedAnnealing;
import instancereaders.TSPInstanceReader;

import java.io.File;

import java.util.Random;
import java.util.ArrayList;

public class TSPSimulatedAnnealing extends SimulatedAnnealing{
   private TSPProblem tspProblem;

   public TSPSimulatedAnnealing(TSPProblem tspProblem, int numberOfIterations){
      super(numberOfIterations);
      this.tspProblem = tspProblem;
   }

   protected int[] getInitialSolution(){
      int[] initialSolution = new int[tspProblem.getNumberOfCities()];
      Random random = new Random();
      ArrayList<Integer> listToGetRandomCities = new ArrayList<Integer>();

      for(int i = 0; i <= initialSolution.length - 1; i++){
         listToGetRandomCities.add(i);
      }

      for(int i = 0; i <= initialSolution.length - 1; i++){
         initialSolution[i] = getRandomCity(random, listToGetRandomCities);
      }

      return initialSolution;
   }

   private int getRandomCity(Random random, ArrayList<Integer> listToGetRandomCities){
      int randomCity;
      int randomIndex = random.nextInt(listToGetRandomCities.size());

      randomCity = listToGetRandomCities.get(randomIndex);
      listToGetRandomCities.remove(randomIndex);
      return randomCity;
   }

   protected int[] getNeighbourSolution(int[] solution){
      return null;
   }

   protected double calculateSolutionValue(int[] solution){
      return 0;
   }
}
