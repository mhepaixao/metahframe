import java.io.File;
import java.io.FileReader;
import java.io.BufferedReader;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import java.util.ArrayList;
import java.util.Collections;

public class TSPInstanceReader{
   private File instance;

   private Matcher twoLettersMatcher;
   private Matcher numbersMatcher;
   private Matcher spacesMatcher;

   public TSPInstanceReader(File instance){
      setInstance(instance);
   }

   private File getInstance(){
      return this.instance;
   }

   private void setInstance(File instance){
      this.instance = instance;
   }

   public double[][] getDistancesMatrix(){
      Node[] nodes = getNodesList();
      double[][] distancesMatrix = new double[nodes.length][nodes.length];

      for(int i = 0; i <= distancesMatrix.length - 1; i++){
         for(int j = 0; j <= distancesMatrix[0].length - 1; j++){
            distancesMatrix[i][j] = calculateDistance(nodes[i], nodes[j]);
         }
      }

      return distancesMatrix;
   }

   /**
    * Method to get the list of nodes when the instance is in cartesian coordinates format.
    *
    * It is used an auxiliary object. dynamicListOfNodes is an ArrayList of Node
    * and is used to create the nodes in a dynamic form. The AntQ algorithm uses an 
    * array of nodes. So, after create all the nodes the dynamicListOfNodes is casted
    * to a simple Node array.
    *
    * The cartesian coordinates have to be in one of the formats that follows:
    * 1) n Xe+P Ye+P, where n is the line number, X is the x cartesian value x*(10^P) and Y is the y cartesian value y*(10^P)  
    * 2) n X Y, where n is the line number, X is the x cartesian value and Y is the y cartesian value  
    * 3) Xe+P Ye+P, where X is the x cartesian value x*(10^P) and Y is the y cartesian value y*(10^P)  
    * 4) X Y, where X is the x cartesian value and Y is the y cartesian value  
    *
    * @author Matheus Paixao
    * @return a Node array containing all the nodes of the instance.
    * @see getInstance
    * @see toArray method from ArrayList class
    */
   private Node[] getNodesList(){
      Node[] nodes = null; //array to return
      ArrayList<Node> dynamicListOfNodes = new ArrayList<Node>(); 
      File instance = getInstance();

      try{
         String instanceLine = null;
         String[] values;
         Node node = null;
         int instanceLineCounter = 0; //will serve as a node id

         BufferedReader reader = new BufferedReader(new FileReader(instance));
         while(reader.ready()){
            instanceLine = reader.readLine();

            Matcher twoLettersMatcher = Pattern.compile("[a-z][a-z]").matcher(instanceLine) ;
            Matcher numbersMatcher = Pattern.compile("[0-9]").matcher(instanceLine) ;

            //if it isn't a text line and has numbers
            if((twoLettersMatcher.find() == false) && (numbersMatcher.find() == true)){
               Matcher spacesMatcher = Pattern.compile("\\s{2,}").matcher(instanceLine);
               instanceLine = spacesMatcher.replaceAll(" ").trim(); //replace all spaces for just one

               values = instanceLine.split(" ");
               if(values.length == 3){
                  node = getCartesianNode(instanceLineCounter, values[1], values[2]); //formats 1 and 2
               }
               else{
                  node = getCartesianNode(instanceLineCounter, values[0], values[1]); //formats 3 and 4
               }

               instanceLineCounter++;
            }

            if(node != null){
               dynamicListOfNodes.add(node);
            }
         }
      }
      catch(Exception e){
         e.printStackTrace();
      }

      Collections.sort(dynamicListOfNodes);
      removeDuplicatedNodes(dynamicListOfNodes);
      setNodesIndexes(dynamicListOfNodes);

      //cast the dynamicListOfNodes to a Node array
      nodes = new Node[dynamicListOfNodes.size()];
      for(int i = 0; i <= nodes.length - 1; i++){
         nodes[i] = dynamicListOfNodes.get(i);
      }

      return nodes;
   }

   /**
    * Method to get the node when the values are in String format.
    *
    * @author Matheus Paixao
    * @param id the id of the node.
    * @param value1 the x cartesian value in String format.
    * @param value2 the y cartesian value in String format.
    * @return the node with the x and y coordinates.
    */
   private Node getCartesianNode(int id, String value1, String value2){
      double x = Double.parseDouble(value1);
      double y = Double.parseDouble(value2);

      return new Node(id, x, y);
   }

   /**
    * Method to delete duplicate nodes in the instance.
    *
    * The list of nodes must be sorted.
    * @author Matheus Paixao
    * @param dynamicListOfNodes the list of nodes to delete duplicated nodes.
    */
   private void removeDuplicatedNodes(ArrayList<Node> dynamicListOfNodes){
      Node node = null;
      Node nextNode = null;

      for(int i = 0; i <= dynamicListOfNodes.size() - 1; i++){
         if(i != dynamicListOfNodes.size() - 1){
            node = dynamicListOfNodes.get(i);
            nextNode = dynamicListOfNodes.get(i + 1);

            if(node.equals(nextNode)){
               dynamicListOfNodes.remove(i);
            }
         }
      }
   }

   /**
    * Method to set the index of each node of the list.
    *
    * After sort and delete duplicate nodes, it's necessary set new indexes.
    * @author Matheus Paixao
    * @param dynamicListOfNodes the list od nodes to set a new index
    * @see setIndex in Node class
    */
   private void setNodesIndexes(ArrayList<Node> dynamicListOfNodes){
      for(int i = 0; i <= dynamicListOfNodes.size() - 1; i++){
         dynamicListOfNodes.get(i).setIndex(i);
      }
   }

   /**
    * Method to calculate the distance between two nodes.
    *
    * The distance is calculated using the equation from analytic geometry.
    * @author Matheus Paixao
    * @param node1 first node
    * @param node2 second node
    * @return the distance between the two nodes.
    */
   private static double calculateDistance(Node node1, Node node2){
      return Math.sqrt(Math.pow(node1.getX() - node2.getX(), 2) + 
            Math.pow(node1.getY() - node2.getY(), 2));
   }
}
