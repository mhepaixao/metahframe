import java.io.File;

public class JSSPAntQ extends AntQ{

   public double getHeuristicValue(Node node1, Node node2, int objective){
      return 0;
   }

   private JSSPInstanceReader jsspInstanceReader;

   private int numberOfJobs;
   private double[][] timesMatrix;

   public JSSPAntQ(File instance, int numberOfIterations){
      super(numberOfIterations);
      jsspInstanceReader = new JSSPInstanceReader(instance);
      this.timesMatrix = jsspInstanceReader.getTimesMatrix();
      this.numberOfJobs = timesMatrix.length;
   }

   public int getNumberOfNodes(){
      return this.numberOfJobs;
   }

   public double getInitialPheromone(){
      return 0.01;
   }

   public double getHeuristicValue(Node node1, Node node2){
      int[] jobSequence = getJobSequence(node1, node2, getCurrentAnt());
      
      return 1 / getMakespan(jobSequence);
   }

   public double calculateSolutionValue(Edge[] solution){
      int[] jobSequence = new int[solution.length];

      for(int i = 0; i <= jobSequence.length - 1; i++){
         jobSequence[i] = solution[i].getNode1().getIndex();
      }

      return getMakespan(jobSequence);
   }

   private int[] getJobSequence(Node node1, Node node2, Ant ant){
      int[] jobSequence;

      Edge[] solution = ant.getTour();
      jobSequence = new int[getNumberOfJobs(solution)];

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

   private int getNumberOfJobs(Edge[] solution){
      int numberOfJobs = 0;

      for(int i = 0; i <= solution.length - 1; i++){
         if(solution[i] != null){
            numberOfJobs++;
         }
      }

      return numberOfJobs + 2;
   }

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

   public boolean isSolutionBest(double iterationSolutionValue, double bestSolutionValue){
      boolean result = false;

      if(iterationSolutionValue < bestSolutionValue){
         result = true;
      }

      return result;
   }
}
