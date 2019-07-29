package com.phuong.musicplayer;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import com.phuong.musicplayer.models.Artist;
import com.phuong.musicplayer.repository.ArtistRepository;

import java.util.List;


public class ArtistViewModel extends AndroidViewModel {

    private ArtistRepository mRepository;
    private LiveData<List<Artist>> mAllArtist;

    public ArtistViewModel(@NonNull Application application) {
        super(application);
        mRepository = new ArtistRepository(application);
        mAllArtist = mRepository.getAllArtist();
    }

    LiveData<List<Artist>> getAllArtist() {
        return mAllArtist;
    }

    public long insert(Artist artist) {
        return mRepository.insert(artist);
    }
}
