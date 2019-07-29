package com.phuong.musicplayerapp.fragment.child.playlist.details;

import android.app.Application;
import android.os.AsyncTask;
import androidx.lifecycle.LiveData;
import com.phuong.musicplayerapp.db.SRDatabase;
import com.phuong.musicplayerapp.db.dao.PlaylistSongDao;
import com.phuong.musicplayerapp.models.PlaylistSong;
import java.util.List;

public class PlaylistDetailRepository {

    private PlaylistSongDao mDao;

    PlaylistDetailRepository(Application application) {
        SRDatabase database = SRDatabase.getDatabase(application);
        mDao = database.mPlaylistSongDao();
    }

    public long insert(PlaylistSong playlistSong) {
        return mDao.insert(playlistSong);
    }

    LiveData<List<PlaylistSong>> getIdSongByIdPlaylist(long playlistId) {
        return mDao.getSongByPlaylistId(playlistId);
    }

    void deleteSongById(long idSong) {
        new deleteSongByIdSync(mDao).execute(idSong);
    }

    private static class deleteSongByIdSync extends AsyncTask<Long, Void, Void> {

        private PlaylistSongDao mPlaylistSongDaoSync;

        deleteSongByIdSync(PlaylistSongDao playlistSongDao) {
            mPlaylistSongDaoSync = playlistSongDao;
        }

        @Override
        protected Void doInBackground(Long... longs) {
            mPlaylistSongDaoSync.deleteSongById(longs[0]);
            return null;
        }
    }
}
