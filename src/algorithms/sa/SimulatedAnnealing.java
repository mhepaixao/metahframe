package algorithms.sa;

import algorithms.Algorithm;

import java.util.Random;

/**
 * Class that implements the Simulated Annealing algorithm.
 *
 * The initialization constants are adjusted by each the problem.
 *
 * The final temperature is the stop criteria.
 *
 * The temperature is the exploitation/exploration parameter control.
 *
 * Alpha is the cooling parameter.
 *
 * Number of Markov Iterations define the number of neighbours tested in each iteration.
 *
 * The algorihtm stores the best so far solution.
 *
 * @author Matheus Paixao
 */
public abstract class SimulatedAnnealing implements Algorithm{
   private double finalTemperature;
   private double temperature;
   private double alpha;
   private int numberOfMarkovChains;
   private int[] bestSolution;
   private int[] bestSoFarSolution;

   //all these parameters and functions depends on the problem
   //they must be implemented by the problem child class
   protected abstract double getInitialTemperature();
   protected abstract double getFinalTemperature();
   protected abstract double getAlpha();
   protected abstract int getNumberOfMarkovChains();
   protected abstract int[] getInitialSolution();
   protected abstract int[] getNeighbourSolution(int[] solution);
   protected abstract double calculateSolutionValue(int[] solution);
   protected abstract boolean isSolutionBest(double solutionValue1, double solutionValue2);

   private Random random;

   private double totalTime;

   /**
    * Method to create an SimulatedAnnealing object
    *
    * @author Matheus Paixao
    */
   public SimulatedAnnealing(){
      this.random = new Random();
   }

   public double getTotalTime(){
      return this.totalTime;
   }

   public void setTotalTime(double totalTime){
      this.totalTime = totalTime;
   }

   /**
    * Method to get the solution of the algorithm and to set the total time spended.
    *
    * @author Matheus Paixao
    * @return solution founded by the algorithm
    * @see initSA
    * @see getNeighbourSolution
    * @see calculateSolutionValue
    * @see isSolutionBest
    * @see getAcceptanceProbability
    * @see setTotalTime
    */
   public double getSolution(){
      int[] neighbourSolution = null;
      double bestSolutionValue = 0;
      double neighbourSolutionValue = 0;
      double acceptanceProbability = 0;

      double initialTime = 0;
      double finalTime = 0 ;

      initSA();

      initialTime = System.currentTimeMillis();
      while(temperature > finalTemperature){
         for(int i = 0; i <= numberOfMarkovChains - 1; i++){
            neighbourSolution = getNeighbourSolution(bestSolution);

            bestSolutionValue = calculateSolutionValue(bestSolution);
            neighbourSolutionValue = calculateSolutionValue(neighbourSolution);

            if(isSolutionBest(neighbourSolutionValue, bestSolutionValue)){
               bestSolution = neighbourSolution;

               if(isSolutionBest(bestSolutionValue, calculateSolutionValue(bestSoFarSolution))){
                  bestSoFarSolution = bestSolution;
               }
            }
            else{
               acceptanceProbability = getAcceptanceProbability(bestSolutionValue, neighbourSolutionValue);
               if(acceptanceProbability > random.nextDouble()){
                  bestSolution = neighbourSolution;
               }
            }
         }

         updateTemperature();
      }
      finalTime = System.currentTimeMillis();

      setTotalTime(finalTime - initialTime);
      return calculateSolutionValue(bestSoFarSolution);
   }

   /**
    * Method to initialize the SimulatedAnnealing algorithm.
    *
    * @author Matheus Paixao
    * @see getInitialTemperature
    * @see getFinalTemperature
    * @see getAlpha
    * @see getNumberOfMarkovChains
    * @see getInitialSolution
    */
   private void initSA(){
      temperature = getInitialTemperature();
      finalTemperature = getFinalTemperature();
      alpha = getAlpha();
      numberOfMarkovChains = getNumberOfMarkovChains();
      bestSolution = getInitialSolution();
      bestSoFarSolution = getInitialSolution();
   }

   /**
    * Method to get the probability to accept a worse solution that the current solution.
    *
    * @author Matheus Paixao
    */
   private double getAcceptanceProbability(double bestSolutionValue, double neighbourSolutionValue){
      return Math.exp((bestSolutionValue - neighbourSolutionValue) / temperature);
   }

   /**
    * Method to update the temperature.
    *
    * The temperature is always decreased by the alpha parameter.
    * @author Matheus Paixao
    */
   private void updateTemperature(){
      temperature = temperature * alpha;
   }
}
