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

   public RobustNRPInstanceGenerator(){
      this.random = new Random();
   }

   private void generateInstances(int numberOfRequirements, int numberOfScenarios, int requirementsRange, int costRange, String[] variationPercentages, 
                                    int[] precedencePercentages){
      String scenariosProbabilities = getScenariosProbabilities(numberOfScenarios);
      String[] requirementsValues = getRequirementsValues(numberOfRequirements, numberOfScenarios, requirementsRange);
      String requirementsCosts = getRandomNumbersSequency(numberOfRequirements, 1, costRange);
      String[] precedenceStrings = null;

      for(int i  = 0; i <= variationPercentages.length - 1; i++){
         for(int j = 0; j <= precedencePercentages.length - 1; j++){
            try{
               BufferedWriter writer = new BufferedWriter(new FileWriter(new File("I_" + numberOfRequirements + "_" + variationPercentages[i] + 
                                                                                    "_" + precedencePercentages[j] + ".txt")));

               writer.write(numberOfRequirements + " " + numberOfScenarios + "\n");
               writer.write("\n");

               writer.write(scenariosProbabilities + "\n");
               writer.write("\n");

               for(int k = 0; k <= requirementsValues.length - 1; k++){
                  writer.write(requirementsValues[k] + "\n");
               }
               writer.write("\n");

               writer.write(requirementsCosts + "\n");

               writer.write(getRequirementsVariations(requirementsCosts, variationPercentages[i]) + "\n");
               writer.write("\n");

               precedenceStrings = getPrecedenceStrings(numberOfRequirements, precedencePercentages[j]);
               for(int k = 0; k <= precedenceStrings.length - 1; k++){
                  writer.write(precedenceStrings[k] + "\n");
               }

               writer.close();
            }
            catch(Exception e){
               e.printStackTrace();
            }
         }
      }
   }

   private String getScenariosProbabilities(int numberOfScenarios){
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

   private String[] getRequirementsValues(int numbersOfRequirements, int numberOfScenarios, int requirementsRange){
      String[] requirementsValues = new String[numberOfScenarios];

      for(int i = 0; i <= requirementsValues.length - 1; i++){
         requirementsValues[i] = getRandomNumbersSequency(numbersOfRequirements, 1, requirementsRange);
      }

      return requirementsValues;
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

   private String getRequirementsVariations(String requirementsCosts, String variationPercentage){
      String requirementsVariations = null;
      double[] costs = getCosts(requirementsCosts);

      if(variationPercentage.equals("random") == true){
         requirementsVariations = getRandomVariations(costs);
      }
      else{
         int percentage = Integer.parseInt(variationPercentage);

         for(int i = 0; i <= costs.length - 1; i++){
            if(requirementsVariations == null){
               requirementsVariations = costs[i] * ((double) percentage / 100) + "";
            }
            else{
               requirementsVariations += " " + costs[i] * ((double) percentage / 100);
            }
         }
      }

      return requirementsVariations;
   }

   private double[] getCosts(String requirementsCosts){
      String[] costsArray = requirementsCosts.split(" ");
      double[] costs = new double[costsArray.length];

      for(int i = 0; i <= costs.length - 1; i++){
         costs[i] = Double.parseDouble(costsArray[i]);
      }

      return costs;
   }

   private String getRandomVariations(double[] costs){
      String randomVariations  = null;

      for(int i = 0; i <= costs.length - 1; i++){
         if(randomVariations == null){
            randomVariations = getNumberInInterval(0, 0.5 * costs[i], random) + "";
         }
         else{
            randomVariations += " " + getNumberInInterval(0, 0.5 * costs[i], random);
         }
      }

      return randomVariations;
   }

   private double getNumberInInterval(double lowerBound, double upperBound, Random random){
      return lowerBound + random.nextDouble() * (upperBound - lowerBound);
   }

   private String[] getPrecedenceStrings(int numberOfRequirements, int precedencePercentage){
      String[] precedenceStrings = new String[numberOfRequirements];
      int[][] precedenceMatrix = getPrecedenceMatrix(numberOfRequirements, precedencePercentage);

      for(int i = 0; i <= precedenceMatrix.length - 1; i++){
         for(int j = 0; j <= precedenceMatrix[0].length - 1; j++){
            if(j == 0){
               precedenceStrings[i] = precedenceMatrix[i][j] + "";
            }
            else{
               precedenceStrings[i] += " " + precedenceMatrix[i][j];
            }
         }
      }

      return precedenceStrings;
   }

   private int[][] getPrecedenceMatrix(int numberOfRequirements, int precedencePercentage){
      int[][] precedenceMatrix = new int[numberOfRequirements][numberOfRequirements];
      int[] requirementsWhoHavePrecedents = getRandomNumbersList(numberOfRequirements, precedencePercentage);
      int precedentsPercentage = 0;
      int[] precedents = null;

      for(int i = 0; i <= requirementsWhoHavePrecedents.length - 1; i++){
         precedentsPercentage = random.nextInt(5) + 1;
         precedents = getRandomNumbersList(numberOfRequirements, precedentsPercentage);

         for(int j = 0; j <= precedents.length - 1; j++){
            precedenceMatrix[requirementsWhoHavePrecedents[i]][precedents[j]] = 1;
         }
      }

      return precedenceMatrix;
   }

   private int[] getRandomNumbersList(int numbers, int percentage){
      int listLength = (int) (numbers * ((double) percentage) / 100);
      int[] randomNumbersList = new int[listLength];
      int randomNumber = 0;

      for(int i = 0; i <= randomNumbersList.length - 1; i++){
         randomNumbersList[i] = numbers + 1;
      }

      for(int i = 0; i <= randomNumbersList.length - 1; i++){
         randomNumber = random.nextInt(numbers);
         while(isNumberInList(randomNumber, randomNumbersList) == true){
            randomNumber = random.nextInt(numbers);
         }

         randomNumbersList[i] = randomNumber;
      }

      return randomNumbersList;
   }

   private boolean isNumberInList(int number, int[] list){
      boolean result = false;

      for(int i = 0; i <= list.length - 1; i++){
         if(list[i] == number){
            result = true;
            break;
         }
      }

      return result;
   }

   public static void main(String[] args){
      RobustNRPInstanceGenerator instanceGenerator = new RobustNRPInstanceGenerator();

      int[] numbersOfRequirements = {50, 100, 150, 200, 250, 300, 350, 400, 450, 500};
      String[] variationPercentages = {"10", "20", "30", "40", "50", "random"};
      int[] precedencePercentages = {0, 10, 20};

      int requirementsRange = 10;
      int costRange = 10;
      int numberOfScenarios = 0;

      for(int i = 0; i <= numbersOfRequirements.length - 1; i++){
         numberOfScenarios = instanceGenerator.random.nextInt(3) + 2;

         instanceGenerator.generateInstances(numbersOfRequirements[i], numberOfScenarios, requirementsRange, costRange, 
               variationPercentages, precedencePercentages);
      }
   }
}
