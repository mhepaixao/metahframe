package algorithms.sa;

import algorithms.Algorithm;

import java.util.Random;

public abstract class SimulatedAnnealing implements Algorithm{
   private int numberOfIterations;
   private double initialTemperature;
   private double finalTemperature;
   private double temperature;
   private double alfa;
   private int numberOfMarkovChains;
   private int[] bestSoFarSolution;

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
      int[] neighbourSolution = null;
      double bestSoFarSolutionValue = 0;
      double neighbourSolutionValue = 0;
      double acceptanceProbability = 0;

      double initialTime = System.currentTimeMillis();

      initSA();

      while(temperature > finalTemperature){
         for(int i = 0; i <= numberOfMarkovChains - 1; i++){
            neighbourSolution = getNeighbourSolution(bestSoFarSolution);

            bestSoFarSolutionValue = calculateSolutionValue(bestSoFarSolution);
            neighbourSolutionValue = calculateSolutionValue(neighbourSolution);

            if(neighbourSolutionValue <= bestSoFarSolutionValue){
               bestSoFarSolution = neighbourSolution;
            }
            else{
               acceptanceProbability = getAcceptanceProbability(bestSoFarSolutionValue, neighbourSolutionValue);
               if(acceptanceProbability > random.nextDouble()){
                  bestSoFarSolution = neighbourSolution;
               }
            }
         }

         updateTemperature();
      }

      setTotalTime(System.currentTimeMillis() - initialTime);
      return calculateSolutionValue(bestSoFarSolution);
   }

   private void initSA(){
      initialTemperature = 100;
      finalTemperature = 1;
      temperature = initialTemperature;
      alfa = 0.9995;
      numberOfMarkovChains = 10;
      bestSoFarSolution = getInitialSolution();
   }

   private double getAcceptanceProbability(double bestSoFarSolutionValue, double neighbourSolutionValue){
      return Math.exp((bestSoFarSolutionValue - neighbourSolutionValue) / temperature);
   }

   private void updateTemperature(){
      temperature = temperature * alfa;
   }
}
