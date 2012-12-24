package instancereaders;

import java.io.File;
import java.io.FileReader;
import java.io.BufferedReader;

public class RobustNRPInstanceReader{
   File instance;
   String[] instanceLines;

   public RobustNRPInstanceReader(File instance){
      this.instance = instance;
   }
}
