/**
 * Class to represent the nodes.
 * 
 * It's formed by the index in the nodes array of AntQ algorithm, X and Y value of cartesian coordinates.
 * @author: Matheus Paixao 
 */
public class Node implements Comparable{
   private int index;
   private double x;
   private double y;

   /**
    * Method to create a node passing the index, X and Y coordinates.
    *
    * @author Matheus Paixao 
    * @param index index of the node
    * @param x X coordinate of the node. 
    * @param y Y coordinate of the node. 
    */
   public Node(int index, double x, double y){
      this.index = index;
      this.x = x;
      this.y = y;
   }

   /**
    * Method to create a node passing only the index.
    *
    * The 'x' and 'y' values are set to 0.
    * @author Matheus Paixao 
    * @param index index of the node
    */
   public Node(int index){
      this.index = index;
      this.x = 0;
      this.y = 0;
   }

   public int getIndex(){
      return this.index;
   }

   public void setIndex(int index){
      this.index = index;
   }

   public double getX(){
      return this.x;
   }

   public double getY(){
      return this.y;
   }

   /**
    * Method to compare if a node is equal to other one.
    *
    * A node is equal to other if they have the same index, if the index isn't the same then the x and y values are tested.
    * @author Matheus Paixao
    * @param node A node to compare.
    * @return true if the nodes are equal, false if not.
    */
   public boolean equals(Node node){
      boolean result = false;

      if(node instanceof Node){
         if(node.getIndex() == this.getIndex()){
            result = true;
         }
         else{
            if(node.getX() == this.getX() && node.getY() == this.getY()){
               result = true;
            }
         }
      }

      return result;
   }

   /**
    * Method to compare two nodes.
    *
    * It's used to sort the list of nodes. 
    * It's compared first the x value, if it is equal then the y value is compared.
    * @author Matheus Paixao
    * @param object the node to compare (the compareTo method of Comparable interface receive a Object)
    * @return -1 if the node is "lesser", 0 if equal and 1 if the node is "higher".
    */
   public int compareTo(Object object){
      int result = 0;
      Node node = null;

      if(object instanceof Node){
         node = (Node) object;

         if(this.getX() != node.getX()){
            if(this.getX() > node.getX()){
               result = 1;
            }
            else{
               result = -1;
            }
         }
         else{
            if(this.getY() > node.getY()){
               result = 1;
            }
            else{
               result = -1;
            }
         }
      }

      return result;
   }

   /**
    * Method to format the node in String format.
    *
    * @author Matheus Paixao.
    * @return The node in String format.
    */
   public String toString(){
      return getIndex() + " " + "(" + getX() + " " + getY() + ")";
   }
}
