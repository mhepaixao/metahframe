package algorithms.acs;

import algorithms.Algorithm;
import algorithms.acs.ACSAnt;

import java.util.ArrayList;
import java.util.Random;

public abstract class ACS implements Algorithm{
   private double q0;
   private double beta;
   private double alpha;
   private double rho;

   private int numberOfIterations;
   private double totalTime;

   private Integer[] nodes;
   private double[][] pheromone;

   protected ACSAnt[] ants;
   protected ACSAnt currentAnt;

   protected abstract double getQ0();
   protected abstract double getAlpha();
   protected abstract double getBeta();
   protected abstract double getRho();

   protected abstract int getNumberOfNodes();
   protected abstract int getNumberOfAnts();
   protected abstract double getInitialPheromone();
   protected abstract double getHeuristicValue(int node1, int node2);
   protected abstract double calculateSolutionValue(Integer[] solution);
   protected abstract boolean isSolutionBest(double solutionValue, double bestSolutionValue);

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

   private void setTotalTime(double totalTime){
      this.totalTime = totalTime;
   }

   public double getTotalTime(){
      return this.totalTime;
   }

   private void setCurrentAnt(ACSAnt currentAnt){
      this.currentAnt = currentAnt;
   }

   private ACSAnt getCurrentAnt(){
      return this.currentAnt;
   }
   
   public double getSolution(){
      double initialTime = 0;
      double finalTime = 0;

      Integer[] iterationSolution = null;
      double iterationSolutionValue = 0;
      Integer[] bestSolution = null;
      double bestSolutionValue = 0;

      int iterationsCounter = 0;

      initACS();

      initialTime = System.currentTimeMillis();
      while(iterationsCounter < getNumberOfIterations()){
         iterationSolution = getIterationSolution();
         iterationSolutionValue = calculateSolutionValue(iterationSolution);

         //System.out.println("iteration: "+iterationsCounter);

         if(bestSolution == null){
            bestSolution = iterationSolution;
            bestSolutionValue = iterationSolutionValue;
         }
         else{
            if(isSolutionBest(iterationSolutionValue, bestSolutionValue) == true){
               //System.out.println("found best solution");
               bestSolution = iterationSolution;
               bestSolutionValue = iterationSolutionValue;
            }
         }

         iterationsCounter++;
      }

      finalTime = System.currentTimeMillis();
      setTotalTime(finalTime - initialTime);

      return bestSolutionValue;
   }

   private void initACS(){
      q0 = getQ0();
      beta = getBeta();
      alpha = getAlpha();
      rho = getRho();

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

   private Integer[] getIterationSolution(){
      setAntsInitialNode(); 

      Integer[] iterationSolution = null;
      double iterationSolutionValue = 0;

      ACSAnt ant = null;
      int nextNode = 0;
      double reinforcementLearningValue = 0;

      for(int i = 1; i <= nodes.length - 1; i++){
         for(int j = 0; j <= ants.length - 1; j++){
            setCurrentAnt(ants[j]);

            ant = getCurrentAnt();

            if(ant.isTourFinished() == false){
               nextNode = ant.chooseNextNode();
               ant.setNextNode(nextNode);
               ant.addNodeToTour(ant.getNextNode());
            }
         }

         for(int j = 0; j <= ants.length - 1; j++){
            setCurrentAnt(ants[j]);

            ant = getCurrentAnt();

            localUpdate(ant.getTour());

            ant.setCurrentNode(ant.getNextNode()); //move to the next choosed node
            if(ant.isTourFinished() == false){
               ant.removeNodeFromNodesToVisit(ant.getCurrentNode()); // remove the current node from the nodes to visit
            }

            if(i == nodes.length - 1){
               ant.loadNodesToVisit();
            }

         }
      }

      iterationSolution = getIterationBestSolution();
      iterationSolutionValue = calculateSolutionValue(iterationSolution);

      for(int i = 0; i <= ants.length - 1; i++){
         ants[i].clearTour();
      }

      reinforcementLearningValue = Math.pow(iterationSolutionValue, -1);
      globalUpdate(iterationSolution, reinforcementLearningValue);

      return iterationSolution;
   }

   private void setAntsInitialNode(){
      int randomInitialNode = 0;

      ArrayList<Integer> listToGetInitialRandomNode = new ArrayList<Integer>();
      fillListToGetInitialRandomNode(listToGetInitialRandomNode);

      for(int i = 0; i <= ants.length - 1; i++){
         randomInitialNode = getRandomInitialNode(listToGetInitialRandomNode);
         listToGetInitialRandomNode.remove(new Integer(randomInitialNode));
         ants[i].setInitialNode(randomInitialNode);
         ants[i].addNodeToTour(ants[i].getInitialNode());
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

   public double getActionChoice(int node1, int node2){
      double actionChoice =  pheromone[node1][node2] * Math.pow(getHeuristicValue(node1, node2), beta);

      if((Double.isNaN(actionChoice)) || (Double.POSITIVE_INFINITY == actionChoice) || (Double.NEGATIVE_INFINITY == actionChoice)){
         actionChoice = 0;
      }

      return actionChoice;
   }

   public double getActionChoiceSum(int currentNode, Integer nodesToVisit[]){
      double actionChoiceSum = 0;

      for(int i = 0; i <= nodesToVisit.length - 1; i++){
         if(nodesToVisit[i] != null){
            actionChoiceSum += getActionChoice(currentNode, nodesToVisit[i]);
         }
      }

      return actionChoiceSum;
   }

   private void localUpdate(Integer[] tour){
      int lastNode = 0;
      int previousNode = 0;

      if(tour[tour.length - 1] != null){
         lastNode = tour[tour.length - 1];
         previousNode = tour[tour.length - 2];
      }
      else{
         for(int i = 0; i <= tour.length - 1; i++){
            if(tour[i] == null){
               lastNode = tour[i - 1];
               previousNode = tour[i - 2];
               break;
            }
         }
      }
      
      pheromone[previousNode][lastNode] = ((1 - rho) * pheromone[previousNode][lastNode]) + (rho * getInitialPheromone());
   }

   private Integer[] getIterationBestSolution(){
      Integer iterationBestSolutionTemp[] = ants[0].getTour();
      Integer iterationBestSolution[] = new Integer[iterationBestSolutionTemp.length];
      Integer solution[] = null;
      double iterationBestSolutionValue = calculateSolutionValue(iterationBestSolutionTemp);
      double solutionValue = 0;

      for(int i = 0; i <= ants.length - 1; i++){
         solution = ants[i].getTour();
         solutionValue = calculateSolutionValue(solution);
         if(isSolutionBest(solutionValue, iterationBestSolutionValue) == true){
            iterationBestSolutionValue = solutionValue;
            iterationBestSolutionTemp = solution;
         }
      }

      for(int i = 0; i <= iterationBestSolutionTemp.length - 1; i++){
         iterationBestSolution[i] = iterationBestSolutionTemp[i];
      }

      return iterationBestSolution;
   }

   private void globalUpdate(Integer[] iterationSolution, double reinforcementLearningValue){
      int lastNode = 0;
      int previousNode = 0;

      for(int i = 1; i <= iterationSolution.length - 1; i++){
         lastNode = iterationSolution[i];
         previousNode = iterationSolution[i - 1];

         pheromone[previousNode][lastNode] = ((1 - alpha) * pheromone[previousNode][lastNode]) + (alpha * reinforcementLearningValue);
      }
      lastNode = iterationSolution[iterationSolution.length - 1];
      previousNode = iterationSolution[0];

      pheromone[previousNode][lastNode] = ((1 - alpha) * pheromone[previousNode][lastNode]) + (alpha * reinforcementLearningValue);
   }
}
