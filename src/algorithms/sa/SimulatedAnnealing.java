package algorithms.sa;

import algorithms.Algorithm;

import java.util.Random;

public abstract class SimulatedAnnealing implements Algorithm{
   private double finalTemperature;
   private double temperature;
   private double alfa;
   private int numberOfMarkovChains;
   private int[] bestSoFarSolution;

   protected abstract double getInitialTemperature();
   protected abstract double getFinalTemperature();
   protected abstract double getAlpha();
   protected abstract int getNumberOfMarkovChains();
   protected abstract int[] getInitialSolution();
   protected abstract int[] getNeighbourSolution(int[] solution);
   protected abstract double calculateSolutionValue(int[] solution);

   private Random random;

   private double totalTime;

   public SimulatedAnnealing(){
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

      double initialTime = 0;
      double finalTime = 0 ;

      initSA();

      initialTime = System.currentTimeMillis();
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
      finalTime = System.currentTimeMillis();

      setTotalTime(finalTime - initialTime);
      return calculateSolutionValue(bestSoFarSolution);
   }

   private void initSA(){
      temperature = getInitialTemperature();
      finalTemperature = getFinalTemperature();
      alfa = getAlpha();
      numberOfMarkovChains = getNumberOfMarkovChains();
      bestSoFarSolution = getInitialSolution();
   }

   private double getAcceptanceProbability(double bestSoFarSolutionValue, double neighbourSolutionValue){
      return Math.exp((bestSoFarSolutionValue - neighbourSolutionValue) / temperature);
   }

   private void updateTemperature(){
      temperature = temperature * alfa;
   }
}
