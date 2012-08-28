import java.io.File;

public class SRPPRandomAlgorithm extends RandomAlgorithm{
   SRPPInstanceReader srppInstanceReader;

   double[][] objectivesValues;
   int numberOfRequirements;

   public SRPPRandomAlgorithm(File instance, int numberOfIterations){
      super(numberOfIterations);
      srppInstanceReader = new SRPPInstanceReader(instance);
      objectivesValues = srppInstanceReader.getObjectiveValues();
      this.numberOfRequirements = objectivesValues[0].length;
   }

   public double calculateSolutionValue(int[] solution){
      double solutionValue = 0;

      for(int i = 0; i <= solution.length - 1; i++){
         solutionValue += (solution.length - i) * getObjectivesSum(solution[i]);
      }

      return solutionValue;
   }

   private double getObjectivesSum(int requirement){
      double objectivesSum = 0;

      for(int i = 0; i <= objectivesValues.length - 1; i++){
         objectivesSum += objectivesValues[i][requirement];
      }

      return objectivesSum;
   }

   public int getNumberOfNodes(){
      return this.numberOfRequirements;
   }

   public boolean isSolutionBest(double iterationSolutionValue, double bestSolutionValue){
      boolean result = false;

      if(iterationSolutionValue > bestSolutionValue){
         result = true;
      }

      return result;
   }
}
