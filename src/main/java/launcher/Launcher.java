package launcher;

import java.io.File;
import java.io.IOException;

import gui.GUI;
import model.Command;
import model.Preferences;
import model.SoftwaresUpdates;

public class Launcher {

    // Fonction pour afficher la page d'aide
    private static void printHelp() {
        System.out.println("Usage: ./mv-dl [OPTIONS]");
        System.out.println("Options disponibles:");
        System.out.println("  --update              Met à jour tous les composants (yt-dlp et ffmpeg).");
        System.out.println("  --update-yt-dlp       Met à jour uniquement yt-dlp.");
        System.out.println("  --update-ffmpeg       Met à jour uniquement ffmpeg.");
        System.out.println("  -h, --help            Affiche cette page d'aide.");
    }

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

        String projectDir = System.getProperty("user.dir"); // Récupère le dossier où se trouve le projet
        String softDirPath = projectDir + File.separator + "soft";
        File softDir = new File(softDirPath);

        // Vérifier si un argument "-h" ou "--help" est présent
        if (args.length > 0 && (args[0].equals("-h") || args[0].equals("--help"))) {
            printHelp();
            return; // On arrête l'exécution du programme après l'affichage de l'aide
        }

        // Vérifier si un argument "update" est présent
        boolean updateAll = args.length > 0 && args[0].equals("--update");
        boolean updateYtDlp = args.length > 0 && args[0].equals("--update-yt-dlp");
        boolean updateFfmpeg = args.length > 0 && args[0].equals("--update-ffmpeg");

        // Créer le dossier "soft" s'il n'existe pas
        if (!softDir.exists()) {
            if (softDir.mkdir()) {
                System.out.println("Dossier 'soft' créé avec succès.");
            } else {
                System.out.println("Impossible de créer le dossier 'soft'.");
                return;
            }
        }
        
        String binName = "yt-dlp";
        if(Command.isWindows())
            binName += ".exe";
        // Vérifier si le fichier "yt-dlp" existe dans le dossier "soft"
        File ytDlpBinary = new File(softDirPath + File.separator + binName);
        if (!ytDlpBinary.exists() || updateAll || updateYtDlp) {
            System.out.println("'yt-dlp' non trouvé. Téléchargement en cours...");
            try {
                SoftwaresUpdates.updateYtDlp(softDirPath);
            } catch (IOException | InterruptedException e) {
                e.printStackTrace();
            }
        }

        binName = "ffmpeg";
        if(Command.isWindows())
            binName += ".exe";
        // Vérifier si le fichier "ffmpeg" existe dans le dossier "soft"
        File ffmpegBinary = new File(softDirPath + File.separator + binName);
        if (!ffmpegBinary.exists() || updateAll || updateFfmpeg) {
            System.out.println("'ffmpeg' non trouvé. Téléchargement en cours...\n");
            try {
                SoftwaresUpdates.updateFfmpeg(softDirPath);
            } catch (IOException | InterruptedException e) {
                e.printStackTrace();
            }
        }

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