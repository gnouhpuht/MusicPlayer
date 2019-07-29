package com.phuong.musicplayer.model;

public class ItemAlbum {
    private String nameAlbum;
    private String nameArtist;
    private String numberSong;

    public ItemAlbum(String nameAlbum, String nameArtist, String singer) {
        this.nameAlbum = nameAlbum;
        this.nameArtist = nameArtist;
        this.numberSong = singer;
    }

    public String getNameAlbum() {
        return nameAlbum;
    }

    public void setNameAlbum(String nameAlbum) {
        this.nameAlbum = nameAlbum;
    }

    public String getNameArtist() {
        return nameArtist;
    }

    public void setNameArtist(String nameArtist) {
        this.nameArtist = nameArtist;
    }

    public String getNumberSong() {
        return numberSong;
    }

    public void setNumberSong(String numberSong) {
        this.numberSong = numberSong;
    }
}
