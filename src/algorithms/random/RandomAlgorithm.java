package algorithms.random;

import algorithms.Algorithm;

import java.util.Collections;
import java.util.ArrayList;
import java.util.Random;

/**
 * Class that implements a random search algorithm.
 *
 * dynamicListOfNodes is an ArrayList of integers that represents the nodes.
 * @author Matheus Paixao
 */
public abstract class RandomAlgorithm implements Algorithm{
   private int numberOfIterations;
   private double totalTime;

   private ArrayList<Integer> dynamicListOfNodes;
   private Random random;

   //abstract methods that each problem to be solved with this random algorithm must implement:
   public abstract double calculateSolutionValue(int[] solution); //fitness function value
   public abstract boolean isSolutionBest(double iterationSolutionValue, double bestSolutionValue); //depends on a max or min problem
   public abstract boolean satisfyAllRestrictions(int[] solution); //if the founded solution broke some restriction
   public abstract int getNumberOfNodes();
   
   /**
    * Method to create an RandomAlgorithm object passing the number of iterations
    * that it will run.
    *
    * @author Matheus Paixao
    * @see setTotalTime
    */
   public RandomAlgorithm(int numberOfIterations){
      this.numberOfIterations = numberOfIterations;
      setTotalTime(0);
      dynamicListOfNodes = new ArrayList<Integer>();
      random = new Random();
   }

   private int getNumberOfIterations(){
      return this.numberOfIterations;
   }

   public double getTotalTime(){
      return this.totalTime;
   }

   private void setTotalTime(double totalTime){
      this.totalTime = totalTime;
   }

   /**
    * Method to get the solution of the algorithm and to set the total time spended.
    *
    * @author Matheus Paixao
    * @return solution founded by the algorithm
    * @see loadDynamicListOfNodes
    * @see getNumberOfIterations
    * @see getIterationSolution
    * @see calculateSolutionValue
    * @see isSolutionBest
    */
   public double getSolution(){
      loadDynamicListOfNodes();

      double initialTime = 0;
      double finalTime = 0;

      int[] iterationSolution = null;
      double iterationSolutionValue = 0;
      int[] bestSolution = null;
      double bestSolutionValue = 0;

      int iterationsCounter = 0;

      initialTime = System.currentTimeMillis();
      while(iterationsCounter <= getNumberOfIterations() - 1){
         iterationSolution = getIterationSolution();
         iterationSolutionValue = calculateSolutionValue(iterationSolution);

         if(bestSolution != null){
            if(isSolutionBest(iterationSolutionValue, bestSolutionValue) == true){
               //System.out.println("found best solution");
               bestSolution = iterationSolution;
               bestSolutionValue = iterationSolutionValue;
            }
         }
         else{
            bestSolution = iterationSolution;
            bestSolutionValue = iterationSolutionValue;
         }

         iterationsCounter++;
      }
      finalTime = System.currentTimeMillis();

      setTotalTime(finalTime - initialTime);

      return bestSolutionValue;
   }

   /**
    * Method to create the list of nodes.
    *
    * @author Matheus Paixao
    */
   private void loadDynamicListOfNodes(){
      for(int i = 0; i <= getNumberOfNodes() - 1; i++){
         dynamicListOfNodes.add(new Integer(i));
      }
   }

   /**
    * Method to get the solution of an iteration.
    *
    * @author Matheus Paixao
    * @return the solution founded in an iteration
    * @see getRandomSolution
    * @see satisfyAllRestrictions
    */
   private int[] getIterationSolution(){
      int[] iterationSolution = getRandomSolution();

      while(satisfyAllRestrictions(iterationSolution) == false){
         iterationSolution = getRandomSolution();
      }

      return iterationSolution;
   }

   /**
    * Method to get a random solution.
    *
    * @author Matheus Paixao
    * @return a random solution
    * @see getNumberOfNodes
    * @see shuffle method of the Collections class
    */
   private int[] getRandomSolution(){
      int[] randomSolution = new int[getNumberOfNodes()];

      Collections.shuffle(dynamicListOfNodes);

      for(int i = 0; i <= randomSolution.length - 1; i++){
         randomSolution[i] = dynamicListOfNodes.get(i);
      }

      return randomSolution;
   }
}
