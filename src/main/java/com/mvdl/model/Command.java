package com.mvdl.model;

import java.io.BufferedReader;
import java.io.File;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

public class Command {

     private Preferences pref;

    public Command(Preferences pref) {
        this.pref = pref;
    }

    public static String getHtmlFromSearch(String search) {
        String s;
        String html = "";
        Process p;
        search = search.replace(" ", "%20");
        try {
            p = Runtime.getRuntime().exec("curl -s https://www.youtube.com/results?search_query=\""+search+"\"");
            BufferedReader br = new BufferedReader(
                new InputStreamReader(p.getInputStream()));
            while ((s = br.readLine()) != null)
                html += s+"\n";
            p.waitFor();
            //System.out.println ("exit: " + p.exitValue());
            p.destroy();
        } catch (Exception e1) {}

        return html;
    }

    public static List<Video> getVideos(String html) {
        List<Video> videos = new ArrayList<>();
        String pattern = "watchCardCompactVideoRenderer";
        int index = html.indexOf(pattern);
        String videoLine;
        while (index >= 0) {
            index = html.indexOf(pattern, index + 1);
            if(index < 0)
                continue;
            videoLine = "";
            for(int i=index;i < html.length() && i<index+650;i++)
                videoLine += html.charAt(i);
            String[] tab = {"\"title\":{\"simpleText\":\"",
                            "\"subtitle\":{\"simpleText\":\"",
                            "\"accessibilityData\":{\"label\":\"",
                            "\"watchEndpoint\":{\"videoId\":\""};
            String[] fields = {
                "title",
                "subtitle",
                "label",
                "videoId"
            };
            int count = 0;
            String val;
            Video video = new Video();
            for(String s : tab) {
                val = getString(videoLine, videoLine.indexOf(s)+s.length(), '\"');
                switch(fields[count]) {
                    case "title":
                        video.setTitle(val);
                        break;
                    case "subtitle":
                        String[] tmp = val.split(" • ");
                        if(tmp != null && tmp.length > 0)
                            video.setViews(tmp[0]);
                        if(tmp != null && tmp.length > 1)
                            video.setDate(tmp[1]);
                        break;
                    case "label":
                        video.setDuration(val);
                        break;
                    case "videoId":
                        video.setId(val);
                        break;
                }
                count++;
            }
            videos.add(video);
        }
        return videos;
    }

    public static String getString(String text, int begin, char delimiter) {
        String str = "";
        for(int i=begin;i<text.length() && text.charAt(i) != delimiter;i++)
            str += text.charAt(i);
        return str;
    }

    public void downloadMusic(Video video) {
        downloadMusic(pref.getDownloadFolder(), video);
    }

    public static void downloadMusic(File folder, Video video) {
        if(folder == null || !folder.isDirectory())
            return;
        ProcessBuilder pB = new ProcessBuilder(
            "yt-dlp", // works with "youtube-dl"
            "--extract-audio",
            "--audio-format", "mp3",
            "--audio-quality", "0",
            "--output", folder.getAbsolutePath()+"/%(title)s.mp3",
            "https://www.youtube.com/watch?v="+video.getId()
        );
        
        Process p;
        try {
            p = pB.start();
            p.waitFor();
            p.destroy();
        } catch (Exception e1) {}
    }

}
