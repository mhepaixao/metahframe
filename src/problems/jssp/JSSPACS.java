package problems.jssp;

import algorithms.acs.ACS;
import algorithms.acs.ACSAnt;

public class JSSPACS extends ACS{
   JSSPProblem jsspProblem;

   public JSSPACS(JSSPProblem jsspProblem, int numberOfIterations){
      super(numberOfIterations);
      this.jsspProblem = jsspProblem;
   }

   public boolean isSolutionBest(double iterationSolutionValue, double bestSolutionValue){
      return jsspProblem.isSolutionBest(iterationSolutionValue, bestSolutionValue);
   }

   public int getNumberOfNodes(){
      return jsspProblem.getNumberOfJobs();
   }

   public double calculateSolutionValue(Integer[] solution){
      return jsspProblem.calculateSolutionValue(solution);
   }

   public double getHeuristicValue(int node1, int node2){
      Integer[] jobSequence = getJobSequence(node1, node2, getCurrentAnt());

      return 1 / jsspProblem.calculateSolutionValue(jobSequence);
   }

   private Integer[] getJobSequence(int node1, int node2, ACSAnt ant){
      Integer[] tour = getCurrentAnt().getTour();
      Integer[] jobSequence = new Integer[getNumberOfJobs(tour) + 1];

      for(int i = 0; i <= jobSequence.length - 2; i++){
         jobSequence[i] = tour[i];
      }
      jobSequence[jobSequence.length - 1] = node2;

      return jobSequence;
   }

   private int getNumberOfJobs(Integer[] solution){
      int numberOfJobs = 0;

      for(int i = 0; i <= solution.length - 1; i++){
         if(solution[i] != null){
            numberOfJobs++;
         }
      }

      return numberOfJobs;
   }

   public double getInitialPheromone(){
      return 0.01;
   }

   public int getNumberOfAnts(){
      return 10;
   }

   public double getRho(){
      return 0.1;
   }

   public double getAlpha(){
      return 0.1;
   }

   public double getBeta(){
      return 2;
   }

   public double getQ0(){
      return 0.9;
   }
}
