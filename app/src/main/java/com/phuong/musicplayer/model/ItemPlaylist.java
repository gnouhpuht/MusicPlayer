package com.phuong.musicplayer.model;

public class ItemPlaylist {
    private String namePlaylist;
    private String numberSong;

    public ItemPlaylist(String namePlaylist, String numberSong) {
        this.namePlaylist = namePlaylist;
        this.numberSong = numberSong;
    }

    public String getNamePlaylist() {
        return namePlaylist;
    }

    public void setNamePlaylist(String namePlaylist) {
        this.namePlaylist = namePlaylist;
    }

    public String getNumberSong() {
        return numberSong;
    }

    public void setNumberSong(String numberSong) {
        this.numberSong = numberSong;
    }
}
