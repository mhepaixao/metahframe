package algorithms.acs;

import algorithms.Algorithm;
import algorithms.acs.ACSAnt;

import java.util.ArrayList;
import java.util.Random;

/**
 * Class that implements the Ant Colony System algorithm.
 *
 * The initialization constants are adjusted by each the problem.
 * They are used to calculate the action choice value (beta), 
 * to do the local update (rho), to do the global update (alpha),
 * and to make a exploration or a exploitation choice (q0).
 *
 * The nodes array stores all the nodes of the instance.
 *
 * The pheromone matrix stores the pheromones values in each edge of the graph.
 *
 * The ants array stores the ants that are acting in the algorithm.  
 *
 * @author Matheus Paixao
 */
public abstract class ACS implements Algorithm{
   //Initialization Constants
   private double q0;
   private double beta;
   private double alpha;
   private double rho;

   private Integer[] nodes;
   private double[][] pheromone;

   protected ACSAnt[] ants;
   protected ACSAnt currentAnt;

   private int numberOfIterations;
   private double totalTime;

   //abstract methods that each problem to be solved with ACS must implement:
   
   //methods to get the initialization constants values
   protected abstract double getQ0();
   protected abstract double getAlpha();
   protected abstract double getBeta();
   protected abstract double getRho();

   protected abstract int getNumberOfNodes();
   protected abstract int getNumberOfAnts();
   protected abstract double getInitialPheromone();
   protected abstract double getHeuristicValue(int node1, int node2);
   protected abstract double calculateSolutionValue(Integer[] solution); //problem's fitness function
   protected abstract boolean isSolutionBest(double solutionValue, double bestSolutionValue); //depends on max or min problem

   /**
    * Method to create an ACS object passing the number of iterations
    * that it will run.
    *
    * @author Matheus Paixao
    * @param numberOfIterations number of iterations that the algorithm will run
    * @see setNumberOfIterations
    * @see setTotalTime
    */
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
   
   /**
    * Method to get the solution of the algorithm and to set the total time spended.
    *
    * @author Matheus Paixao
    * @return solution founded by the algorithm
    * @see initACS
    * @see getNumberOfIterations
    * @see getIterationSolution
    * @see calculateSolutionValue
    * @see isSolutionBest
    */
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

         //System.out.println("iteration "+ iterationsCounter + " -> " + iterationSolutionValue);

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

   /**
    * Method to initialize the ACS algorithm.
    *
    * @author Matheus Paixao
    * @see getQ0
    * @see getBeta
    * @see getAlpha
    * @see getRho
    * @see initNodes
    * @see initPheromoneValues
    * @see initAnts
    */
   private void initACS(){
      q0 = getQ0();
      beta = getBeta();
      alpha = getAlpha();
      rho = getRho();

      initNodes();
      initPheromoneValues();
      initAnts();
   }

   /**
    * Method to init the nodes array.
    *
    * @author Matheus Paixao
    * @see getNumberOfNodes
    */
   private void initNodes(){
      nodes = new Integer[getNumberOfNodes()];

      for(int i = 0; i <= nodes.length - 1; i++){
         nodes[i] = i;
      }
   }

   /**
    * Method to set the initial pheromone value for each edge.
    *
    * When 'i' is equal to 'j' there is no edge, so the pheromone value is 0.
    * @author Matheus Paixao
    * @see getInitialPheromone
    */
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

   /**
    * Method to init the ants array.
    *
    * @author Matheus Paixao
    * @see getNumberOfAnts
    * @see ACSAnt constructor in ACSAnt class.
    */
   private void initAnts(){
      ants = new ACSAnt[getNumberOfAnts()];

      for(int i = 0; i <= ants.length - 1; i++){
         ants[i] = new ACSAnt(this, q0);
      }
   }

   /**
    * Method to get the solution of an iteration, method where the ACS algorithm is runned.
    *
    * @author Matheus Paixao
    * @return the best solution founded in an iteration
    * @see setAntsInitialNode
    * @see setCurrentAnt
    * @see getCurrentAnt
    * @see isTourFinished in ACSAnt class
    * @see chooseNextNode in ACSAnt class
    * @see setNextNode in ACSAnt class
    * @see getNextNode in ACSAnt class
    * @see addNodeToTour in ACSAnt class
    * @see getTour in ACSAnt class
    * @see localUpdate
    * @see loadNodesToVisit in ACSAnt class
    * @see setCurrentNode in ACSAnt class
    * @see removeNodeFromNodesToVisit in ACSAnt class
    * @see getIterationBestSolution
    * @see calculateSolutionValue
    * @see clearTour in ACSAnt class
    * @see globalUpdate
    */
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

            localUpdate(ant);

            ant.setCurrentNode(ant.getNextNode()); //move to the next choosed node

            if(ant.isTourFinished() == false){
               ant.removeNodeFromNodesToVisit(ant.getCurrentNode()); // remove the current node from the nodes to visit
            }

            if(ant.isTourFinished() == true){
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

   /**
    * Method to randomly set the initial node to each ant.
    *
    * @author Matheus Paixao
    * @see initListToGetInitialRandomNode
    * @see getRandomInitialNode
    * @see setInitialNode in ACSAnt class
    * @see addNodeToTour in ACSAnt class
    */
   private void setAntsInitialNode(){
      ACSAnt ant = null;
      int randomInitialNode = 0;

      ArrayList<Integer> listToGetInitialRandomNode = new ArrayList<Integer>();
      initListToGetInitialRandomNode(listToGetInitialRandomNode);

      for(int i = 0; i <= ants.length - 1; i++){
         ant = ants[i];

         randomInitialNode = getRandomInitialNode(listToGetInitialRandomNode);
         listToGetInitialRandomNode.remove(new Integer(randomInitialNode));
         ant.setInitialNode(randomInitialNode);
         ant.addNodeToTour(ant.getInitialNode());
      }
   }

   /**
    * Method to init the list to randomly get the initial node for each ant.
    *
    * @author Matheus Paixao
    * @param listToGetInitialRandomNode the dynamic list used to randomly choose the node
    */
   private void initListToGetInitialRandomNode(ArrayList<Integer> listToGetInitialRandomNode){
      for(int i = 0; i <= nodes.length - 1; i++){
         listToGetInitialRandomNode.add(new Integer(i));
      }
   }

   /**
    * Method to randomly get an initial node of the dynamic list.
    *
    * @author Matheus Paixao
    * @param listToGetInitialRandomNode the dynamic list used to randomly choose the node
    */
   private int getRandomInitialNode(ArrayList<Integer> listToGetInitialRandomNode){
      Random random = new Random();
      int randomIndex = random.nextInt(listToGetInitialRandomNode.size());
      int randomInitialNode = listToGetInitialRandomNode.get(randomIndex);

      return randomInitialNode;
   }

   /**
    * Method to get the action choice of an edge.
    *
    * @author Matheus Paixao
    * @param node1 the first node of the edge 
    * @param node2 the second node of the edge 
    * @return the action choice of the edge
    */
   public double getActionChoice(int node1, int node2){
      double actionChoice =  pheromone[node1][node2] * Math.pow(getHeuristicValue(node1, node2), beta);

      if((Double.isNaN(actionChoice)) || (Double.POSITIVE_INFINITY == actionChoice) || (Double.NEGATIVE_INFINITY == actionChoice)){
         actionChoice = 0;
      }

      return actionChoice;
   }

   /**
    * Method to get the sum of action choices of all remaining nodes to visit of an ant.
    *
    * @author Matheus Paixao
    * @param currentNode the current node of the ant
    * @param nodesToVisit array of the nodes still to be visited by the ant
    * @return the sum of action choices of all remaining nodes to visit of an ant.
    * @see getActionChoice
    */
   public double getActionChoiceSum(int currentNode, Integer nodesToVisit[]){
      double actionChoiceSum = 0;

      for(int i = 0; i <= nodesToVisit.length - 1; i++){
         if(nodesToVisit[i] != null){
            actionChoiceSum += getActionChoice(currentNode, nodesToVisit[i]);
         }
      }

      return actionChoiceSum;
   }

   /**
    * Method to do the local pheromone update of ACS algorithm.
    *
    * @param tour the tour done by the ant
    */
   private void localUpdate(ACSAnt ant){
      Integer[] tour = ant.getTour();
      int lastNode = 0;
      int previousNode = 0;

      if(ant.isTourFinished() == true){ 
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

   /**
    * Method to get the best solution, of all ants, of an iteration.
    *
    * It's used an temporary array to create a new array with the same elements. 
    * @author Matheus Paixao
    * @return the iteration best solution
    * @see getTour in Ant class
    * @see calculateSolutionValue
    */
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

   /**
    * Method to do the global pheromone update of ACS algorithm.
    *
    * It updates the pheromone of all edges in the iterationSolution
    * @param iterationSolution the solution of an iteration
    * @param reinforcementLearningValue reinforcemente value proportional to the quality of the solution founded
    */
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
