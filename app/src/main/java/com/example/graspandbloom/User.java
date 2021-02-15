package com.example.graspandbloom;


public class User {
    String personName, personEmail, personId,img_uri;
    public User()
    {

    }


    public User(String personName, String personEmail, String personId,String img_uri) {
        this.personName = personName;
        this.personEmail = personEmail;
        this.personId = personId;
        this.img_uri = img_uri;
    }

    public String getPersonName() {
        return personName;
    }

    public void setPersonName(String personName) {
        this.personName = personName;
    }

    public String getPersonEmail() {
        return personEmail;
    }

    public void setPersonEmail(String personEmail) {
        this.personEmail = personEmail;
    }

    public String getPersonId() {
        return personId;
    }

    public void setPersonId(String personId) {
        this.personId = personId;
    }

    public String getImg_uri() {
        return img_uri;
    }

    public void setImg_uri(String img_uri) {
        this.img_uri = img_uri;
    }

}
