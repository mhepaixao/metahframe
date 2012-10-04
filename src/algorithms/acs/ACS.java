package algorithms.acs;

import algorithms.Algorithm;
import algorithms.acs.ACSAnt;

import java.util.ArrayList;
import java.util.Random;

public abstract class ACS implements Algorithm{
   private static final double q0 = 0.9;
   private static final double beta = 2;
   private static final double alpha = 0.1;
   private static final double rho = 0.1;

   private int numberOfIterations;
   private double totalTime;

   private Integer[] nodes;
   private double[][] pheromone;

   private ACSAnt[] ants;

   protected abstract int getNumberOfNodes();
   protected abstract int getNumberOfAnts();
   protected abstract double getInitialPheromone();

   public ACS(int numberOfIterations){
      setNumberOfIterations(numberOfIterations);
      setTotalTime(0);
   }

   private void setNumberOfIterations(int numberOfIterations){
      this.numberOfIterations = numberOfIterations;
   }

   private int getNumberOfIterations(){
      return this.numberOfIterations;
   }

   private double getQ0(){
      return this.q0;
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

      int[] iterationSolution = null;
      double iterationSolutionValue = 0;
      int[] bestSolution = null;
      double bestSolutionValue = 0;

      int iterationsCounter = 0;

      initACS();

      initialTime = System.currentTimeMillis();
      while(iterationsCounter < getNumberOfIterations()){
         iterationSolution = getIterationSolution();

         iterationsCounter++;
      }

      finalTime = System.currentTimeMillis();
      setTotalTime(finalTime - initialTime);

      return 0;
   }

   private void initACS(){
      initNodes();
      initPheromoneValues();
      initAnts();
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

   private void initAnts(){
      ants = new ACSAnt[getNumberOfAnts()];

      for(int i = 0; i <= ants.length - 1; i++){
         ants[i] = new ACSAnt(this, getQ0());
      }
   }

   private int[] getIterationSolution(){
      setAntsInitialNode(); 

      for(int i = 0; i <= ants.length - 1; i++){
         ants[i].loadNodesToVisit();
      }

      return new int[1];
   }

   private void setAntsInitialNode(){
      int randomInitialNode = 0;

      ArrayList<Integer> listToGetInitialRandomNode = new ArrayList<Integer>();
      fillListToGetInitialRandomNode(listToGetInitialRandomNode);

      for(int i = 0; i <= ants.length - 1; i++){
         randomInitialNode = getRandomInitialNode(listToGetInitialRandomNode);
         listToGetInitialRandomNode.remove(new Integer(randomInitialNode));
         ants[i].setInitialNode(randomInitialNode);
      }
   }

   private void fillListToGetInitialRandomNode(ArrayList<Integer> listToGetInitialRandomNode){
      for(int i = 0; i <= nodes.length - 1; i++){
         listToGetInitialRandomNode.add(new Integer(i));
      }
   }

   private int getRandomInitialNode(ArrayList<Integer> listToGetInitialRandomNode){
      Random random = new Random();
      int randomIndex = random.nextInt(listToGetInitialRandomNode.size());
      int randomInitialNode = listToGetInitialRandomNode.get(randomIndex);

      return randomInitialNode;
   }
}
