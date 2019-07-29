package com.phuong.musicplayer;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import com.phuong.musicplayer.models.Song;
import com.phuong.musicplayer.repository.SongRepository;

import java.util.List;

public class SongViewModel extends AndroidViewModel {
    private SongRepository mRepository;
    private LiveData<List<Song>> mAllSongs;

    public SongViewModel(@NonNull Application application) {
        super(application);
        mRepository = new SongRepository(application);
        mAllSongs = mRepository.getAllSong();
    }

    public LiveData<List<Song>> getAllSongs() {
        return mAllSongs;
    }

    public int count() {
        return mRepository.count();
    }

    public long insert(Song song) {
        return mRepository.insert(song);
    }

    public List<Long> getIdByTitle(String title) {
        return mRepository.getIdByTitle(title);
    }

    public List<Song> getSongById(long idSong) {
        return mRepository.getSongById(idSong);
    }
}
