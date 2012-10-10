package problems.jssp;

import instancereaders.JSSPInstanceReader;

import util.Edge;

import java.io.File;

public class JSSPProblem{
   private JSSPInstanceReader jsspInstanceReader;

   private int numberOfJobs;
   private double[][] timesMatrix;

   public JSSPProblem(File instance){
      jsspInstanceReader = new JSSPInstanceReader(instance);
      this.timesMatrix = jsspInstanceReader.getTimesMatrix();
      this.numberOfJobs = timesMatrix.length;
   }

   public int getNumberOfJobs(){
      return this.numberOfJobs;
   }

   public double calculateSolutionValue(Integer[] solution){
      return getMakespan(solution);
   }

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

   public boolean isSolutionBest(double solution1, double solution2){
      boolean result = false;

      if(solution1 < solution2){
         result = true;
      }

      return result;
   }
}
