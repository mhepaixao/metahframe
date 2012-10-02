package algorithms.acs;

import algorithms.Algorithm;

public class ACS implements Algorithm{
   private int numberOfIterations;
   private double totalTime;

   public ACS(int numberOfIterations){
      setNumberOfIterations(numberOfIterations);
      setTotalTime(0);
   }

   private void setNumberOfIterations(int numberOfIterations){
      this.numberOfIterations = numberOfIterations;
   }

   private void setTotalTime(double totalTime){
      this.totalTime = totalTime;
   }

   public double getSolution(){
      return 0;
   }

   public double getTotalTime(){
      return this.totalTime;
   }
}
