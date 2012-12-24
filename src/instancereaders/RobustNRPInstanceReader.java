package instancereaders;

import java.io.File;
import java.io.FileReader;
import java.io.BufferedReader;

public class RobustNRPInstanceReader{
   File instance;
   String[] instanceLines;

   public RobustNRPInstanceReader(File instance){
      this.instance = instance;
      this.instanceLines = getInstanceLines();
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

   public int getNumberOfRequirements(){
      return Integer.parseInt(instanceLines[0].split(" ")[0]);
   }

   public int getNumberOfScenarios(){
      return Integer.parseInt(instanceLines[0].split(" ")[1]);
   }

   public double[] getRequirementsValues(){
      double[] requirementsValues = new double[getNumberOfRequirements()];
      double[][] requirementsScenariosValues = getRequirementsScenariosValues();
      double[] scenariosProbabilities = getScenariosProbabilities();

      for(int i = 0; i <= requirementsValues.length - 1; i++){
         for(int j = 0; j <= requirementsScenariosValues.length - 1; j++){
            requirementsValues[i] += requirementsScenariosValues[j][i] * scenariosProbabilities[j];
         }
      }

      return requirementsValues;
   }

   private double[][] getRequirementsScenariosValues(){
      int numberOfScenarios = getNumberOfScenarios();
      int numberOfRequirements = getNumberOfRequirements();
      double[][] requirementsScenariosValues = new double[numberOfScenarios][numberOfRequirements];
      String[] instanceScenarioValues = null;

      for(int i = 0; i <= numberOfScenarios - 1; i++){
         instanceScenarioValues = instanceLines[4 + i].split(" ");
         for(int j = 0; j <= numberOfRequirements - 1; j++){
            requirementsScenariosValues[i][j] = Double.parseDouble(instanceScenarioValues[j]);
         }
      }

      return requirementsScenariosValues;
   }

   private double[] getScenariosProbabilities(){
      double[] scenariosProbabilities = new double[getNumberOfScenarios()];
      String[] instanceScenariosProbabilities = instanceLines[2].split(" ");

      for(int i = 0; i <= scenariosProbabilities.length - 1; i++){
         scenariosProbabilities[i] = Double.parseDouble(instanceScenariosProbabilities[i]);
      }

      return scenariosProbabilities;
   }

   public int[] getRequirementsCosts(){
      String[] instanceRequirementsCosts = instanceLines[4 + getNumberOfScenarios() + 1].split(" ");
      int[] requirementsCosts = new int[getNumberOfRequirements()];

      for(int i = 0; i <= requirementsCosts.length - 1; i++){
         requirementsCosts[i] = Integer.parseInt(instanceRequirementsCosts[i]);
      }

      return requirementsCosts;
   }

   public int[] getRequirementsDeviances(){
      String[] instanceRequirementsDeviances = instanceLines[4 + getNumberOfScenarios() + 2].split(" ");
      int[] requirementsDeviances = new int[getNumberOfRequirements()];

      for(int i = 0; i <= requirementsDeviances.length - 1; i++){
         requirementsDeviances[i] = Integer.parseInt(instanceRequirementsDeviances[i]);
      }

      return requirementsDeviances;
   }
}
