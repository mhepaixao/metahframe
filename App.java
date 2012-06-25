import java.io.File;

public class App{
   public static void main(String[] args){
      String problem = null;
      int numberOfIterations = 0;
      Algorithm algorithm = null;

      InstanceChooser instanceChooser = new InstanceChooser();
      File instance = instanceChooser.getInstance();

      if(args.length >= 1){
         problem = args[0];

         if(args.length >= 2){
            numberOfIterations = Integer.parseInt(args[1]);
         }
         else{
            numberOfIterations = 200;
         }
      }

      if(problem.equals("tsp")){
         algorithm = new TSPAntQ(instance, numberOfIterations);
      }
      else if(problem.equals("jssp")){
         algorithm = new JSSPAntQ(instance, numberOfIterations);
      }
      else if(problem.equals("rpp")){
         algorithm = new RPPAntQ(instance, numberOfIterations);
      }

      System.out.println("Best Solution: "+algorithm.getSolution());
      System.out.println("Time elapsed: "+algorithm.getTotalTime());
      System.exit(0);
   }
}
