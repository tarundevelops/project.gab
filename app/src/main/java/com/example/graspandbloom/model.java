package com.example.graspandbloom;
public class model {
    private String eventName;
    private String SpeakerName;
    private String date;
    private String organizerName;
    private String eventDescription;
    private String time;
    private String duration;
    private String imageUrl;


    public model() {
    }


    public model(String eventName, String speakerName, String date, String organizerName, String eventDescription, String time, String duration, String imageUrl) {
        this.eventName = eventName;
        SpeakerName = speakerName;
        this.date = date;
        this.organizerName = organizerName;
        this.eventDescription = eventDescription;
        this.time = time;
        this.duration = duration;
        this.imageUrl = imageUrl;
    }


    public String getEventName() {
        return eventName;
    }

    public void setEventName(String eventName) {
        this.eventName = eventName;
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

    public String getOrganizerName() {
        return organizerName;
    }

    public void setOrganizerName(String organizerName) {
        this.organizerName = organizerName;
    }

    public String getEventDescription() {
        return eventDescription;
    }

    public void setEventDescription(String eventDescription) {
        this.eventDescription = eventDescription;
    }





    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
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
}
