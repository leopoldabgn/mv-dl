package model;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;

public class Wget {

    public static void downloadFile(String urlString, String destinationFile) throws IOException {
        // Créer un objet URL
        URL url = new URL(urlString);
        
        // Ouvrir la connexion à l'URL
        HttpURLConnection httpConn = (HttpURLConnection) url.openConnection();
        int responseCode = httpConn.getResponseCode();

        // Vérifier si la connexion est réussie (code HTTP 200)
        if (responseCode == HttpURLConnection.HTTP_OK) {
            // Ouvrir les flux d'entrée et de sortie
            InputStream inputStream = httpConn.getInputStream();
            FileOutputStream outputStream = new FileOutputStream(destinationFile);

            // Créer un buffer pour la lecture et l'écriture
            byte[] buffer = new byte[4096];
            int bytesRead = -1;

            // Lire depuis l'URL et écrire dans le fichier de destination
            while ((bytesRead = inputStream.read(buffer)) != -1) {
                outputStream.write(buffer, 0, bytesRead);
            }

            // Fermer les flux
            outputStream.close();
            inputStream.close();

            System.out.println("Fichier téléchargé avec succès : " + destinationFile);
        } else {
            System.out.println("Échec du téléchargement. Code de réponse HTTP : " + responseCode);
        }

        // Fermer la connexion HTTP
        httpConn.disconnect();
    }
    
}
