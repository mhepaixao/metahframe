import java.io.File;

public class SRPPAntQ extends AntQ{
   SRPPInstanceReader srppInstanceReader;

   double[][] objectivesValues;
   int numberOfRequirements;

   public SRPPAntQ(File instance, int numberOfIterations){
      super(numberOfIterations);
      srppInstanceReader = new SRPPInstanceReader(instance);
      objectivesValues = srppInstanceReader.getObjectiveValues();
   }

   public int getNumberOfNodes(){
      return 0;
   }

   public double getInitialPheromone(){
      return 0;
   }

   public double getHeuristicValue(Node node1, Node node2){
      return 0;
   }

   public double calculateSolutionValue(Edge[] solution){
      return 0;
   }
   
   public boolean isSolutionBest(double iterationSolutionValue, double bestSolutionValue){
      return false;
   }
}
