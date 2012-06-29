/**
 * Class that implements the Ant Q algorithm.
 *
 * The constants are the initialization parameters of the algorithm.
 * They are used to calculate the action choice value (delta and beta), 
 * to update the AQ Value (alfa and gamma), to make a exploration or a exploitation choice (q0) 
 * and to calculate the reinforcement learning value (w).
 *
 * The nodes array stores all the nodes of the instance.
 *
 * The edges matrix stores all the edges of the instance (complete graph).
 * 
 * The actionChoices matrix stores the action choices of all edges.
 *
 * The ants array stores the ants, or ants, that are acting in the algorithm.  
 *
 * @author Matheus Paixao
 */
public abstract class AntQ implements Algorithm{
   //constant initialization parameters
   private static final double delta = 1;
   private static final double beta = 2;
   private static final double alfa = 0.1;
   //private static final double gamma = 0.3;
   private static final double gamma = 1;
   private static final double q0 = 0.9;
   //private static final double w = 10.0;
   private static final double w = 0.000000001;

   private int numberOfIterations;
   private double totalTime;

   private Node[] nodes;
   private double[][] pheromone;

   private Edge[][] edges;

   protected Ant[] ants;
   protected Ant currentAnt;

   //abstract methods:
   public abstract int getNumberOfNodes();
   public abstract double getInitialPheromone();
   public abstract double getHeuristicValue(Node node1, Node node2);
   public abstract double calculateSolutionValue(Edge[] solution);
   public abstract boolean isSolutionBest(double iterationSolutionValue, double bestSolutionValue);

   public AntQ(int numberOfIterations){
      setNumberOfIterations(numberOfIterations);
      setTotalTime(0);
   }

   public double getQ0(){
      return this.q0;
   }

   public int getNumberOfIterations(){
      return this.numberOfIterations;
   }

   public void setNumberOfIterations(int numberOfIterations){
      this.numberOfIterations = numberOfIterations;
   }

   public double getTotalTime(){
      return this.totalTime;
   }

   public void setTotalTime(double totalTime){
      this.totalTime = totalTime;
   }

   public Node[] getNodes(){
      return this.nodes;
   }

   public Edge[][] getEdges(){
      return this.edges;
   }

   protected Ant getCurrentAnt(){
      return this.currentAnt;
   }

   private void setCurrentAnt(Ant ant){
      this.currentAnt = ant;
   }

   public double getSolution(){
      double initialTime = 0;
      double finalTime = 0;

      Edge[] iterationSolution = null;
      double iterationSolutionValue = 0;
      Edge[] bestSolution = null;
      double bestSolutionValue = 0;

      int iterationsCounter = 0;

      initAntQ();

      initialTime = System.currentTimeMillis();
      while(iterationsCounter <= getNumberOfIterations() - 1){
         iterationSolution = getIterationSolution();

         iterationSolutionValue = calculateSolutionValue(iterationSolution);
         System.out.println("iteration "+ iterationsCounter+ " solution value: "+iterationSolutionValue);

         if(bestSolution != null){
            if(isSolutionBest(iterationSolutionValue, bestSolutionValue) == true){
               System.out.println("found best solution");
               bestSolution = iterationSolution;
               bestSolutionValue = iterationSolutionValue;
            }
         }
         else{
            bestSolution = iterationSolution;
            bestSolutionValue = iterationSolutionValue;
         }

         iterationsCounter++;
      }
      finalTime = System.currentTimeMillis();

      setTotalTime(finalTime - initialTime);

      return bestSolutionValue;
   }

   /**
    * Method to initialize the AntQ algorithm.
    *
    * @author Matheus Paixao
    * @see createNodes
    * @see createEdges
    * @see getNumberOfNodes
    * @see getInitialPheromone
    * @see initAnts
    */
   private void initAntQ(){
      createNodes();
      createEdges();

      pheromone = new double[getNumberOfNodes()][getNumberOfNodes()];
      initPheromoneValues(getInitialPheromone());

      initAnts();
   }

   private void createNodes(){
      this.nodes = new Node[getNumberOfNodes()];

      for(int i = 0; i <= nodes.length - 1; i++){
         this.nodes[i] = new Node(i);
      }
   }

   private void createEdges(){
      edges = new Edge[nodes.length][nodes.length];

      for(int i = 0; i <= edges.length - 1; i++){
         for(int j = 0; j <= edges[0].length - 1; j++){
            edges[i][j] = new Edge(nodes[i], nodes[j]);
         }
      }
   }

   /**
    * Method to set the initial pheromone value for each edge.
    *
    * When 'i' is equal to 'j' there is no edge, so the pheromone value is 0.
    * @author Matheus Paixao
    * @param initialPheromone the initial pheromone value for all edges.
    */
   private void initPheromoneValues(double initialPheromone){
      for(int i = 0; i <= pheromone.length - 1; i++){
         for(int j = 0; j <= pheromone[0].length - 1; j++){
            if(i != j){
               pheromone[i][j] = initialPheromone;
            }
         }
      }
   }

   /**
    * Method to init the ants.
    *
    * One ant is put in each node of the instance.
    * @author Matheus Paixao
    * @see Ant constructor in Ant class.
    */
   protected void initAnts(){
      this.ants = new Ant[nodes.length]; 
      //this.ants = new Ant[5]; 

      for(int i = 0; i <= this.ants.length - 1; i++){
         this.ants[i] = new Ant(this, getQ0(), new Node(i));
      }
   }

   /**
    * Method to get the solution of an iteration, method where the AntQ algorithm is runned.
    *
    * iterationBestSolution array is the best solution of all ants in an iteration.
    *
    * @author Matheus Paixao
    * @see chooseNextNode in Ant class
    * @see setNextNode in Ant class
    * @see getNextNode in Ant class
    * @see addNodeToTour in Ant class
    * @see addInitialNodeToNodesToVisit in Ant class
    * @see addNodeToTour in Ant class
    * @see getInitialNode in Ant class
    * @see getLastTourEdge in Ant class
    * @see getMaxPheromoneValue
    * @see updatePheromoneValue
    * @see loadNodesToVisit in Ant class
    * @see setCurrentNode in Ant class
    * @see removeNodeFromNodesToVisit in Ant class
    * @see getIterationBestSolution
    * @see clearTour in Ant class
    * @see calculateSolutionValue
    */
   private Edge[] getIterationSolution(){
      Edge[] iterationSolution = null;
      double iterationSolutionValue = 0;

      Ant ant = null;
      Node nextNode = null;
      double reinforcementLearningValue = 0;

      //in this step all the ants chooses the next node to move to
      //when all the ants have choosen the next node, they update the AQ value of the correspondent edge 
      for(int i = 0; i <= nodes.length - 1; i++){
         //if the ant didn't visit all the nodes yet
         if(i != nodes.length - 1){
            for(int j = 0; j <= ants.length - 1; j++){
               setCurrentAnt(ants[j]);

               ant = getCurrentAnt();
               if(ant.isTourFinished() == false){
                  nextNode = ant.chooseNextNode();
                  ant.setNextNode(nextNode);
                  ant.addNodeToTour(ant.getNextNode());
               }

               //if the ant has choosen the last node to visit
               if(i == nodes.length - 2){
                  ant.addInitialNodeToNodesToVisit();
               }
            }
         }
         //all the ants go back to their initial node
         else{
            for(int j = 0; j <= ants.length - 1; j++){
               setCurrentAnt(ants[j]);

               ant = getCurrentAnt();
               nextNode = ant.getInitialNode();
               ant.setNextNode(nextNode);
               ant.addNodeToTour(ant.getNextNode());
            }
         }

         //all the ants update the AQ value of the last edge added to their tour
         for(int j = 0; j <= ants.length - 1; j++){
            ant = ants[j];
            updatePheromoneValue(ant.getLastTourEdge(), 0);

            //if the ants has done the tour
            if(i == nodes.length - 1){
               ant.loadNodesToVisit(); //prepare the nodes to visit array for another tour
            }

            ant.setCurrentNode(ant.getNextNode()); //move to the next choosed node
            if((ant.isTourFinished() == false) || ant.getCurrentNode().getIndex() == ant.getInitialNode().getIndex()){
               ant.removeNodeFromNodesToVisit(ant.getCurrentNode()); // remove the current node from the nodes to visit
            }
         }
      }

      iterationSolution = getIterationBestSolution();
      iterationSolutionValue = calculateSolutionValue(iterationSolution);

      //all the ants clear their tours
      for(int i = 0; i <= ants.length - 1; i++){
         ants[i].clearTour();
      }

      //in this step is calculated the reinforcement learning value and is updated the AQ value only 
      //the edges belonging to the iterationBestTour
      reinforcementLearningValue = w / iterationSolutionValue;
      for(int i = 0; i <= iterationSolution.length - 1; i++){
         updatePheromoneValue(iterationSolution[i], reinforcementLearningValue);
      }

      return iterationSolution;
   }

   /**
    * Method to get the max pheromone value of the next choosed node.
    *
    * The method evaluates the pheromone values of all the edges from the next choosed node
    * to all the nodes that the ant didn't visit yet.
    * @author Matheus Paixao
    * @param nodesToVisit array of the nodes to be visited by the ant
    * @param nextNode the next choosed node
    * @return the max pheromone value of the next choosed node.
    * @see equals method in Node class.
    */
   public double getMaxPheromoneValue(Node nodesToVisit[], Node nextNode){
      double maxPheromoneValue = 0;
      double edgePheromoneValue = 0;
      int nextNodeIndex = nextNode.getIndex();

      for(int i = 0; i <= nodesToVisit.length - 1; i++){
         //only evaluate the node if the ant didn't visit it yet and it is different of the next choosed node
         if((nodesToVisit[i] != null) && (!nextNode.equals(nodesToVisit[i]))){
            edgePheromoneValue = pheromone[nextNode.getIndex()][nodesToVisit[i].getIndex()];
            if(edgePheromoneValue > maxPheromoneValue){
               maxPheromoneValue = edgePheromoneValue;
            }
         }
      }

      return maxPheromoneValue;
   }

   /**
    * Method to get the action choice of an edge.
    *
    * @author Matheus Paixao
    * @param node1 the first node of the edge 
    * @param node2 the second node of the edge 
    * @return the action choice of the edge
    */
   public double getActionChoice(Node node1, Node node2){
      double actionChoice =  Math.pow(pheromone[node1.getIndex()][node2.getIndex()], delta) * Math.pow(getHeuristicValue(node1, node2), beta);

      if((Double.isNaN(actionChoice)) || (Double.POSITIVE_INFINITY == actionChoice) || (Double.NEGATIVE_INFINITY == actionChoice)){
         actionChoice = 0;
      }

      return actionChoice;
   }

   /**
    * Method to get the sum of action choices of all remaining nodes to visit of an ant.
    *
    * @author Matheus Paixao
    * @return the sum of action choices of all remaining nodes to visit of an ant.
    * @see getActionChoice
    */
   public double getActionChoiceSum(Node currentNode, Node nodesToVisit[]){
      double actionChoiceSum = 0;

      for(int i = 0; i <= nodesToVisit.length - 1; i++){
         if(nodesToVisit[i] != null){
            actionChoiceSum += getActionChoice(currentNode, nodesToVisit[i]);
         }
      }

      return actionChoiceSum;
   }

   /**
    * Method to update the pheromone value of the passed edge.
    *
    * To update the pheromone value of an edge, it's used the reinforcement learning value of the edge
    * and the max pheromone value of the next choosed node.
    * @author Matheus Paixao
    * @param edge the edge to update.
    * @param reinforcementLearningValue the reinforcement learning value of the edge.
    * @param maxPheromoneValue the max pheromone value of the next choosed node.
    */
   private void updatePheromoneValue(Edge edge, double reinforcementLearningValue){
      int n1Index = edge.getNode1().getIndex();
      int n2Index = edge.getNode2().getIndex();

      pheromone[n1Index][n2Index] = ((1 - alfa) * pheromone[n1Index][n2Index] + alfa * (reinforcementLearningValue + gamma * getInitialPheromone()));
   }

   /**
    * Method to get the best solution, of all ants, of an iteration.
    *
    * It's used an auxiliary array to create a new array with the same elements. 
    * @author Matheus Paixao
    * @return the iteration best solution
    * @see getTour in Ant class
    * @see calculateSolutionValue
    */
   private Edge[] getIterationBestSolution(){
      Edge iterationBestSolutionTemp[] = ants[0].getTour();
      Edge iterationBestSolution[] = new Edge[iterationBestSolutionTemp.length];
      Edge solution[] = null;
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
}
