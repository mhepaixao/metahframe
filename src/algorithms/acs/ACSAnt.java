package algorithms.acs;

import java.util.Arrays;
import java.util.Random;

/**
 * Class to describe the behavior of the ants used in ACS algorithm, in the goal
 * to find best tours over the nodes.
 *
 * The initialNode variable stores the initial node of the ant.
 *
 * The currentNode variable stores the current node of the ant. It's used in the state transition rule.
 *
 * Each ant has to know its nextNode to go before really go. It's used basically in ACS class.
 *
 * The nodesToVisit array store the nodes that the ant didn't visit yet. The null values represents the visited nodes.
 *
 * The tour array is the path, the sequency of nodes, done by the ant.
 *
 * @author Matheus Paixao
 */
public class ACSAnt{
   private int initialNode;
   private int currentNode;
   private int nextNode;
   protected Integer nodesToVisit[];
   private Integer[] tour;

   protected ACS acs; //used to call some acs methods
   private double q0; //used in chooseNextNode method

   private Random random;

   /**
    * Method to create an ant.
    *
    * Create the nodesToVisit array with the same size of the nodes array of acs.
    * Create the tour array with the same size of the nodesToVisit.
    * @author Matheus Paixao
    * @param acs the ACS object
    * @param q0 the q0 value used in the transition rule
    * @see loadNodesToVisit
    */
   public ACSAnt(ACS acs, double q0){
      this.random = new Random();

      this.acs = acs;
      this.q0 = q0;

      this.nodesToVisit = new Integer[acs.getNumberOfNodes()];
      this.tour = new Integer[acs.getNumberOfNodes()];

      loadNodesToVisit();
   }

   public int getInitialNode(){
      return this.initialNode;
   }

   /**
    * Method to set the initial node for an ant.
    *
    * Also sets the initial node to the current node and remove the initial node from the
    * nodes to visit array.
    * @author Matheus Paixao
    * @param initialNode the initial node of the ant
    * @see setCurrentNode
    * @see removeNodeFromNodesToVisit
    */
   public void setInitialNode(int initialNode){
      this.initialNode = initialNode;
      setCurrentNode(getInitialNode());
      removeNodeFromNodesToVisit(getInitialNode());
   }

   public int getCurrentNode(){
      return this.currentNode;
   }

   public void setCurrentNode(int node){
      this.currentNode = nodesToVisit[node];
   }

   public int getNextNode(){
      return this.nextNode;
   }

   public void setNextNode(int node){
      this.nextNode = nodesToVisit[node];
   }

   public Integer[] getNodesToVisit(){
      return this.nodesToVisit;
   }

   /**
    * Method to init the nodesToVisit array.
    *
    * @author Matheus Paixao
    */
   public void loadNodesToVisit(){
      for(int i = 0; i <= nodesToVisit.length - 1; i++){
         nodesToVisit[i] = i;
      }
   }

   /**
    * Method to remove a node from nodes to be visited.
    *
    * @author Matheus Paixao
    * @param node the node to be removed from nodesToVisit.
    */
   public void removeNodeFromNodesToVisit(int node){
      nodesToVisit[node] = null;
   }

   public Integer[] getTour(){
      return this.tour;
   }

   /**
    * Method to know if the ant have finished it's tour or not.
    *
    * @author Matheus Paixao
    */
   public boolean isTourFinished(){
      boolean result = true;

      for(int i = 0; i <= tour.length - 1; i++){
         if(tour[i] == null){
            result = false;
            break;
         }
      }

      return result;
   }

   /**
    * Method to clear the ant tour.
    *
    * It's used when an ant finishes a tour (visit all nodes) and has to start another one.
    * @author Matheus Paixao
    */
   public void clearTour(){
      for(int i = 0; i <= tour.length - 1; i++){
         tour[i] = null;
      }
   }

   /**
    * Method to add a new node to the tour.
    *
    * @author Matheus Paixao
    * @param node node to be added to the tour.
    */
   public void addNodeToTour(int node){
      for(int i = 0; i <= tour.length - 1; i++){
         if(tour[i] == null){
            tour[i] = node;
            break;
         }
      }
   }

   /**
    * Method that implements the AntQ transition rule.
    *
    * It's generated a random number q in the interval (0,1). Then q is compared 
    * with the initialization parameter q0, this test will define if the ant will
    * choose the best possible action (exploitation) or a random action (exploration).
    * @author Matheus Paixao
    * @return the next node of a an ant
    * @see getRandomNumber
    * @see getMaxActionChoiceNode
    * @see getPseudoRandomProportionalNode
    */
   public int chooseNextNode(){
      double q = getRandomNumber();
      int nextNode = 0;

      if(q <= q0){
         nextNode = getMaxActionChoiceNode(); //exploitation
      }
      else{
         nextNode = getPseudoRandomProportionalNode(); //exploration
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
    * @see getActionChoice in ACS class
    */
   private int getMaxActionChoiceNode(){
      int maxActionChoiceNode = getFirstNodeToVisit();
      int node = 0;

      for(int i = 0; i <= nodesToVisit.length - 1; i++){
         if(nodesToVisit[i] != null){
            node = nodesToVisit[i];
            if(acs.getActionChoice(getCurrentNode(), node) > acs.getActionChoice(getCurrentNode(), maxActionChoiceNode)){
               maxActionChoiceNode = node;
            }
         }
      }

      return maxActionChoiceNode;
   }

   /**
    * Method to get the first possible node to be visited by the ant.
    *
    * @author Matheus Paixao
    * @return the first possible node (not null) to go in the nodes to be visited array
    */
   private int getFirstNodeToVisit(){
      int firstNodeToVisit = 0;

      for(int i = 0; i <= nodesToVisit.length - 1; i++){
         if(nodesToVisit[i] != null){
            firstNodeToVisit = nodesToVisit[i];
            break;
         }
      }

      return firstNodeToVisit;
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
   private int getPseudoRandomProportionalNode(){
      int node = 0;
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
    * @author Matheus Paixao
    * @return an array containing the pseudo random proportional probability of the nodes to visit.
    * @see getPseudoRandomProportionalProbability
    */
   private double[] getPseudoRandomProportionalProbabilities(){
      double probabilities[] = new double[nodesToVisit.length];
      double actionChoiceSum = acs.getActionChoiceSum(getCurrentNode(), nodesToVisit);

      for(int i = 0; i <= probabilities.length - 1; i++){
         if(nodesToVisit[i] != null){
            probabilities[i] = acs.getActionChoice(getCurrentNode(), nodesToVisit[i]) / actionChoiceSum;
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
}
