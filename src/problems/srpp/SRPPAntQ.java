package problems.srpp;

import algorithms.antq.AntQ;
import algorithms.antq.Ant;
import util.Node;
import util.Edge;

/**
 * Class to implement the AntQ algorithm to the Software Requirement Priorization Problem.
 *
 * @author Matheus Paixao
 */
public class SRPPAntQ extends AntQ{
   SRPPProblem srppProblem;
   double maxPossibleHeuristicValue;

   /**
    * Method to create the SRRPAntQ object, receive SRPPProblem object and
    * the number of iterations is passed to AntQ constructor.
    *
    * @author Matheus Paixao
    * @param srppProblem the SRPPProblem object
    * @param numberOfIterations number of iterations to be runned
    * @see AntQ constructor
    * @see getMaxPossibleHeuristicValue
    */
   public SRPPAntQ(SRPPProblem srppProblem, int numberOfIterations){
      super(numberOfIterations);
      this.srppProblem = srppProblem;
      this.maxPossibleHeuristicValue = getMaxPossibleHeuristicValue();
   }

   public int getNumberOfNodes(){
      return srppProblem.getNumberOfRequirements();
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
    * @see getNumberOfRequirementsWithNoPrecedence in SRPPProblem class
    * @see getNumberOfNodes
    * @see Node constructor in Node class
    * @see hasPredecessor
    * @see addAnt
    * @see PrecedenceConstrainedAnt constructor in PrecedenceConstrainedAnt class.
    * @see getQ0 in Ant class
    * @see getPrecedencesMatrix in SRPPProblem class
    */
   protected void initAnts(){
      this.ants = new Ant[srppProblem.getNumberOfRequirementsWithNoPrecedence()]; 
      //this.ants = new Ant[1]; 
      Node initialNode = null;

      for(int i = 0; i <= getNumberOfNodes() - 1; i++){
         initialNode = new Node(i);
         if(hasPredecessor(initialNode) == false){
            addAnt(new PrecedenceConstrainedAnt(this, getQ0(), new Node(i), srppProblem.getPrecedencesMatrix()));
         }
      }
   }

   /**
    * Method to know if a requirement has some predecessor or don't.
    *
    * @author Matheus Paixao
    * @param requirement the requirement to test the predecessors
    * @return true if the requirement has some predecessor, false if don't
    * @see getPrecedencesMatrix in SRPPProblem class
    */
   private boolean hasPredecessor(Node requirement){
      int[][] precedencesMatrix = srppProblem.getPrecedencesMatrix();
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
    * @see getNumberOfClients in SRPPProblem class
    */
   private double getMaxPossibleHeuristicValue(){
      int numberOfClients = srppProblem.getNumberOfClients();
      double maxPossibleHeuristicValue = 0;

      for(int i = 0; i <= numberOfClients - 1; i++){
         maxPossibleHeuristicValue += 10 * 10;
      }

      return maxPossibleHeuristicValue + 9;
   }

   /**
    * Method to get the heuristic value of an edge.
    *
    * In SRPP as higher the score and the risk, higher is the heuristic value.
    * @author Matheus Paixao
    * @param node1 the first node of the edge
    * @param node2 the second node of the edge
    * @return the heuristic value of the edge composed by the two passed nodes
    * @see getObjectivesSum in SRPPProblem class
    */
   public double getHeuristicValue(Node node1, Node node2){
      return srppProblem.getObjectivesSum(node2.getIndex()) / maxPossibleHeuristicValue;
   }
   
   /**
    * Method that implements the fitness function of SRRP problem.
    *
    * @author Matheus Paixao
    * @param solution the array of edges that corresponds to the solution founded by the algorithm
    * @return fitness value of the solution
    * @see getNodes
    * @see calculateSolutionValue in SRPPProblem class
    */
   public double calculateSolutionValue(Edge[] solution){
      Integer[] nodes = getNodes(solution);
      return srppProblem.calculateSolutionValue(nodes);
   }

   /**
    * Method to, given an edge array, get an int array representing the solution.
    *
    * @author Matheus Paixao
    * @param solution the egde array
    * @return the int array representing the solution
    */
   private Integer[] getNodes(Edge[] solution){
      Integer[] nodes = new Integer[solution.length];

      for(int i = 0; i <= nodes.length - 1; i++){
         nodes[i] = solution[i].getNode1().getIndex();
      }

      return nodes;
   }

   /**
    * Method to compare if a solution value is better than another one.
    *
    * @author Matheus Paixao
    * @param iterationSolutionValue the fitness value of some solution
    * @param bestSolutionValue the best fitness value of an iteration
    * @return true if the first fitness value is best than the other one
    * @see isSolutionBest in SRPPProblem class
    */
   public boolean isSolutionBest(double iterationSolutionValue, double bestSolutionValue){
      return srppProblem.isSolutionBest(iterationSolutionValue, bestSolutionValue);
   }
}
