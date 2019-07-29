package com.phuong.musicplayer;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;


import com.phuong.musicplayer.models.ArtistSong;
import com.phuong.musicplayer.repository.ArtistDetailRepository;

import java.util.List;

public class ArtistDetailViewModel extends AndroidViewModel {
    private ArtistDetailRepository mRepository;

    public ArtistDetailViewModel(@NonNull Application application) {
        super(application);
        mRepository = new ArtistDetailRepository(application);
    }

    LiveData<List<Long>> getSongIdByArtistId(long artistId) {
        return mRepository.getSongIdByArtistId(artistId);
    }

    public long insert(ArtistSong artistSong) {
        return mRepository.insert(artistSong);
    }
}
