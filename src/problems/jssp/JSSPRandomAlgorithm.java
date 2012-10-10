package problems.jssp;

import algorithms.random.RandomAlgorithm;

/**
 * Class to implement the RandomAlgorithm algorithm to the Job Sequence Schedule Problem.
 *
 * @author Matheus Paixao
 */
public class JSSPRandomAlgorithm extends RandomAlgorithm{
   private JSSPProblem jsspProblem;

   /**
    * Method to create the JSSPRandomAlgorithm object, receive the JSSPProblem object and
    * the number of iterations is passed to RandomAlgorithm constructor.
    *
    * @author Matheus Paixao
    * @param jsspProblem the JSSPProblem object
    * @param numberOfIterations number of iterations to be runned
    */
   public JSSPRandomAlgorithm(JSSPProblem jsspProblem, int numberOfIterations){
      super(numberOfIterations);
      this.jsspProblem = jsspProblem;
   }

   public int getNumberOfNodes(){
      return jsspProblem.getNumberOfJobs(); //in JSSP the jobs are represented by the nodes
   }

   /**
    * Method to compare if a solution value is better than another one.
    *
    * @author Matheus Paixao
    * @param iterationSolutionValue the fitness value of some solution
    * @param bestSolutionValue the best fitness value of an iteration
    * @return true if the first fitness value is best than the other one
    * @see isSolutionBest in JSSPProblem class
    */
   public boolean isSolutionBest(double iterationSolutionValue, double bestSolutionValue){
      return jsspProblem.isSolutionBest(iterationSolutionValue, bestSolutionValue);
   }

   /**
    * Method that implements the fitness function of JSSP problem.
    *
    * @author Matheus Paixao
    * @param solution the array of int that corresponds to the solution founded by the algorithm
    * @return fitness value of the solution
    * @see calculateSolutionValue in JSSPProblem class
    */
   public double calculateSolutionValue(Integer[] solution){
      return jsspProblem.calculateSolutionValue(solution);
   }

   /**
    * Method to test if the founded solution broke some restriction.
    *
    * In JSSP there's no job sequence restriction.
    * @author Matheus Paixao
    */
   public boolean satisfyAllRestrictions(Integer[] solution){
      return true;
   }
}
