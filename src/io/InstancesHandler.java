package io;

import java.io.File;

import java.util.ArrayList;

public class InstancesHandler{
   private File[] instances;
   String[] instancesNames;

   public InstancesHandler(){
      InstanceChooser instanceChooser = new InstanceChooser();
      File instance = instanceChooser.getInstance();

      createSingleInstance(instance);
   }

   public InstancesHandler(String pathString){
      File path = new File(pathString);

      if(isInstancePath(path) == true){
         createSingleInstance(path);
      }
      else{
         createInstancesList(path);
      }
   }

   private void createSingleInstance(File instance){
      this.instances = new File[1];
      this.instances[0] = instance;

      this.instancesNames = new String[1];
      this.instancesNames[0] = instance.getName();
   }

   private boolean isInstancePath(File path){
      boolean result = false;

      if(path.getName().contains(".txt") == true){
         result = true;
      }

      return result;
   }

   private void createInstancesList(File folder){
      File[] pathsList = folder.listFiles();
      ArrayList<File> instancesList = getInstancesList(pathsList);

      this.instances = new File[instancesList.size()];
      for(int i = 0; i <= instances.length - 1; i++){
         this.instances[i] = instancesList.get(i);
      }

      this.instancesNames = new String[instances.length];
      for(int i = 0; i <= instancesNames.length - 1; i++){
         this.instancesNames[i] = instances[i].getName();
      }
   }

   private ArrayList<File> getInstancesList(File[] pathsList){
      ArrayList<File> instancesList = new ArrayList<File>();

      for(int i = 0; i <= pathsList.length - 1; i++){
         if(pathsList[i].getName().contains(".txt") == true){
            instancesList.add(pathsList[i]);
         }
      }

      return instancesList;
   }

   public File[] getInstances(){
      return this.instances;
   }

   public String getInstanceName(int instanceIndex){
      return this.instancesNames[instanceIndex];
   }
}
