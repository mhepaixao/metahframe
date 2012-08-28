import java.io.File;

/**
 * Class to implement the RandomAlgorithm class to the Travel Salesman Problem.
 *
 * @author Matheus Paixao
 */
public class TSPRandomAlgorithm extends RandomAlgorithm{
   private TSPInstanceReader tspInstanceReader;

   private int numberOfCities;
   private double[][] distancesMatrix;

   /**
    * Method to create the TSPRandomAlgorithm object, receive the instance to read and
    * the number of iterations is passed to the TSPRandomAlgorithm constructor.
    *
    * @author Matheus Paixao
    * @param instance the instance to read
    * @param numberOfIterations number of iterations to be runned
    * @see RandomAlgorithm constructor
    * @see getDistancesMatrix in TSPInstanceReader
    */
   public TSPRandomAlgorithm(File instance, int numberOfIterations){
      super(numberOfIterations);
      this.tspInstanceReader = new TSPInstanceReader(instance);
      this.distancesMatrix = tspInstanceReader.getDistancesMatrix();
      this.numberOfCities = distancesMatrix.length; 
   }

   public int getNumberOfNodes(){
      return this.numberOfCities;
   }

   /**
    * Method that implements the fitness function of TSP problem.
    *
    * @author Matheus Paixao
    * @param solution the array of int that corresponds to the solution founded by the algorithm
    * @return fitness value of the solution
    */
   public double calculateSolutionValue(int[] solution){
      double solutionValue = 0;

      for(int i = 0; i <= solution.length - 2; i++){
         solutionValue += distancesMatrix[solution[i]][solution[i + 1]];
      }
      solutionValue += distancesMatrix[solution.length - 1][0];

      return solutionValue;
   }

   /**
    * Method to comprare if a solution value is better than another one.
    *
    * In TSP as smaller fitness value as better.
    * @author Matheus Paixao
    * @param iterationSolutionValue the fitness value of some solution
    * @param bestSolutionValue the best fitness value of an iteration
    * @return true if the first fitness value is best than the other one
    */
   public boolean isSolutionBest(double iterationSolutionValue, double bestSolutionValue){
      boolean result =  false;

      if(iterationSolutionValue < bestSolutionValue){
         result = true;
      }

      return result;
   }
}
