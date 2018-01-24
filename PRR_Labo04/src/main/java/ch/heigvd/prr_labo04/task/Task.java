/**
 * File: Task.java
 * Date: 24.01.2018
 * Authors: Sathiya Kiruhsnapillai & Mathieu Monteverde
 */

package ch.heigvd.prr_labo04.task;

import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Class that executes the tasks on a given site. 
 * It simulates the execution of a calculus.
 */
public class Task {
   
   // Arbitrary max duration allowed to simulate a calculus
   public static final int MAX_TASK_DURATION = 2000;
   
   // Arbitrary probability of generating a new task
   public static final double PROBABILITY = 0.5;
   
   private final Thread thread;
   
   private final TaskManager taskManager;
   
   /**
    * Constructor. Prepares a new Thread in which to execute the task.
    * @param taskManager the TaskManager that manages this task
    */
   public Task(TaskManager taskManager) {
      this.taskManager = taskManager;
      
      thread = new Thread(() -> {
         while (true) {
            try {
               Thread.sleep((int) (Math.random() * MAX_TASK_DURATION));
            } catch (InterruptedException ex) {
               Logger.getLogger(Task.class.getName()).log(Level.SEVERE, null, ex);
               this.taskManager.taskFinished();
               return;
            }
            
            if (Math.random() < PROBABILITY) {
               // Pick a random site
               // Start a new task on this site
            } else {
               this.taskManager.taskFinished();
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
