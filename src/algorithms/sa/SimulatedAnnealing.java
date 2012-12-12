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

   private double totalTime;

   public SimulatedAnnealing(int numberOfIterations){
      this.numberOfIterations = numberOfIterations;
      this.random = new Random();
   }

   public double getTotalTime(){
      return this.totalTime;
   }

   public void setTotalTime(double totalTime){
      this.totalTime = totalTime;
   }

   public double getSolution(){
      double initialTime = System.currentTimeMillis();
      int[] neighbourSolution = null;

      initSA();

      while(temperature > 1){
         neighbourSolution = getNeighbourSolution(bestSolutionSoFar);
      }

      setTotalTime(System.currentTimeMillis() - initialTime);
      return 0;
   }

   private void initSA(){
      temperature = 100;
      numberOfMarkovChains = 10;
      bestSolutionSoFar = getInitialSolution();
   }
}
