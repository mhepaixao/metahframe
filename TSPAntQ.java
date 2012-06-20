import java.io.File;

public class TSPAntQ extends AntQ{
   private TSPInstanceReader tspInstanceReader;

   private double[][] distancesMatrix;

   public TSPAntQ(File instance, int numberOfIterations){
      super(numberOfIterations);
      tspInstanceReader = new TSPInstanceReader(instance);
      distancesMatrix = tspInstanceReader.getDistancesMatrix();
   }

   public double calculateSolutionValue(Edge[] solution){
      double solutionValue = 0;

      Edge edge = null;
      for(int i = 0; i <= solution.length - 1; i++){
         edge = solution[i];
         solutionValue += distancesMatrix[edge.getNode1().getIndex()][edge.getNode2().getIndex()];
      }

      return solutionValue;
   }

   public boolean isSolutionBest(double iterationSolutionValue, double bestSolutionValue){
      boolean result = false;

      if(iterationSolutionValue < bestSolutionValue){
         result = true;
      }

      return result;
   }
}
