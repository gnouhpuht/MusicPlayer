package com.phuong.musicplayerapp.db.dao;


import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;
import com.phuong.musicplayerapp.models.Albums;
import java.util.List;

@Dao
public abstract class AlbumsDao {
    @Insert
    public abstract Long insert(Albums albums);

    @Query("SELECT COUNT(id) FROM album_table")
    public abstract int count();

    @Query("DELETE FROM album_table")
    public abstract void deleteAll();

    @Query("SELECT * FROM album_table ORDER BY album_name")
    public abstract LiveData<List<Albums>> getAllAlbums();

    @Query("SELECT * FROM album_table ORDER BY album_name")
    public abstract List<Albums> getAlbumTest();
}
