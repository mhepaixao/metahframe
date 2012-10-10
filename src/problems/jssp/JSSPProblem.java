package problems.jssp;

import instancereaders.JSSPInstanceReader;

import util.Edge;

import java.io.File;

/**
 * Class that describes the Job Sequence Scheduling Problem features.
 *
 * @author Matheus Paixao
 */
public class JSSPProblem{
   private JSSPInstanceReader jsspInstanceReader;

   private int numberOfJobs;
   private double[][] timesMatrix;

   /**
    * Method to create the JSSPProblem object.
    *
    * @author Matheus Paixao
    * @param instance the problem's instance
    * @see JSSPInstanceReader constructor
    * @see getTimesMatrix in JSSPInstanceReader class
    */
   public JSSPProblem(File instance){
      jsspInstanceReader = new JSSPInstanceReader(instance);
      this.timesMatrix = jsspInstanceReader.getTimesMatrix();
      this.numberOfJobs = timesMatrix.length;
   }

   public int getNumberOfJobs(){
      return this.numberOfJobs;
   }

   /**
    * Method that returns the fitness function of JSSP problem.
    *
    * @author Matheus Paixao
    * @param solution the Integer array that corresponds to the solution to be calculated
    * @return fitness value of the solution
    * @see getMakespan
    */
   public double calculateSolutionValue(Integer[] solution){
      return getMakespan(solution);
   }

   /**
    * Method that implements the fitness function of JSSP problem.
    *
    * Uses flowchart makespan algorithm.
    * @author Matheus Paixao
    * @param jobSequence the Integer array that corresponds to the sequence of jobs to be made
    * @return fitness value of the solution
    */
   private double getMakespan(Integer[] jobSequence){
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
    * Method to comprare if a solution value is better than another one.
    *
    * In JSSP as smaller fitness value as better.
    * @author Matheus Paixao
    * @param solution1
    * @param solution2
    * @return true if the first fitness value is better than the other one
    */
   public boolean isSolutionBest(double solution1, double solution2){
      boolean result = false;

      if(solution1 < solution2){
         result = true;
      }

      return result;
   }
}
