package com.phuong.musicplayer;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import com.phuong.musicplayer.models.Albums;
import com.phuong.musicplayer.repository.AlbumRepository;

import java.util.List;


public class AlbumViewModel extends AndroidViewModel {

    private AlbumRepository mRepository;
    private LiveData<List<Albums>> mAllAlbums;

    public AlbumViewModel(@NonNull Application application) {
        super(application);
        mRepository = new AlbumRepository(application);
        mAllAlbums = mRepository.getAllAlbums();
    }

    LiveData<List<Albums>> getAllAlbums() {
        return mAllAlbums;
    }

    public long insert(Albums albums) {
        return mRepository.insert(albums);
    }

    public int count() {
        return mRepository.count();
    }
}
