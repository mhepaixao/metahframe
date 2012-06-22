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
   private static final double gamma = 0.3;
   private static final double q0 = 0.9;
   private static final double w = 10.0;

   private int numberOfIterations;
   private double totalTime;

   private static Node nodes[];
   private double[][] pheromone;

   private static Edge edges[][];

   public static double actionChoices[][];

   private static Ant ants[];

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

         if(bestSolution != null){
            if(isSolutionBest(iterationSolutionValue, bestSolutionValue) == true){
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

   private void initAntQ(){
      InstanceReader instanceReader = new InstanceReader();

      if(problem.equals("jssp")){
         createJSSPEdges(instanceReader.getNumberOfJobs());
         times = instanceReader.getTimesMatrix();
         initAQValues(0.01);
      }
      else{
         String instanceType = instanceReader.getInstanceType();
         //System.out.println("memory before edges");
         //printUsedMemory();
         if(instanceType == "coordinates"){
            createCartesianCoordinatesEdges(instanceReader.getNodesList());
         }
         else if(instanceType == "matrix"){
            createMatrixEdges(instanceReader.getEdgesValuesMatrix());
         }
         //System.out.println("memory after edges");
         //printUsedMemory();

         pheromone = new double[getNumberOfNodes()][getNumberOfNodes()];
         initPheromoneValues(getInitialPheromone());
         initAQValues(getInitialPheromone());
      }

      initAnts();
   }

   private void initPheromoneValues(double initialPheromone){
      for(int i = 0; i <= pheromone.length - 1; i++){
         for(int j = 0; j <= pheromone[0].length - 1; j++){
            if(i != j){
               pheromone[i][j] = initialPheromone;
            }
         }
      }
   }

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
               ant = ants[j];
               nextNode = ant.chooseNextNode();
               ant.setNextNode(nextNode);
               ant.addNodeToTour(ant.getNextNode());

               //if the ant has choosen the last node to visit
               if(i == nodes.length - 2){
                  ant.addInitialNodeToNodesToVisit();
               }
            }
         }
         //all the ants go back to their initial node
         else{
            for(int j = 0; j <= ants.length - 1; j++){
               ant = ants[j];
               nextNode = ant.getInitialNode();
               ant.setNextNode(nextNode);
               ant.addNodeToTour(ant.getNextNode());
            }
         }

         //all the ants update the AQ value of the last edge added to their tour
         for(int j = 0; j <= ants.length - 1; j++){
            ant = ants[j];
            updatePheromoneValue(ant.getLastTourEdge(), 0, getMaxAQValue(ant.getNodesToVisit(), ant.getNextNode()));

            //if the ants has done the tour
            if(i == nodes.length - 1){
               ant.loadNodesToVisit(); //prepare the nodes to visit array for another tour
            }

            ant.setCurrentNode(ant.getNextNode()); //move to the next choosed node
            ant.removeNodeFromNodesToVisit(ant.getCurrentNode()); // remove the current node from the nodes to visit
         }
      }

      iterationSolution = getIterationBestTour();
      iterationSolutionValue = calculateTourValue(iterationSolution);

      //all the ants clear their tours
      for(int i = 0; i <= ants.length - 1; i++){
         ants[i].clearTour();
      }

      //in this step is calculated the reinforcement learning value and is updated the AQ value only 
      //the edges belonging to the iterationBestTour
      reinforcementLearningValue = w / iterationSolutionValue;
      for(int i = 0; i <= iterationSolution.length - 1; i++){
         updatePheromoneValue(iterationSolution[i], reinforcementLearningValue, 0);
      }

      return iterationSolution;
   }

   public double getActionChoice2(Node node1, Node node2){
      double actionChoice =  Math.pow(pheromone[node1.getIndex()][node2.getIndex()], delta) * Math.pow(getHeuristicValue(node1, node2), beta);

      if((Double.isNaN(actionChoice)) || (Double.POSITIVE_INFINITY == actionChoice) || (Double.NEGATIVE_INFINITY == actionChoice)){
         actionChoice = 0;
      }

      return actionChoice;
   }

   public double getActionChoiceSum2(Node currentNode, Node nodesToVisit[]){
      double actionChoiceSum = 0;

      for(int i = 0; i <= nodesToVisit.length - 1; i++){
         if(nodesToVisit[i] != null){
            actionChoiceSum += getActionChoice2(currentNode, nodesToVisit[i]);
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
   private void updatePheromoneValue(Edge edge, double reinforcementLearningValue, double maxPheromoneValue){
      int n1Index = edge.getNode1().getIndex();
      int n2Index = edge.getNode2().getIndex();

      pheromone[n1Index][n2Index] = ((1 - alfa) * pheromone[n1Index][n2Index] + alfa * (reinforcementLearningValue + gamma * maxPheromoneValue));
   }

   private static String problem = "";
   private static double[][] times;

   /**
    * Main method, where the Ant Q algorithm is runned.
    *
    * totalIterations and iterationsCounter are used to stop the algorithm after some iterations.
    * iterationBestTour array is the best tour of all ants in a iteration.
    * globalBestTour array is the best tour of all ants in all iterations.
    *
    * @author Matheus Paixao
    * @param args[0] the number of iterations (optional)
    * @see init
    * @see chooseNextNode in Ant class
    * @see setNextNode in Ant class
    * @see getNextNode in Ant class
    * @see addNodeToTour in Ant class
    * @see addInitialNodeToNodesToVisit in Ant class
    * @see getInitialNode in Ant class
    * @see getLastTourEdge in Ant class
    * @see getMaxAQValue
    * @see updateAQValue
    * @see loadNodesToVisit in Ant class
    * @see setCurrentNode in Ant class
    * @see removeNodeFromNodesToVisit in Ant class
    * @see getIterationBestTour
    * @see clearTour in Ant class
    * @see updateReinforcementLearningValue
    * @see clearReinforcementLearningValue
    * @see calculateTourValue
    */
   public static void main(String[] args){
      //algorithm variables:

      int totalIterations = 0; //number of iterations to run
      int iterationsCounter = 0;
      
      //time variables
      double initialTime = 0;
      double finalTime = 0;
      double iterationTime = 0;
      double averageIterationTime = 0;

      Ant ant = null;
      Node nextNode = null;
      double reinforcementLearningValue = 0;

      Edge iterationBestTour[] = null; 
      double iterationBestTourValue = 0;
      Edge globalBestTour[] = null; 
      int hereCounter = 0;

      if(args.length > 0){
         totalIterations = Integer.parseInt(args[0]); //the user can pass the number of iterations

         if(args.length > 1){
            problem = args[1];
         }
      }
      else{
         totalIterations = 200;
      }

      //initialization of the algorithm
      init();

      while(iterationsCounter <= totalIterations){
         initialTime = System.currentTimeMillis();
         if(!problem.equals("jssp")){
            //update the action choices of all edges
            updateActionChoices();
         }
         //in this step all the ants chooses the next node to move to
         //when all the ants have choosen the next node, they update the AQ value of the correspondent edge 
         for(int i = 0; i <= nodes.length - 1; i++){
            //if the ant didn't visit all the nodes yet
            if(i != nodes.length - 1){
               for(int j = 0; j <= ants.length - 1; j++){
                  ant = ants[j];
                  nextNode = ant.chooseNextNode();
                  ant.setNextNode(nextNode);
                  ant.addNodeToTour(ant.getNextNode());

                  //if the ant has choosen the last node to visit
                  if(i == nodes.length - 2){
                     ant.addInitialNodeToNodesToVisit();
                  }
               }
            }
            //all the ants go back to their initial node
            else{
               for(int j = 0; j <= ants.length - 1; j++){
                  ant = ants[j];
                  nextNode = ant.getInitialNode();
                  ant.setNextNode(nextNode);
                  ant.addNodeToTour(ant.getNextNode());
               }
            }

            //all the ants update the AQ value of the last edge added to their tour
            for(int j = 0; j <= ants.length - 1; j++){
               ant = ants[j];
               updateAQValue(ant.getLastTourEdge(), 0, getMaxAQValue(ant.getNodesToVisit(), ant.getNextNode()));

               //if the ants has done the tour
               if(i == nodes.length - 1){
                  ant.loadNodesToVisit(); //prepare the nodes to visit array for another tour
               }

               ant.setCurrentNode(ant.getNextNode()); //move to the next choosed node
               ant.removeNodeFromNodesToVisit(ant.getCurrentNode()); // remove the current node from the nodes to visit
            }
         }

         iterationBestTour = getIterationBestTour();
         iterationBestTourValue = calculateTourValue(iterationBestTour);

         //all the ants clear their tours
         for(int i = 0; i <= ants.length - 1; i++){
            ants[i].clearTour();
         }

         //in this step is calculated the reinforcement learning value and is updated the AQ value only 
         //the edges belonging to the iterationBestTour
         reinforcementLearningValue = w / iterationBestTourValue;
         for(int i = 0; i <= iterationBestTour.length - 1; i++){
            updateAQValue(iterationBestTour[i], reinforcementLearningValue, 0);
         }

         if(iterationsCounter == 0){
            globalBestTour = iterationBestTour;
         }
         else{
            if(iterationBestTourValue < calculateTourValue(globalBestTour)){
               System.out.println("found best tour");
               hereCounter++;
               globalBestTour = iterationBestTour;
            }
         }

         finalTime = System.currentTimeMillis();
         iterationTime = finalTime - initialTime;
         System.out.println("time of iteration "+iterationsCounter + ": "+iterationTime);
         averageIterationTime += iterationTime;
         iterationsCounter++;
      }
      //System.out.println("here counter: " + hereCounter);
      System.out.println("Total time " + averageIterationTime * Math.pow(10, -3) + " seconds");
      System.out.println("Average time of iterations: " + averageIterationTime / iterationsCounter);
      System.out.println("Best tour value: " + calculateTourValue(globalBestTour));
      System.exit(0);
   }
   
   public static double getQ0(){
      return q0;
   }

   public static Node[] getNodes(){
      return nodes;
   }

   public static Edge[][] getEdges(){
      return edges;
   }

   public static double getGamma(){
      return gamma;
   }

   /**
    * Method to initialize the algorithm.
    *
    * @author Matheus Paixao
    * @see getNodesList in InstanceReader class
    * @see createEdges
    * @see initAQValues
    * @see getAQ0
    * @see initAnts
    */
   private static void init(){
      InstanceReader instanceReader = new InstanceReader();

      if(problem.equals("jssp")){
         createJSSPEdges(instanceReader.getNumberOfJobs());
         times = instanceReader.getTimesMatrix();
         initAQValues(0.01);
      }
      else{
         String instanceType = instanceReader.getInstanceType();
         //System.out.println("memory before edges");
         //printUsedMemory();
         if(instanceType == "coordinates"){
            createCartesianCoordinatesEdges(instanceReader.getNodesList());
         }
         else if(instanceType == "matrix"){
            createMatrixEdges(instanceReader.getEdgesValuesMatrix());
         }
         //System.out.println("memory after edges");
         //printUsedMemory();

         actionChoices = new double[edges.length][edges.length];
         initAQValues(getAQ0());
      }

      //initAnts();
   }

   private static void createJSSPEdges(int numberOfJobs){
      nodes = new Node[numberOfJobs];

      for(int i = 0; i <= nodes.length - 1; i++){
         nodes[i] = new Node(i);
      }

      edges = new Edge[nodes.length][nodes.length];
      for(int i = 0; i <= nodes.length - 1; i++){
         for(int j = 0; j <= nodes.length - 1; j++){
            edges[i][j] = new Edge(nodes[i], nodes[j]);
         }
      }
   }

   /**
    * Method to create the edges from each node to each node.
    *
    * It's a complete graph.
    * @author Matheus Paixao
    */
   private static void createCartesianCoordinatesEdges(Node nodesList[]){
      nodes = nodesList;
      edges = new Edge[nodes.length][nodes.length];

      for(int i = 0; i <= nodes.length - 1; i++){
         System.out.println("line "+i);
         for(int j = 0; j <= nodes.length - 1; j++){
            edges[i][j] = new Edge(nodes[i], nodes[j]);
         }
      }
   }

   private static void createMatrixEdges(double edgesValuesMatrix[][]){
      nodes = new Node[edgesValuesMatrix.length];

      for(int i = 0; i <= edgesValuesMatrix.length - 1; i++){
         nodes[i] = new Node(i);
      }

      edges = new Edge[nodes.length][nodes.length];
      for(int i = 0; i <= nodes.length - 1; i++){
         for(int j = 0; j <= nodes.length - 1; j++){
            edges[i][j] = new Edge(nodes[i], nodes[j], edgesValuesMatrix[i][j]);
         }
      }
   }

   /**
    * Method to get the initial AQ value for all edges.
    *
    * The initial AQ value is composed by the average value of the edges and the number of nodes.
    * @author Matheus Paixao
    * @return the initial AQ value for all edges.
    * @see getEdgeValue in Edge class
    * @see getNumberOfEdges
    */
   private static double getAQ0(){
      double sumOfEdges = 0;
      double averageValueOfEdges = 0;

      for(int i = 0; i <= edges.length - 1; i++){
         for(int j = 0; j <= edges.length - 1; j++){
            sumOfEdges += edges[i][j].getEdgeValue();
         }
      }
      averageValueOfEdges = sumOfEdges / getNumberOfEdges();
      
      return 1 / (averageValueOfEdges * nodes.length);
   }

   /**
    * Method to get the number of existing edges.
    *
    * When 'i' is equal to 'j' there is no edge.
    * @author Matheus Paixao
    * @return the number of existing edges.
    */
   private static int getNumberOfEdges(){
      int numberOfEdges = 0;

      for(int i = 0; i <= nodes.length - 1; i++){
         for(int j = 0; j <= nodes.length - 1; j++){
            if(i != j){
               numberOfEdges++;
            }
         }
      }

      return numberOfEdges;
   }

   /**
    * Method to set the initial AQ value for each edge.
    *
    * When 'i' is equal to 'j' there is no edge, so the initial AQ value is 0.
    * @author Matheus Paixao
    * @param AQ0 the initial AQ value for all edges.
    * @see setAQValue in Edge class.
    */
   private static void initAQValues(double AQ0){
      for(int i = 0; i <= edges.length - 1; i++){
         for(int j = 0; j <= edges.length - 1; j++){
            if(i == j){
               edges[i][j].setAQValue(0);
            }
            else{
               edges[i][j].setAQValue(AQ0);
            }
         }
      }
   }

   /**
    * Method to init the ants, or the ants.
    *
    * One ant is put in each node of the instance.
    * @author Matheus Paixao
    * @see Ant constructor in Ant class.
    */
   //private static void initAnts(){
   private void initAnts(){
      ants = new Ant[nodes.length]; 
      //ants = new Ant[1]; 

      for(int i = 0; i <= ants.length - 1; i++){
         //ants[i] = new Ant(nodes[i]);
         ants[i] = new Ant(this, nodes[i]);
      }
   }

   /**
    * Method to update the action choices of all edges.
    *
    * Uses the edges of the edges matrix.
    * When the action choice value is too low (Not a Number or Infinity) is considered equal to 0.
    * @author Matheus Paixao
    * @see Math.pow
    */
   private static void updateActionChoices(){
      Edge edge =  null;
      double actionChoice = 0;

      for(int i = 0; i <= actionChoices.length - 1; i++){
         for(int j = 0; j <= actionChoices.length - 1; j++){
            edge = edges[i][j];
            if(i != j){
               actionChoice =  Math.pow(edge.getAQValue(), delta) * Math.pow(edge.getEdgeHeuristicValue(), beta);

               if((Double.isNaN(actionChoice)) || (Double.POSITIVE_INFINITY == actionChoice) || (Double.NEGATIVE_INFINITY == actionChoice)){
                  actionChoice = 0;
               }

               actionChoices[i][j] = actionChoice;
            }
         }
      }
   }

   /**
    * Method to get the action choice of an edge.
    *
    * @author Matheus Paixao
    * @param node1 the first node of the edge 
    * @param node2 the second node of the edge 
    * @return the action choice of the edge
    */
   public static double getActionChoice(Node node1, Node node2, Ant ant){
      double actionChoice = 0;

      if(problem.equals("jssp")){
         int[] nodes = getNodes(node1, node2, ant);
         double heuristicValue = 1 / getMakespan(nodes);

         Edge edge = edges[node1.getIndex()][node2.getIndex()];
         actionChoice =  Math.pow(edge.getAQValue(), delta) * Math.pow(heuristicValue, beta);

         if(Double.isNaN(actionChoice) || actionChoice == Double.POSITIVE_INFINITY || actionChoice == Double.NEGATIVE_INFINITY){
            actionChoice = 0;
         }
      }
      else{
         actionChoice = actionChoices[node1.getIndex()][node2.getIndex()];
      }

      return actionChoice;
   }

   private static int getNumberOfNodes(Edge[] tour){
      int nodesCounter = 0;

      for(int i = 0; i <= tour.length - 1; i++){
         if(tour[i] != null){
            nodesCounter++;
         }
      }

      return nodesCounter + 2;
   }

   private static int[] getNodes(Node node1, Node node2, Ant ant){
      int[] nodes;

      Edge[] tour = ant.getTour();
      nodes = new int[getNumberOfNodes(tour)];

      if(tour[0] == null){
         nodes[0] = node1.getIndex();
         nodes[1] = node2.getIndex();
      }
      else{
         for(int i = 0; i <= tour.length - 1; i++){
            if(tour[i] != null){
               nodes[i] = tour[i].getNode1().getIndex();
               nodes[i+1] = tour[i].getNode2().getIndex();
            }
         }
         nodes[nodes.length - 1] = node2.getIndex();
      }

      return nodes;
   }

   private static double getMakespan(int[] nodes){
      double[] makespan = new double[times[0].length];
      int job = 0;

      for(int i = 0; i <= nodes.length - 1; i++){
         job = nodes[i];
         makespan[0] = makespan[0] + times[job][0];
         for(int j = 1; j <= times[0].length - 1; j++){
            if(makespan[j] > makespan[j - 1]){
               makespan[j] = makespan[j] + times[job][j];
            }
            else{
               makespan[j] = makespan[j - 1] + times[job][j];
            }
         }
      }

      return makespan[times[0].length - 1];
   }

   /**
    * Method to get the sum of action choices of all remaining nodes to visit of an ant.
    *
    * @author Matheus Paixao
    * @return the sum of action choices of all remaining nodes to visit of an ant.
    * @see getActionChoice
    */
   public static double getActionChoiceSum(Node currentNode, Node nodesToVisit[], Ant ant){
      double actionChoiceSum = 0;

      for(int i = 0; i <= nodesToVisit.length - 1; i++){
         if(nodesToVisit[i] != null){
            actionChoiceSum += getActionChoice(currentNode, nodesToVisit[i], ant);
         }
      }

      return actionChoiceSum;
   }

   /**
    * Method to get the max AQ value of the next choosed node.
    *
    * The method evaluates the AQ values of all the edges from the next choosed node
    * to all the nodes that the ant didn't visit yet.
    * @author Matheus Paixao
    * @param nodesToVisit array of the nodes to be visited by the ant
    * @param nextNode the next choosed node
    * @return the max AQ value of the next choosed node.
    * @see equals method in Node class.
    * @see getAQValue method in Edge class.
    */
   public static double getMaxAQValue(Node nodesToVisit[], Node nextNode){
      double maxAQValue = 0;
      double edgeAQValue = 0;
      int nextNodeIndex = nextNode.getIndex();

      for(int i = 0; i <= nodesToVisit.length - 1; i++){
         //only evaluate the node if the ant didn't visit it yet and it is different of the next choosed node
         if((nodesToVisit[i] != null) && (!nextNode.equals(nodesToVisit[i]))){
            edgeAQValue = edges[nextNodeIndex][nodesToVisit[i].getIndex()].getAQValue();
            if(edgeAQValue > maxAQValue){
               maxAQValue = edgeAQValue;
            }
         }
      }

      return maxAQValue;
   }

   /**
    * Method to update the AQ value of the passed edge.
    *
    * To update the AQ value of an edge, it's used the reinforcement learning value of the edge
    * and the max AQ value of the next choosed node.
    * @author Matheus Paixao
    * @param edge the edge to update.
    * @param reinforcementLearningValue the reinforcement learning value of the edge.
    * @param maxAQValue the max AQ value of the next choosed node.
    * @see getAQValue in Edge class.
    * @see getReinforcementLearningValue in Edge class.
    * @see setAQValue in Edge class.
    */
   private static void updateAQValue(Edge edge, double reinforcementLearningValue, double maxAQValue){
      int node1Index = edge.getNode1().getIndex();
      int node2Index = edge.getNode2().getIndex();
      Edge edgeToUpdate = edges[node1Index][node2Index];

      edgeToUpdate.setAQValue((1 - alfa) * edgeToUpdate.getAQValue() + alfa * (reinforcementLearningValue + gamma * maxAQValue));
   }

   /**
    * Method to get the best tour, of all ants, of an iteration.
    *
    * It's used an auxiliary array to create a new array with the same elements. 
    * @author Matheus Paixao
    * @return the iteration best tour
    * @see getTour in Ant class
    * @see calculateTourValue
    */
   private static Edge[] getIterationBestTour(){
      Edge iterationBestTourTemp[] = ants[0].getTour();
      Edge iterationBestTour[] = new Edge[iterationBestTourTemp.length];
      Edge tour[] = null;
      double iterationBestTourValue = calculateTourValue(iterationBestTourTemp);
      double tourValue = 0;

      for(int i = 0; i <= ants.length - 1; i++){
         tour = ants[i].getTour();
         tourValue = calculateTourValue(tour);
         if(calculateTourValue(tour) < iterationBestTourValue){
            iterationBestTourValue = tourValue;
            iterationBestTourTemp = tour;
         }
      }

      for(int i = 0; i <= iterationBestTourTemp.length - 1; i++){
         iterationBestTour[i] = iterationBestTourTemp[i];
      }

      return iterationBestTour;
   }

   /**
    * Method to calculate the value of a tour.
    *
    * @author Matheus Paixao
    * @param tour the tour to calculate the value
    * @return the value of the tour
    * @see getEdgeValue in Edge class
    */
   private static double calculateTourValue(Edge[] tour){
      double tourValue = 0;

      if(problem.equals("jssp")){
         int[] nodes = new int[tour.length];

         for(int i = 0; i <= nodes.length - 1; i++){
            nodes[i] = tour[i].getNode1().getIndex();
         }

         tourValue = getMakespan(nodes);
      }
      else{
         for(int i = 0; i <= tour.length - 1; i++){
            tourValue += tour[i].getEdgeValue();
         }
      }

      return tourValue;
   }

   private static void printUsedMemory(){
      double mb = 1024 * 1024;
      Runtime runtime = Runtime.getRuntime();

      System.out.println("used memory: "+(runtime.totalMemory() - runtime.freeMemory()) / mb + " MegaBytes");
      System.out.println("==========================================================");
   }
}
