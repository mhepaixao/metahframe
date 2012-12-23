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
      double[] probabilities = new double[numberOfScenarios];
      double randomNumbersSum = 0;

      DecimalFormatSymbols dfs = new DecimalFormatSymbols();
      dfs.setDecimalSeparator('.');
      DecimalFormat df = new DecimalFormat();
      df.setMaximumFractionDigits(1);
      df.setMinimumFractionDigits(1);
      df.setDecimalFormatSymbols(dfs);

      scenariosProbabilities = null;

      while(isProbabilitiesValid(probabilities) == false){
         for(int i = 0; i <= randomNumbers.length - 1; i++){
            randomNumbers[i] = random.nextDouble();
            randomNumbersSum += randomNumbers[i];
         }

         for(int i = 0; i <= randomNumbers.length - 1; i++){
            probabilities[i] = Double.parseDouble(df.format(randomNumbers[i] / randomNumbersSum));
         }
      }

      for(int i = 0; i <= probabilities.length - 1; i++){
         if(scenariosProbabilities == null){
            scenariosProbabilities = df.format(probabilities[i])+ "";
         }
         else{
            scenariosProbabilities += " " + df.format(probabilities[i]);
         }
      }

      return scenariosProbabilities;
   }

   private boolean isProbabilitiesValid(double[] probabilities){
      boolean result = false;
      double probabilitiesSum = 0;
      boolean invalidProbabilityPresence = false;

      for(int i = 0; i <= probabilities.length - 1; i++){
         if(probabilities[i] == 0){
            invalidProbabilityPresence = true;
            break;
         }
         else{
            probabilitiesSum += probabilities[i];
         }
      }

      if((probabilitiesSum == 1.0) && (invalidProbabilityPresence == false)){
         result = true;
      }

      return result;
   }

   public static void main(String[] args){
      RobustNRPInstanceGenerator instanceGenerator = new RobustNRPInstanceGenerator();

      instanceGenerator.getGenerationsParameters(args);
      instanceGenerator.generateInstance();
   }
}
