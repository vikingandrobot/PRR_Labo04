/**
 * File: Message.java
 * Date: 24.01.2018
 * Authors: Sathiya Kiruhsnapillai & Mathieu Monteverde
 */

package ch.heigvd.prr_labo04.message;

/**
 * Te Message class encapsulate messages used in the termination algorithm
 * and provides a convenient way to store theses messages or manipulate them.
 */
public class Message {
   
   // Id of the recipient
   private final int recipient;
   
   // Type of message
   private final byte type;
   
   // Size of a message in its byte array representation
   public static final int MESSAGE_BYTE_SIZE = 2;
   
   // Position of the type byte in the byte array message
   public static final int TYPE_INDEX = 0;
   
   // Position of the recipient byte in the byte array message
   public static final int RECIPIENT_INDEX = 1;
   
   /**
    * Create a new Message.
    * @param recipient the recipient of the message
    * @param type the type of the message
    */
   public Message(int recipient, byte type) {
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
   
   /**
    * Get the recipient site Id
    * @return the Id of the recipient site
    */
   public int getRecipient() {
      return recipient;
   }
   
   /**
    * Convert a byte array message to its Message reprsentation.
    * @param buf the data to convert
    * @return a new Message representing the message
    */
   public static Message byteBufferToMessage(byte[] buf) {
      byte type = buf[TYPE_INDEX];
      byte recipient = buf[RECIPIENT_INDEX];
      
      return new Message(recipient, type);
   }
   
   /**
    * Convert a Message to its byte array representation 
    * @param message the Message to convert
    * @param buf the buf in which to insert the values
    * @return buf
    */
   public static byte[] messageToByteBuffer(Message message, byte[] buf) {
      buf[TYPE_INDEX] = message.getType();
      buf[RECIPIENT_INDEX] = (byte)message.getRecipient();
      
      return buf;
   }
}
