package algorithms;

/**
 * Interface to specify the behavior of the algorithms.
 *
 * @author Matheus Paixao
 */
public interface Algorithm{
   public double getSolution(); //value calculated by the fitness function of each problem

   public double getTotalTime(); //time spended to found the solution
}
