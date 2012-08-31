package problems.srpp;

import algorithms.antq.AntQ;
import algorithms.antq.Ant;
import util.Node;

import java.util.ArrayList;

/**
 * Class to extend the behavior of a generic Ant.
 *
 * It's used in the Software Requirement Priorization Problem.
 * This kind of ant can deal with the constraints of precedence between nodes (requirements).
 * @author Matheus Paixao
 */
public class PrecedenceConstrainedAnt extends Ant{
   int[][] precedencesMatrix;

   /**
    * Method to create a precedence constrained ant with its initial node.
    *
    * The precedences matrix is now added to the ant.
    * @author Matheus Paixao
    * @param antQ the AntQ object
    * @param q0 the q0 value used in the transition rule
    * @param initialNode the node that will be the initial node of the ant.
    * @param precedencesMatrix matrix which describes the precedences between the requirements
    * @see Ant constructor in Ant class
    */
   public PrecedenceConstrainedAnt(AntQ antQ, double q0, Node initialNode, int[][] precedencesMatrix){
      super(antQ, q0, initialNode);
      this.precedencesMatrix = precedencesMatrix;
   }

   /**
    * Method to return if the ant have finished it's tour or not.
    *
    * This kind of ant can finish its tour before the (nodes - 1) iteration of AntQ algorithm.
    * @author Matheus Paixao
    */
   public boolean isTourFinished(){
      boolean result = false;

      if(tour[tour.length - 2] != null){
         result = true;
      }

      return result;
   }

   /**
    * Method to choose the next rule using the transition rule of AntQ, but dealing
    * with the precedences constraints.
    *
    * Uses the chooseNextNode method of Ant class and after choose the next node add its
    * predecessors if necessary.
    * @author Matheus Paixao
    * @return the next node of a an ant
    * @see chooseNextNode of super class (Ant)
    * @see hasPrecedecessorInNodesToVisit
    * @see addAllPredecessors
    */
   public Node chooseNextNode(){
      Node nextNode = super.chooseNextNode();

      if(hasPrecedecessorInNodesToVisit(nextNode) == true){
         addAllPredecessors(nextNode);
      }

      return nextNode;
   }

   /**
    * Method to know if a requirment has some unvisited predecessor.
    *
    * @author Matheus Paixao
    * @param requirement the requirement to know if it has unvisited predecessors
    * @return true if the requirement has unvisited predecessors, false if hasn't
    */
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

   /**
    * Method to add all unvisited predecessors of a requirement to the tour.
    *
    * @author Matheus Paixao
    * @param requirement the requirement to add all unvisited predecessors
    * @see getPredecessors
    * @see addPredecessor
    */
   private void addAllPredecessors(Node requirement){
      ArrayList<Node> predecessors = getPredecessors(requirement);

      for(int i = 0; i <= predecessors.size() - 1; i++){
         addPredecessor(predecessors.get(i));
      }
   }

   /**
    * Method to get a list containing all unvisited predecessors of a requirement.
    *
    * @author Matheus Paixao
    * @param requirement the requirement to get all unvisited predecessors
    * @return list containing all unvisited predecessors of the requirement
    */
   private ArrayList<Node> getPredecessors(Node requirement){
      ArrayList<Node> predecessors = new ArrayList<Node>();

      for(int i = 0; i <= precedencesMatrix[requirement.getIndex()].length - 1; i++){
         if(precedencesMatrix[requirement.getIndex()][i] == 1 && nodesToVisit[i] != null){
            predecessors.add(nodesToVisit[i]);
         }
      }

      return predecessors;
   }

   /**
    * Method to add a singles predecessor to the tour.
    *
    * It's also added the predecessors of the predecessor if necessary.
    * @author Matheus Paixao
    * @param predecessor the predecessor to be added
    * @see hasPrecedecessorInNodesToVisit
    * @see addAllPredecessors
    * @see addNodeToTour in Ant class
    * @see setCurrentNode in Ant class
    * @see removeNodeFromNodesToVisit in Ant class
    */
   private void addPredecessor(Node predecessor){
      if(hasPrecedecessorInNodesToVisit(predecessor) == true){
         addAllPredecessors(predecessor);
      }

      addNodeToTour(predecessor);
      setCurrentNode(predecessor);
      removeNodeFromNodesToVisit(predecessor);
   }
}
