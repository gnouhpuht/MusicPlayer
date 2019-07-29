package com.phuong.musicplayerapp.db.dao;


import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;
import com.phuong.musicplayerapp.models.Artist;
import java.util.List;

@Dao
public abstract class ArtistDao {
    @Query("SELECT * FROM artist_table ORDER BY artist_name asc")
    public abstract LiveData<List<Artist>> getAllArtist();

    @Query("DELETE FROM artist_table")
    public abstract void deleteAll();

    @Query("SELECT COUNT(*) FROM artist_table")
    public abstract int getCount();

    @Insert
    public abstract long insert(Artist artist);

    @Query("SELECT * FROM artist_table")
    public abstract List<Artist> testGetArtist();
}
