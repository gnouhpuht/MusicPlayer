package com.phuong.musicplayer.item;

public class ItemArtist {
    private String nameArtist;
    private String path;


    public ItemArtist(String path,String nameArtist ) {
        this.nameArtist = nameArtist;
        this.path = path;
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
}
