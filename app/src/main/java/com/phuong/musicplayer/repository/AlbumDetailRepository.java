package com.phuong.musicplayerapp.fragment.child.my_music.albums.detail;

import android.app.Application;
import androidx.lifecycle.LiveData;
import com.phuong.musicplayerapp.db.SRDatabase;
import com.phuong.musicplayerapp.db.dao.AlbumSongDao;
import com.phuong.musicplayerapp.models.AlbumSong;
import java.util.List;

class AlbumDetailRepository {

    private AlbumSongDao mAlbumSongDao;

    AlbumDetailRepository(Application application) {
        SRDatabase database = SRDatabase.getDatabase(application);
        mAlbumSongDao = database.mAlbumSongDao();
    }

    long insert(AlbumSong albumSong) {
        return mAlbumSongDao.insert(albumSong);
    }

    LiveData<List<Long>> getSongByAlbumId(long albumId) {
        return mAlbumSongDao.getSongByAlbumId(albumId);
    }
}
