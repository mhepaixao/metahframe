import java.io.File;
import java.io.FileReader;
import java.io.BufferedReader;

public class RPPInstanceReader{
   File instance;
   String[] instanceLines;

   public RPPInstanceReader(File instance){
      this.instance = instance;
      instanceLines = getInstanceLines();
   }

   private File getInstance(){
      return this.instance;
   }

   private String[] getInstanceLines(){
      String[] instanceLines = new String[getNumberOfLinesInInstance()];
      int instanceLineCounter = 0;

      try{
         BufferedReader reader = new BufferedReader(new FileReader(getInstance()));
         while(reader.ready()){
            instanceLines[instanceLineCounter] = reader.readLine();
            instanceLineCounter++;
         }
      }
      catch(Exception e){
         System.out.println("Get instance lines error");
         e.printStackTrace();
      }

      return instanceLines;
   }

   private int getNumberOfLinesInInstance(){
      int numberOfLinesInInstance = 0;
      String instanceLine = null;

      try{
         BufferedReader reader = new BufferedReader(new FileReader(getInstance()));
         while(reader.ready()){
            instanceLine = reader.readLine();
            numberOfLinesInInstance++;
         }
      }
      catch(Exception e){
         System.out.println("Get number of lines in instance error");
         e.printStackTrace();
      }

      return numberOfLinesInInstance;
   }

   public double[][] getObjectiveValues(){
      double[][] objecivesValues = null;

      return objecivesValues;
   }
}
