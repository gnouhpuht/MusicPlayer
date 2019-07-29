package com.phuong.musicplayerapp.db.dao;


import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;
import com.phuong.musicplayerapp.models.Favorite;

import java.util.List;

@Dao
public abstract class FavoriteDao {
    @Insert
    public abstract long insert(Favorite favorite);

    @Query("DELETE FROM favorite_table WHERE id=:idSong")
    public abstract void deleteSong(long idSong);

    @Query("SELECT * FROM favorite_table")
    public abstract LiveData<List<Favorite>> getIdSong();
}
