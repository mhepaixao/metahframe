import java.io.File;

public class JSSPAntQ extends AntQ{
   private JSSPInstanceReader jsspInstanceReader;

   public JSSPAntQ(File instance, int numberOfIterations){
      super(numberOfIterations);
      jsspInstanceReader = new JSSPInstanceReader(instance);
   }

   public int getNumberOfNodes(){
      return 0;
   }

   public double getInitialPheromone(){
      return 0;
   }

   public double getHeuristicValue(Node node1, Node node2){
      return 0;
   }

   public double calculateSolutionValue(Edge[] solution){
      double solutionValue = 0;

      return solutionValue;
   }

   public boolean isSolutionBest(double iterationSolutionValue, double bestSolutionValue){
      boolean result = false;

      return result;
   }
}
