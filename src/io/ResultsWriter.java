package io;

import statistics.StatisticalAnalyzer;

import java.io.File;
import java.io.FileWriter;
import java.io.BufferedWriter;

import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;

public class ResultsWriter{
   private StatisticalAnalyzer statisticalAnalyzer;
   private InstancesHandler instancesHandler;
   String algorithm;

   //public ResultsWriter(StatisticalAnalyzer statisticalAnalyzer, InstancesHandler instancesHandler){
   public ResultsWriter(StatisticalAnalyzer statisticalAnalyzer, InstancesHandler instancesHandler, String algorithm){
      this.statisticalAnalyzer = statisticalAnalyzer;
      this.instancesHandler = instancesHandler;
      this.algorithm = algorithm;
      this.parameters = parameters;

      DecimalFormatSymbols dfs = new DecimalFormatSymbols();
      dfs.setDecimalSeparator('.');
      df = new DecimalFormat("0.00", dfs);
   }

   public void printResults(){
      int numberOfInstances = statisticalAnalyzer.getNumberOfInstances();

      for(int i = 0; i <= numberOfInstances - 1; i++){
         printInstanceResults(statisticalAnalyzer, i);
         System.out.println("--------------");
         if(i != numberOfInstances - 1){
            System.out.println("");
         } 
      }
   }

   private void printInstanceResults(StatisticalAnalyzer statisticalAnalyzer, int instanceIndex){
      System.out.println(instancesHandler.getInstanceName(instanceIndex));
      System.out.println("");

      System.out.println("solution: " + df.format(statisticalAnalyzer.getInstanceSolutionMean(instanceIndex)) + " +/- " 
                           + df.format(statisticalAnalyzer.getInstanceSolutionStandardDeviation(instanceIndex)));
      System.out.println("run time: " + df.format(statisticalAnalyzer.getInstanceRunTimeMean(instanceIndex)) + " +/- " 
                           + df.format(statisticalAnalyzer.getInstanceRuntimeStandardDeviation(instanceIndex)));
   }

   public void writeResults(){
      int numberOfInstances = statisticalAnalyzer.getNumberOfInstances();

      try{
         BufferedWriter writer = new BufferedWriter(new FileWriter(new File("/home/mhepaixao/instancias/rnrp/jss/parametrizacao/results_"+algorithm+".txt"), true));

         for(int i = 0; i <= numberOfInstances - 1; i++){
            writeInstanceResults(i, writer);
            writer.write("--------------\n");
            if(i != numberOfInstances - 1){
               writer.write("\n");
            } 
         }

         writer.close();
      }
      catch(Exception e){
         System.out.println("Error in write results to output file");
      }
   }

   private void writeInstanceResults(int instanceIndex, BufferedWriter writer){
      try{
         writer.write(instancesHandler.getInstanceName(instanceIndex) + "\n");
         writer.write("\n");

         writer.write("solution: " + df.format(statisticalAnalyzer.getInstanceSolutionMean(instanceIndex)) + " +/- " 
                           + df.format(statisticalAnalyzer.getInstanceSolutionStandardDeviation(instanceIndex)) + "\n");
         writer.write("run time: " + df.format(statisticalAnalyzer.getInstanceRunTimeMean(instanceIndex)) + " +/- " 
                           + df.format(statisticalAnalyzer.getInstanceRuntimeStandardDeviation(instanceIndex)) + "\n");
      }
      catch(Exception e){
         System.out.println("Error in write instance results");
      }
   }
}
