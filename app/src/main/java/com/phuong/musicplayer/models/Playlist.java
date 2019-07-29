package com.phuong.musicplayerapp.models;


import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

import java.io.Serializable;

@Entity(tableName = "playlist_table")
public class Playlist implements Serializable {

    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "id")
    private long id = 0;

    @ColumnInfo(name = "title")
    private String title = "";

    @ColumnInfo(name = "from_users")
    private int fromUsers = 0;

    public Playlist(String title) {
        this.title = title;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public int getFromUsers() {
        return fromUsers;
    }

    public void setFromUsers(int fromUsers) {
        this.fromUsers = fromUsers;
    }
}
