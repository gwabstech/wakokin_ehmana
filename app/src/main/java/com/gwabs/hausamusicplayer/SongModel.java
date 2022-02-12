package com.gwabs.hausamusicplayer;

public class SongModel {
   private String songNname,songUrl;

    public SongModel(String songName, String songUrl) {
        this.songNname = songName;
        this.songUrl = songUrl;
    }

    public String getSongNname() {
        return songNname;
    }

    public void setSongNname(String songNname) {
        this.songNname = songNname;
    }

    public String getSongUrl() {
        return songUrl;
    }

    public void setSongUrl(String songUrl) {
        this.songUrl = songUrl;
    }
}
