package problems.tsp;

import algorithms.sa.SimulatedAnnealing;
import instancereaders.TSPInstanceReader;

import java.io.File;

public class TSPSimulatedAnnealing extends SimulatedAnnealing{
   private TSPProblem tspProblem;

   public TSPSimulatedAnnealing(TSPProblem tspProblem, int numberOfIterations){
      super(numberOfIterations);
      this.tspProblem = tspProblem;
   }
}
