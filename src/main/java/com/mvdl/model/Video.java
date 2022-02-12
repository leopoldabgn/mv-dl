package com.mvdl.model;

public class Video {
    
    private String id, title,
            duration, date, views;
    private String thumbnailURL;

    public Video() {}

    public boolean isValid() {
        return !(id.isBlank() || title.isBlank() || duration.isBlank());
    }

    @Override
    public String toString() {
        return id+", "+title+", "+duration+
               ", "+views+", "+date;
    }

    public String getTitle() {
        return this.title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getViews() {
        return this.views;
    }

    public void setViews(String views) {
        this.views = views;
    }

    public String getId() {
        return this.id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getDate() {
		return this.date;
	}

    public void setDate(String date) {
		this.date = date;
	}

    public String getDuration() {
        return this.duration;
    }

    public void setDuration(String duration) {
        this.duration = duration;
    }

    public void setThumbnailURL(String URL) {
        this.thumbnailURL = URL;
    }

    public String getThumbnailURL() {
        return thumbnailURL;
    }

    public static class VideoInfos {
        private String id, extension, resolution, size = "";

        public VideoInfos() {}
        
        @Override
        public String toString() {
            return id+" "+extension+" "+resolution+" "+size;
        }

        public String getId() {
            return this.id;
        }

        public void setId(String id) {
            this.id = id;
        }

        public String getExtension() {
            return this.extension;
        }

        public void setExtension(String extension) {
            this.extension = extension;
        }

        public String getQuality() {
            String resolution = getResolution(),
                   error_msg = "no quality";
            if(resolution == null || !resolution.contains("x"))
                return error_msg;

            String[] qp = resolution.split("x");
            if(qp != null && qp.length >= 2)
                return qp[1]+"p";
            return error_msg;
        }
        public String getResolution() {
            return this.resolution;
        }

        public void setResolution(String resolution) {
            this.resolution = resolution;
        }

        public String getSize() {
            return this.size;
        }

        public void setSize(String size) {
            this.size = size;
        }

    }

}
