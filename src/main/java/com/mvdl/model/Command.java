package com.mvdl.model;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

public class Command {

    public static int downloadsInProgress;

     private Preferences pref;

    public Command(Preferences pref) {
        this.pref = pref;
    }

    public static String exec(String cmd) {
        String s, result = "";
        Process p;
        try {
            p = Runtime.getRuntime().exec(cmd);
            BufferedReader br = new BufferedReader(
                new InputStreamReader(p.getInputStream()));
            while ((s = br.readLine()) != null)
                result += s+"\n";
            p.waitFor();
            //System.out.println ("exit: " + p.exitValue());
            p.destroy();
        } catch (Exception e1) {}
        return result;
    }

    // TODO: Recreer la fonction avec l'aide de exec.
    public static String getHtmlFromSearch(String search) {
        String s, html = "";
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

    public static boolean checkPrgm(String prgm) {
        Process p;
        int exit = 1;
        try {
            p = Runtime.getRuntime().exec(prgm+" --version");
            p.waitFor();
            exit = p.exitValue();
            p.destroy();
        } catch (Exception e1) {}

        return exit == 0;
    }

    public static String readFile(String path) throws IOException {
        InputStream is = new FileInputStream(path);
        InputStreamReader isr = new InputStreamReader(is);
        BufferedReader buffer = new BufferedReader(isr);
            
        String line = buffer.readLine();
        StringBuilder builder = new StringBuilder();
            
        while(line != null){
           builder.append(line).append("\n");
           line = buffer.readLine();
        }
            
        return builder.toString();
    }

    public static List<Video> readJSON(String path) {
        JSONParser jsonParser = new JSONParser();
        List<Video> videos = new ArrayList<Video>();
        try (FileReader reader = new FileReader(path))
        {
            //Read JSON file
            Object obj = jsonParser.parse(reader);
 
            JSONObject tmp = ((JSONObject) obj);
            String[] tab = {"contents", "twoColumnSearchResultsRenderer",
            "primaryContents", "sectionListRenderer"};

            for(String s : tab)
                tmp = (JSONObject)tmp.get(s);
            
            JSONArray arr = (JSONArray)tmp.get("contents");
            tmp = (JSONObject)arr.get(0);
            tmp = (JSONObject)tmp.get("itemSectionRenderer");
            arr = (JSONArray)tmp.get("contents");
            Video video;
            for(int i=0;i<arr.size();i++) {
                try {
                    tmp = (JSONObject)arr.get(i);
                    tmp = (JSONObject)tmp.get("videoRenderer");
                } catch(Exception e) {
                    continue;
                }
                video = new Video();
                video.setId(extractId(tmp));
                video.setTitle(extractTitle(tmp));
                video.setDuration(extractDuration(tmp));
                video.setThumbnailURL(extractThumbnailURL(tmp));
                video.setDate(extractDate(tmp));
                video.setViews(extractViews(tmp));
                if(video.isValid()) {
                    System.out.println(video);
                    videos.add(video);
                }
            }
            
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (ParseException e) {
            e.printStackTrace();
        }

        return videos;
    }
    
    public static String extractId(JSONObject obj) {
        try {
            return (String)obj.get("videoId");
        } catch(Exception e){
            return "";
        }
    }

    public static String extractThumbnailURL(JSONObject obj) {
        try {
            obj = (JSONObject)obj.get("thumbnail");
            JSONArray arr = (JSONArray)obj.get("thumbnails");
            obj = (JSONObject)arr.get(0);

            return (String)obj.get("url");
        } catch(Exception e) {
            return "";
        }
    }

    public static String extractTitle(JSONObject obj) {
        try {
            obj = (JSONObject)obj.get("title");
            JSONArray arr = (JSONArray)obj.get("runs");
            obj = (JSONObject)arr.get(0);

            return (String)obj.get("text");
        } catch(Exception e) {
            return "";
        }
    }

    public static String extractDate(JSONObject obj) {
        try {
            obj = (JSONObject)obj.get("publishedTimeText");

            return (String)obj.get("simpleText");
        } catch(Exception e) {
            return "";
        }
    }

    public static String extractSimpleDuration(JSONObject obj) {
        try {
            obj = (JSONObject)obj.get("lengthText");

            return (String)obj.get("simpleText");
        } catch(Exception e) {
            return "";
        }
    }

    public static String extractDuration(JSONObject obj) {
        try {
            String[] tab = {"lengthText", "accessibility", "accessibilityData"};
            for(String str : tab)
                obj = (JSONObject)obj.get(str);

            return (String)obj.get("label");
        } catch(Exception e) {
            return "";
        }
    }

    public static String extractViews(JSONObject obj) {
        try {
            obj = (JSONObject)obj.get("viewCountText");

            return (String)obj.get("simpleText");
        } catch(Exception e) {
            return "";
        }
    }

}
