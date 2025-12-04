package com.oranbyte.fiber.models;

public class User {

    public int id;
    public String username;

    private String email;

    private  String name;

    private String image;

    private String api_key;

    private String key_visibility;

    public User(){}


    public User(int id, String username, String email, String name, String image, String api_key, String key_visibility) {
        this.id = id;
        this.username = username;
        this.email = email;
        this.name = name;
        this.image = image;
        this.api_key = api_key;
        this.key_visibility = key_visibility;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getApiKey() {
        return api_key;
    }

    public void setApiKey(String api_key) {
        this.api_key = api_key;
    }

    public String getKeyVisibility() {
        return key_visibility;
    }

    public void setKeyVisibility(String key_visibility) {
        this.key_visibility = key_visibility;
    }

    @Override
    public String toString() {
        return "User{" +
                "id=" + id +
                ", username='" + username + '\'' +
                ", email='" + email + '\'' +
                ", name='" + name + '\'' +
                ", image='" + image + '\'' +
                ", key_visibility='" + key_visibility + '\'' +
                '}';
    }
}
