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
   
   /**
    * Constructor. Prepares a new Thread in which to execute the task.
    */
   public Task() {
      thread = new Thread(() -> {
         while (true) {
            try {
               Thread.sleep((int) (Math.random() * MAX_TASK_DURATION));
            } catch (InterruptedException ex) {
               Logger.getLogger(Task.class.getName()).log(Level.SEVERE, null, ex);
               exit();
            }
            
            if (Math.random() < PROBABILITY) {
               // Pick a random site
               // Start a new task on this site
            } else {
               exit();
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
   
   /**
    * Exit the task and notify the TaskManager.
    */
   private void exit() {
      // Notify the TaskManager that we finished
      // Exit the task 
   }
}
