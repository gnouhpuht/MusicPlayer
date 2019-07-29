package com.phuong.musicplayerapp.models;


import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.Ignore;
import androidx.room.PrimaryKey;

@Entity(tableName = "album_table")
public class Albums {
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "id")
    private long id = 0;

    @ColumnInfo(name = "album_name")
    private String name = "";

    @ColumnInfo(name = "album_image")
    private String albumArt = null;

    @Ignore
    public Albums() {
    }

    public Albums(long id, String name, String albumArt) {
        this.id = id;
        this.name = name;
        this.albumArt = albumArt;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getAlbumArt() {
        return albumArt;
    }

    public void setAlbumArt(String albumArt) {
        this.albumArt = albumArt;
    }
}
