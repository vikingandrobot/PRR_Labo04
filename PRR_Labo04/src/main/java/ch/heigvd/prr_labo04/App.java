package ch.heigvd.prr_labo04;

import ch.heigvd.prr_labo04.configuration.Configuration;
import ch.heigvd.prr_labo04.task.SiteManager;
import java.net.SocketException;
import java.util.InputMismatchException;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.Scanner;

/**
 *
 */
public class App {
   public static void main(String[] args) throws SocketException {
      
      if (args.length != 2) {
         System.out.println("Le programme doit recevoir en paramètre son ID de site"
                 + " (0 à 3) et le nombre de sites.");
         System.out.println("Lance le programme : java -jar <nom_application_jar> <id_du_site> <nombre_de_sites>");
         System.exit(1);
      }
      
      int id = Integer.parseInt(args[0]);
      
      if (id < 0 || id > 3) {
         System.out.println("Erreur: l'ID de site doit être dans l'intervalle doit être "
                 + "entre 0 et 3 compris.");
         System.exit(1);
      }
      
      int n = Integer.parseInt(args[1]);
      
      if (n < 1 || n > 4) {
         System.out.println("Erreur: le nombre de sites doit être dans l'intervalle doit être "
                 + "entre 1 et 4 compris.");
         System.exit(1);
      }
      
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
      SiteManager manager = new SiteManager(id, config);
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
                  manager.requestTerminate();
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