package com.mvdl.model;

public class Video {
    
    private String id, title,
            duration, date, views;

    public Video() {}

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

}
