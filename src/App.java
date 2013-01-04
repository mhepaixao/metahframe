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

import java.util.ArrayList;

/**
 * Class used to run the application.
 * @author Matheus Paixao
 *
 * How to run:
 * java App <algorithm> <problem> <number of runs> <iterations per run>
 */
public class App{

   File[] instances;
   String[] instancesNames;
   double[][] solutions;
   double[][] runTimes;

   double[] instancesSolutionsMean;
   double[] instancesSolutionsStandardDeviation;
   double[] instancesRunTimesMean;
   double[] instancesRunTimesStandardDeviation;

   public App(){
      InstanceChooser instanceChooser = new InstanceChooser();
      File instance = instanceChooser.getInstance(); //choose the instance to be used by the algorithm

      createSingleInstance(instance);
   }

   public App(File path){
      if(isInstancePath(path) == true){
         createSingleInstance(path);
      }
      else{
         createInstancesList(path);
      }
   }

   private File[] getInstances(){
      return this.instances;
   }

   private boolean isInstancePath(File path){
      boolean result = false;

      if(path.getName().contains(".txt") == true){
         result = true;
      }

      return result;
   }

   private void createSingleInstance(File instance){
      this.instances = new File[1];
      this.instances[0] = instance;

      this.instancesNames = new String[1];
      this.instancesNames[0] = instance.getName();
   }

   private void createInstancesList(File folder){
      File[] pathsList = folder.listFiles();
      ArrayList<File> instancesList = getInstancesList(pathsList);

      this.instances = new File[instancesList.size()];
      for(int i = 0; i <= instances.length - 1; i++){
         this.instances[i] = instancesList.get(i);
      }

      this.instancesNames = new String[instances.length];
      for(int i = 0; i <= instancesNames.length - 1; i++){
         this.instancesNames[i] = instances[i].getName();
      }
   }

   private ArrayList<File> getInstancesList(File[] pathsList){
      ArrayList<File> instancesList = new ArrayList<File>();

      for(int i = 0; i <= pathsList.length - 1; i++){
         if(pathsList[i].getName().contains(".txt") == true){
            instancesList.add(pathsList[i]);
         }
      }

      return instancesList;
   }

   //private void solve(String problem, String algorithm, int numberOfRuns, int iterationsPerRun){
   private void solve(String problem, String algorithm, int numberOfRuns, int iterationsPerRun, int gammaPercentage){
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
                  RobustNextReleaseProblem robustNRP = new RobustNextReleaseProblem(instances[i], gammaPercentage);
                  adaptedAlgorithm = new RobustNRPSimulatedAnnealing(robustNRP);
               }
            }
            else if(algorithm.equals("ga")){
               if(problem.equals("tsp")){
                  TSPProblem tspProblem = new TSPProblem(instances[i]);
                  adaptedAlgorithm = new TSPGeneticAlgorithm(tspProblem, iterationsPerRun);
               }
               else if(problem.equals("rnrp")){ 
                  //RobustNextReleaseProblem robustNRP = new RobustNextReleaseProblem(instances[i]);
                  RobustNextReleaseProblem robustNRP = new RobustNextReleaseProblem(instances[i], gammaPercentage);
                  adaptedAlgorithm = new RobustNRPGeneticAlgorithm(robustNRP, iterationsPerRun);
               }
            }

            solutions[i][j] = adaptedAlgorithm.getSolution();
            runTimes[i][j] = adaptedAlgorithm.getTotalTime();
         }
      }
   }

   private void calculateMetrics(){
      this.instancesSolutionsMean = new double[instances.length];
      this.instancesSolutionsStandardDeviation = new double[instances.length];
      this.instancesRunTimesMean = new double[instances.length];
      this.instancesRunTimesStandardDeviation = new double[instances.length];

      for(int i = 0; i <= instances.length - 1; i++){
         this.instancesSolutionsMean[i] = getMean(solutions[i]);
         this.instancesSolutionsStandardDeviation[i] = getStandardDeviation(instancesSolutionsMean[i], solutions[i]);
         this.instancesRunTimesMean[i] = getMean(runTimes[i]);
         this.instancesRunTimesStandardDeviation[i] = getStandardDeviation(instancesRunTimesMean[i], runTimes[i]);
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
      for(int i = 0; i <= instances.length - 1; i++){
         printInstanceResults(i);
         System.out.println("--------------");
         if(i != instances.length - 1){
            System.out.println("");
         } 
      }
   }

   private void printInstanceResults(int instanceIndex){
      System.out.println(instancesNames[instanceIndex]);
      System.out.println("");

      System.out.println("solution: " + instancesSolutionsMean[instanceIndex] + " +/- " 
                           + instancesSolutionsStandardDeviation[instanceIndex]);
      System.out.println("run time: " + instancesRunTimesMean[instanceIndex] + " +/- " 
                           + instancesRunTimesStandardDeviation[instanceIndex]);
   }

   //private void writeResults(File outputFile){
   private void writeResults(File outputFile, int gammaPercentage){
      try{
         //BufferedWriter writer = new BufferedWriter(new FileWriter(outputFile));
         BufferedWriter writer = new BufferedWriter(new FileWriter(outputFile, true));

         for(int i = 0; i <= instances.length - 1; i++){
            //writeInstanceResults(i, writer);
            writeInstanceResults(i, writer, gammaPercentage);
            writer.write("--------------\n");
            if(i != instances.length - 1){
               writer.write("\n");
            } 
         }

         writer.close();
      }
      catch(Exception e){
         System.out.println("Error in write results to output file");
      }
   }

   //private void writeInstanceResults(int instanceIndex, BufferedWriter writer){
   private void writeInstanceResults(int instanceIndex, BufferedWriter writer, int gammaPercentage){
      try{
         //writer.write(instancesNames[instanceIndex] + "\n");
         writer.write(instancesNames[instanceIndex] + " with gamma = " + gammaPercentage + "%\n");
         writer.write("\n");

         writer.write("solution: " + instancesSolutionsMean[instanceIndex] + " +/- "
               + instancesSolutionsStandardDeviation[instanceIndex] + "\n");
         writer.write("run time: " + instancesRunTimesMean[instanceIndex] + " +/- "
               + instancesRunTimesStandardDeviation[instanceIndex] + "\n");
      }
      catch(Exception e){
         System.out.println("Error in write instance results");
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
         app = new App(new File(instancePath));

         if(args.length >= 6){
            outputFile = new File(args[5]);
         }
      }
      else{
         app = new App();
      }

      int[] gammaPercentages = {0, 25, 50, 80, 90, 100};

      for(int i = 0; i <= gammaPercentages.length - 1; i++){
         //app.solve(problem, algorithm, numberOfRuns, iterationsPerRun);
         app.solve(problem, algorithm, numberOfRuns, iterationsPerRun, gammaPercentages[i]);
         app.calculateMetrics();

         if(outputFile == null){
            app.printResults();
         }
         else{
            //app.writeResults(outputFile);
            app.writeResults(outputFile, gammaPercentages[i]);
         }

         System.out.println("executed for gamma = " + gammaPercentages[i] + "%");
      }

      System.exit(0);
   }
}
