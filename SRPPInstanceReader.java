import java.io.File;
import java.io.FileReader;
import java.io.BufferedReader;

public class SRPPInstanceReader{
   File instance;
   String[] instanceLines;

   public SRPPInstanceReader(File instance){
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
      double[][] objecivesValues = new double[2][getNumberOfRequirements()];
      
      objecivesValues[0] = getObjective1Values();
      objecivesValues[1] = getObjective2Values();

      return objecivesValues;
   }

   public int getNumberOfRequirements(){
      //the number of requirements is in the 3rd line of the instance
      return Integer.parseInt(instanceLines[2]);
   }

   //objective 1 is risk
   private double[] getObjective1Values(){
      //the risk of each requirement is in the 6th line of the instance
      String[] objective1StringValues = instanceLines[5].split(" ");
      double[] objective1Values = new double[getNumberOfRequirements()];

      for(int i = 0; i <= objective1Values.length - 1; i++){
         objective1Values[i] = Double.parseDouble(objective1StringValues[i]);
      }

      return objective1Values;
   }

   //objective 2 is importance
   private double[] getObjective2Values(){
      double[] objective2Values = new double[getNumberOfRequirements()];
      double[] clientsImportances = getClientsImportances();
      double[][] clientRequirementsImportances = getClientRequirementsImportances();

      for(int i = 0; i <= objective2Values.length - 1; i++){
         for(int j = 0; j <= clientRequirementsImportances.length - 1; j++){
            objective2Values[i] += clientsImportances[j] * clientRequirementsImportances[j][i];
         }
      }

      return objective2Values;
   }

   private double[] getClientsImportances(){
      double[] clientsImportances = new double[getNumberOfClients()];
      String[] clientsImportancesStringValues = instanceLines[7].split(" ");

      for(int i = 0; i <= clientsImportances.length - 1; i++){
         clientsImportances[i] = Double.parseDouble(clientsImportancesStringValues[i]);
      }

      return clientsImportances;
   }

   public int getNumberOfClients(){
      return Integer.parseInt(instanceLines[3]);
   }

   private double[][] getClientRequirementsImportances(){
      double[][] clientRequirementsImportances = new double[getNumberOfClients()][getNumberOfRequirements()];

      for(int i = 0; i <= clientRequirementsImportances.length - 1; i++){
         clientRequirementsImportances[i] = getClientRequirementsImportance(i);
      }

      return clientRequirementsImportances;
   }

   private double[] getClientRequirementsImportance(int client){
      double[] clientRequirementsImportance = new double[getNumberOfRequirements()];
      String[] clientRequirementsImportanceStringValues = instanceLines[9 + client].split(" ");

      for(int i = 0; i <= clientRequirementsImportance.length - 1; i++){
         clientRequirementsImportance[i] = Double.parseDouble(clientRequirementsImportanceStringValues[i]);
      }

      return clientRequirementsImportance;
   }
}
