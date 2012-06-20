import javax.swing.JFileChooser;
import javax.swing.JFrame;

import java.io.File;

public class InstanceChooser extends JFrame{

   /**
    * Method to get the instance file.
    *
    * Uses a JFileChooser to select the instance file.
    * @author Matheus Paixao
    * @return the instance file.
    */
   public File getInstance(){
      JFileChooser instanceChooser = new JFileChooser();
      File instance = null;

      Integer choose = instanceChooser.showOpenDialog(this);
      if (choose.equals(JFileChooser.APPROVE_OPTION)) {
         instance = instanceChooser.getSelectedFile();
      }

      return instance;
   }
}
