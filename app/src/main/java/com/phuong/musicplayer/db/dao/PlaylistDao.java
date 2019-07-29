package com.phuong.musicplayerapp.db.dao;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;
import com.phuong.musicplayerapp.models.Playlist;
import java.util.List;

@Dao
public abstract class PlaylistDao {
    @Insert
    public abstract long insert(Playlist playlist);

    @Query("DELETE FROM playlist_table")
    public abstract void deleteAll();

    @Update
    public abstract void update(Playlist playlist);

    @Query("DELETE FROM playlist_table WHERE title=:playlistId")
    public abstract void delete(long playlistId);

    @Query("SELECT * FROM playlist_table ORDER BY title ASC")
    public abstract LiveData<List<Playlist>> getAllPlaylist();

    @Delete
    public abstract void deleteByPlaylist(Playlist playlist);
}
