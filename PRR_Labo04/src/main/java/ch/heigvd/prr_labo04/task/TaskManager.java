/**
 * File: TaskManager.java
 * Date: 24.01.2018
 * Authors: Sathiya Kiruhsnapillai & Mathieu Monteverde
 */


package ch.heigvd.prr_labo04.task;

import ch.heigvd.prr_labo04.configuration.Configuration;

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
   
   /**
    * Get the system configuration
    * @return the configuration to use
    */
   public Configuration getConfiguration();
   
   /**
    * Get the current site id;
    * @return the site id
    */
   public int getSiteId();
}
