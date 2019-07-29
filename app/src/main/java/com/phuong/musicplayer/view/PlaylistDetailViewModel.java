package com.phuong.musicplayerapp.fragment.child.playlist.details;

import android.app.Application;
import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import com.phuong.musicplayerapp.models.PlaylistSong;
import java.util.List;

public class PlaylistDetailViewModel extends AndroidViewModel {
    private PlaylistDetailRepository mRepository;

    public PlaylistDetailViewModel(@NonNull Application application) {
        super(application);
        mRepository = new PlaylistDetailRepository(application);
    }

    public long insert(PlaylistSong playlistSong) {
        return mRepository.insert(playlistSong);
    }

    LiveData<List<PlaylistSong>> getIdSongByIdPlaylist(long playlistId) {
        return mRepository.getIdSongByIdPlaylist(playlistId);
    }

    void deleteSongById(long idSong) {
        mRepository.deleteSongById(idSong);
    }
}
