import java.util.Random;
import java.util.Arrays;

/**
 * Class to describe the behavior of the ants, or ants, in the goal
 * to find best tours over the nodes.
 *
 * The initialNode variable stores the initial node of the ant. It's necessary when the ant 
 * finish its tour and has to go back to the beginning.
 *
 * The currentNode variable stores the current node of the ant. It's used in the state transition rule.
 *
 * Each ant has to know its nextNode to go before really go. It's used basically in AntQ class.
 *
 * The nodesToVisit array store the nodes that the ant didn't visit yet. The null values represents the visited nodes.
 *
 * The tour array is the path, the sequency of nodes, done by the ant.
 *
 * @author Matheus Paixao
 */
public class Ant {
   private Node initialNode;
   private Node currentNode;
   private Node nextNode;
   private Node nodesToVisit[];
   public Edge tour[];

   private Random random;

   /**
    * Method to create an ant with its initial node.
    *
    * Create the nodesToVisit array with the same size of the nodes array of AntQ.
    * Create the tour array with the same size of the nodesToVisit.
    * Fill the nodesToVisit array with Node objects equals to the nodes array of AntQ.
    * Set the initial node the current node and remove the initial node of the nodes to be visited.
    * @author Matheus Paixao
    * @param initialNode the node that will be the initial node of the ant.
    * @see loadNodesToVisit
    * @see removeNodeFromNodesToVisit
    */
   public Ant(Node initialNode){
      this.nodesToVisit = new Node[AntQ.getNodes().length];
      tour = new Edge[getNodesToVisit().length];

      loadNodesToVisit();

      this.initialNode = initialNode;
      setCurrentNode(getInitialNode());
      removeNodeFromNodesToVisit(getInitialNode());

      random = new Random();
   }

   public Node getInitialNode(){
      return this.initialNode;
   }

   public Node getCurrentNode(){
      return this.currentNode;
   }

   public void setNextNode(Node node){
      this.nextNode = nodesToVisit[node.getIndex()];
   }

   public Node getNextNode(){
      return this.nextNode;
   }

   public Node[] getNodesToVisit(){
      return this.nodesToVisit;
   }

   public void setCurrentNode(Node currentNode){
      this.currentNode = nodesToVisit[currentNode.getIndex()];
   }

   public Edge[] getTour(){
      return this.tour;
   }

   /**
    * Method to fill the nodesToVisit array with Node objects that 
    * are equal to the nodes from the AntQ algorithm.
    *
    * @author Matheus Paixao
    */
   public void loadNodesToVisit(){
      Node nodes[] = AntQ.getNodes();

      for(int i = 0; i <= nodes.length - 1; i++){
         this.nodesToVisit[i] = nodes[i];
      }
   }

   /**
    * Method to remove a node from nodes to be visited.
    *
    * Get the index of the node in the nodes array in AntQ and
    * set the correspondent node in nodesToVisit to null.
    * @author Matheus Paixao
    * @param node the node to be removed from nodesToVisit.
    */
   public void removeNodeFromNodesToVisit(Node node){
      nodesToVisit[node.getIndex()] = null;
   }

   /**
    * Method to add the initial node to the nodes to be visited.
    *
    * Get the index of the initial node of the ant in the nodes array in AntQ
    * and set the correspondent position of the nodesToVisit with the initial node.
    * It's used when the ant have visited all the nodes and has to go back to the first one.
    * @author Matheus Paixao
    */
   public void addInitialNodeToNodesToVisit(){
      Node initialNode = getInitialNode();
      nodesToVisit[initialNode.getIndex()] = initialNode;
   }

   /**
    * Method to add a new node to the tour.
    *
    * Insert an edge where the node 1 is the current node and the node 2 is the node to be added.
    * @author Matheus Paixao
    * @param node node to be added to the tour.
    * @see insertEdge
    */
   public void addNodeToTour(Node node){
      Edge[][] edges = AntQ.getEdges();
      insertEdge(edges[getCurrentNode().getIndex()][node.getIndex()]);
   }
   
   /**
    * Method to insert an edge to the ant tour.
    *
    * Insert an edge equal to the edge from the AntQ algorihtm. The edge is inserted in the last null position of the tour.
    * @author Matheus Paixao
    * @param edge the edge from the edges matrix of AntQ algorithm to be inserted in the ant's tour.
    * @see Edge constructor
    */
   private void insertEdge(Edge edge){
      for(int i = 0; i <= tour.length - 1; i++){
         if(tour[i] == null){
            tour[i] = new Edge(edge.getNode1(), edge.getNode2(), edge.getEdgeValue());
            break;
         }
      }
   }

   /**
    * Method to get the last edge added to the ant tour.
    *
    * @author Matheus Paixao
    * @return the last edge added to the ant tour.
    */
   public Edge getLastTourEdge(){
      Edge lastTourEdge = null;

      for(int i = tour.length - 1; i >= 0; i--){
         if(tour[i] != null){
            lastTourEdge = tour[i];
            break;
         }
      }

      return lastTourEdge;
   }

   /**
    * Method to clear the ant tour.
    *
    * It's used when an ant finish a tour (visit all nodes) and has to start another one.
    * @author Matheus Paixao
    */
   public void clearTour(){
      for(int i = 0; i <= tour.length - 1; i ++){
         tour[i] = null;
      }
   }

   /**
    * Method that implements the AntQ transition rule.
    *
    * It's generated a random number q in the interval (0,1). Then q is compared 
    * with the initialization parameter q0, this test will define if the ant will
    * choose the best possible action (exploitation) or a random action (exploration).
    * The exploration choice can be done by two methods:
    * 1) pseudo-random
    * 2) pseudo-random-proportional
    * @author Matheus Paixao
    * @return the next node of a an ant
    * @see getRandomNumber
    * @see getMaxActionChoiceNode
    * @see getPseudoRandomProportionalNode
    * @see getPseudoRandomNode
    */
   public Node chooseNextNode(){
      double q = getRandomNumber();
      Node nextNode = null;

      if(q <= AntQ.getQ0()){
         //exploitation
         nextNode = getMaxActionChoiceNode();
      }
      else{
         //exploration
         //nextNode = getPseudoRandomNode(); //method 1
         nextNode = getPseudoRandomProportionalNode(); //method 2
      }

      return nextNode;
   }

   /**
    * Method to get a random number in the (0,1) interval.
    *
    * @author Matheus Paixao
    * @return a random number in the (0,1) interval
    * @see nextDouble method in Random class
    */
   private double getRandomNumber(){
      return random.nextDouble();
   }

   /**
    * Method to get the best possible node to go.
    *
    * How 'good' is an action is measured by it's action choice.
    * @author Matheus Paixao
    * @return the best possible node to go.
    * @see getFirstNodeToVisit
    * @see getActionChoice
    */
   private Node getMaxActionChoiceNode(){
      Node maxActionChoiceNode = getFirstNodeToVisit();
      Node node = null;

      for(int i = 0; i <= nodesToVisit.length - 1; i++){
         if(nodesToVisit[i] != null){
            node = nodesToVisit[i];
            if(AntQ.getActionChoice(getCurrentNode(), node, this) > AntQ.getActionChoice(getCurrentNode(), maxActionChoiceNode, this)){
               maxActionChoiceNode = node;
            }
         }
      }

      return maxActionChoiceNode;
   }

   /**
    * Method to get the next ant node using the pseudo-random method.
    * 
    * In this method each possible node to go receive a random probability in (0,1) interval.
    * The max probability node is choosen.
    * @author Matheus Paixao
    * @return the next ant node using pseudo-random method.
    * @see getRandomNumber
    */
   private Node getPseudoRandomNode(){
      double probabilities[] = new double[nodesToVisit.length];
      double maxProbability = 0;
      Node maxProbabilityNode = null;
      
      for(int i = 0; i <= probabilities.length - 1; i++){
         if(nodesToVisit[i] != null){
            probabilities[i] = getRandomNumber();
            if(probabilities[i] > maxProbability){
               maxProbability = probabilities[i];
               maxProbabilityNode = nodesToVisit[i];
            }
         }
      }

      return maxProbabilityNode;
   }

   /**
    * Method to get the next node using the pseudo-random-proportional method.
    *
    * Each possible node to go has a pseudo random proportional probability
    * calculated in the getPseudoRandomProportionalProbabilities method.
    *
    * Then a roulette selection method is runned to select the next node.
    * @author Matheus Paixao
    * @return the next node using the pseudo-random-proportional method.
    * @see getPseudoRandomProportionalProbabilities
    * @see getRouletteValue
    */
   private Node getPseudoRandomProportionalNode(){
      Node node = null;
      double rouletteValue = 0;
      double probabilities[] = getPseudoRandomProportionalProbabilities();

      rouletteValue = getRouletteValue(probabilities);

      for(int i = 0; i <= probabilities.length - 1; i++){
         if(rouletteValue == probabilities[i]){
            node = nodesToVisit[i];
            break;
         }
      }

      return node;
   }

   /**
    * Method to calculate the pseudo random proportional probability of all the
    * nodes to be visited by the ant.
    *
    * The pseudo random proportional probability of a node is calculated by 
    * the getPseudoRandomProportionalProbability method.
    * @author Matheus Paixao
    * @return an array containing the pseudo random proportional probability of the nodes to visit.
    * @see getPseudoRandomProportionalProbability
    */
   private double[] getPseudoRandomProportionalProbabilities(){
      double probabilities[] = new double[nodesToVisit.length];
      double actionChoiceSum = AntQ.getActionChoiceSum(getCurrentNode(), nodesToVisit, this);

      for(int i = 0; i <= probabilities.length - 1; i++){
         if(nodesToVisit[i] != null){
            probabilities[i] = AntQ.getActionChoice(getCurrentNode(), nodesToVisit[i], this) / actionChoiceSum;
         }
         else{
            probabilities[i] = 0;
         }
      }

      return probabilities;
   }

   /**
    * Method to get the value of the probability selected by the roulette.
    *
    * Higher the probability of a node, higher the chance to be choosen by the roulette.
    * For more information search for "roulette selection method".
    * @author Matheus Paixao
    * @param probabilities an array containing the probabilities for roulette selection.
    * @return the probability value choosen by the roulette.
    * @see getRandomNumber
    */
   private double getRouletteValue(double[] probabilities){
      double[] rouletteProbabilities = new double[probabilities.length];
      double neddle = 0;
      double neddleChecker = 0;
      double rouletteValue = 0;

      for(int i = 0; i <= rouletteProbabilities.length - 1; i++){
         rouletteProbabilities[i] = probabilities[i];
      }

      Arrays.sort(rouletteProbabilities);

      neddle = getRandomNumber();

      for(int i = 0; i <= rouletteProbabilities.length - 1; i++){
         neddleChecker += rouletteProbabilities[i];
         if(neddleChecker >= neddle){
            rouletteValue = rouletteProbabilities[i];
            break;
         }
      }

      return rouletteValue;
   }

   /**
    * Method to get the first possible node to be visited by the ant.
    *
    * @author Matheus Paixao
    * @return the first possible node (not null) to go in the nodes to be visited array
    */
   private Node getFirstNodeToVisit(){
      Node firstNodeToVisit = null;

      for(int i = 0; i <= nodesToVisit.length - 1; i++){
         if(nodesToVisit[i] != null){
            firstNodeToVisit = nodesToVisit[i];
            break;
         }
      }

      return firstNodeToVisit;
   }
}
