package com.example.user.lifeslicetest;

/**
 * Created by User on 26.01.2017.
 */
public class Record {
    private String avatarUrl;
    private String thumbnailUrl;
    private String username;
    private String videoLowURL;
    private String videoUrl;

    public String getAvatarUrl() {
        return avatarUrl;
    }

    public void setAvatarUrl(String avatarUrl) {
        this.avatarUrl = avatarUrl;
    }

    public String getThumbnailUrl() {
        return thumbnailUrl;
    }

    public void setThumbnailUrl(String thumbnailUrl) {
        this.thumbnailUrl = thumbnailUrl;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getVideoLowURL() {
        return videoLowURL;
    }

    public void setVideoLowURL(String videoLowURL) {
        this.videoLowURL = videoLowURL;
    }

    public String getVideoUrl() {
        return videoUrl;
    }

    public void setVideoUrl(String videoUrl) {
        this.videoUrl = videoUrl;
    }
}
