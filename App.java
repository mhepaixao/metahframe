import java.io.File;

public class App{
   public static void main(String[] args){
      String problem = null;
      String algorithm = null;
      int numberOfIterations = 0;
      Algorithm adaptedAlgorithm = null;

      InstanceChooser instanceChooser = new InstanceChooser();
      File instance = instanceChooser.getInstance();

      if(args.length >= 1){
         algorithm = args[0];
         problem = args[1];

         if(args.length >= 3){
            numberOfIterations = Integer.parseInt(args[2]);
         }
         else{
            numberOfIterations = 200;
         }
      }

      if(algorithm.equals("antq")){
         if(problem.equals("tsp")){
            adaptedAlgorithm = new TSPAntQ(instance, numberOfIterations);
         }
         else if(problem.equals("jssp")){
            adaptedAlgorithm = new JSSPAntQ(instance, numberOfIterations);
         }
         else if(problem.equals("srpp")){
            adaptedAlgorithm = new SRPPAntQ(instance, numberOfIterations);
         }
      }
      else if(algorithm.equals("random")){
         if(problem.equals("tsp")){
            adaptedAlgorithm = new TSPRandomAlgorithm(instance, numberOfIterations);
         }
      }

      System.out.println("Best Solution: "+adaptedAlgorithm.getSolution());
      System.out.println("Time elapsed: "+adaptedAlgorithm.getTotalTime());
      System.exit(0);
   }
}
