package com.phuong.musicplayerapp.db.dao;


import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;

import com.phuong.musicplayerapp.models.PlaylistSong;

import java.util.List;


@Dao
public abstract class PlaylistSongDao {

    @Insert
    public abstract long insert(PlaylistSong playlistSong);

    @Query("SELECT * FROM playlist_song_table WHERE id_playlist=:playlistId")
    public abstract LiveData<List<PlaylistSong>> getSongByPlaylistId(long playlistId);

    @Query("DELETE FROM playlist_song_table WHERE id=:idSong")
    public abstract void deleteSongById(long idSong);
}
