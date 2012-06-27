public class PrecedenceConstrainedAnt extends Ant{
   int[][] precedencesMatrix;

   public PrecedenceConstrainedAnt(AntQ antQ, double q0, Node initialNode, int[][] precedencesMatrix){
      super(antQ, q0, initialNode);
      this.precedencesMatrix = precedencesMatrix;
   }
}
