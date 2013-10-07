import statistics.StatisticalAnalyzer;

import io.ResultsWriter;
import io.InstancesHandler;

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

      //metahframe.solve(instancesHandler.getInstances(), problem, algorithm, numberOfRuns, iterationsPerRun);
      metahframe.solve(instancesHandler.getInstances(), problem, algorithm, numberOfRuns, numberOfFitnessEvaluations);
      statisticalAnalyzer = new StatisticalAnalyzer(metahframe.solutions, metahframe.runTimes);
      //resultsWriter = new ResultsWriter(statisticalAnalyzer, instancesHandler);
      resultsWriter = new ResultsWriter(statisticalAnalyzer, instancesHandler, algorithm);
      resultsWriter.printResults();
      //resultsWriter.writeResults();

      System.exit(0);
   }
}
