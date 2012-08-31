package instancereaders;

import java.io.File;
import java.io.FileReader;
import java.io.BufferedReader;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class JSSPInstanceReader{
   File instance;

   private Matcher spacesMatcher;

   public JSSPInstanceReader(File instance){
      this.instance = instance;
   }

   private File getInstance(){
      return this.instance;
   }

   public double[][] getTimesMatrix(){
      double[][] timesMatrix = null;
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
               timesMatrix = new double[Integer.parseInt(values[0])][Integer.parseInt(values[1])];
            }
            else{
               for(int i = 0; i <= values.length - 1; i = i + 2){
                  timesMatrix[jobCounter][Integer.parseInt(values[i])] = Double.parseDouble(values[i + 1]);
               }

               jobCounter++;
            }
         }
      }
      catch(Exception e){
         System.out.println("JSSP get times matrix error");
         e.printStackTrace();
      }

      return timesMatrix;
   }
}
