package problems.jssp;

import algorithms.random.RandomAlgorithm;
import instancereaders.JSSPInstanceReader;

import java.io.File;

/**
 * Class to implement the RandomAlgorithm class to the Job Sequence Schedule Problem.
 *
 * @author Matheus Paixao
 */
public class JSSPRandomAlgorithm extends RandomAlgorithm{
   private JSSPInstanceReader jsspInstanceReader;

   private int numberOfJobs;
   private double[][] timesMatrix;

   /**
    * Method to create the JSSPRandomAlgorithm object, receive the instance to read and
    * the number of iterations is passed to RandomAlgorithm constructor.
    *
    * @author Matheus Paixao
    * @param instance the instance to read
    * @param numberOfIterations number of iterations to be runned
    * @see RandomAlgorithm constructor
    * @see JSSPInstanceReader constructor
    * @see getTimesMatrix in JSSPInstanceReader
    */
   public JSSPRandomAlgorithm(File instance, int numberOfIterations){
      super(numberOfIterations);
      jsspInstanceReader = new JSSPInstanceReader(instance);
      this.timesMatrix = jsspInstanceReader.getTimesMatrix();
      this.numberOfJobs = timesMatrix.length;
   }

   public int getNumberOfNodes(){
      return this.numberOfJobs; //in JSSP the jobs are represented by the nodes
   }

   /**
    * Method to compare if a solution value is better than another one.
    *
    * In JSSP as smaller fitness value as better.
    * @author Matheus Paixao
    * @param iterationSolutionValue the fitness value of some solution
    * @param bestSolutionValue the best fitness value of an iteration
    * @return true if the first fitness value is best than the other one
    */
   public boolean isSolutionBest(double iterationSolutionValue, double bestSolutionValue){
      boolean result = false;

      if(iterationSolutionValue < bestSolutionValue){
         result = true;
      }

      return result;
   }

   /**
    * Method that implements the fitness function of JSSP problem.
    *
    * @author Matheus Paixao
    * @param solution the array of int that corresponds to the solution founded by the algorithm
    * @return fitness value (makespan) of the solution
    * @see getMakespan
    */
   public double calculateSolutionValue(int[] solution){
      return getMakespan(solution);
   }

   /**
    * Method to calculate the makespan of a sequence of jobs (fitness function).
    *
    * For more information about this algorithm, search for "JSSP flowchart makespan".
    * @author Matheus Paixao
    * @param jobSequence sequence of jobs to calculate the makespan
    * @return the makespan value of the job sequence
    */
   private double getMakespan(int[] jobSequence){
      double[] makespan = new double[timesMatrix[0].length];
      int job = 0;

      for(int i = 0; i <= jobSequence.length - 1; i++){
         job = jobSequence[i];
         makespan[0] = makespan[0] + timesMatrix[job][0];
         for(int j = 1; j <= timesMatrix[0].length - 1; j++){
            if(makespan[j] > makespan[j - 1]){
               makespan[j] = makespan[j] + timesMatrix[job][j];
            }
            else{
               makespan[j] = makespan[j - 1] + timesMatrix[job][j];
            }
         }
      }

      return makespan[timesMatrix[0].length - 1];
   }

   /**
    * Method to test if the founded solution broke some restriction.
    *
    * In JSSP there's no job sequence restriction.
    * @author Matheus Paixao
    */
   public boolean satisfyAllRestrictions(int[] solution){
      return true;
   }
}
