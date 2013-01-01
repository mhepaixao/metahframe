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

   public File getInstance(){
      return this.instance;
   }

   public void solve(String problem, String algorithm, int numberOfRuns, int iterationsPerRun){
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

   public void printResults(){
      System.out.println("solution values:");
      for(int i = 0; i <= solutions.length - 1; i++){
         System.out.println(solutions[i]);
      }
      System.out.println("");

      System.out.println("run times:");
      for(int i = 0; i <= runTimes.length - 1; i++){
         System.out.println(runTimes[i]);
      }
      System.out.println("");

      System.exit(0);
   }

   public static void main(String[] args){
      String problem = null;
      String algorithm = null;
      int numberOfRuns = 0;
      int iterationsPerRun = 0;

      algorithm = args[0]; //first parameter is the algorithm to be used
      problem = args[1]; //second parameter is the problem to be solved
      numberOfRuns = Integer.parseInt(args[2]);
      iterationsPerRun = Integer.parseInt(args[3]);

      App app = new App();
      app.solve(problem, algorithm, numberOfRuns, iterationsPerRun);
      app.printResults();
   }
}
