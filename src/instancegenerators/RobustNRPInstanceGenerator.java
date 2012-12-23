import java.io.File;
import java.io.FileWriter;
import java.io.BufferedWriter;

import java.util.Random;

import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;

public class RobustNRPInstanceGenerator{
   private Random random;

   private int numberOfRequirements;
   private int numberOfScenarios;
   private int requirementRange;

   public RobustNRPInstanceGenerator(){
      this.random = new Random();
   }

   private void getGenerationsParameters(String[] parameters){
      numberOfRequirements = Integer.parseInt(parameters[0]);
      numberOfScenarios = Integer.parseInt(parameters[1]);
      requirementRange = Integer.parseInt(parameters[2]);
   }

   private void generateInstance(){
      try{
         BufferedWriter writer = new BufferedWriter(new FileWriter(new File("/home/mhepaixao/instancias/rnrp/instancia.txt")));

         writer.write(numberOfRequirements + " " + numberOfScenarios);
         writer.write("\n");
         writer.write("\n");

         writer.write(getScenariosProbabilities());
         writer.write("\n");
         writer.write("\n");
      
         for(int i = 0; i <= numberOfScenarios - 1; i++){
            writer.write(getRequirementsValues());
            writer.write("\n");
         }

         writer.close();
      }
      catch(Exception e){
         e.printStackTrace();
      }
   }

   private String getRequirementsValues(){
      String requirementsValues = null;

      for(int i = 0; i <= numberOfRequirements - 1; i++){
         if(requirementsValues == null){
            requirementsValues = getRequirementValue() + "";
         }
         else{
            requirementsValues += " " + getRequirementValue();
         }
      }

      return requirementsValues;
   }

   private int getRequirementValue(){
      return 1 + random.nextInt(requirementRange);
   }

   private String getScenariosProbabilities(){
      String scenariosProbabilities = null;
      double[] randomNumbers = new double[numberOfScenarios];
      double randomNumbersSum = 0;

      for(int i = 0; i <= randomNumbers.length - 1; i++){
         randomNumbers[i] = random.nextDouble();
         randomNumbersSum += randomNumbers[i];
      }

      for(int i = 0; i <= randomNumbers.length - 1; i++){
         if(scenariosProbabilities == null){
            scenariosProbabilities = randomNumbers[i] / randomNumbersSum+ "";
         }
         else{
            scenariosProbabilities += " " + randomNumbers[i] / randomNumbersSum;
         }
      }

      return scenariosProbabilities;
   }

   public static void main(String[] args){
      RobustNRPInstanceGenerator instanceGenerator = new RobustNRPInstanceGenerator();

      instanceGenerator.getGenerationsParameters(args);
      instanceGenerator.generateInstance();
   }
}
