import java.io.File;
import java.io.FileWriter;
import java.io.BufferedWriter;

import java.util.Random;

import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;

/**
 * Class to generate random instances for the Robust Next Release Problem proposed to GECCO 2013.
 *
 * To generate an instance just run: 
 * java RobustNRPInstanceGenerator <number of requirements> <number of scenarios> <requirements values upper bound>
 *                                  <requirements costs upper bound> <requirements deviance percentage> <path>
 * Example: java RobustNRPInstanceGenerator 50 5 10 10 5 /home/instances
 *
 * The instance format is:
 * Line 1 - <number of requirements> <number of scnarios>
 * Line 2 - blank
 * Line 3 - scenarios probabilities
 * Line 4 - blank
 * Line 5 - requirements values for scenario 1
 * Line 6 - requirements values for scenario 2
 *    .
 *    .
 *    .
 * Line (5 + number of scenarios + 1) - requirements costs
 * Line (5 + number of scenarios + 2) - requirements deviances
 *
 * @author Matheus Paixao
 */
public class RobustNRPInstanceGenerator{
   private Random random;

   private int numberOfRequirements;
   private int numberOfScenarios;
   private int requirementRange;
   private int costRange;
   private int deviancePercentage;

   private String path;

   public RobustNRPInstanceGenerator(){
      this.random = new Random();
   }

   private void getGenerationsParameters(String[] parameters){
      numberOfRequirements = Integer.parseInt(parameters[0]);
      numberOfScenarios = Integer.parseInt(parameters[1]);
      requirementRange = Integer.parseInt(parameters[2]);
      costRange = Integer.parseInt(parameters[3]);
      deviancePercentage = Integer.parseInt(parameters[4]);
      path = parameters[5];
   }

   private void generateInstance(){
      try{
         BufferedWriter writer = new BufferedWriter(new FileWriter(new File(path + "I_" + numberOfRequirements + ".txt")));

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

         String costs = getRandomNumbersSequency(numberOfRequirements, 1, costRange);
         writer.write(costs);
         writer.write("\n");

         writer.write(getRequirementsDeviances(costs, deviancePercentage));
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
            scenariosProbabilities = randomNumbers[i] / randomNumbersSum + "";
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

   private String getRequirementsDeviances(String costs, int deviancePercentage){
      String deviances = null;
      String[] costsValues = costs.split(" ");
      double[] deviancesValues = new double[costsValues.length];

      for(int i = 0; i <= deviancesValues.length - 1; i++){
         deviancesValues[i] = Integer.parseInt(costsValues[i]) * ((double) deviancePercentage / 100);

         if(deviances == null){
            deviances = deviancesValues[i] + "";
         }
         else{
            deviances += " " + deviancesValues[i];
         }
      }

      return deviances;
   }

   public static void main(String[] args){
      RobustNRPInstanceGenerator instanceGenerator = new RobustNRPInstanceGenerator();

      instanceGenerator.getGenerationsParameters(args);
      instanceGenerator.generateInstance();
   }
}
