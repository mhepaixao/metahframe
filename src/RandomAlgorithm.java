import java.util.Collections;
import java.util.ArrayList;
import java.util.Random;

public abstract class RandomAlgorithm implements Algorithm{
   private int numberOfIterations;
   private double totalTime;

   private ArrayList<Integer> dynamicListOfNodes;
   private Random random;

   public abstract double calculateSolutionValue(int[] solution);
   public abstract boolean isSolutionBest(double iterationSolutionValue, double bestSolutionValue);
   public abstract int getNumberOfNodes();
   
   public RandomAlgorithm(int numberOfIterations){
      this.numberOfIterations = numberOfIterations;
      setTotalTime(0);
      dynamicListOfNodes = new ArrayList<Integer>();
      random = new Random();
   }

   private int getNumberOfIterations(){
      return this.numberOfIterations;
   }

   public double getTotalTime(){
      return this.totalTime;
   }

   private void setTotalTime(double totalTime){
      this.totalTime = totalTime;
   }

   public double getSolution2(){
      return 0;
   }

   public double getSolution(){
      loadDynamicListOfNodes();

      double initialTime = 0;
      double finalTime = 0;

      int[] iterationSolution = null;
      double iterationSolutionValue = 0;
      int[] bestSolution = null;
      double bestSolutionValue = 0;

      int iterationsCounter = 0;

      initialTime = System.currentTimeMillis();
      while(iterationsCounter <= getNumberOfIterations() - 1){
         iterationSolution = getIterationSolution();
         iterationSolutionValue = calculateSolutionValue(iterationSolution);

         if(bestSolution != null){
            if(isSolutionBest(iterationSolutionValue, bestSolutionValue) == true){
               System.out.println("found best solution");
               bestSolution = iterationSolution;
               bestSolutionValue = iterationSolutionValue;
            }
         }
         else{
            bestSolution = iterationSolution;
            bestSolutionValue = iterationSolutionValue;
         }

         iterationsCounter++;
      }
      finalTime = System.currentTimeMillis();

      setTotalTime(finalTime - initialTime);

      return bestSolutionValue;
   }

   private void loadDynamicListOfNodes(){
      for(int i = 0; i <= getNumberOfNodes() - 1; i++){
         dynamicListOfNodes.add(new Integer(i));
      }
   }

   private int[] getIterationSolution(){
      int[] iterationSolution = new int[getNumberOfNodes()];
      Collections.shuffle(dynamicListOfNodes);

      for(int i = 0; i <= iterationSolution.length - 1; i++){
         iterationSolution[i] = dynamicListOfNodes.get(i);
      }

      return iterationSolution;
   }

   private int getRandomNumber(int limit){
      return random.nextInt(limit);
   }
}
