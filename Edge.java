/**
 * Class to represent the edges between the nodes.
 * 
 * It's formed by two nodes.
 * The edge value is the distance between these nodes.
 * The edge heuristic value (in TSP problem) is the inverse of the edge value.
 * Each edge has its AntQ value to represent how 'good' is the edge.
 * The edge still has the respective reinforcement learning value, used to update its AntQ value.
 * @author: Matheus Paixao 
 */
public class Edge {
   private Node node1;
   private Node node2;
   private double edgeValue;
   private double AQValue;

   /**
    * Method to create an edge passing two nodes and the edge value.
    *
    * The edge heuristic value is calculated only in the creation of the edge.
    * The AntQ Value and the reinforcement learning value are initiated with 0.
    * @author Matheus Paixao 
    * @param node1 First node of the edge. 
    * @param node2 Second node of the edge. 
    * @param edgeValue the value of the edge (distance between the two nodes)
    * @see calculateEdgeHeuristicValue
    */
   public Edge(Node node1, Node node2){
      this.node1 = node1;
      this.node2 = node2;
   }

   public Node getNode1(){
      return this.node1;
   }

   public Node getNode2(){
      return this.node2;
   }

   public double getEdgeValue(){
      return this.edgeValue;
   }

   public double getEdgeHeuristicValue(){
      return 1 / getEdgeValue();
   }

   public double getAQValue(){
      return this.AQValue;
   }

   public void setAQValue(double AQValue){
      this.AQValue = AQValue;
   }

   /**
    * Method to compare if an edge is equal to other one.
    *
    * An edge is equal to other if its nodes are equal.
    * @author Matheus Paixao
    * @param edge An Edge to compare.
    * @return true if the edges are equal, false if not.
    * @see equals method of Node class 
    */
   public boolean equals(Edge edge){
      boolean result = false;

      if(edge instanceof Edge){
         Node node1 = edge.getNode1();
         Node node2 = edge.getNode2();
         if((node1.equals(this.getNode1())) && (node2.equals(this.getNode2()))){
            result = true;
         }
      }

      return result;
   }

   /**
    * Method to calculate the value of an edge.
    *
    * The edge value is calculated using the distance between two points equation from analytic geometry.
    * @author Matheus Paixao
    * @param node1 first node of the edge
    * @param node2 second node of the edge
    * @return The edge value (distance between the two nodes).
    */
   private static double calculateEdgeValue(Node node1, Node node2){
      return Math.sqrt(Math.pow(node1.getX() - node2.getX(), 2) + 
            Math.pow(node1.getY() - node2.getY(), 2));
   }

   /**
    * Method to format the edge in String format.
    *
    * @author Matheus Paixao.
    * @return The edge in String format.
    * @see equals method of Node class.
    */
   public String toString(){
      return "(" + getNode1().toString() + " " + getNode2().toString() + ")";
   }
}
