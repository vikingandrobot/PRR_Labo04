/**
 * File: MessageReceiver.java
 * Date: 24.01.2018
 * Authors: Sathiya Kiruhsnapillai & Mathieu Monteverde
 */

package ch.heigvd.prr_labo04.message;

import ch.heigvd.prr_labo04.task.SiteManager;
import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.SocketException;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * The MessageReceiver class allows to start a new Thread in which 
 * it will receive Message messages.
 */
public class MessageReceiver {
   
   // The MessageListener to use
   private MessageListener messageListener;
   
   // Thread on which to receive message
   private final Thread thread;
   
   // Whether or not the thread should stop receiving
   private boolean mustStop = false;
   
   // The socket on which to receive
   private DatagramSocket receiveSocket;
   
   /**
    * Create a MessageReceiver
    * @param port the port on which to receive messages
    * @param messageListener the MessageListener to use
    */
   public MessageReceiver(int port, MessageListener messageListener) {
      this.messageListener = messageListener;
      
       // Start a thread to receive messages
      thread = new Thread(() -> {
         try {
            receiveSocket = new DatagramSocket(
                    port
            );
            
            byte[] buf = new byte[2];
            DatagramPacket packet = new DatagramPacket(buf, buf.length);
            
            // Receive messages
            while (true) {
               try {
                  
                  if (mustStop) {
                     System.out.println("Stopping the receiving thread.");
                     return;
                  }
                  
                  // Receive messages
                  receiveSocket.receive(packet);
                  
                  // Notify the MessageListener
                  this.messageListener.messageReceived(Message.byteBufferToMessage(packet.getData()));
                  
               } catch (IOException ex) {
                  if (mustStop) {
                     System.out.println("Stopping the receiving thread.");
                     return;
                  }
                  Logger.getLogger(SiteManager.class.getName()).log(Level.SEVERE, null, ex);
               }
            }
         } catch (SocketException ex) {
            Logger.getLogger(SiteManager.class.getName()).log(Level.SEVERE, null, ex);
         }
      });
   }
   
   /**
    * Start receiving.
    */
   public void start() {
      thread.start();
   }
   
   /**
    * Stop receiving.
    */
   public void stop() {
      mustStop = true;
      receiveSocket.close();
   }
}
