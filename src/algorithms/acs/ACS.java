package algorithms.acs;

import algorithms.Algorithm;

public abstract class ACS implements Algorithm{
   private static final double q0 = 0.9;
   private static final double beta = 2;
   private static final double alpha = 0.1;
   private static final double rho = 0.1;

   private int numberOfIterations;
   private double totalTime;

   private Integer[] nodes;
   private double[][] pheromone;

   protected abstract int getNumberOfNodes();
   protected abstract double getInitialPheromone();

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

   public double getTotalTime(){
      return this.totalTime;
   }
   
   public double getSolution(){
      double initialTime = 0;
      double finalTime = 0;

      initACS();

      initialTime = System.currentTimeMillis();
      finalTime = System.currentTimeMillis();
      setTotalTime(finalTime - initialTime);

      return 0;
   }

   private void initACS(){
      initNodes();
      initPheromoneValues();
   }

   private void initNodes(){
      nodes = new Integer[getNumberOfNodes()];

      for(int i = 0; i <= nodes.length - 1; i++){
         nodes[i] = i;
      }
   }

   private void initPheromoneValues(){
      pheromone = new double[nodes.length][nodes.length];
      double initialPheromone = getInitialPheromone();

      for(int i = 0; i <= pheromone.length - 1; i++){
         for(int j = 0; j <= pheromone[0].length - 1; j++){
            if(i != j){
               pheromone[i][j] = initialPheromone;
            }
         }
      }
   }
}
