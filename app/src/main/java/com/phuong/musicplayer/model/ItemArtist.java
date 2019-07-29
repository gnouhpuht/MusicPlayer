package com.phuong.musicplayer.model;

public class ItemArtist {
    private String nameArtist;
    private String path;
    private String singer;


    public ItemArtist(String path,String nameArtist,String singer) {
        this.nameArtist = nameArtist;
        this.path = path;
        this.singer=singer;
    }

    public String getNameArtist() {
        return nameArtist;
    }

    public void setNameArtist(String nameArtist) {
        this.nameArtist = nameArtist;
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public String getSinger() {
        return singer;
    }

    public void setSinger(String singer) {
        this.singer = singer;
    }
}
