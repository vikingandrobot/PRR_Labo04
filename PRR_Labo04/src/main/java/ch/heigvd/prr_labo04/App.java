/**
 * File: App.java
 * Date: 24.01.2018
 * Authors: Sathiya Kiruhsnapillai & Mathieu Monteverde
 */

package ch.heigvd.prr_labo04;

import ch.heigvd.prr_labo04.configuration.Configuration;
import ch.heigvd.prr_labo04.task.SiteManager;
import java.net.SocketException;
import java.util.InputMismatchException;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.Scanner;

/**
 * The App class is the entry point of the program for this project. The main 
 * logic and the implementation of the termination algorithm is located in the 
 * SiteManager class. 
 * 
 * The main method of the App class creates the resources to run the SiteManager
 * (configuration and such) and offers a text interface in the command line
 * to start tasks and/or to forbid the creation of new tasks. 
 * 
 * Each program must be given it's site Id and the number of sites 
 * in the system when executed. 
 * 
 * java -jar PRR_Labo04-with-dependencies-0.1.jar <id> <numberOfSites>
 * 
 * For example, run : 
 * 
 * java -jar PRR_Labo04-with-dependencies-0.1.jar 0 4
 * 
 * We made the assumption that the site of id 0 starts as the holder of the token to
 * pass around when the site become inactive. This means that when starting the whole
 * systema, the site to start the first task (or request the termination) must be 
 * the site of id 0. Otherwise, the site executing said commands won't be the holder
 * of the token and won't be able to request the termination of the system 
 * if the tasks directly end.
 */
public class App {
   public static void main(String[] args) throws SocketException {
      
      // The program must be executed with two arguments
      if (args.length != 2) {
         System.out.println("Le programme doit recevoir en paramètre son ID de site"
                 + " (0 à 3) et le nombre de sites.");
         System.out.println("Lance le programme : java -jar <nom_application_jar> <id_du_site> <nombre_de_sites>");
         System.exit(1);
      }
      
      // Get the Id of the current site
      int id = Integer.parseInt(args[0]);
      
      // The Id must be inside [1, 3]
      if (id < 0 || id > 3) {
         System.out.println("Erreur: l'ID de site doit être dans l'intervalle doit être "
                 + "entre 0 et 3 compris.");
         System.exit(1);
      }
      
      // Get the number of sites
      int n = Integer.parseInt(args[1]);
      
      // The number of sites must be inside [1, 4]
      if (n < 1 || n > 4) {
         System.out.println("Erreur: le nombre de sites doit être dans l'intervalle doit être "
                 + "entre 1 et 4 compris.");
         System.exit(1);
      }
      
      // Load the configuration
      Configuration config;
      try {
         Configuration.loadConfiguration("/processes.txt", id, n);
         config = Configuration.getConfiguration();
      } catch (Exception ex) {
         System.out.println("An error occured when reading the configuration.");
         Logger.getLogger(App.class.getName())
                 .log(Level.SEVERE, null, ex);
         System.exit(1);
         return;
      }

      // Boolean for the main loop
      boolean running = true;
      
      // Site manager that controls everything
      SiteManager manager = new SiteManager();
      manager.start();
      
      System.out.println("Welcome to this application");
      System.out.println("");
      
      do {
         System.out.println("Please, select one of the following");
         System.out.println("\t1 : Create a new task");
         System.out.println("\t2 : End the application");
         System.out.println("");
         
         try {
            System.out.print("Type your choice : ");
            Scanner scanner = new Scanner(System.in);
            int option = scanner.nextInt();
            
            switch(option) {
               case 1:
                  System.out.println("A new task has started");
                  System.out.println("");
                  manager.startNewTaskOnSite(id);
                  break;
               case 2:
                  System.out.println("End the application");
                  System.out.println("");
                  manager.fordidNewTasks();
                  running = false;
                  break;
               default:
                  System.out.println("This input is not valid");
                  System.out.println("");
            }
         }
         catch(InputMismatchException e) {
            System.out.println("This input is not valid");
            System.out.println("");
         }
      } while(running);
   }
}