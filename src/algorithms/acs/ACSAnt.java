package algorithms.acs;

import java.util.Arrays;
import java.util.Random;

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
    * Method to create an ant with its initial node.
    *
    * Create the nodesToVisit array with the same size of the nodes array of acs.
    * Create the tour array with the same size of the nodesToVisit.
    * Set the initial node the current node and remove the initial node of the nodes to be visited.
    * @author Matheus Paixao
    * @param acs the ACS object
    * @param q0 the q0 value used in the transition rule
    * @param initialNode the node that will be the initial node of the ant.
    * @see loadNodesToVisit
    * @see removeNodeFromNodesToVisit
    */
   public ACSAnt(ACS acs, double q0){
      this.random = new Random();

      this.acs = acs;
      this.q0 = q0;

      this.nodesToVisit = new Integer[acs.getNumberOfNodes()];
      tour = new Integer[acs.getNumberOfNodes()];

      loadNodesToVisit();
   }

   public int getInitialNode(){
      return this.initialNode;
   }

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

   public void loadNodesToVisit(){
      for(int i = 0; i <= nodesToVisit.length - 1; i++){
         nodesToVisit[i] = i;
      }
   }

   public void removeNodeFromNodesToVisit(int node){
      nodesToVisit[node] = null;
   }

   public Integer[] getTour(){
      return this.tour;
   }

   public boolean isTourFinished(){
      return false;
   }

   public void clearTour(){
      for(int i = 0; i <= tour.length - 1; i++){
         tour[i] = null;
      }
   }

   public void addNodeToTour(int node){
      for(int i = 0; i <= tour.length - 1; i++){
         if(tour[i] == null){
            tour[i] = node;
            break;
         }
      }
   }

   public int chooseNextNode(){
      double q = getRandomNumber();
      int nextNode = 0;

      if(q <= q0){
         //exploitation
         nextNode = getMaxActionChoiceNode();
      }
      else{
         //exploration
         nextNode = getPseudoRandomProportionalNode(); 
      }

      return nextNode;
   }

   private double getRandomNumber(){
      return random.nextDouble();
   }

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

   protected double[] getPseudoRandomProportionalProbabilities(){
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
