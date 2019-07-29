package com.phuong.musicplayerapp.db.dao;


import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;
import com.phuong.musicplayerapp.models.ArtistSong;
import java.util.List;

@Dao
public abstract class ArtistSongDao {
    @Insert
    public abstract long insert(ArtistSong artistSong);

    @Query("SELECT song_id FROM artist_song_table WHERE artist_id=:artistId")
    public abstract LiveData<List<Long>> getSongIdByArtistId(long artistId);
}
