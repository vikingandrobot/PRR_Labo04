package ch.heigvd.prr_labo04;

import ch.heigvd.prr_labo04.configuration.Configuration;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 */
public class App {
   public static void main(String[] args) {
      try {
         Configuration config = new Configuration("/processes.txt");
      } catch (Exception ex) {
         System.out.println("An error occured when reading the configuration.");
         Logger.getLogger(App.class.getName()).log(Level.SEVERE, null, ex);
      }
   }
}