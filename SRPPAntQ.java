import java.io.File;

public class SRPPAntQ extends AntQ{
   SRPPInstanceReader srppInstanceReader;

   double[][] objectivesValues;
   int numberOfRequirements;
   int numberOfClients;
   double maxPossibleHeuristicValue;

   public SRPPAntQ(File instance, int numberOfIterations){
      super(numberOfIterations);
      srppInstanceReader = new SRPPInstanceReader(instance);
      objectivesValues = srppInstanceReader.getObjectiveValues();
      this.numberOfRequirements = srppInstanceReader.getNumberOfRequirements();
      this.numberOfClients = srppInstanceReader.getNumberOfClients();
      this.maxPossibleHeuristicValue = getMaxPossibleHeuristicValue();
   }

   public int getNumberOfNodes(){
      return this.numberOfRequirements;
   }

   public double getInitialPheromone(){
      return 0.01;
   }

   private double getMaxPossibleHeuristicValue(){
      double maxPossibleHeuristicValue = 0;

      for(int i = 0; i <= this.numberOfClients - 1; i++){
         maxPossibleHeuristicValue += 10 * 10;
      }

      return maxPossibleHeuristicValue + 9;
   }

   public double getHeuristicValue(Node node1, Node node2){
      return getObjectivesSum(node2.getIndex()) / maxPossibleHeuristicValue;
   }

   public double calculateSolutionValue(Edge[] solution){
      double solutionValue = 0;
      int[] nodes = getNodes(solution);

      for(int i = 0; i <= nodes.length - 1; i++){
         solutionValue += ((nodes.length) - i) * getObjectivesSum(nodes[i]);
      }

      return solutionValue;
   }

   private int[] getNodes(Edge[] solution){
      int[] nodes = new int[solution.length];

      for(int i = 0; i <= nodes.length - 1; i++){
         nodes[i] = solution[i].getNode2().getIndex();
      }

      return nodes;
   }

   private double getObjectivesSum(int requirement){
      double objectivesSum = 0;

      for(int i = 0; i <= objectivesValues.length - 1; i++){
         objectivesSum += objectivesValues[i][requirement];
      }

      return objectivesSum;
   }
   
   public boolean isSolutionBest(double iterationSolutionValue, double bestSolutionValue){
      boolean result = false;

      if(iterationSolutionValue > bestSolutionValue){
         result = true;
      }

      return result;
   }
}
