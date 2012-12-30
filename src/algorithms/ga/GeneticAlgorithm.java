package algorithms.ga;

import algorithms.Algorithm;

import java.util.Random;
import java.util.Arrays;

public abstract class GeneticAlgorithm implements Algorithm{

   private int numberOfIterations;
   private int[][] population;
   private double crossoverProbability;
   private double mutationProbability;
   private int numberOfEliteIndividuals;

   private double totalTime;

   private Random random;

   protected abstract int[][] getInitialPopulation();
   protected abstract double getCrossoverProbability();
   protected abstract double getMutationProbability();
   protected abstract int[][] getParents(int[][] population, double[] individualsSolutionValues);
   protected abstract int[][] getChildsByCrossover(int[][] parents);
   protected abstract void mutate(int[] individual, int indexToMutate);
   protected abstract double calculateSolutionValue(int[] individual);
   protected abstract boolean isSolutionBetter(double solutionValue1, double solutionValue2);
   protected abstract int getNumberOfEliteIndividuals();
   protected abstract boolean isMinimizationProblem();

   public GeneticAlgorithm(int numberOfIterations){
      this.numberOfIterations = numberOfIterations;
      this.random = new Random();
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

      double[] individualsSolutionValues = null;
      int[][] iterationPopulation = new int[population.length][population[0].length];
      int[][] eliteIndividuals = null;
      int[][] parents = null;
      int[][] childs = null;
      double randomNumber = 0;

      initialTime = System.currentTimeMillis();

      for(int i = 0; i <= numberOfIterations - 1; i++){
         individualsSolutionValues = getIndividualsSolutionValues();

         for(int j = 0; j <= iterationPopulation.length - 1; j++){
            for(int k = 0; k <= iterationPopulation[0].length - 1; k++){
               iterationPopulation[j][k] = population[j][k];
            }
         }

         eliteIndividuals = getEliteIndividuals(individualsSolutionValues, numberOfEliteIndividuals);
         for(int j = 0; j <= eliteIndividuals.length - 1; j++){
            population[j] = eliteIndividuals[j];
         }

         for(int j = numberOfEliteIndividuals; j <= population.length - 1; j = j + 2){
            parents = getParents(iterationPopulation, individualsSolutionValues);

            randomNumber = getRandomNumber();
            if(randomNumber < crossoverProbability){
               childs = getChildsByCrossover(parents);
            }
            else{
               childs = parents;
            }

            for(int k = 0; k <= childs.length - 1; k++){
               for(int w = 0; w <= childs[0].length - 1; w++){
                  randomNumber = getRandomNumber();
                  if(randomNumber < mutationProbability){
                     mutate(childs[k], w);
                  }
               }
            }

            population[j] = childs[0];
            population[j + 1] = childs[1];
         }
      }

      finalTime = System.currentTimeMillis();
      setTotalTime(finalTime - initialTime);

      return getBestIndividualSolutionValue();
   }

   private void initGA(){
      this.population = getInitialPopulation();
      this.crossoverProbability = getCrossoverProbability();
      this.mutationProbability = getMutationProbability();
      this.numberOfEliteIndividuals = getNumberOfEliteIndividuals();
   }

   private double[] getIndividualsSolutionValues(){
      double[] individualsSolutionValues = new double[population.length];

      for(int i = 0; i <= individualsSolutionValues.length - 1; i++){
         individualsSolutionValues[i] = calculateSolutionValue(population[i]);
      }

      return individualsSolutionValues;
   }

   private double getRandomNumber(){
      return random.nextDouble();
   }

   private int[][] getEliteIndividuals(double[] individualsSolutionValues, int numberOfEliteIndividuals){
      int[][] eliteIndividuals = new int[numberOfEliteIndividuals][population[0].length];
      double[] sortedIndividualsSolutionValues = null;

      sortedIndividualsSolutionValues = Arrays.copyOf(individualsSolutionValues, individualsSolutionValues.length);
      Arrays.sort(sortedIndividualsSolutionValues);

      for(int i = 0; i <= eliteIndividuals.length - 1; i++){
         for(int j = 0; j <= individualsSolutionValues.length - 1; j++){
            if(isMinimizationProblem() == true){
               if(sortedIndividualsSolutionValues[i] == individualsSolutionValues[j]){
                  eliteIndividuals[i] = Arrays.copyOf(population[j], population[j].length);
                  break;
               }
            }
            else{
               if(sortedIndividualsSolutionValues[sortedIndividualsSolutionValues.length - 1 - i] == individualsSolutionValues[j]){
                  eliteIndividuals[i] = Arrays.copyOf(population[j], population[j].length);
                  break;
               }
            }
         }
      }

      return eliteIndividuals;
   }

   private double getBestIndividualSolutionValue(){
      double individualSolutionValue = 0;
      double bestIndividualSolutionValue = 0;

      for(int i = 0; i <= population.length - 1; i++){
         individualSolutionValue = calculateSolutionValue(population[i]);
         if(i == 0){
            bestIndividualSolutionValue = individualSolutionValue;
         }
         else{
            if(isSolutionBetter(individualSolutionValue, bestIndividualSolutionValue)){
               bestIndividualSolutionValue = individualSolutionValue;
            }
         }
      }

      return bestIndividualSolutionValue;
   }
}
