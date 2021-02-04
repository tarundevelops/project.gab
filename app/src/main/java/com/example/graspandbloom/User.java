package com.example.graspandbloom;

public class User {
    private String name;
    private String email;
    private String city;
    private String country;

    public User() {
    }

    public User(String name, String email, String city, String country) {
        this.name = name;
        this.email = email;
        this.city = city;
        this.country = country;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }
}
