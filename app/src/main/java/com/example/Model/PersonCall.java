package com.example.Model;

public class PersonCall {

    private String name ;
    private String avatar ;

    private String urlVideo ;


    public PersonCall(String name, String avatar, String urlVideo) {
        this.name = name;
        this.avatar = avatar;
        this.urlVideo = urlVideo;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getAvatar() {
        return avatar;
    }

    public void setAvatar(String avatar) {
        this.avatar = avatar;
    }

    public String getUrlVideo() {
        return urlVideo;
    }

    public void setUrlVideo(String urlVideo) {
        this.urlVideo = urlVideo;
    }
}
