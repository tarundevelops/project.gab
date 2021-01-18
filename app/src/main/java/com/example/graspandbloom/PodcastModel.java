package com.example.graspandbloom;
public class PodcastModel {
    private String Topic;
    private String SpeakerName;
    private String date;
    private String topicDescription;
    private String duration;
    private String imageUrl;
    private String audioUrl;



    public PodcastModel() {
    }


    public PodcastModel(String topic, String speakerName, String date, String topicDescription, String duration, String imageUrl, String audioUrl) {
        this.Topic = topic;
        SpeakerName = speakerName;
        this.date = date;
        this.topicDescription = topicDescription;
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


    public String getTopicDescription() {
        return topicDescription;
    }

    public void setTopicDescription(String topicDescription) {
        this.topicDescription = topicDescription;
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
