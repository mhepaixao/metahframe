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
   protected abstract int[][] getParents(int[][] population);

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

      int[][] iterationPopulation = new int[population.length][population[0].length];
      int[][] parents = null;

      initialTime = System.currentTimeMillis();

      for(int i = 0; i <= numberOfIterations - 1; i++){
         for(int j = 0; j <= iterationPopulation.length - 1; j++){
            for(int k = 0; k <= iterationPopulation[0].length - 1; k++){
               iterationPopulation[j][k] = population[j][k];
            }
         }

         for(int j = 0; j <= population.length - 1; j = j + 2){
            parents = getParents(iterationPopulation);
         }
      }

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
