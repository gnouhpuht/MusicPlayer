package com.phuong.musicplayerapp.fragment.child.playlist;

import android.app.Application;
import android.os.AsyncTask;
import androidx.lifecycle.LiveData;
import com.phuong.musicplayerapp.db.SRDatabase;
import com.phuong.musicplayerapp.db.dao.PlaylistDao;
import com.phuong.musicplayerapp.models.Playlist;
import java.util.List;

public class PlaylistRepository {

    private LiveData<List<Playlist>> mListLiveDataPlaylist;
    private PlaylistDao mPlaylistDao;

    PlaylistRepository(Application application) {
        SRDatabase database = SRDatabase.getDatabase(application);
        mPlaylistDao = database.mPlaylistDao();
        mListLiveDataPlaylist = mPlaylistDao.getAllPlaylist();
    }

    LiveData<List<Playlist>> getAllPlaylist() {
        if (mListLiveDataPlaylist == null) {
            mListLiveDataPlaylist = mPlaylistDao.getAllPlaylist();
        }
        return mListLiveDataPlaylist;
    }

    void deleteAll() {
        new deleteAllPlaylistAsync(mPlaylistDao).execute();
    }

    private static class deleteAllPlaylistAsync extends AsyncTask<Playlist, Void, Void> {

        private PlaylistDao mPlaylistDaoAsync;

        deleteAllPlaylistAsync(PlaylistDao playlistDao) {
            mPlaylistDaoAsync = playlistDao;
        }

        @Override
        protected Void doInBackground(Playlist... playlists) {
            mPlaylistDaoAsync.deleteAll();
            return null;
        }
    }

    void updatePlaylist(Playlist playlist) {
        new updatePlaylistAysnc(mPlaylistDao).execute(playlist);
    }

    private static class updatePlaylistAysnc extends AsyncTask<Playlist, Void, Void> {

        PlaylistDao mPlaylistDao;

        updatePlaylistAysnc(PlaylistDao playlistDao) {
            this.mPlaylistDao = playlistDao;
        }

        @Override
        protected Void doInBackground(Playlist... playlists) {
            mPlaylistDao.update(playlists[0]);
            return null;
        }
    }

    void insert(Playlist playlist) {
        new insertAsyncTask(mPlaylistDao).execute(playlist);
    }

    private static class insertAsyncTask extends AsyncTask<Playlist, Void, Void> {

        private PlaylistDao mPlaylistDaoAsyncTask;

        insertAsyncTask(PlaylistDao dao) {
            this.mPlaylistDaoAsyncTask = dao;
        }

        @Override
        protected Void doInBackground(Playlist... playlists) {
            mPlaylistDaoAsyncTask.insert(playlists[0]);
            return null;
        }
    }

    void deleteById(long id) {
        mPlaylistDao.delete(id);
    }

    void deleteByPlaylist(Playlist playlist) {
        new deletePlaylistAsync(mPlaylistDao).execute(playlist);
    }

    private static class deletePlaylistAsync extends AsyncTask<Playlist, Void, Void> {

        private PlaylistDao mPlaylistDaoAsync;

        deletePlaylistAsync(PlaylistDao dao) {
            mPlaylistDaoAsync = dao;
        }

        @Override
        protected Void doInBackground(Playlist... playlists) {
            mPlaylistDaoAsync.deleteByPlaylist(playlists[0]);
            return null;
        }
    }

}
