package problems.rnrp;

import instancereaders.RobustNRPInstanceReader;

import java.io.File;

/**
 * Class that describes the Robust Next Release Problem features.
 *
 * @author Matheus Paixao
 */
public class RobustNextReleaseProblem{
   RobustNRPInstanceReader robustNRPInstanceReader;

   int numberOfRequirements;
   double[] requirementsValues;
   int[] requirementsCosts;
   int[] requirementsDeviances;

   /**
    * Method to create the RobustNextReleaseProblem object.
    *
    * @author Matheus Paixao
    * @param instance the problem's instance
    */
   public RobustNextReleaseProblem(File instance){
      robustNRPInstanceReader = new RobustNRPInstanceReader(instance);
      this.numberOfRequirements = robustNRPInstanceReader.getNumberOfRequirements();
      this.requirementsValues = robustNRPInstanceReader.getRequirementsValues();
      this.requirementsCosts = robustNRPInstanceReader.getRequirementsCosts();
      this.requirementsDeviances = robustNRPInstanceReader.getRequirementsDeviances();
   }
}
