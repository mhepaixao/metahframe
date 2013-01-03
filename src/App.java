import algorithms.Algorithm;

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

import instancereaders.InstanceChooser;

import java.io.File;
import java.io.FileWriter;
import java.io.BufferedWriter;

/**
 * Class used to run the application.
 * @author Matheus Paixao
 *
 * How to run:
 * java App <algorithm> <problem> <number of runs> <iterations per run>
 */
public class App{

   File instance;
   double[] solutions;
   double[] runTimes;

   public App(){
      InstanceChooser instanceChooser = new InstanceChooser();
      this.instance = instanceChooser.getInstance(); //choose the instance to be used by the algorithm
   }

   public App(String instancePath){
      this.instance = new File(instancePath);
   }

   private File getInstance(){
      return this.instance;
   }

   private void solve(String problem, String algorithm, int numberOfRuns, int iterationsPerRun){
      Algorithm adaptedAlgorithm = null;
      solutions = new double[numberOfRuns];
      runTimes = new double[numberOfRuns];

      for(int i = 0; i <= numberOfRuns - 1; i++){
         if(algorithm.equals("antq")){
            if(problem.equals("tsp")){
               TSPProblem tspProblem = new TSPProblem(instance);
               adaptedAlgorithm = new TSPAntQ(tspProblem, iterationsPerRun);
            }
            else if(problem.equals("jssp")){
               JSSPProblem jsspProblem = new JSSPProblem(instance);
               adaptedAlgorithm = new JSSPAntQ(jsspProblem, iterationsPerRun);
            }
            else if(problem.equals("srpp")){
               SRPPProblem srppProblem = new SRPPProblem(instance);
               adaptedAlgorithm = new SRPPAntQ(srppProblem, iterationsPerRun);
            }
         }
         else if(algorithm.equals("acs")){
            if(problem.equals("tsp")){
               TSPProblem tspProblem = new TSPProblem(instance);
               adaptedAlgorithm = new TSPACS(tspProblem, iterationsPerRun);
            }
            else if(problem.equals("jssp")){
               JSSPProblem jsspProblem = new JSSPProblem(instance);
               adaptedAlgorithm = new JSSPACS(jsspProblem, iterationsPerRun);
            }
         }
         else if(algorithm.equals("random")){
            if(problem.equals("tsp")){
               TSPProblem tspProblem = new TSPProblem(instance);
               adaptedAlgorithm = new TSPRandomAlgorithm(tspProblem, iterationsPerRun);
            }
            else if(problem.equals("jssp")){
               JSSPProblem jsspProblem = new JSSPProblem(instance);
               adaptedAlgorithm = new JSSPRandomAlgorithm(jsspProblem, iterationsPerRun);
            }
            else if(problem.equals("srpp")){
               SRPPProblem srppProblem = new SRPPProblem(instance);
               adaptedAlgorithm = new SRPPRandomAlgorithm(srppProblem, iterationsPerRun);
            }
         }
         else if(algorithm.equals("sa")){
            if(problem.equals("tsp")){
               TSPProblem tspProblem = new TSPProblem(instance);
               adaptedAlgorithm = new TSPSimulatedAnnealing(tspProblem);
            }
            else if(problem.equals("rnrp")){ 
               RobustNextReleaseProblem robustNRP = new RobustNextReleaseProblem(instance);
               adaptedAlgorithm = new RobustNRPSimulatedAnnealing(robustNRP);
            }
         }
         else if(algorithm.equals("ga")){
            if(problem.equals("tsp")){
               TSPProblem tspProblem = new TSPProblem(instance);
               adaptedAlgorithm = new TSPGeneticAlgorithm(tspProblem, iterationsPerRun);
            }
            else if(problem.equals("rnrp")){ 
               RobustNextReleaseProblem robustNRP = new RobustNextReleaseProblem(instance);
               adaptedAlgorithm = new RobustNRPGeneticAlgorithm(robustNRP, iterationsPerRun);
            }
         }

         solutions[i] = adaptedAlgorithm.getSolution();
         runTimes[i] = adaptedAlgorithm.getTotalTime();
      }
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

   private void printResults(){
      double solutionMean = getMean(solutions);
      double runTimeMean = getMean(runTimes);
      double solutionStandardDeviation = getStandardDeviation(solutionMean, solutions);
      double runTimeStandardDeviation = getStandardDeviation(runTimeMean, runTimes);

      System.out.println("solution: " + solutionMean + " +/- " + solutionStandardDeviation);
      System.out.println("run time: " + runTimeMean + " +/- " + runTimeStandardDeviation);
   }

   private void writeResults(File outputFile){
      double solutionMean = getMean(solutions);
      double runTimeMean = getMean(runTimes);
      double solutionStandardDeviation = getStandardDeviation(solutionMean, solutions);
      double runTimeStandardDeviation = getStandardDeviation(runTimeMean, runTimes);

      try{
         BufferedWriter writer = new BufferedWriter(new FileWriter(outputFile));

         writer.write("solution: " + solutionMean + " +/- " + solutionStandardDeviation);
         writer.write("\n");
         writer.write("run time: " + runTimeMean + " +/- " + runTimeStandardDeviation);

         writer.close();
      }
      catch(Exception e){
         System.out.println("Error in write results to output file");
      }
   }

   public static void main(String[] args){
      App app = null;

      String problem = null;
      String algorithm = null;
      int numberOfRuns = 0;
      int iterationsPerRun = 0;
      String instancePath = null;
      File outputFile = null;

      algorithm = args[0]; //first parameter is the algorithm to be used
      problem = args[1]; //second parameter is the problem to be solved
      numberOfRuns = Integer.parseInt(args[2]);
      iterationsPerRun = Integer.parseInt(args[3]);

      if(args.length >= 5){
         instancePath = args[4];
         app = new App(instancePath);

         if(args.length >= 6){
            outputFile = new File(args[5]);
         }
      }
      else{
         app = new App();
      }

      app.solve(problem, algorithm, numberOfRuns, iterationsPerRun);

      if(outputFile == null){
         app.printResults();
      }
      else{
         app.writeResults(outputFile);
      }

      System.exit(0);
   }
}
