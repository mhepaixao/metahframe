package algorithms.ga;

import algorithms.Algorithm;

import java.util.Random;

public abstract class GeneticAlgorithm implements Algorithm{

   private int numberOfIterations;
   private int[][] population;
   private double crossoverProbability;
   private double mutationProbability;

   private double totalTime;

   protected abstract int[][] getInitialPopulation();
   protected abstract double getCrossoverProbability();
   protected abstract double getMutationProbability();

   public GeneticAlgorithm(int numberOfIterations){
      this.numberOfIterations = numberOfIterations;
   }

   public double getTotalTime(){
      return this.totalTime;
   }

   private void setTotalTime(double totalTime){
      this.totalTime = totalTime;
   }

   public double getSolution(){
      double initialTime;
      double finalTime;
      initGA();

      initialTime = System.currentTimeMillis();

      finalTime = System.currentTimeMillis();
      setTotalTime(finalTime - initialTime);
      return 0;
   }

   private void initGA(){
      this.population = getInitialPopulation();
      this.crossoverProbability = getCrossoverProbability();
      this.mutationProbability = getMutationProbability();
   }
}
