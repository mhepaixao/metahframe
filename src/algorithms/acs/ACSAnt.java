package algorithms.acs;

import java.util.Random;

public class ACSAnt{
   private int initialNode;
   private int currentNode;
   private int nextNode;
   protected Integer nodesToVisit[];
   private int[] tour;

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
      tour = new int[acs.getNumberOfNodes()];

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

   private void setCurrentNode(int node){
      this.currentNode = nodesToVisit[node];
   }

   public Integer[] getNodesToVisit(){
      return this.nodesToVisit;
   }

   public void loadNodesToVisit(){
      for(int i = 0; i <= nodesToVisit.length - 1; i++){
         nodesToVisit[i] = i;
      }
   }

   private void removeNodeFromNodesToVisit(int node){
      nodesToVisit[node] = null;
   }

   public boolean isTourFinished(){
      return false;
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
         //nextNode = getPseudoRandomProportionalNode(); //method 2
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
}
