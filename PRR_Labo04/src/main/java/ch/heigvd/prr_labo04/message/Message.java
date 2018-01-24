/**
 * File: Message.java
 * Date: 24.01.2018
 * Authors: Sathiya Kiruhsnapillai & Mathieu Monteverde
 */

package ch.heigvd.prr_labo04.message;

/**
 * Class to encapsulate messages.
 */
public class Message {
   
   // Id of the sender
   private final int sender;
   
   // Id of the recipient
   private final int recipient;
   
   // Type of message
   private final byte type;
   
   /**
    * Create a new Message.
    * @param sender the type of the message
    * @param recipient the recipient of the message
    * @param type the type of the message
    */
   public Message(int sender, int recipient, byte type) {
      this.sender = sender;
      this.type = type;
      this.recipient = recipient;
   }
   
   /**
    * Get the type of the message.
    * @return the type of the message
    */
   public byte getType() {
      return type;
   }

   public int getSender() {
      return sender;
   }

   public int getRecipient() {
      return recipient;
   }
}
