package com.phuong.musicplayerapp.db.dao;


import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;
import com.phuong.musicplayerapp.models.AlbumSong;

import java.util.List;

@Dao
public abstract class AlbumSongDao {
    @Insert
    public abstract long insert(AlbumSong albumSong);

    @Query("SELECT * FROM album_song_table")
    public abstract List<AlbumSong> getSongTest();

    @Query("SELECT song_id FROM album_song_table WHERE album_id =:albumId")
    public abstract LiveData<List<Long>> getSongByAlbumId(long albumId);
}
