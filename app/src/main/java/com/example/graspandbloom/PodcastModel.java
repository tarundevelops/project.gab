package com.example.graspandbloom;


import java.util.ArrayList;
import java.util.HashSet;

public class PodcastModel  {
    private String Topic;
    private String SpeakerName;
    private String date;
    private String aboutTopic;
    private String duration;
    private String imageUrl;
    private String audioUrl;
    private ArrayList<String> listenedBy;
    private ArrayList<String> likedBy;


    public ArrayList<String> getListenedBy() {

       HashSet<String> filter= new HashSet<String>(listenedBy);
       return new ArrayList<String>(filter);
    }

    public void setListenedBy(ArrayList<String> listenedBy) {
        this.listenedBy = listenedBy;
    }

    public ArrayList<String> getLikedBy() {
        HashSet<String> filter =new HashSet<>(likedBy);
        return new ArrayList<>(likedBy);
    }

    public void setLikedBy(ArrayList<String> likedBy) {
        this.likedBy = likedBy;
    }

    public PodcastModel() {
    }


    public PodcastModel(String topic, String speakerName, String date, String topicDescription, String duration, String imageUrl, String audioUrl) {
        this.Topic = topic;
        SpeakerName = speakerName;
        this.date = date;
        this.aboutTopic = topicDescription;
        this.duration = duration;
        this.imageUrl = imageUrl;
        this.audioUrl = audioUrl;
    }


    public String getTopic() {
        return Topic;
    }

    public void setTopic(String topic) {
        this.Topic = topic;
    }

    public String getSpeakerName() {
        return SpeakerName;
    }

    public void setSpeakerName(String speakerName) {
        SpeakerName = speakerName;

    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
   }


    public String getAboutTopic() {
        return aboutTopic;
    }

    public void setAboutTopic(String aboutTopic) {
        this.aboutTopic = aboutTopic;
    }






    public String getDuration() {
        return duration;
    }

    public void setDuration(String duration) {
        this.duration = duration;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public String getAudioUrl() {
        return audioUrl;
    }

    public void setAudioUrl(String audioUrl) {
        this.audioUrl = audioUrl;
    }
}
