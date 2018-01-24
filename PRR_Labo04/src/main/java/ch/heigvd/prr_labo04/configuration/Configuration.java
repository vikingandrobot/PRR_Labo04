/**
 * File: Configuration.java
 * Date: 24.01.2018
 * Authors: Sathiya Kiruhsnapillai & Mathieu Monteverde
 */

package ch.heigvd.prr_labo04.configuration;

import ch.heigvd.prr_labo04.App;
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
 * Class to read a configuration from a text file in the resources
 * folder. This class keeps a list of pairs containing the IP addresses 
 * and port of other sites in the system.
 */
public class Configuration {

   private final List<Pair<InetAddress, Integer>> sites;

   public Configuration(String configFileName) throws Exception {

      try (
              BufferedReader buffer = new BufferedReader(
                      new InputStreamReader(
                              App.class.getResourceAsStream(configFileName)
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

}
