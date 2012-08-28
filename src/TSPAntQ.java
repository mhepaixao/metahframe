import java.io.File;

/**
 * Class to implement the AntQ class to the Travel Salesman Problem.
 *
 * @author Matheus Paixao
 */
public class TSPAntQ extends AntQ{
   private TSPInstanceReader tspInstanceReader;

   private int numberOfCities;
   private double[][] distancesMatrix;
   private double initialPheromone;

   /**
    * Method to create the TSPAntQ object, receive the instance to read and
    * the number of iterations is passed to AntQ constructor.
    *
    * @author Matheus Paixao
    * @param instance the instance to read
    * @param numberOfIterations number of iterations to be runned
    * @see AntQ constructor
    * @see TSPInstanceReader constructor
    * @see getDistancesMatrix in TSPInstanceReader
    * @see calculateInitialPheromone
    */
   public TSPAntQ(File instance, int numberOfIterations){
      super(numberOfIterations);
      this.tspInstanceReader = new TSPInstanceReader(instance);
      this.distancesMatrix = tspInstanceReader.getDistancesMatrix();
      this.numberOfCities = distancesMatrix.length; 
      this.initialPheromone = calculateInitialPheromone();
   }

   public int getNumberOfNodes(){
      return this.numberOfCities;
   }

   public double getInitialPheromone(){
      return this.initialPheromone;
   }

   /**
    * Method that implements the fitness function of TSP problem.
    *
    * @author Matheus Paixao
    * @param solution the array of edges that corresponds to the solution founded by the algorithm
    * @return fitness value of the solution
    */
   public double calculateSolutionValue(Edge[] solution){
      double solutionValue = 0;

      Edge edge = null;
      for(int i = 0; i <= solution.length - 1; i++){
         edge = solution[i];
         solutionValue += distancesMatrix[edge.getNode1().getIndex()][edge.getNode2().getIndex()];
      }

      return solutionValue;
   }

   /**
    * Method to compare if a solution value is better than another one.
    *
    * In TSP as smaller fitness value as better.
    * @author Matheus Paixao
    * @param iterationSolutionValue the fitness value of some solution
    * @param bestSolutionValue the best fitness value of an iteration
    * @return true if the first fitness value is best than the other one
    */
   public boolean isSolutionBest(double iterationSolutionValue, double bestSolutionValue){
      boolean result = false;

      if(iterationSolutionValue < bestSolutionValue){
         result = true;
      }

      return result;
   }

   /**
    * Method to get the initial pheromone value for all edges.
    *
    * The initial pheromone value is composed with the distances average and the number of nodes.
    * @author Matheus Paixao
    * @return the initial pheromone value for all edges.
    * @see getNumberOfEdges
    */
   public double calculateInitialPheromone(){
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

   /**
    * Method to get the heuristic value of an edge.
    *
    * In TSP as smaller the distance, higher is the heuristic value.
    * @author Matheus Paixao
    * @param node1 the first node of the edge
    * @param node2 the second node of the edge
    * @return the heuristic value of the edge composed by the two passed nodes
    */
   public double getHeuristicValue(Node node1, Node node2){
      return 1 / distancesMatrix[node1.getIndex()][node2.getIndex()];
   }
}
