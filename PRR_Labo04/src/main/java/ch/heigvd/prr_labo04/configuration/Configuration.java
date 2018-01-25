/**
 * File: Configuration.java
 * Date: 24.01.2018
 * Authors: Sathiya Kiruhsnapillai & Mathieu Monteverde
 */

package ch.heigvd.prr_labo04.configuration;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.util.Pair;

/**
 * This Class encapsulates the configuration of the program, such as the IP
 * addresses and ports of the sites, the number of sites and the Id of the current 
 * site. It reads the IP configuration from a text file in the resources
 * folder. This class keeps a list of pairs containing the IP addresses 
 * and port of other sites in the system. It is implemented as a Singleton.
 */
public class Configuration {
   
   // List of Pairs object, the key being the IP address, the value being the port
   private final List<Pair<InetAddress, Integer>> sites;
   
   // Number of sites in the system
   private final int numberOfSites;
   
   // The Id of the current site
   private final int siteId;
   
   // Singleton instance
   private static Configuration configuration;
   
   /**
    * Create a Configuration instance
    * @param configFileName the path to the configuration file 
    * @param siteId the current site Id
    * @param numberOfSites the number of sites in the system
    * @throws Exception 
    */
   private Configuration(String configFileName, int siteId, int numberOfSites) throws Exception {

      try (
              BufferedReader buffer = new BufferedReader(
                      new InputStreamReader(
                              Configuration.class.getResourceAsStream(configFileName)
                      )
              )) {

         // Liste des sites par adresse IP et port
         sites = new ArrayList<>();

         // Lecture du fichier ligne par ligne
         buffer.lines().forEach((t) -> {
            try {
               String[] address = t.split(" ");
               sites.add(
                       new Pair<>(
                               InetAddress.getByName(address[0]),
                               Integer.parseInt(address[1])
                       )
               );
            } catch (UnknownHostException e) {
               Logger.getLogger(Process.class.getName())
                       .log(Level.SEVERE, null, e);
            }
         });

         sites.forEach(System.out::println);

      } catch (IOException e) {
         throw new Exception("Une erreur est survenue en lisant les fichiers.", e);
      }
      
      this.numberOfSites = numberOfSites;
      this.siteId = siteId;
   }
   
   /**
    * Load the Configuration for the program. To be called at the beginning
    * of the main method.
    * @param configFileName the path to the configuration file 
    * @param siteId the current site Id
    * @param numberOfSites the number of sites in the system
    * @throws Exception 
    */
   public static void loadConfiguration(String configFileName, int siteId, int numberOfSites) throws Exception {
      if (configuration == null) {
         configuration = new Configuration(configFileName, siteId, numberOfSites);
      }
   }
   
   /**
    * Get the Configuration instance. Better call loadConfiguration before 
    * calling this method for the first time.
    * @return the Configuration instance.
    */
   public static Configuration getConfiguration() {
      return configuration;
   }

   /**
    * Get a given site configuration
    *
    * @param siteId
    * @return
    */
   public Pair<InetAddress, Integer> getSite(int siteId) {
      return sites.get(siteId);
   }
   
   /**
    * Get the number of sites
    * @return 
    */
   public int getNumberOfSites() {
      return numberOfSites;
   }
   
   /**
    * Get the current site Id
    * @return the site Id
    */
   public int getSiteId() {
      return siteId;
   }
}
