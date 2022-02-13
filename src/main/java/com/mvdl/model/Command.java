package com.mvdl.model;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import com.mvdl.model.Video.VideoInfos;

public class Command {

    // Location du logiciel yt_dlp avec la bonne extension
    private static String yt_dlp = isWindows() ? "./yt-dlp.exe" : "./yt-dlp",
                          ffmpeg_loc = "."; // location du dossier ou se situe ffmpeg
    public static int downloadsInProgress;

    private Preferences pref;

    public Command(Preferences pref) {
        this.pref = pref;
    }

    public static String exec(String cmd) {
        String s, result = "";
        Process p = null;
        try {
            p = Runtime.getRuntime().exec(cmd);
            BufferedReader br = new BufferedReader(
                new InputStreamReader(p.getInputStream()));
            while ((s = br.readLine()) != null)
                result += s+"\n";
            p.waitFor();
            p.destroy();
        } catch (Exception e1) {
        	if(p != null)
        		System.out.println ("exit: " + p.exitValue());
        	e1.printStackTrace();
        }
        return result;
    }

    public static String cutHTML(String html) {
        try {
            String search = "ytInitialData = ";
            html = html.substring(html.indexOf(search)+search.length());
            html = html.substring(0, html.indexOf(";</script>"));
        } catch(Exception e) {}
        return html;
    }

    public static String getYtbHTMLCode(String search) {
        search = search.replace(" ", "%20");
        String url = "https://www.youtube.com/results?search_query=\""+search+"\"";
        return getHTMLCode(url);
    }

    public static String getHTMLCode(String url) {
        String code = "";
        url = url.replace(" ", "%20");
        BufferedReader in = null;
        try {
            URL site = new URL(url);
            in = new BufferedReader(new InputStreamReader(site.openStream()));

            String inputLine;
            while ((inputLine = in.readLine()) != null)
                code = code + "\n" + (inputLine);

            in.close();
        }
        catch (IOException ex) {
            System.out.println("Erreur dans l'ouverture de l'URL : " + ex);
        }
        finally {
            try {
                in.close();
            }
            catch (IOException ex) {
                System.out.println("Erreur dans la fermeture du buffer : " + ex);
            }
        }
        
       return code;
    }

    public String downloadMusic(Video video) {
        return downloadMusic(pref.getDownloadFolder(), video);
    }

    public static String downloadMusic(File folder, Video video) {
        if(folder == null || !folder.isDirectory())
            return "";
        String ffmpeg_attr = isWindows() ? "" : "--ffmpeg-location";
        String ffmpeg = isWindows() ? "" : ffmpeg_loc;
        ProcessBuilder pB = new ProcessBuilder(
            yt_dlp, // works with "youtube-dl"
            ffmpeg_attr, ffmpeg,
            "--extract-audio",
            "--audio-format", "mp3",
            "--audio-quality", "0",
            "--output", folder.getAbsolutePath()+"/%(title)s.mp3",
            "https://www.youtube.com/watch?v="+video.getId()
        );
        
        StringBuilder output = new StringBuilder();
		try {
			Process process = pB.start();
			BufferedReader reader = new BufferedReader(
					new InputStreamReader(process.getInputStream()));

			String line;
			while ((line = reader.readLine()) != null)
				output.append(line + "\n");

			process.waitFor();
		} catch (IOException | InterruptedException e) {
			e.printStackTrace();
		}
		
		return output.toString();
    }

    public static boolean checkPrgm(String prgm) {
        Process p;
        int exit = 1;
        try {
            p = Runtime.getRuntime().exec(prgm+" --help");
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
        buffer.close();
        
        return builder.toString();
    }

    public static List<Video> getVideosByJSON(String json) {
        JSONParser jsonParser = new JSONParser();
        List<Video> videos = new ArrayList<Video>();
        try
        {
            //Read JSON file
            Object obj = jsonParser.parse(json);
 
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
                if(video.isValid())
                    videos.add(video);
            }
            
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

    public static List<VideoInfos> getVideoQualities(Video video) {
        List<VideoInfos> qualities = new ArrayList<VideoInfos>();
        String str = exec(yt_dlp+" -F https://www.youtube.com/watch?v="+video.getId());
        
        if(str == null || !str.contains("\n"))
            return qualities;
        String[] lines = str.split("\n");
        VideoInfos vInf = null;
        for(String s : lines) {
            vInf = new VideoInfos();
            try {
                String[] sp = s.split("[ ]+");
                String id = sp[0],
                       ext = sp[1],
                       res = sp[2];
                       
                if(!ext.equals("mp4")) // || id.length() > 2
                    continue;
                
                vInf.setId(id);
                vInf.setExtension(ext);
                vInf.setResolution(res);
                for(String s2 : sp) {
                    if(s2.contains("KiB") || s2.contains("MiB")) {
                        vInf.setSize(s2);
                        break;
                    }
                }
            } catch(Exception e) {
                vInf = null;
            }
            
            if(vInf != null)
                qualities.add(vInf);
        }
        
        return qualities;
    }

    public String downloadVideo(Video video, VideoInfos infos) {
        return downloadVideo(pref.getDownloadFolder(), video, infos);
    }

    public static String downloadVideo(File folder, Video video, VideoInfos infos) {
        if(folder == null || !folder.isDirectory())
            return null;
        String ffmpeg_attr = isWindows() ? "" : "--ffmpeg-location";
        String ffmpeg = isWindows() ? "" : ffmpeg_loc;
        ProcessBuilder pB;
        pB = new ProcessBuilder(
                yt_dlp, // works with "youtube-dl"
                ffmpeg_attr, ffmpeg,
                "-f", infos.getId()+"+140", "--write-sub",
                "--output", folder.getAbsolutePath()+"/%(title)s "+infos.getQuality()+".mp4",
                "https://www.youtube.com/watch?v="+video.getId()
            );
        
        StringBuilder output = new StringBuilder();
		try {
			Process process = pB.start();
			BufferedReader reader = new BufferedReader(
					new InputStreamReader(process.getInputStream()));

			String line;
			while ((line = reader.readLine()) != null)
				output.append(line + "\n");

			process.waitFor();
		} catch (IOException | InterruptedException e) {
			e.printStackTrace();
		}
        	
        return output.toString();
    }

    ////////////////////////////////////////////////////////
    ////                                                ////
    ////            ANCIENNES METHODES                  ////
    ////                                                ////
    ////////////////////////////////////////////////////////


    // TODO: Recreer la fonction avec l'aide de exec.
    public static String getHtmlFromSearch(String search) {
        String s, html = "";
        Process p = null;
        search = search.replace(" ", "%20");
        try {
            p = Runtime.getRuntime().exec("curl -s https://www.youtube.com/results?search_query=\""+search+"\"");
            BufferedReader br = new BufferedReader(
                new InputStreamReader(p.getInputStream()));
            while ((s = br.readLine()) != null)
                html += s+"\n";
            p.waitFor();
            p.destroy();
        } catch (Exception e1) {
            System.out.println ("exit: " + p.exitValue());
        }

        return html;
    }

    // Fonction de secours. Au cas où la methode actuelle ne fonction de plus.
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

    public static String getOS() {
        return System.getProperty("os.name");
    }

    public static boolean isLinux() {
        return getOS().toUpperCase().contains("NUX");
    }

    public static boolean isWindows() {
        return getOS().toUpperCase().contains("WIN");
    }

}
