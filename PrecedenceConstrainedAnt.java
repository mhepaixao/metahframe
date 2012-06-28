public class PrecedenceConstrainedAnt extends Ant{
   int[][] precedencesMatrix;

   public PrecedenceConstrainedAnt(AntQ antQ, double q0, Node initialNode, int[][] precedencesMatrix){
      super(antQ, q0, initialNode);
      this.precedencesMatrix = precedencesMatrix;
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
      addPredecessor(getPredecessor(requirement));
   }

   private Node getPredecessor(Node requirement){
      Node predecessor = null;

      for(int i = 0; i <= precedencesMatrix[requirement.getIndex()].length - 1; i++){
         if(precedencesMatrix[requirement.getIndex()][i] == 1){
            predecessor = nodesToVisit[i];
            break;
         }
      }

      return predecessor;
   }

   private void addPredecessor(Node predecessor){
      addNodeToTour(predecessor);
      setCurrentNode(predecessor);
      removeNodeFromNodesToVisit(predecessor);
   }
}
