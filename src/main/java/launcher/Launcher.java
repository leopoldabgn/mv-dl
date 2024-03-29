package launcher;

import java.io.IOException;

import gui.GUI;
import model.Preferences;

public class Launcher {

    public static void main(String[] args) {
        /* TEST pour telecharger une video
        Video video = new Video();
        video.setId("gmdqrUwS0ok");
        Command.downloadVideo(new File("/home/leopold/Musique"), video, "22");
        */

        //boolean ok = true;
        // OLD METHOD
        // Peut servir au cas où la methode actuelle ne fonctionne plus.
        /*if(!Command.checkPrgm("curl")) {
            System.out.println("- You need to install curl   (ubuntu : sudo apt install curl)");
            ok = false;
        }*/

        // Commande qui pourra servir en cas de probleme
        // yt-dlp --dump-json https://www.youtube.com/watch?v=

        // Important. Sinon on ne peut rien telecharger.
        /*if(!Command.checkPrgm("yt-dlp")) {
            System.out.println("- You need to install yt-dlp (ubuntu : sudo apt install yt-dlp)");
            ok = false;
        }*/
        // On verifie ffmpeg pour linux
        /*
        if(Command.isLinux() && !Command.checkPrgm("ffmpeg")) {
            System.out.println("- You need to install ffmpeg (ubuntu : sudo apt install ffmpeg)");
            ok = false;
        }
        */
        /*
        if(!ok)
           return;
        */
        // On recupere le fichier de prefs si il existe.
		// Sinon, on en cree un nouveau
		Preferences prefs = null;

        try {
            prefs = Preferences.load();
        } catch (ClassNotFoundException | IOException e) {
            prefs = new Preferences();
        }

        new GUI(prefs, 800, 600);
    }
    
}