package com.phuong.musicplayerapp.models;



import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.Ignore;
import androidx.room.PrimaryKey;

@Entity(tableName = "playlist_song_table")
public class PlaylistSong {
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "id")
    private long id = 0;

    @ColumnInfo(name = "id_song")
    private long songId;

    @ColumnInfo(name = "id_playlist")
    private long playlistId;

    public PlaylistSong(long songId, long playlistId) {
        this.songId = songId;
        this.playlistId = playlistId;
    }

    @Ignore
    public PlaylistSong() {
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public long getSongId() {
        return songId;
    }

    public void setSongId(long songId) {
        this.songId = songId;
    }

    public long getPlaylistId() {
        return playlistId;
    }

    public void setPlaylistId(long playlistId) {
        this.playlistId = playlistId;
    }
}
