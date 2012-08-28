/**
 * Class to represent the edges between the nodes.
 * 
 * It's formed by two nodes.
 * @author Matheus Paixao 
 */
public class Edge {
   private Node node1;
   private Node node2;

   /**
    * Method to create an edge passing two nodes.
    *
    * @author Matheus Paixao 
    * @param node1 First node of the edge. 
    * @param node2 Second node of the edge. 
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
