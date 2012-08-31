import java.io.File;

/**
 * Class to implement the AntQ class to the Software Requirement Priorization Problem.
 *
 * This problem is modeled in the paper:
 * "Aplicando o Algoritmo Ant-Q na Priorização de Requisitos de Software com Precedência",
 * published at WESB 2012
 * @author Matheus Paixao
 */
public class SRPPAntQ extends AntQ{
   SRPPInstanceReader srppInstanceReader;

   double[][] objectivesValues; //value of each objective for each requirement
   int numberOfRequirements;
   int numberOfClients;
   double maxPossibleHeuristicValue;
   int[][] precedencesMatrix;

   /**
    * Method to create the SRRPAntQ object, receive the instance to read and
    * the number of iterations is passed to AntQ constructor.
    *
    * @author Matheus Paixao
    * @param instance the instance to read
    * @param numberOfIterations number of iterations to be runned
    * @see AntQ constructor
    * @see SRPPInstanceReader constructor
    * @see getObjectiveValues in SRPPInstanceReader
    * @see getNumberOfRequirements in SRPPInstanceReader
    * @see getNumberOfClients in SRPPInstanceReader
    * @see getMaxPossibleHeuristicValue
    * @see getPrecedencesMatrix in SRPPInstanceReader
    */
   public SRPPAntQ(File instance, int numberOfIterations){
      super(numberOfIterations);
      srppInstanceReader = new SRPPInstanceReader(instance);
      objectivesValues = srppInstanceReader.getObjectiveValues();
      this.numberOfRequirements = srppInstanceReader.getNumberOfRequirements();
      this.numberOfClients = srppInstanceReader.getNumberOfClients();
      this.maxPossibleHeuristicValue = getMaxPossibleHeuristicValue();
      this.precedencesMatrix = srppInstanceReader.getPrecedencesMatrix();
   }

   public int getNumberOfNodes(){
      return getNumberOfRequirements();
   }

   private int getNumberOfRequirements(){
      return this.numberOfRequirements;
   }

   private int[][] getPrecedencesMatrix(){
      return this.precedencesMatrix;
   }

   public double getInitialPheromone(){
      return 0.01; //this initial pheromone value was founded empirically
   }

   /**
    * Method to init the precedence constrained ants.
    *
    * Override the initAnts method in Ant class. 
    * The SRRP problem needs an ant that can deal with precedence constraints.
    * One ant is placed in each requirement with no predecessors of the instance.
    * @author Matheus Paixao
    * @see getNumberOfRequirementsWithNoPrecedence in SRPPInstanceReader class
    * @see getNumberOfRequirements
    * @see Node constructor in Node class
    * @see hasPredecessor
    * @see addAnt
    * @see PrecedenceConstrainedAnt constructor in PrecedenceConstrainedAnt class.
    * @see getQ0 in Ant class
    * @see getPrecedencesMatrix
    */
   protected void initAnts(){
      this.ants = new Ant[srppInstanceReader.getNumberOfRequirementsWithNoPrecedence()]; 
      //this.ants = new Ant[1]; 
      Node initialNode = null;

      for(int i = 0; i <= getNumberOfRequirements() - 1; i++){
         initialNode = new Node(i);
         if(hasPredecessor(initialNode) == false){
            addAnt(new PrecedenceConstrainedAnt(this, getQ0(), new Node(i), getPrecedencesMatrix()));
         }
      }
   }

   /**
    * Method to know if a requirement has some predecessor or don't.
    *
    * @author Matheus Paixao
    * @param requirement the requirement to test the predecessors
    * @return true if the requirement has some predecessor, false if don't
    */
   private boolean hasPredecessor(Node requirement){
      boolean result = false;

      for(int i = 0; i <= precedencesMatrix[requirement.getIndex()].length - 1; i++){
         if(precedencesMatrix[requirement.getIndex()][i] == 1){
            result = true;
            break;
         }
      }

      return result;
   }

   /**
    * Method to add an ant to the ants array.
    *
    * @author Matheus Paixao
    * @param ant the ant to be added
    */
   private void addAnt(Ant ant){
      for(int i = 0; i <= this.ants.length - 1; i++){
         if(this.ants[i] == null){
            this.ants[i] = ant;
            break;
         }
      }
   }

   /**
    * Method to get the max possible heuristic value of the instance.
    *
    * It's used as a normalization factor to calculate the heuristic value.
    * @author Matheus Paixao
    * @return the max possible heuristic value of the instance
    * @see the SRRP instance format in SRPPInstanceReader class
    */
   private double getMaxPossibleHeuristicValue(){
      double maxPossibleHeuristicValue = 0;

      for(int i = 0; i <= this.numberOfClients - 1; i++){
         maxPossibleHeuristicValue += 10 * 10;
      }

      return maxPossibleHeuristicValue + 9;
   }

   /**
    * Method to get the heuristic value of an edge.
    *
    * In TSP as higher the score and the risk, higher is the heuristic value.
    * @author Matheus Paixao
    * @param node1 the first node of the edge
    * @param node2 the second node of the edge
    * @return the heuristic value of the edge composed by the two passed nodes
    */
   public double getHeuristicValue(Node node1, Node node2){
      return getObjectivesSum(node2.getIndex()) / maxPossibleHeuristicValue;
   }
   
   /**
    * Method that implements the fitness function of SRRP problem.
    *
    * This fitness function is described in the paper.
    * @author Matheus Paixao
    * @param solution the array of edges that corresponds to the solution founded by the algorithm
    * @return fitness value of the solution
    * @see getNodes
    * @see getObjectivesSum
    */
   public double calculateSolutionValue(Edge[] solution){
      double solutionValue = 0;
      int[] nodes = getNodes(solution);

      for(int i = 0; i <= nodes.length - 1; i++){
         solutionValue += (nodes.length - i) * getObjectivesSum(nodes[i]);
      }

      return solutionValue;
   }

   /**
    * Method to, given an edge array, get an int array representing the solution.
    *
    * @author Matheus Paixao
    * @param solution the egde array
    * @return the int array representing the solution
    */
   private int[] getNodes(Edge[] solution){
      int[] nodes = new int[solution.length];

      for(int i = 0; i <= nodes.length - 1; i++){
         nodes[i] = solution[i].getNode1().getIndex();
      }

      return nodes;
   }

   /**
    * Method to get the sum of all objectives values for a requirement.
    *
    * @author Matheus Paixao
    * @param requirement the requirement to know the objectives sum
    * @return the sum of all objectives values for the requirement
    */
   private double getObjectivesSum(int requirement){
      double objectivesSum = 0;

      for(int i = 0; i <= objectivesValues.length - 1; i++){
         objectivesSum += objectivesValues[i][requirement];
      }

      return objectivesSum;
   }
   
   /**
    * Method to compare if a solution value is better than another one.
    *
    * In SRPP as bigger fitness value as better.
    * @author Matheus Paixao
    * @param iterationSolutionValue the fitness value of some solution
    * @param bestSolutionValue the best fitness value of an iteration
    * @return true if the first fitness value is best than the other one
    */
   public boolean isSolutionBest(double iterationSolutionValue, double bestSolutionValue){
      boolean result = false;

      if(iterationSolutionValue > bestSolutionValue){
         result = true;
      }

      return result;
   }
}
