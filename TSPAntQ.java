public class TSPAntQ extends AntQ{
   public TSPAntQ(int numberOfIterations){
      super(numberOfIterations);
   }

   public double calculateSolutionValue(Edge[] solution){
      double solutionValue = 0;
      System.out.println("tsp calculate solution value");

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
