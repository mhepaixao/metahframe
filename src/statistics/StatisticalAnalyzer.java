package statistics;

public class StatisticalAnalyzer{
   double[][] solutions;
   double[][] runTimes;

   int numberOfInstances;

   double[] instancesSolutionsMean;
   double[] instancesSolutionsStandardDeviation;
   double[] instancesRunTimesMean;
   double[] instancesRunTimesStandardDeviation;

   public StatisticalAnalyzer(double[][] solutions, double[][] runTimes){
      this.solutions = solutions;
      this.runTimes = runTimes;

      this.numberOfInstances = solutions.length;

      this.instancesSolutionsMean = new double[numberOfInstances];
      this.instancesSolutionsStandardDeviation = new double[numberOfInstances];
      this.instancesRunTimesMean = new double[numberOfInstances];
      this.instancesRunTimesStandardDeviation = new double[numberOfInstances];

      for(int i = 0; i <= numberOfInstances - 1; i++){
         this.instancesSolutionsMean[i] = getMean(solutions[i]);
         this.instancesSolutionsStandardDeviation[i] = getStandardDeviation(instancesSolutionsMean[i], solutions[i]);
         this.instancesRunTimesMean[i] = getMean(runTimes[i]);
         this.instancesRunTimesStandardDeviation[i] = getStandardDeviation(instancesRunTimesMean[i], runTimes[i]);
      }
   }

   public double getInstanceSolutionMean(int instanceIndex){
      return instancesSolutionsMean[instanceIndex];
   }

   public double getInstanceSolutionStandardDeviation(int instanceIndex){
      return instancesSolutionsStandardDeviation[instanceIndex];
   }

   public double getInstanceRunTimeMean(int instanceIndex){
      return instancesRunTimesMean[instanceIndex];
   }

   public double getInstanceRuntimeStandardDeviation(int instanceIndex){
      return instancesRunTimesStandardDeviation[instanceIndex];
   }

   public int getNumberOfInstances(){
      return this.numberOfInstances;
   }

   private double getMean(double[] values){
      double valuesSum = 0;

      for(int i = 0; i <= values.length - 1; i++){
         valuesSum += values[i];
      }

      return valuesSum / values.length;
   }

   private double getStandardDeviation(double mean, double[] values){
      double[] deviances = getDeviances(mean, values);
      double variance = getVariance(deviances);

      return Math.sqrt(variance);
   }

   private double[] getDeviances(double mean, double[] values){
      double[] deviances = new double[values.length];

      for(int i = 0; i <= deviances.length - 1; i++){
         deviances[i] = mean - values[i];
      }

      return deviances;
   }

   private double getVariance(double[] deviances){
      double quadraticDeviancesSum = 0;

      for(int i = 0; i <= deviances.length - 1; i++){
         quadraticDeviancesSum += Math.pow(deviances[i], 2);
      }

      return quadraticDeviancesSum / deviances.length;
   }
}
