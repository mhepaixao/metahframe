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
    * Method to get the list of cities when the instance is in cartesian coordinates format.
    *
    * It is used an auxiliary object. dynamicListOfCities is an ArrayList of City
    * and is used to create the cities in a dynamic form. The AntQ algorithm uses an 
    * array of cities. So, after create all the cities the dynamicListOfCities is casted
    * to a simple City array.
    *
    * The cartesian coordinates have to be in one of the formats that follows:
    * 1) n Xe+P Ye+P, where n is the line number, X is the x cartesian value x*(10^P) and Y is the y cartesian value y*(10^P)  
    * 2) n X Y, where n is the line number, X is the x cartesian value and Y is the y cartesian value  
    * 3) Xe+P Ye+P, where X is the x cartesian value x*(10^P) and Y is the y cartesian value y*(10^P)  
    * 4) X Y, where X is the x cartesian value and Y is the y cartesian value  
    *
    * @author Matheus Paixao
    * @return a City array containing all the cities of the instance.
    * @see getInstance
    * @see toArray method from ArrayList class
    */
   public City[] getCitiesList(){
      City[] cities = null; //array to return
      ArrayList<City> dynamicListOfCities = new ArrayList<City>(); 
      File instance = getInstance();

      try{
         String instanceLine = null;
         String[] values;
         City city = null;
         int instanceLineCounter = 0; //will serve as a city id

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
                  city = getCartesianCity(instanceLineCounter, values[1], values[2]); //formats 1 and 2
               }
               else{
                  city = getCartesianCity(instanceLineCounter, values[0], values[1]); //formats 3 and 4
               }

               instanceLineCounter++;
            }

            if(city != null){
               dynamicListOfCities.add(city);
            }
         }
      }
      catch(Exception e){
         e.printStackTrace();
      }

      Collections.sort(dynamicListOfCities);
      removeDuplicatedCities(dynamicListOfCities);
      setCitiesIndexes(dynamicListOfCities);

      //cast the dynamicListOfCities to a City array
      cities = new City[dynamicListOfCities.size()];
      for(int i = 0; i <= cities.length - 1; i++){
         cities[i] = dynamicListOfCities.get(i);
      }

      return cities;
   }

   /**
    * Method to get the city when the values are in String format.
    *
    * @author Matheus Paixao
    * @param id the id of the city.
    * @param value1 the x cartesian value in String format.
    * @param value2 the y cartesian value in String format.
    * @return the city with the x and y coordinates.
    */
   private City getCartesianCity(int id, String value1, String value2){
      double x = Double.parseDouble(value1);
      double y = Double.parseDouble(value2);

      return new City(id, x, y);
   }

   /**
    * Method to delete duplicate cities in the instance.
    *
    * The list of cities must be sorted.
    * @author Matheus Paixao
    * @param dynamicListOfCities the list of cities to delete duplicated cities.
    */
   private void removeDuplicatedCities(ArrayList<City> dynamicListOfCities){
      City city = null;
      City nextCity = null;

      for(int i = 0; i <= dynamicListOfCities.size() - 1; i++){
         if(i != dynamicListOfCities.size() - 1){
            city = dynamicListOfCities.get(i);
            nextCity = dynamicListOfCities.get(i + 1);

            if(city.equals(nextCity)){
               dynamicListOfCities.remove(i);
            }
         }
      }
   }

   /**
    * Method to set the index of each city of the list.
    *
    * After sort and delete duplicate cities, it's necessary set new indexes.
    * @author Matheus Paixao
    * @param dynamicListOfCities the list od cities to set a new index
    * @see setIndex in City class
    */
   private void setCitiesIndexes(ArrayList<City> dynamicListOfCities){
      for(int i = 0; i <= dynamicListOfCities.size() - 1; i++){
         dynamicListOfCities.get(i).setIndex(i);
      }
   }

   /**
    * Method to get the edges values matrix when the instance is in the distance matrix format.
    *
    * The distance matrix must be in one of the formats that follows:
    * Let n be the number of cities.
    * 1) Symmetric matrix, where the first line is A1, A2 to A1, An. And the last line is An-1, An.
    *
    * @author Matheus Paixao
    * @return the edges values matrix
    * @see getNumberOfCitiesInMatrixFormat
    */
   public double[][] getEdgesValuesMatrix(){
      int numberOfCities = getNumberOfCitiesInMatrixFormat();
      double edgesValuesMatrix[][] = new double[numberOfCities][numberOfCities];

      try{
         String instanceLine = null;
         String[] values;
         int lineIndex = 0;

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

               //format 1
               for(int i = 0; i <= values.length - 1; i++){
                  edgesValuesMatrix[lineIndex][lineIndex + (i + 1)] = Double.parseDouble(values[i]);
                  edgesValuesMatrix[lineIndex + (i + 1)][lineIndex] = Double.parseDouble(values[i]);
               }

               lineIndex++;
            }

         }
      }
      catch(Exception e){
         System.out.println("Error in get edges values matrix");
      }

      return edgesValuesMatrix;
   }

   /**
    * Method to get the number of cities when the instance is in distance matrix format.
    *
    * @author Matheus Paixao
    * @return the number of cities 
    */
   private int getNumberOfCitiesInMatrixFormat(){
      int numberOfCities = 0;

      try{
         String instanceLine = null;

         BufferedReader reader = new BufferedReader(new FileReader(instance));
         while(reader.ready()){
            instanceLine = reader.readLine();

            Matcher twoLettersMatcher = Pattern.compile("[a-z][a-z]").matcher(instanceLine) ;
            Matcher numbersMatcher = Pattern.compile("[0-9]").matcher(instanceLine) ;

            //if it isn't a text line and has numbers
            if((twoLettersMatcher.find() == false) && (numbersMatcher.find() == true)){
               numberOfCities++;
            }
         }
      }
      catch(Exception e){
         System.out.println("Error in get number of cities in matrix format");
      }

      //in symmetric matrix the last line is the city An-1
      return numberOfCities + 1;
   }
}
