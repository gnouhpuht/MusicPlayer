package com.phuong.musicplayer.item;

import android.widget.ImageView;

import java.util.Date;

public class ItemAlbum {
    private String nameAlbum;
    private String path;
    private String singer;

    public ItemAlbum(String nameAlbum, String path, String singer) {
        this.nameAlbum = nameAlbum;
        this.path = path;
        this.singer = singer;
    }

    public String getNameAlbum() {
        return nameAlbum;
    }

    public void setNameAlbum(String nameAlbum) {
        this.nameAlbum = nameAlbum;
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
