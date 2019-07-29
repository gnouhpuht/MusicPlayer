package com.phuong.musicplayerapp.db.dao;


import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;
import com.phuong.musicplayerapp.models.Song;
import java.util.List;

@Dao
public abstract class SongDao {
    @Insert
    public abstract long insert(Song  song);

    @Query("SELECT COUNT(id) FROM song_table")
    public abstract int count();

    @Query("DELETE FROM song_table")
    public abstract void deleteAllSong();

    @Query("SELECT * FROM song_table ORDER BY title ASC")
    public abstract LiveData<List<Song>> getAllSong();

    @Query("SELECT * FROM song_table WHERE title LIKE :keyword")
    public abstract List<Song> searchSong(String keyword);

    @Query("SELECT id FROM song_table WHERE title =:title")
    public abstract List<Long> getIdSong(String title);

    @Query("SELECT * FROM song_table WHERE id =:songId")
    public abstract List<Song> getSongByID(long songId);

    @Query("SELECT * FROM song_table ORDER BY title ASC")
    public abstract List<Song> getAllSongTest();
}
