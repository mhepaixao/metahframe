package instancereaders;

import util.Node;

import java.io.File;
import java.io.FileReader;
import java.io.BufferedReader;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import java.util.ArrayList;
import java.util.Collections;

/**
 * Class used to read the TSP instance.
 *
 * @author Matheus Paixao
 */
public class TSPInstanceReader{
   private File instance;

   //regex matchers
   private Matcher twoLettersMatcher;
   private Matcher numbersMatcher;
   private Matcher spacesMatcher;

   /**
    * Method to create a TSPInstanceReader.
    *
    * @author Matheus Paixao
    * @param instance the instance to read
    * @see setInstance
    */
   public TSPInstanceReader(File instance){
      setInstance(instance);
   }

   private File getInstance(){
      return this.instance;
   }

   private void setInstance(File instance){
      this.instance = instance;
   }

   /**
    * Method to get the distances matrix.
    *
    * The instance can be in cartesian coordinates or synmetric matrix format.
    * @author Matheus Paixao
    * @return the distances matrix
    * @see getInstanceFormat
    * @see getDistancesMatrixInCartesianFormatInstance
    * @see getDistancesMatrixInMatrixFormatInstance
    */
   public double[][] getDistancesMatrix(){
      double[][] distancesMatrix = null;
      String instanceType = getInstanceFormat();

      if(instanceType.equals("coordinates")){
         distancesMatrix = getDistancesMatrixInCartesianFormatInstance();
      }
      else if(instanceType.equals("matrix")){
         distancesMatrix = getDistancesMatrixInMatrixFormatInstance();
      }

      return distancesMatrix;
   }

   /**
    * Method to get the format of the instance.
    *
    * @author Matheus Paixao
    * @return the format of the instance.
    * @see regex package
    * @see getInstance
    */
   private String getInstanceFormat(){
      String instanceType = null;
      String instanceLine = null;
      String values[] = null;

      try{
         BufferedReader reader = new BufferedReader(new FileReader(getInstance()));
         while(reader.ready()){
            instanceLine = reader.readLine();

            twoLettersMatcher = Pattern.compile("[a-z][a-z]").matcher(instanceLine) ;
            numbersMatcher = Pattern.compile("[0-9]").matcher(instanceLine) ;

            //if it isn't a text line and has numbers
            if((twoLettersMatcher.find() == false) && (numbersMatcher.find() == true)){
               spacesMatcher = Pattern.compile("\\s{2,}").matcher(instanceLine);
               instanceLine = spacesMatcher.replaceAll(" ").trim(); //replace all spaces for just one

               values = instanceLine.split(" ");
               if(values.length > 3){
                  instanceType = "matrix";
                  break;
               }
               else{
                  instanceType = "coordinates";
                  break;
               }
            }
         }
      }
      catch(Exception e){
         System.out.println("Get instance format error");
      }

      return instanceType;
   }

   /**
    * Method to get the distances matrix when the instance is in cartesian coordinates format.
    *
    * @author Matheus Paixao
    * @return the distances matrix
    * @see getNodesList
    * @see calculateDistance
    */
   private double[][] getDistancesMatrixInCartesianFormatInstance(){
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
    * array of nodes. So, after create all the nodes the dynamicListOfNodes is sorted,
    * the duplicate nodes are removed and the ArrayList is casted to a simple Node array.
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
    * @see getCartesianNode
    * @see removeDuplicatedNodes
    * @see setNodesIndexes
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
    * @param dynamicListOfNodes the list of nodes to remove duplicated nodes.
    */
   private void removeDuplicatedNodes(ArrayList<Node> dynamicListOfNodes){
      Node node = null;
      Node nextNode = null;

      for(int i = 0; i <= dynamicListOfNodes.size() - 2; i++){
         node = dynamicListOfNodes.get(i);
         nextNode = dynamicListOfNodes.get(i + 1);

         if(node.equals(nextNode)){
            dynamicListOfNodes.remove(i);
         }
      }
   }

   /**
    * Method to set the index of each node of the list.
    *
    * After sort and delete duplicate nodes, it's necessary set new indexes.
    * @author Matheus Paixao
    * @param dynamicListOfNodes the list of nodes to set a new index
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

   /**
    * Method to get the distances matrix when the instance is in matrix format.
    *
    * The matrix must be in one of the formats that follows:
    * 1) Symmetric matrix, where the '0's delimits each row
    *
    * Let n be the number of nodes.
    * 2) Symmetric matrix, where the first row is A1, A2 to A1, An. And the last row is An-1, An.
    *
    * @author Matheus Paixao
    * @return the distances matrix
    * @see getNumberOfNodesInMatrixFormat
    */
   public double[][] getDistancesMatrixInMatrixFormatInstance(){
      double distancesMatrix[][] = null;

      try{
         String instanceLine = null;
         String[] values;

         BufferedReader reader = new BufferedReader(new FileReader(getInstance()));
         while(reader.ready()){
            instanceLine = reader.readLine();

            Matcher twoLettersMatcher = Pattern.compile("[a-z][a-z]").matcher(instanceLine) ;
            Matcher numbersMatcher = Pattern.compile("[0-9]").matcher(instanceLine) ;

            //if it isn't a text line and has numbers
            if((twoLettersMatcher.find() == false) && (numbersMatcher.find() == true)){
               Matcher spacesMatcher = Pattern.compile("\\s{2,}").matcher(instanceLine);
               instanceLine = spacesMatcher.replaceAll(" ").trim(); //replace all spaces for just one

               values = instanceLine.split(" ");

               //if the matrix is in format 1
               if(Double.parseDouble(values[0]) == 0.0){
                  distancesMatrix = getDistancesMatrixInMatrixFormat1();
               }
               else{
                  distancesMatrix = getDistancesMatrixInMatrixFormat2();
               }

               //read only the first line of values
               break;
            }
         }
      }
      catch(Exception e){
         System.out.println("Error in get edges values matrix");
         e.printStackTrace();
      }

      return distancesMatrix;
   }

   /**
    * Method to get the distances matrix when the matrix instance is in format 1.
    *
    * Format 1: Symmetric matrix, where the '0's delimits each row.
    * @author Matheus Paixao
    * @return the distances matrix
    * @see getNumberOfNodesInMatrixFormat1
    */
   private double[][] getDistancesMatrixInMatrixFormat1(){
      int numberOfNodes = getNumberOfNodesInMatrixFormat1();
      double distancesMatrix[][] = new double[numberOfNodes][numberOfNodes];

      try{
         String instanceLine = null;
         String[] values;
         int rowIndex = -1;
         int columnIndex = -1;

         BufferedReader reader = new BufferedReader(new FileReader(getInstance()));
         while(reader.ready()){
            instanceLine = reader.readLine();

            Matcher twoLettersMatcher = Pattern.compile("[a-z][a-z]").matcher(instanceLine) ;
            Matcher numbersMatcher = Pattern.compile("[0-9]").matcher(instanceLine) ;

            //if it isn't a text line and has numbers
            if((twoLettersMatcher.find() == false) && (numbersMatcher.find() == true)){
               Matcher spacesMatcher = Pattern.compile("\\s{2,}").matcher(instanceLine);
               instanceLine = spacesMatcher.replaceAll(" ").trim(); //replace all spaces for just one

               values = instanceLine.split(" ");

               //format 1
               for(int i = 0; i <= values.length - 1; i++){
                  //if it's a new row of the matrix
                  if(Double.parseDouble(values[i]) == 0.0){
                     rowIndex++;
                     columnIndex = -1;
                  }
                  else{
                     columnIndex++;
                  }

                  //symmetric matrix
                  distancesMatrix[rowIndex][rowIndex + (columnIndex + 1)] = Double.parseDouble(values[i]);
                  distancesMatrix[rowIndex + (columnIndex + 1)][rowIndex] = Double.parseDouble(values[i]);
               }
            }
         }
      }
      catch(Exception e){
         System.out.println("Error in get edges values matrix in format 1");
         e.printStackTrace();
      }

      return distancesMatrix;
   }

   /**
    * Method to get the number of nodes when the instance is in matrix format 1.
    *
    * Format 1: Symmetric matrix, where the '0's delimits each row.
    * @author Matheus Paixao
    * @return the number of nodes 
    */
   private int getNumberOfNodesInMatrixFormat1(){
      int numberOfNodes = 0;

      try{
         String instanceLine = null;
         String[] values;

         BufferedReader reader = new BufferedReader(new FileReader(getInstance()));
         while(reader.ready()){
            instanceLine = reader.readLine();

            Matcher twoLettersMatcher = Pattern.compile("[a-z][a-z]").matcher(instanceLine) ;
            Matcher numbersMatcher = Pattern.compile("[0-9]").matcher(instanceLine) ;

            //if it isn't a text line and has numbers
            if((twoLettersMatcher.find() == false) && (numbersMatcher.find() == true)){
               Matcher spacesMatcher = Pattern.compile("\\s{2,}").matcher(instanceLine);
               instanceLine = spacesMatcher.replaceAll(" ").trim(); //replace all spaces for just one

               values = instanceLine.split(" ");

               for(int i = 0; i <= values.length - 1; i++){
                  //if it's a new row of the matrix
                  if(Double.parseDouble(values[i]) == 0.0){
                     numberOfNodes++;
                  }
               }
            }

         }
      }
      catch(Exception e){
         System.out.println("Error in get number od nodes in matrix format 1");
         e.printStackTrace();
      }

      return numberOfNodes;
   }

   /**
    * Method to get the distances matrix when the matrix is in format 2.
    *
    * Let n be the number of nodes.
    * Format 2: Symmetric matrix, where the first row is A1, A2 to A1, An. And the last row is An-1, An.
    * @author Matheus Paixao
    * @return the distances matrix
    * @see getNumberOfNodesInMatrixFormat2
    */
   private double[][] getDistancesMatrixInMatrixFormat2(){
      int numberOfNodes = getNumberOfNodesInMatrixFormat2();
      double distancesMatrix[][] = new double[numberOfNodes][numberOfNodes];

      try{
         String instanceLine = null;
         String[] values;
         int rowIndex = 0;

         BufferedReader reader = new BufferedReader(new FileReader(getInstance()));
         while(reader.ready()){
            instanceLine = reader.readLine();

            Matcher twoLettersMatcher = Pattern.compile("[a-z][a-z]").matcher(instanceLine) ;
            Matcher numbersMatcher = Pattern.compile("[0-9]").matcher(instanceLine) ;

            //if it isn't a text line and has numbers
            if((twoLettersMatcher.find() == false) && (numbersMatcher.find() == true)){
               Matcher spacesMatcher = Pattern.compile("\\s{2,}").matcher(instanceLine);
               instanceLine = spacesMatcher.replaceAll(" ").trim(); //replace all spaces for just one

               values = instanceLine.split(" ");

               //format 2
               for(int i = 0; i <= values.length - 1; i++){
                  //symmetric matrix
                  distancesMatrix[rowIndex][rowIndex + (i + 1)] = Double.parseDouble(values[i]);
                  distancesMatrix[rowIndex + (i + 1)][rowIndex] = Double.parseDouble(values[i]);
               }

               rowIndex++;
            }
         }
      }
      catch(Exception e){
         System.out.println("Error in get edges values matrix in format 2");
         e.printStackTrace();
      }

      return distancesMatrix;
   }

   /**
    * Method to get the number of nodes when the instance is in matrix format 2.
    *
    * Let n be the number of nodes.
    * Format 2: Symmetric matrix, where the first row is A1, A2 to A1, An. And the last row is An-1, An.
    * @author Matheus Paixao
    * @return the number of nodes 
    */
   private int getNumberOfNodesInMatrixFormat2(){
      int numberOfNodes = 0;

      try{
         String instanceLine = null;

         BufferedReader reader = new BufferedReader(new FileReader(getInstance()));
         while(reader.ready()){
            instanceLine = reader.readLine();

            Matcher twoLettersMatcher = Pattern.compile("[a-z][a-z]").matcher(instanceLine) ;
            Matcher numbersMatcher = Pattern.compile("[0-9]").matcher(instanceLine) ;

            //if it isn't a text line and has numbers
            if((twoLettersMatcher.find() == false) && (numbersMatcher.find() == true)){
               numberOfNodes++;
            }
         }
      }
      catch(Exception e){
         System.out.println("Error in get number of nodes in matrix format");
      }

      //in symmetric matrix the last line is the node An-1
      return numberOfNodes + 1;
   }
}
