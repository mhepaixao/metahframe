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
   private int costRange;
   private int devianceRange;

   private String path;

   public RobustNRPInstanceGenerator(){
      this.random = new Random();
   }

   private void getGenerationsParameters(String[] parameters){
      numberOfRequirements = Integer.parseInt(parameters[0]);
      numberOfScenarios = Integer.parseInt(parameters[1]);
      requirementRange = Integer.parseInt(parameters[2]);
      costRange = Integer.parseInt(parameters[3]);
      devianceRange = Integer.parseInt(parameters[4]);
      path = parameters[5];
   }

   private void generateInstance(){
      try{
         BufferedWriter writer = new BufferedWriter(new FileWriter(new File(path + "Inst_" + numberOfRequirements + "_" + numberOfScenarios + 
                                                                              "_" + devianceRange + ".txt")));

         writer.write(numberOfRequirements + " " + numberOfScenarios);
         writer.write("\n");
         writer.write("\n");

         writer.write(getScenariosProbabilities());
         writer.write("\n");
         writer.write("\n");
      
         for(int i = 0; i <= numberOfScenarios - 1; i++){
            writer.write(getRandomNumbersSequency(numberOfRequirements, 1, requirementRange));
            writer.write("\n");
         }
         writer.write("\n");

         writer.write(getRandomNumbersSequency(numberOfRequirements, 1, costRange));
         writer.write("\n");

         writer.write(getRandomNumbersSequency(numberOfRequirements, 0, devianceRange));
         writer.write("\n");

         writer.close();
      }
      catch(Exception e){
         e.printStackTrace();
      }
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

   private String getRandomNumbersSequency(int size, int lowerBound, int upperBound){
      String randomNumbersSequency = null;

      for(int i = 0; i <= size - 1; i++){
         if(randomNumbersSequency == null){
            randomNumbersSequency = getRandomNumber(lowerBound, upperBound) + "";
         }
         else{
            randomNumbersSequency += " " + getRandomNumber(lowerBound, upperBound);
         }
      }

      return randomNumbersSequency;
   }

   private int getRandomNumber(int lowerBound, int upperBound){
      if(lowerBound == 0){
         return random.nextInt(upperBound + 1);
      }
      else{
         return lowerBound + random.nextInt(upperBound);
      }
   }

   public static void main(String[] args){
      RobustNRPInstanceGenerator instanceGenerator = new RobustNRPInstanceGenerator();

      instanceGenerator.getGenerationsParameters(args);
      instanceGenerator.generateInstance();
   }
}
