import statistics.StatisticalAnalyzer;

import io.ResultsWriter;
import io.InstancesHandler;

import java.io.File;

public class ExecuteMetahframe{

   public static void main(String[] args){
      InstancesHandler instancesHandler = null;
      Metahframe metahframe = null;
      StatisticalAnalyzer statisticalAnalyzer = null;
      ResultsWriter resultsWriter = null;

      String problem = null;
      String algorithm = null;
      int numberOfRuns = 0;
      int numberOfFitnessEvaluations = 0;

      algorithm = args[0]; //first parameter is the algorithm to be used
      problem = args[1]; //second parameter is the problem to be solved
      numberOfRuns = Integer.parseInt(args[2]);
      numberOfFitnessEvaluations = Integer.parseInt(args[3]);

      if(args.length <= 4){
         instancesHandler = new InstancesHandler();
      }
      else{
         instancesHandler = new InstancesHandler(args[4]);
      }

      metahframe= new Metahframe();

      File[] instances = instancesHandler.getInstances();
      InstancesHandler ih = null;
      int[] gammaPercentages = {0, 5, 10, 15, 20, 25, 30, 35, 40, 45, 50, 55, 60, 65, 70, 75, 80, 85, 90, 95, 100};

      for(int i = 0; i <= instances.length - 1; i++){
         for(int j = 0; j <= gammaPercentages.length - 1; j++){
            //metahframe.solve(instancesHandler.getInstances(), problem, algorithm, numberOfRuns, iterationsPerRun);
            ih = new InstancesHandler(instances[i]);
            //metahframe.solve(instancesHandler.getInstances(), problem, algorithm, numberOfRuns, numberOfFitnessEvaluations);
            System.out.println("Executing " + algorithm + " for instance " + instances[i].getName() + " and gamma = " + gammaPercentages[j] + "%");
            metahframe.solve(ih.getInstances(), problem, algorithm, numberOfRuns, numberOfFitnessEvaluations, gammaPercentages[j]);
            statisticalAnalyzer = new StatisticalAnalyzer(metahframe.solutions, metahframe.runTimes);
            //resultsWriter = new ResultsWriter(statisticalAnalyzer, instancesHandler);
            //resultsWriter = new ResultsWriter(statisticalAnalyzer, instancesHandler, algorithm, gammaPercentages[i]);
            resultsWriter = new ResultsWriter(statisticalAnalyzer, ih, algorithm, gammaPercentages[j]);
            //resultsWriter.printResults();
            resultsWriter.writeResults();
         }
      }

      System.exit(0);
   }
}
