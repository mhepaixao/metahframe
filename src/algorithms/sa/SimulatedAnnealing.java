package algorithms.sa;

import algorithms.Algorithm;

import java.util.Random;

public abstract class SimulatedAnnealing implements Algorithm{
   private int numberOfIterations;
   private double temperature;
   private int numberOfMarkovChains;
   private int[] bestSolutionSoFar;

   protected abstract int[] getInitialSolution();
   protected abstract int[] getNeighbourSolution(int[] solution);
   protected abstract double calculateSolutionValue(int[] solution);

   private Random random;

   public SimulatedAnnealing(int numberOfIterations){
      this.numberOfIterations = numberOfIterations;
      this.random = new Random();
   }

   public double getSolution(){
      initSA();

      return 0;
   }

   private void initSA(){
      temperature = 100;
      numberOfMarkovChains = 10;
      bestSolutionSoFar = getInitialSolution();
   }

   public double getTotalTime(){
      return 0;
   }
}
