package model;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;

public class SoftwaresUpdates {

    public static void updateFfmpeg(String dirPath) throws IOException, InterruptedException {
        String binaryUrl, binaryPath;
        if(Command.isWindows()) {
            binaryUrl = "https://github.com/eugeneware/ffmpeg-static/releases/latest/download/ffmpeg-win32-x64";
            binaryPath = dirPath + File.separator + "ffmpeg.exe";
        }
        else {
            binaryUrl = "https://github.com/eugeneware/ffmpeg-static/releases/latest/download/ffmpeg-linux-x64";
            binaryPath = dirPath + File.separator + "ffmpeg";
        }

        // Télécharger le fichier ffmpeg via wget
        String[] downloadCommand = {"wget", binaryUrl, "-O", binaryPath};
        System.out.println("Getting latest ffmpeg binary...\n\n" + String.join(" ", downloadCommand) + "\n");
        runCommandWithLiveOutput(downloadCommand);

        if(!Command.isWindows()) {
            // Ajouter les droits d'exécution
            String[] chmodCommand = {"chmod", "+x", binaryPath};
            System.out.println("Adding execution rights...\n" + String.join(" ", chmodCommand));
            runCommandWithLiveOutput(chmodCommand);
        }

        // Afficher la version
        String[] versionCommand = {binaryPath, "-version"};
        Process process = runCommandWithLiveOutput(versionCommand);
        // System.out.println("ffmpeg version = " + getProcessOutput(process));
    }

    public static void updateYtDlp(String dirPath) throws IOException, InterruptedException {
        String binaryUrl, binaryPath;
        if(Command.isWindows()) {
            binaryUrl = "https://github.com/yt-dlp/yt-dlp/releases/latest/download/yt-dlp.exe";
            binaryPath = dirPath + File.separator + "yt-dlp.exe";
        }
        else {
            binaryUrl = "https://github.com/yt-dlp/yt-dlp/releases/latest/download/yt-dlp";
            binaryPath = dirPath + File.separator + "yt-dlp";
        }

        // Télécharger le fichier yt-dlp via wget
        String[] downloadCommand = {"wget", binaryUrl, "-O", binaryPath};
        System.out.println("Getting latest yt-dlp binary...\n\n" + String.join(" ", downloadCommand) + "\n");
        runCommandWithLiveOutput(downloadCommand);

        if(!Command.isWindows()) {
            // Ajouter les droits d'exécution
            String[] chmodCommand = {"chmod", "+x", binaryPath};
            System.out.println("Adding execution rights...\n" + String.join(" ", chmodCommand));
            runCommandWithLiveOutput(chmodCommand);
        }

        // Afficher la version
        String[] versionCommand = {binaryPath, "--version"};
        Process process = runCommandWithLiveOutput(versionCommand);
        // System.out.println("yt-dlp version = " + getProcessOutput(process));
    }

    public static Process runCommand(String[] command) throws IOException, InterruptedException {
        ProcessBuilder processBuilder = new ProcessBuilder(command);
        processBuilder.redirectErrorStream(true);  // Rediriger les erreurs vers l'output standard
        Process process = processBuilder.start();
        process.waitFor();  // Attendre la fin du processus
        return process;
    }

    public static Process runCommandWithLiveOutput(String[] command) throws IOException, InterruptedException {
        ProcessBuilder processBuilder = new ProcessBuilder(command);
        processBuilder.redirectErrorStream(true);  // Rediriger les erreurs vers l'output standard
        Process process = processBuilder.start();

        // Lire l'output en temps réel
        BufferedReader reader = new BufferedReader(new InputStreamReader(process.getInputStream()));
        String line;
        while ((line = reader.readLine()) != null) {
            System.out.println(line);  // Affiche chaque ligne dès qu'elle est disponible
        }

        process.waitFor();  // Attendre que le processus se termine
        return process;
    }

    public static String getProcessOutput(Process process) throws IOException {
        BufferedReader reader = new BufferedReader(new InputStreamReader(process.getInputStream()));
        StringBuilder output = new StringBuilder();
        String line;
        while ((line = reader.readLine()) != null) {
            output.append(line).append("\n");
        }
        return output.toString();
    } 
}
