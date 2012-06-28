public class PrecedenceConstrainedAnt extends Ant{
   int[][] precedencesMatrix;

   public PrecedenceConstrainedAnt(AntQ antQ, double q0, Node initialNode, int[][] precedencesMatrix){
      super(antQ, q0, initialNode);
      this.precedencesMatrix = precedencesMatrix;
   }

   public Node chooseNextNode(){
      Node nextNode = super.chooseNextNode();

      if(hasPrecedecessor(nextNode) == true){
         addPredecessors(nextNode);
      }

      return nextNode;
   }

   private boolean hasPrecedecessor(Node requirement){
      boolean result = false;

      for(int i = 0; i <= precedencesMatrix[requirement.getIndex()].length - 1; i++){
         if(precedencesMatrix[requirement.getIndex()][i] == 1){
            result = true;
            break;
         }
      }

      return result;
   }

   private void addPredecessors(Node requirement){
   }
}
