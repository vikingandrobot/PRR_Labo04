/**
 * File: SiteManager.java
 * Date: 24.01.2018
 * Authors: Sathiya Kiruhsnapillai & Mathieu Monteverde
 */

package ch.heigvd.prr_labo04.task;

/**
 * Class that manages the site in the environment.
 */
public class SiteManager implements TaskManager {
   
   private int numberOfTasks;
   
   public SiteManager() {
      numberOfTasks = 0;
   }
   
   /**
    * Announce the end of a Task
    */
   @Override
   public synchronized void taskFinished() {
      --numberOfTasks;
   }

   @Override
   public void startNewTaskOnSite(int siteId) {
      throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
   }

   @Override
   public synchronized void startNewTask() {
      Task t = new Task(this);
      t.execute();
      ++numberOfTasks;
   }
   
   
}
