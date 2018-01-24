package ch.heigvd.prr_labo04;

import ch.heigvd.prr_labo04.task.SiteManager;
import java.util.InputMismatchException;
import java.util.Scanner;

/**
 *
 */
public class App {
   public static void main(String[] args) {
      
      // Boolean for the main loop
      boolean running = true;
      
      // Site manager that controls everything
      SiteManager manager = new SiteManager();
      
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
                  manager.startNewTask();
                  break;
               case 2:
                  System.out.println("End the application");
                  System.out.println("");
                  //manager.mustTerminate();
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