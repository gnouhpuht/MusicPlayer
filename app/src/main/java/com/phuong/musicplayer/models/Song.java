package com.phuong.musicplayerapp.models;


import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.Ignore;
import androidx.room.PrimaryKey;

import java.io.Serializable;

@Entity(tableName = "song_table")
public class Song implements Serializable {
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "id")
    private long id = 0;

    @ColumnInfo(name = "title")
    private String title;

    @ColumnInfo(name = "duration")
    private int duration = 0;

    @ColumnInfo(name = "song_path")
    private String mSongPath = null;

    @ColumnInfo(name = "artist")
    private String artist;

    @Ignore
    private boolean isAdded = false;

    @Ignore
    private long playlistSongId;

    @Ignore
    private long favoriteSongId;

    @Ignore
    public Song(long id, String title, String artist) {
        this.id = id;
        this.title = title;
        this.artist = artist;
    }

    @Ignore
    public Song(long id, String title) {
        this.id = id;
        this.title = title;
    }

    @Ignore
    public Song(String title) {
        this.title = title;
    }

    public Song() {
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

    public int getDuration() {
        return duration;
    }

    public void setDuration(int duration) {
        this.duration = duration;
    }

    public String getSongPath() {
        return mSongPath;
    }

    public void setSongPath(String songPath) {
        this.mSongPath = songPath;
    }

    public String getArtist() {
        return artist;
    }

    public void setArtist(String artist) {
        this.artist = artist;
    }

    public boolean isAdded() {
        return isAdded;
    }

    public void setAdded(boolean added) {
        isAdded = added;
    }

    public long getPlaylistSongId() {
        return playlistSongId;
    }

    public void setPlaylistSongId(long playlistSongId) {
        this.playlistSongId = playlistSongId;
    }

    public long getFavoriteSongId() {
        return favoriteSongId;
    }

    public void setFavoriteSongId(long favoriteSongId) {
        this.favoriteSongId = favoriteSongId;
    }
}
