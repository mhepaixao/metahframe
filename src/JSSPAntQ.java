import java.io.File;

/**
 * Class to implement the AntQ class to the Job Sequence Schedule Problem.
 *
 * @author Matheus Paixao
 */
public class JSSPAntQ extends AntQ{
   private JSSPInstanceReader jsspInstanceReader;

   private int numberOfJobs;
   private double[][] timesMatrix;

   /**
    * Method to create the JSSPAntQ object, receive the instance to read and
    * the number of iterations is passed to AntQ constructor.
    *
    * @author Matheus Paixao
    * @param instance the instance to read
    * @param numberOfIterations number of iterations to be runned
    * @see AntQ constructor
    * @see JSSPInstanceReader constructor
    * @see getTimesMatrix in TSPInstanceReader
    */
   public JSSPAntQ(File instance, int numberOfIterations){
      super(numberOfIterations);
      jsspInstanceReader = new JSSPInstanceReader(instance);
      this.timesMatrix = jsspInstanceReader.getTimesMatrix();
      this.numberOfJobs = timesMatrix.length;
   }

   public int getNumberOfNodes(){
      return this.numberOfJobs; //in JSSP the jobs are represented by the nodes
   }

   public double getInitialPheromone(){
      return 0.01; //this initial pheromone value was founded empirically
   }

   /**
    * Method to get the heuristic value of an edge.
    *
    * In JSSP as smaller the makespan, higher is the heuristic value.
    * @author Matheus Paixao
    * @param node1 the first node of the edge
    * @param node2 the second node of the edge
    * @return the heuristic value of the edge composed by the two passed nodes
    * @see getCurrentAnt
    * @see getJobSequence
    * @see getMakespan
    */
   public double getHeuristicValue(Node node1, Node node2){
      int[] jobSequence = getJobSequence(node1, node2, getCurrentAnt());
      
      return 1 / getMakespan(jobSequence);
   }

   /**
    * Method that implements the fitness function of JSSP problem.
    *
    * @author Matheus Paixao
    * @param solution the array of edges that corresponds to the solution founded by the algorithm
    * @return fitness value (makespan) of the solution
    * @see getMakespan
    */
   public double calculateSolutionValue(Edge[] solution){
      int[] jobSequence = new int[solution.length];

      for(int i = 0; i <= jobSequence.length - 1; i++){
         jobSequence[i] = solution[i].getNode1().getIndex();
      }

      return getMakespan(jobSequence);
   }

   /**
    * Method to get the job sequence of an "in construction" solution, given the ant
    * and the next edge to add.
    *
    * @author Matheus Paixao
    * @param node1 the first node of the next edge
    * @param node2 the second node of the next edge
    * @param ant the ant that are building its solution
    * @return an array of int that represents the job sequence with the new edge
    * @see getTour in Ant class
    * @see getNumberOfJobs
    */
   private int[] getJobSequence(Node node1, Node node2, Ant ant){
      int[] jobSequence;

      Edge[] solution = ant.getTour();
      jobSequence = new int[getNumberOfJobs(solution) + 2]; //there is the next edge to add

      if(solution[0] == null){
         jobSequence[0] = node1.getIndex();
         jobSequence[1] = node2.getIndex();
      }
      else{
         for(int i = 0; i <= solution.length - 1; i++){
            if(solution[i] != null){
               jobSequence[i] = solution[i].getNode1().getIndex();
               jobSequence[i+1] = solution[i].getNode2().getIndex();
            }
         }
         jobSequence[jobSequence.length - 1] = node2.getIndex();
      }

      return jobSequence;
   }

   /**
    * Method to get the number of jobs of a solution.
    *
    * @author Matheus Paixao
    * @param solution the solution to count the number of jobs
    * @return the number of jobs of the solution
    */
   private int getNumberOfJobs(Edge[] solution){
      int numberOfJobs = 0;

      for(int i = 0; i <= solution.length - 1; i++){
         if(solution[i] != null){
            numberOfJobs++;
         }
      }

      return numberOfJobs;
   }

   /**
    * Method to calculate the makespan of a sequence of jobs (fitness function).
    *
    * For more information about this algorithm, search for "JSSP flowchart makespan".
    * @author Matheus Paixao
    * @param jobSequence sequence of jobs to calculate the makespan
    * @return the makespan value of the job sequence
    */
   private double getMakespan(int[] jobSequence){
      double[] makespan = new double[timesMatrix[0].length];
      int job = 0;

      for(int i = 0; i <= jobSequence.length - 1; i++){
         job = jobSequence[i];
         makespan[0] = makespan[0] + timesMatrix[job][0];
         for(int j = 1; j <= timesMatrix[0].length - 1; j++){
            if(makespan[j] > makespan[j - 1]){
               makespan[j] = makespan[j] + timesMatrix[job][j];
            }
            else{
               makespan[j] = makespan[j - 1] + timesMatrix[job][j];
            }
         }
      }

      return makespan[timesMatrix[0].length - 1];
   }

   /**
    * Method to compare if a solution value is better than another one.
    *
    * In JSSP as smaller fitness value as better.
    * @author Matheus Paixao
    * @param iterationSolutionValue the fitness value of some solution
    * @param bestSolutionValue the best fitness value of an iteration
    * @return true if the first fitness value is best than the other one
    */
   public boolean isSolutionBest(double iterationSolutionValue, double bestSolutionValue){
      boolean result = false;

      if(iterationSolutionValue < bestSolutionValue){
         result = true;
      }

      return result;
   }
}
