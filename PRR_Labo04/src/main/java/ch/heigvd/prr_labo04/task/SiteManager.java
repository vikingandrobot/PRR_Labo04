/**
 * File: SiteManager.java
 * Date: 24.01.2018
 * Authors: Sathiya Kiruhsnapillai & Mathieu Monteverde
 */
package ch.heigvd.prr_labo04.task;

import ch.heigvd.prr_labo04.configuration.Configuration;
import ch.heigvd.prr_labo04.message.Message;
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
 * Class that manages the site in the environment.
 */
public class SiteManager implements TaskManager {
   
   private final int siteId;
   
   private final Configuration config;

   private final List<Message> messages;

   private int numberOfTasks;
   
   private final DatagramSocket sendSocket;

   public SiteManager(int siteId, Configuration config) throws SocketException {
      numberOfTasks = 0;
      messages = new ArrayList<>();
      this.siteId = siteId;
      this.config = config;
      
      sendSocket = new DatagramSocket();
      
      // Start a thread to receive messages
      new Thread(() -> {
         try {
            System.out.println("Created receiving socket");
            DatagramSocket receiveSocket = new DatagramSocket(
                    config.getSite(siteId).getValue()
            );
            
            byte[] buf = new byte[3];
            DatagramPacket packet = new DatagramPacket(buf, buf.length);
            while (true) {
               try {
                  System.out.println("Waiting for message...");
                  receiveSocket.receive(packet);
                  System.out.println("Received a message...");
                  
                  byte type = packet.getData()[0];
                  byte sender = packet.getData()[1];
                  byte recipient = packet.getData()[2];
                  
                  synchronized(SiteManager.this) {
                     messages.add(new Message(sender, recipient, type));
                  }
               } catch (IOException ex) {
                  Logger.getLogger(SiteManager.class.getName()).log(Level.SEVERE, null, ex);
               }
            }
         } catch (SocketException ex) {
            Logger.getLogger(SiteManager.class.getName()).log(Level.SEVERE, null, ex);
         }
      }).start();
   }

   /**
    * Start the SiteManager in a new thread
    */
   public void start() {
      Thread t = new Thread(() -> {

         while (true) {
            synchronized (SiteManager.this) {
               if (messages.isEmpty()) {
                  try {
                     wait();
                  } catch (InterruptedException ex) {
                     Logger.getLogger(SiteManager.class.getName()).log(Level.SEVERE, null, ex);
                  }
               }
            }
            
            if (!messages.isEmpty()) {
               Message m;
               synchronized(SiteManager.this) 
               {
                  m = messages.get(0);
                  messages.remove(m);
               }
               
               switch (m.getType()) {
                  case MessageType.REQUEST:
                     startNewTask();
                     break;
                  
                  case MessageType.TOKEN:
                     
                     break;
                     
                  case MessageType.END:
                     
                     break;
               }
            }
         }

      });
      t.start();
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
      InetAddress ipAddress = config.getSite(siteId).getKey();
      int port = config.getSite(siteId).getValue();
      
      // SEND a REQUEST message
   }

   private synchronized void startNewTask() {
      Task t = new Task(this);
      t.execute();
      ++numberOfTasks;
   }
   
   public boolean isActive() {
      return numberOfTasks == 0;
   }

   @Override
   public int getSiteId() {
      return siteId;
   }

   @Override
   public Configuration getConfiguration() {
      return config;
   }

}
