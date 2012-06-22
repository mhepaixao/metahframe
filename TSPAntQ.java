import java.io.File;

public class TSPAntQ extends AntQ{
   private TSPInstanceReader tspInstanceReader;

   private int numberOfNodes;
   private double[][] distancesMatrix;

   public TSPAntQ(File instance, int numberOfIterations){
      super(numberOfIterations);
      this.tspInstanceReader = new TSPInstanceReader(instance);
      this.distancesMatrix = tspInstanceReader.getDistancesMatrix();
      this.numberOfNodes = distancesMatrix.length; 
   }

   public int getNumberOfNodes(){
      return this.numberOfNodes;
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

   /**
    * Method to get the initial AQ value for all edges.
    *
    * The initial AQ value is composed with the distances average and the number of nodes.
    * @author Matheus Paixao
    * @return the initial AQ value for all edges.
    * @see getNumberOfEdges
    */
   public double getInitialPheromone(){
      double sumOfDistances = 0;
      double distancesAverage = 0;

      for(int i = 0; i <= distancesMatrix.length - 1; i++){
         for(int j = 0; j <= distancesMatrix[0].length - 1; j++){
            sumOfDistances += distancesMatrix[i][j];
         }
      }
      distancesAverage = sumOfDistances / getNumberOfEdges();
      
      return 1 / (distancesAverage * distancesMatrix.length);
   }

   /**
    * Method to get the number of existing edges.
    *
    * When 'i' is equal to 'j' there is no edge.
    * @author Matheus Paixao
    * @return the number of existing edges.
    */
   private int getNumberOfEdges(){
      int numberOfEdges = 0;

      for(int i = 0; i <= distancesMatrix.length - 1; i++){
         for(int j = 0; j <= distancesMatrix[0].length - 1; j++){
            if(i != j){
               numberOfEdges++;
            }
         }
      }

      return numberOfEdges;
   }

   public double getHeuristicValue(Node node1, Node node2){
      return 1 / distancesMatrix[node1.getIndex()][node2.getIndex()];
   }
}
