package ch.heigvd.prr_labo04.task;

/**
 * TaskManager interface represents components that manage Task instances.
 */
public interface TaskManager {
   
   /**
    * Start a new task.
    */
   public void startNewTask();
   
   /**
    * Start a new task on a given site.
    * @param siteId the Id of the site
    */
   public void startNewTaskOnSite(int siteId);
   
   /**
    * Notify that a task is finished.
    */
   public void taskFinished();
}
