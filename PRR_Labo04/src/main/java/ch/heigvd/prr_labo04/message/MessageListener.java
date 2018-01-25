/**
 * File: MessageListener.java
 * Date: 24.01.2018
 * Authors: Sathiya Kiruhsnapillai & Mathieu Monteverde
 */

package ch.heigvd.prr_labo04.message;

/**
 * The MessageListener interface allows to be notified when a message 
 * has been received.
 */
public interface MessageListener {
   
   /**
    * Method called when a message has been received.
    * @param message the received message
    */
   public void messageReceived(Message message);
}
