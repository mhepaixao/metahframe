import java.util.ArrayList;

public class PrecedenceConstrainedAnt2 extends Ant{
   int[][] precedencesMatrix;
   int objective;

   public PrecedenceConstrainedAnt2(AntQ antQ, double q0, Node initialNode, int[][] precedencesMatrix, int objective){
      super(antQ, q0, initialNode);
      this.precedencesMatrix = precedencesMatrix;
      this.objective = objective;
   }

   public boolean isTourFinished(){
      boolean result = false;

      if(tour[tour.length - 2] != null){
         result = true;
      }

      return result;
   }

   public Node chooseNextNode(){
      Node nextNode = super.chooseNextNode();

      if(hasPrecedecessorInNodesToVisit(nextNode) == true){
         addAllPredecessors(nextNode);
      }

      return nextNode;
   }

   private boolean hasPrecedecessorInNodesToVisit(Node requirement){
      boolean result = false;

      for(int i = 0; i <= precedencesMatrix[requirement.getIndex()].length - 1; i++){
         if((precedencesMatrix[requirement.getIndex()][i] == 1) && (nodesToVisit[i] != null)){
            result = true;
            break;
         }
      }

      return result;
   }

   private void addAllPredecessors(Node requirement){
      ArrayList<Node> predecessors = getPredecessors(requirement);

      for(int i = 0; i <= predecessors.size() - 1; i++){
         addPredecessor(predecessors.get(i));
      }
   }

   private ArrayList<Node> getPredecessors(Node requirement){
      ArrayList<Node> predecessors = new ArrayList<Node>();

      for(int i = 0; i <= precedencesMatrix[requirement.getIndex()].length - 1; i++){
         if(precedencesMatrix[requirement.getIndex()][i] == 1 && nodesToVisit[i] != null){
            predecessors.add(nodesToVisit[i]);
         }
      }

      return predecessors;
   }

   private void addPredecessor(Node predecessor){
      if(hasPrecedecessorInNodesToVisit(predecessor) == true){
         addAllPredecessors(predecessor);
      }

      addNodeToTour(predecessor);
      setCurrentNode(predecessor);
      removeNodeFromNodesToVisit(predecessor);
   }

   public Node getMaxActionChoiceNode(){
      Node maxActionChoiceNode = getFirstNodeToVisit();
      Node node = null;

      for(int i = 0; i <= nodesToVisit.length - 1; i++){
         if(nodesToVisit[i] != null){
            node = nodesToVisit[i];
            if(antQ.getActionChoice2(getCurrentNode(), node, objective) > antQ.getActionChoice2(getCurrentNode(), maxActionChoiceNode, objective)){
               maxActionChoiceNode = node;
            }
         }
      }

      return maxActionChoiceNode;
   }

   protected double[] getPseudoRandomProportionalProbabilities(){
      double probabilities[] = new double[nodesToVisit.length];
      double actionChoiceSum = antQ.getActionChoiceSum2(getCurrentNode(), nodesToVisit, objective);

      for(int i = 0; i <= probabilities.length - 1; i++){
         if(nodesToVisit[i] != null){
            probabilities[i] = antQ.getActionChoice2(getCurrentNode(), nodesToVisit[i], objective) / actionChoiceSum;
         }
         else{
            probabilities[i] = 0;
         }
      }

      return probabilities;
   }
}
