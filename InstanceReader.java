import javax.swing.JFileChooser;
import javax.swing.JFrame;

import java.io.File;
import java.io.FileReader;
import java.io.BufferedReader;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import java.util.ArrayList;
import java.util.Collections;

/**
 * Class used to read the instance.
 * 
 * This class extends JFrame because the user chooses the instance by a JFileChooser.
 * @author: Matheus Paixao 
 */
public class InstanceReader extends JFrame {
   private File instance;
   private String instanceType;
   private Matcher twoLettersMatcher;
   private Matcher numbersMatcher;
   private Matcher spacesMatcher;

   public InstanceReader(){
      this.instance = loadInstance();
      this.instanceType = readInstanceType();
   }

   private File getInstance(){
      return this.instance;
   }

   public String getInstanceType(){
      return this.instanceType;
   }

   /**
    * Method to load the instance file.
    *
    * Uses a JFileChooser to select the instance file.
    * @author Matheus Paixao
    * @return the instance file.
    */
   private File loadInstance(){
      JFileChooser instanceChooser = new JFileChooser();
      File instance = null;

      Integer choose = instanceChooser.showOpenDialog(this);
      if (choose.equals(JFileChooser.APPROVE_OPTION)) {
         instance = instanceChooser.getSelectedFile();
      }

      return instance;
   }

   public int getNumberOfJobs(){
      int numberOfJobs = 0;
      String instanceLine = null;

      try{
         BufferedReader reader = new BufferedReader(new FileReader(getInstance()));

         instanceLine = reader.readLine();

         numberOfJobs = Integer.parseInt(instanceLine.split(" ")[0]);
      }
      catch(Exception e){
         System.out.println("JSSP instance reader error");
      }

      return numberOfJobs;
   }

   public double[][] getTimesMatrix(){
      double[][] times = null;
      String instanceLine = null;
      String[] values = null;
      int jobCounter = 0;

      try{
         BufferedReader reader = new BufferedReader(new FileReader(getInstance()));

         while(reader.ready()){
            instanceLine = reader.readLine();
            spacesMatcher = Pattern.compile("\\s{2,}").matcher(instanceLine);
            instanceLine = spacesMatcher.replaceAll(" ").trim(); //replace all spaces for just one

            values = instanceLine.split(" ");
            if(values.length == 2){
               times = new double[Integer.parseInt(values[0])][Integer.parseInt(values[1])];
            }
            else{
               for(int i = 0; i <= values.length - 1; i = i + 2){
                  times[jobCounter][Integer.parseInt(values[i])] = Double.parseDouble(values[i + 1]);
               }

               jobCounter++;
            }
         }
      }
      catch(Exception e){
         System.out.println("JSSP get times matrix error");
         e.printStackTrace();
      }

      return times;
   }

   public int getNumberOfRequirements(){
      int numberOfRequirements = 0;
      String instanceLine = null;

      try{
         BufferedReader reader = new BufferedReader(new FileReader(getInstance()));

         instanceLine = reader.readLine();

         numberOfRequirements = Integer.parseInt(instanceLine.split(" ")[0]);
      }
      catch(Exception e){
         System.out.println("requirements problem instance reader error");
      }

      return numberOfRequirements;
   }

   public double[][] getObjectivesMatrix(int numberOfRequirements){
      double[][] objectivesMatrix = new double[getNumberOfObjectives()][numberOfRequirements];
      String instanceLine = null;
      String[] values = null;
      int objectivesCounter = 0;

      try{
         BufferedReader reader = new BufferedReader(new FileReader(getInstance()));

         while(reader.ready()){
            instanceLine = reader.readLine();
            spacesMatcher = Pattern.compile("\\s{2,}").matcher(instanceLine);
            instanceLine = spacesMatcher.replaceAll(" ").trim(); //replace all spaces for just one

            values = instanceLine.split(" ");
            if(values.length > 1){
               for(int i = 0; i <= values.length - 1; i++){
                  objectivesMatrix[objectivesCounter][i] = Double.parseDouble(values[i]);
               }

               objectivesCounter++;
            }
         }
      }
      catch(Exception e){
         System.out.println("requirements problem get number of objectives error");
         e.printStackTrace();
      }

      return objectivesMatrix;
   }

   private int getNumberOfObjectives(){
      int numberOfObjectives = 0;
      String instanceLine = null;
      String[] values = null;

      try{
         BufferedReader reader = new BufferedReader(new FileReader(getInstance()));

         while(reader.ready()){
            instanceLine = reader.readLine();
            spacesMatcher = Pattern.compile("\\s{2,}").matcher(instanceLine);
            instanceLine = spacesMatcher.replaceAll(" ").trim(); //replace all spaces for just one

            values = instanceLine.split(" ");
            if(values.length > 1){
               numberOfObjectives++;
            }
         }
      }
      catch(Exception e){
         System.out.println("requirements problem get number of objectives error");
         e.printStackTrace();
      }

      return numberOfObjectives;
   }

   /**
    * Method to read the type of the instance.
    *
    * In TSP problem the instance can be in matrix format or in cartesian coordinates format.
    * @author Matheus Paixao
    * @return the type of the instance.
    * @see regex package
    * @see getInstance
    */
   public String readInstanceType(){
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
         System.out.println("Get instance type error");
      }

      return instanceType;
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
   public Node[] getNodesList(){
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
    * Method to get the edges values matrix when the instance is in the distance matrix format.
    *
    * The distance matrix must be in one of the formats that follows:
    * 1) Symmetric matrix, where the '0's delimits each row
    * Let n be the number of nodes.
    * 2) Symmetric matrix, where the first row is A1, A2 to A1, An. And the last row is An-1, An.
    *
    * @author Matheus Paixao
    * @return the edges values matrix
    * @see getNumberOfNodesInMatrixFormat
    */
   public double[][] getEdgesValuesMatrix(){
      double edgesValuesMatrix[][] = null;

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
                  edgesValuesMatrix = getEdgesValuesMatrixInFormat1();
               }
               else{
                  edgesValuesMatrix = getEdgesValuesMatrixInFormat2();
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

      return edgesValuesMatrix;
   }

   /**
    * Method to get the edges values matrix when the matrix instance is in format 1.
    *
    * Format 1: Symmetric matrix, where the '0's delimits each row.
    * @author Matheus Paixao
    * @return the edges values matrix
    * @see getNumberOfNodesInMatrixFormat1
    */
   private double[][] getEdgesValuesMatrixInFormat1(){
      int numberOfNodes = getNumberOfNodesInMatrixFormat1();
      double edgesValuesMatrix[][] = new double[numberOfNodes][numberOfNodes];

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
                  edgesValuesMatrix[rowIndex][rowIndex + (columnIndex + 1)] = Double.parseDouble(values[i]);
                  edgesValuesMatrix[rowIndex + (columnIndex + 1)][rowIndex] = Double.parseDouble(values[i]);
               }
            }
         }
      }
      catch(Exception e){
         System.out.println("Error in get edges values matrix in format 1");
         e.printStackTrace();
      }

      return edgesValuesMatrix;
   }

   /**
    * Method to get the number of nodes when the instance is in distance matrix format 1.
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
    * Method to get the edges values matrix when the matrix instance is in format 2.
    *
    * Let n be the number of nodes.
    * Format 2: Symmetric matrix, where the first row is A1, A2 to A1, An. And the last row is An-1, An.
    * @author Matheus Paixao
    * @return the edges values matrix
    * @see getNumberOfNodesInMatrixFormat2
    */
   private double[][] getEdgesValuesMatrixInFormat2(){
      int numberOfNodes = getNumberOfNodesInMatrixFormat2();
      double edgesValuesMatrix[][] = new double[numberOfNodes][numberOfNodes];

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
                  edgesValuesMatrix[rowIndex][rowIndex + (i + 1)] = Double.parseDouble(values[i]);
                  edgesValuesMatrix[rowIndex + (i + 1)][rowIndex] = Double.parseDouble(values[i]);
               }

               rowIndex++;
            }
         }
      }
      catch(Exception e){
         System.out.println("Error in get edges values matrix in format 2");
         e.printStackTrace();
      }

      return edgesValuesMatrix;
   }

   /**
    * Method to get the number of nodes when the instance is in distance matrix format 2.
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
