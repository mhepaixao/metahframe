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
   private City city1;
   private City city2;
   private double edgeValue;
   private double AQValue;

   /**
    * Method to create an edge passing two cities.
    *
    * This method calculates the edge value and call the other Edge constructor.
    * @author Matheus Paixao 
    * @param city1 First city of the edge. 
    * @param city2 Second city of the edge. 
    * @see calculateEdgeValue
    * @see Edge other constructor
    */
   public Edge(City city1, City city2){
      this(city1, city2, calculateEdgeValue(city1, city2));
   }

   /**
    * Method to create an edge passing two cities and the edge value.
    *
    * The edge heuristic value is calculated only in the creation of the edge.
    * The AntQ Value and the reinforcement learning value are initiated with 0.
    * @author Matheus Paixao 
    * @param city1 First city of the edge. 
    * @param city2 Second city of the edge. 
    * @param edgeValue the value of the edge (distance between the two cities)
    * @see calculateEdgeHeuristicValue
    */
   public Edge(City city1, City city2, double edgeValue){
      this.city1 = city1;
      this.city2 = city2;

      this.edgeValue = edgeValue;

      this.AQValue = 0;
   }

   public City getCity1(){
      return this.city1;
   }

   public City getCity2(){
      return this.city2;
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
         City city1 = edge.getCity1();
         City city2 = edge.getCity2();
         if((city1.equals(this.getCity1())) && (city2.equals(this.getCity2()))){
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
    * @param city1 first city of the edge
    * @param city2 second city of the edge
    * @return The edge value (distance between the two cities).
    */
   private static double calculateEdgeValue(City city1, City city2){
      return Math.sqrt(Math.pow(city1.getX() - city2.getX(), 2) + 
            Math.pow(city1.getY() - city2.getY(), 2));
   }

   /**
    * Method to format the edge in String format.
    *
    * @author Matheus Paixao.
    * @return The edge in String format.
    * @see equals method of Node class.
    */
   public String toString(){
      return getCity1().toString() + " " + getCity2().toString();
   }
}
