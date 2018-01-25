/**
 * File: TaskManager.java
 * Date: 24.01.2018
 * Authors: Sathiya Kiruhsnapillai & Mathieu Monteverde
 */

package ch.heigvd.prr_labo04.task;

/**
 * TaskManager interface represents components that manage Task instances.
 */
public interface TaskManager {
   
   /**
    * Start a new task on a given site.
    * @param siteId the Id of the site on which to start a task
    */
   public void startNewTaskOnSite(int siteId);
   
   /**
    * Notify that a task is finished.
    */
   public void finishedTask();
}
