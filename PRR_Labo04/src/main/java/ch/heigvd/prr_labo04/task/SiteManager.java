/**
 * File: SiteManager.java
 * Date: 24.01.2018
 * Authors: Sathiya Kiruhsnapillai & Mathieu Monteverde
 */
package ch.heigvd.prr_labo04.task;

import ch.heigvd.prr_labo04.configuration.Configuration;
import ch.heigvd.prr_labo04.message.Message;
import ch.heigvd.prr_labo04.message.MessageListener;
import ch.heigvd.prr_labo04.message.MessageReceiver;
import ch.heigvd.prr_labo04.message.MessageType;
import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Class that manages the site in the system.
 */
public class SiteManager implements TaskManager, MessageListener {
   
   // The id of the current site
   private final int siteId;
   
   // The configuration of the site system
   private final Configuration config;

   // List of received messages
   private final List<Message> messages;
   
   // Number of tasks currently executing
   private int numberOfTasks;
   
   private final MessageReceiver messageReceiver;
   
   // Socket to send messages
   private final DatagramSocket sendSocket;
   
   // Whether or not other site has sent the END message
   private boolean terminate;
   
   // True if a request to terminate from the same site has benn made
   private boolean allowNewTasks;
   
   // Whether or not 
   private boolean hasToken;
   
   // Wether or not the site has been active since the token was sent
   private boolean hasBeenActive;

   /**
    * Constructor
    * @throws SocketException if it can't open the sending socket
    */
   public SiteManager() throws SocketException {
      
      // Initialize attributes
      numberOfTasks = 0;
      messages = new ArrayList<>();
      this.config = Configuration.getConfiguration();
      this.siteId = this.config.getSiteId();
      this.hasToken = this.siteId == 0;
      this.hasBeenActive = false;
      this.allowNewTasks = true;
      
      
      // Create the socket to send messages
      sendSocket = new DatagramSocket();
      
      // Create the MessageReceiver
      messageReceiver = new MessageReceiver(
              Configuration.getConfiguration().getSite(this.siteId).getValue(), this
      );
      messageReceiver.start();
   }

   /**
    * Start the SiteManager in a new thread
    */
   public void start() {
      Thread t = new Thread(() -> {
         
         // In a loop, manage received messages
         while (true) {
            synchronized (SiteManager.this) {
               // Wait if there is no message in the list
               if (messages.isEmpty()) {
                  try {
                     wait();
                  } catch (InterruptedException ex) {
                     Logger.getLogger(SiteManager.class.getName()).log(Level.SEVERE, null, ex);
                  }
               }
            }
            
            // Check if the site must terminate
            if (getTerminate() && !isActive()) {
               // Terminate
               System.out.println("All tasks finished and termination was requested.");
               System.out.println("Terminating...");
               closeResources();
               return;
            }
            
            // If we have messages to read
            if (!messages.isEmpty()) {
               
               // Get the oldest one
               Message message;
               synchronized(SiteManager.this) 
               {
                  message = messages.get(0);
                  messages.remove(0);
               }
               
               // Switch on message types
               switch (message.getType()) {
                  case MessageType.REQUEST:
                     
                     // If we are the recipient
                     if (message.getRecipient() == this.siteId) {
                        // Start a new task
                        startNewTask();
                     } else {
                        // Send the message to our neighbour
                        sendMessage(message);
                     }
                     hasBeenActive = true;
                     break;
                  
                  case MessageType.TOKEN:
                     hasToken = true;
                     if (!isActive() && !hasBeenActive) {
                        sendMessage(
                                new Message(
                                        (this.siteId + 1) % config.getNumberOfSites(), 
                                        MessageType.END
                                )
                        );
                     } else if (!isActive()) {
                        sendMessage(
                                 new Message(
                                         (this.siteId + 1) % config.getNumberOfSites(), 
                                         MessageType.TOKEN
                                 )
                         );
                         hasBeenActive = false;
                         hasToken = false;
                     }
                     break;
                     
                  case MessageType.END:
                     // If the request of termination hasn't been made yet
                     if (!getTerminate()) {
                        setTerminate(true);
                        
                        // Send the message to our neighbour
                        sendMessage(
                                new Message(
                                        (this.siteId + 1) % config.getNumberOfSites(), 
                                        MessageType.END
                                )
                        );
                     }
                     
                     // If there is no task excuting, end the SiteManager 
                     if (!isActive()) {
                        System.out.println("Ending application after receiving an END message.");
                        closeResources();
                        return;
                     }
                     break;
               }
            } else {
               // If there is no task executing
               if (!isActive()) {
                  if (hasToken) {
                     sendMessage(
                             new Message(
                                     (this.siteId + 1) % config.getNumberOfSites(), 
                                     MessageType.TOKEN
                             )
                     );
                     hasBeenActive = false;
                     hasToken = false;
                  }
               }
            }
         }

      });
      t.start();
   }
   
   /**
    * Close the resources
    */
   private void closeResources() {
      messageReceiver.stop();
   }
   
   @Override
   public synchronized void messageReceived(Message message) {
      // Add the message to the list and notify the SiteManager
      messages.add(message);
      this.notify();
   }

   /**
    * Notify the end of a Task.
    */
   @Override
   public synchronized void finishedTask() {
      System.out.println("A task has completed. Remaining: " + (numberOfTasks - 1));
      --numberOfTasks;
      this.notify();
   }

   @Override
   public void startNewTaskOnSite(int siteId) {
      // If asking for current site to do a task
      if (siteId == this.siteId) {
         synchronized(this) {
            messages.add(new Message(this.siteId, MessageType.REQUEST));
            this.notify();
         }
         return;
      }
      
      // Send a message
      sendMessage(new Message(siteId, MessageType.REQUEST));
   }
   
   /**
    * Request to terminate the system
    */
   public synchronized void requestTerminate() {
      allowNewTasks = false;
   }
   
   /**
    * Get wether or not a termination request has been made
    * @return true if a termination request has been made
    */
   private synchronized boolean getTerminate() {
      return terminate;
   }
   
   /**
    * Set the termination request
    * @param requestTerminate true to make a termination request, false otherwise
    */
   private synchronized void setTerminate(boolean requestTerminate) {
      this.terminate = requestTerminate;
   }
   
   /**
    * Send a message to a site by sending to the current site neighbour
    * @param message the message to send
    */
   private void sendMessage(Message message) {
      
      // Get the neighbour data
      InetAddress ipAddress = config.getSite((this.siteId + 1) % config.getNumberOfSites()).getKey();
      int port = config.getSite((this.siteId + 1) % config.getNumberOfSites()).getValue();
      
      // Construct the message
      byte[] buf = new byte[2];
      buf = Message.messageToByteBuffer(message, buf);
      
      
      try {
         // Send message
         sendSocket.send(new DatagramPacket(
                 buf,
                 buf.length,
                 ipAddress,
                 port
         ));
      } catch (IOException ex) {
         Logger.getLogger(SiteManager.class.getName()).log(Level.SEVERE, null, ex);
      }
   }

   /**
    * Start a new task on the current site.
    */
   private synchronized void startNewTask() {
      if (allowNewTasks) {
         Task t = new Task(this);
         t.execute();
         ++numberOfTasks;
         System.out.println("A task has started. Remaining: " + numberOfTasks );
      }
   }
   
   /**
    * Get if there are tasks currently executing
    * @return true if there are tasks currently executing
    */
   public boolean isActive() {
      return numberOfTasks != 0;
   }
}
