/**
 * File: Task.java
 * Date: 24.01.2018
 * Authors: Sathiya Kiruhsnapillai & Mathieu Monteverde
 */

package ch.heigvd.prr_labo04.task;

import ch.heigvd.prr_labo04.configuration.Configuration;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Class that executes the tasks on a given site. 
 * It simulates the execution of a calculus.
 */
public class Task {
   
   // The Configuration to use
   private final Configuration config;
   
   // Arbitrary max duration allowed to simulate a calculus
   public static final int MAX_TASK_DURATION = 3000;
   
   // Arbitrary probability of generating a new task
   public static final double PROBABILITY = 0.7;
   
   // The thread in which to execute the task
   private final Thread thread;
   
   // The TaskManager that instanciated the Task
   private final TaskManager taskManager;
   
   /**
    * Constructor. Prepares a new Thread in which to execute the task.
    * @param taskManager the TaskManager that manages this task
    */
   public Task(TaskManager taskManager) {
      // Save TaskManager
      this.taskManager = taskManager;
      // Configuration
      this.config = Configuration.getConfiguration();
      
      thread = new Thread(() -> {
         // Do tasks in loop
         while (true) {
            try {
               // Sleep to simulate the calculus
               Thread.sleep((int)(Math.random() * MAX_TASK_DURATION));
            } catch (InterruptedException ex) {
               // If an error occurs, consider it finished
               Logger.getLogger(Task.class.getName()).log(Level.SEVERE, null, ex);
               this.taskManager.finishedTask();
               return;
            }
            
            // Choose whether to stop or start another task
            if (Math.random() < PROBABILITY && config.getNumberOfSites() > 1) {
               
               // Start a new task on a random site.
               int j;
               int numberOfSites = config.getNumberOfSites();
               do {
                  j = (int) (Math.random() * numberOfSites);
               } while (j == config.getSiteId());
               taskManager.startNewTaskOnSite(j);
            } else {
               this.taskManager.finishedTask();
               break;
            }
         }
      });
   }
   
   /**
    * Execute the task.
    */
   public void execute() {
      thread.start();
   }
}
