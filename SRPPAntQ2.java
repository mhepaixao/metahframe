import java.io.File;

public class SRPPAntQ2 extends AntQ{
   SRPPInstanceReader srppInstanceReader;

   double[][] objectivesValues;
   int numberOfRequirements;
   int numberOfClients;
   double maxPossibleHeuristicValue1;
   double maxPossibleHeuristicValue2;
   int[][] precedencesMatrix;

   public SRPPAntQ2(File instance, int numberOfIterations){
      super(numberOfIterations);
      srppInstanceReader = new SRPPInstanceReader(instance);
      objectivesValues = srppInstanceReader.getObjectiveValues();
      this.numberOfRequirements = srppInstanceReader.getNumberOfRequirements();
      this.numberOfClients = srppInstanceReader.getNumberOfClients();
      this.maxPossibleHeuristicValue1 = getMaxPossibleHeuristicValue1();
      this.maxPossibleHeuristicValue2 = getMaxPossibleHeuristicValue2();
      this.precedencesMatrix = srppInstanceReader.getPrecedencesMatrix();
   }

   public int getNumberOfNodes(){
      return this.numberOfRequirements;
   }

   public double getInitialPheromone(){
      return 0.01;
   }

   /**
    * Method to init the precedence constrained ants.
    *
    * One ant is put in each requirement of the instance.
    * @author Matheus Paixao
    * @see PrecedenceConstrainedAnt constructor in PrecedenceConstrainedAnt class.
    */
   protected void initAnts2(){
      this.ants1 = new Ant[srppInstanceReader.getNumberOfRequirementsWithNoPrecedence()]; 
      //this.ants1 = new Ant[1]; 
      Node initialNode = null;

      for(int i = 0; i <= srppInstanceReader.getNumberOfRequirements() - 1; i++){
         initialNode = new Node(i);
         if(hasPrecedecessor(initialNode) == false){
            addAnt(new PrecedenceConstrainedAnt2(this, getQ0(), new Node(i), srppInstanceReader.getPrecedencesMatrix(), 1), 1);
         }
      }

      //this.ants2 = new Ant[srppInstanceReader.getNumberOfRequirementsWithNoPrecedence()]; 
      this.ants2 = new Ant[1]; 
      initialNode = null;

      for(int i = 0; i <= srppInstanceReader.getNumberOfRequirements() - 1; i++){
         initialNode = new Node(i);
         if(hasPrecedecessor(initialNode) == false){
            addAnt(new PrecedenceConstrainedAnt2(this, getQ0(), new Node(i), srppInstanceReader.getPrecedencesMatrix(), 2), 2);
         }
      }
   }

   private boolean hasPrecedecessor(Node requirement){
      boolean result = false;

      for(int i = 0; i <= precedencesMatrix[requirement.getIndex()].length - 1; i++){
         if(precedencesMatrix[requirement.getIndex()][i] == 1){
            result = true;
            break;
         }
      }

      return result;
   }

   private void addAnt(Ant ant, int objective){
      if(objective == 1){
         for(int i = 0; i <= this.ants1.length - 1; i++){
            if(this.ants1[i] == null){
               this.ants1[i] = ant;
               break;
            }
         }
      }
      else if(objective == 2){
         for(int i = 0; i <= this.ants2.length - 1; i++){
            if(this.ants2[i] == null){
               this.ants2[i] = ant;
               break;
            }
         }
      }
   }

   private double getMaxPossibleHeuristicValue1(){
      double maxPossibleHeuristicValue = 0;

      for(int i = 0; i <= this.numberOfClients - 1; i++){
         maxPossibleHeuristicValue += 10 * 10;
      }

      return maxPossibleHeuristicValue;
   }

   private double getMaxPossibleHeuristicValue2(){
      return 9;
   }

   public double getHeuristicValue(Node node1, Node node2, int objective){
      double heuristicValue = 0;

      if(objective == 1){
         heuristicValue = objectivesValues[0][node2.getIndex()] / maxPossibleHeuristicValue1;
      }
      else if(objective == 2){
         heuristicValue = objectivesValues[1][node2.getIndex()] / maxPossibleHeuristicValue2;
      }

      return heuristicValue;
   }

   public double getHeuristicValue(Node node1, Node node2){
      return 0;
   }

   public double calculateSolutionValue(Edge[] solution, int objective){
      double solutionValue = 0;
      int[] nodes = getNodes(solution);

      if(objective == 1){
         for(int i = 0; i <= nodes.length - 1; i++){
            solutionValue += (nodes.length - i) * objectivesValues[0][nodes[i]];
         }
      }
      else if(objective == 2){
         for(int i = 0; i <= nodes.length - 1; i++){
            solutionValue += (nodes.length - i) * objectivesValues[1][nodes[i]];
         }
      }
      else if(objective == 3){
         for(int i = 0; i <= nodes.length - 1; i++){
            solutionValue += (nodes.length - i) * getObjectivesSum(nodes[i]);
         }
      }

      return solutionValue;
   }

   public double calculateSolutionValue(Edge[] solution){
      return 0;
   }

   private int[] getNodes(Edge[] solution){
      int[] nodes = new int[solution.length];

      for(int i = 0; i <= nodes.length - 1; i++){
         nodes[i] = solution[i].getNode1().getIndex();
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
