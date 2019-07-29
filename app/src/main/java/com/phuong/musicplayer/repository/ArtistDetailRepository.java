package com.phuong.musicplayerapp.fragment.child.my_music.artists.details;

import android.app.Application;
import androidx.lifecycle.LiveData;
import com.phuong.musicplayerapp.db.SRDatabase;
import com.phuong.musicplayerapp.db.dao.ArtistSongDao;
import com.phuong.musicplayerapp.db.dao.SongDao;
import com.phuong.musicplayerapp.models.ArtistSong;
import com.phuong.musicplayerapp.models.Song;

import java.util.List;

class ArtistDetailRepository {

    private SongDao mSongDao;
    private LiveData<List<Song>> mListLiveDataSong;
    private ArtistSongDao mArtistSongDao;
    private LiveData<List<Long>> mListSongArtist;

    ArtistDetailRepository(Application application) {
        SRDatabase database = SRDatabase.getDatabase(application);
        mSongDao = database.mSongDao();
        mArtistSongDao = database.mArtistSongDao();
    }

    LiveData<List<Long>> getSongIdByArtistId(long artistId) {
        mListSongArtist = mArtistSongDao.getSongIdByArtistId(artistId);
        return mListSongArtist;
    }

    public long insert(ArtistSong artistSong) {
        return mArtistSongDao.insert(artistSong);
    }
}
