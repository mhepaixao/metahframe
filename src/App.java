import algorithms.Algorithm;

import statistics.StatisticalAnalyzer;

import io.ResultsWriter;
import io.InstancesHandler;

import problems.tsp.TSPProblem;
import problems.tsp.TSPAntQ;
import problems.tsp.TSPRandomAlgorithm;
import problems.tsp.TSPACS;
import problems.tsp.TSPSimulatedAnnealing;
import problems.tsp.TSPGeneticAlgorithm;
import problems.jssp.JSSPProblem;
import problems.jssp.JSSPAntQ;
import problems.jssp.JSSPRandomAlgorithm;
import problems.jssp.JSSPACS;
import problems.srpp.SRPPProblem;
import problems.srpp.SRPPAntQ;
import problems.srpp.SRPPRandomAlgorithm;
import problems.rnrp.RobustNextReleaseProblem;
import problems.rnrp.RobustNRPSimulatedAnnealing;
import problems.rnrp.RobustNRPGeneticAlgorithm;
import problems.rrnrp.RecoverableRobustNextReleaseProblem;
import problems.rrnrp.RecoverableRobustNRPGeneticAlgorithm;

import java.io.File;

/**
 * Class used to run the application.
 * @author Matheus Paixao
 *
 * How to run:
 * java App <algorithm> <problem> <number of runs> <iterations per run>
 */
public class App{

   double[][] solutions;
   double[][] runTimes;

   private void solve(File[] instances, String problem, String algorithm, int numberOfRuns, int iterationsPerRun){
      Algorithm adaptedAlgorithm = null;
      solutions = new double[instances.length][numberOfRuns];
      runTimes = new double[instances.length][numberOfRuns];

      for(int i = 0; i <= instances.length - 1; i++){
         for(int j = 0; j <= numberOfRuns - 1; j++){
            if(algorithm.equals("antq")){
               if(problem.equals("tsp")){
                  TSPProblem tspProblem = new TSPProblem(instances[i]);
                  adaptedAlgorithm = new TSPAntQ(tspProblem, iterationsPerRun);
               }
               else if(problem.equals("jssp")){
                  JSSPProblem jsspProblem = new JSSPProblem(instances[i]);
                  adaptedAlgorithm = new JSSPAntQ(jsspProblem, iterationsPerRun);
               }
               else if(problem.equals("srpp")){
                  SRPPProblem srppProblem = new SRPPProblem(instances[i]);
                  adaptedAlgorithm = new SRPPAntQ(srppProblem, iterationsPerRun);
               }
            }
            else if(algorithm.equals("acs")){
               if(problem.equals("tsp")){
                  TSPProblem tspProblem = new TSPProblem(instances[i]);
                  adaptedAlgorithm = new TSPACS(tspProblem, iterationsPerRun);
               }
               else if(problem.equals("jssp")){
                  JSSPProblem jsspProblem = new JSSPProblem(instances[i]);
                  adaptedAlgorithm = new JSSPACS(jsspProblem, iterationsPerRun);
               }
            }
            else if(algorithm.equals("random")){
               if(problem.equals("tsp")){
                  TSPProblem tspProblem = new TSPProblem(instances[i]);
                  adaptedAlgorithm = new TSPRandomAlgorithm(tspProblem, iterationsPerRun);
               }
               else if(problem.equals("jssp")){
                  JSSPProblem jsspProblem = new JSSPProblem(instances[i]);
                  adaptedAlgorithm = new JSSPRandomAlgorithm(jsspProblem, iterationsPerRun);
               }
               else if(problem.equals("srpp")){
                  SRPPProblem srppProblem = new SRPPProblem(instances[i]);
                  adaptedAlgorithm = new SRPPRandomAlgorithm(srppProblem, iterationsPerRun);
               }
            }
            else if(algorithm.equals("sa")){
               if(problem.equals("tsp")){
                  TSPProblem tspProblem = new TSPProblem(instances[i]);
                  adaptedAlgorithm = new TSPSimulatedAnnealing(tspProblem);
               }
               else if(problem.equals("rnrp")){ 
                  //RobustNextReleaseProblem robustNRP = new RobustNextReleaseProblem(instances[i]);
                  //RobustNextReleaseProblem robustNRP = new RobustNextReleaseProblem(instances[i], gammaPercentage);
                  //adaptedAlgorithm = new RobustNRPSimulatedAnnealing(robustNRP);
               }
            }
            else if(algorithm.equals("ga")){
               if(problem.equals("tsp")){
                  TSPProblem tspProblem = new TSPProblem(instances[i]);
                  adaptedAlgorithm = new TSPGeneticAlgorithm(tspProblem, iterationsPerRun);
               }
               else if(problem.equals("rnrp")){ 
                  //RobustNextReleaseProblem robustNRP = new RobustNextReleaseProblem(instances[i]);
                  //RobustNextReleaseProblem robustNRP = new RobustNextReleaseProblem(instances[i], gammaPercentage);
                  //adaptedAlgorithm = new RobustNRPGeneticAlgorithm(robustNRP, iterationsPerRun);
               }
               else if(problem.equals("rrnrp")){ 
                  //RecoverableRobustNextReleaseProblem recoverableRobustNRP = 
                                 //new RecoverableRobustNextReleaseProblem(instances[i], gammaPercentage, recoveryPercentage);
                  //adaptedAlgorithm = new RecoverableRobustNRPGeneticAlgorithm(recoverableRobustNRP, iterationsPerRun);
               }
            }

            solutions[i][j] = adaptedAlgorithm.getSolution();
            runTimes[i][j] = adaptedAlgorithm.getTotalTime();
         }
      }
   }

   public static void main(String[] args){
      InstancesHandler instancesHandler = null;
      App app = null;
      StatisticalAnalyzer statisticalAnalyzer = null;
      ResultsWriter resultsWriter = null;

      String problem = null;
      String algorithm = null;
      int numberOfRuns = 0;
      int iterationsPerRun = 0;

      algorithm = args[0]; //first parameter is the algorithm to be used
      problem = args[1]; //second parameter is the problem to be solved
      numberOfRuns = Integer.parseInt(args[2]);
      iterationsPerRun = Integer.parseInt(args[3]);

      if(args.length <= 4){
         instancesHandler = new InstancesHandler();
      }
      else{
         instancesHandler = new InstancesHandler(args[4]);
      }

      app = new App();

      app.solve(instancesHandler.getInstances(), problem, algorithm, numberOfRuns, iterationsPerRun);
      statisticalAnalyzer = new StatisticalAnalyzer(app.solutions, app.runTimes);
      resultsWriter = new ResultsWriter(statisticalAnalyzer, instancesHandler);
      resultsWriter.printResults();

      System.exit(0);
   }
}
