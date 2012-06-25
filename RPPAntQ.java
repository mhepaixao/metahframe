import java.io.File;

public class RPPAntQ extends AntQ{
   RPPInstanceReader rppInstanceReader;

   double[][] objectivesValues;
   int numberOfRequirements;

   public RPPAntQ(File instance, int numberOfIterations){
      super(numberOfIterations);
      rppInstanceReader = new RPPInstanceReader(instance);
      objectivesValues = rppInstanceReader.getObjectiveValues();
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
