package com.phuong.musicplayerapp.fragment.child.my_music.artists;

import android.app.Application;
import androidx.lifecycle.LiveData;
import com.phuong.musicplayerapp.db.SRDatabase;
import com.phuong.musicplayerapp.db.dao.ArtistDao;
import com.phuong.musicplayerapp.models.Artist;
import java.util.List;

class ArtistRepository {
    private ArtistDao mArtistDao;
    private LiveData<List<Artist>> mAllArtist;

    ArtistRepository(Application application) {
        SRDatabase database = SRDatabase.getDatabase(application);
        mArtistDao = database.mArtistDao();
        mAllArtist = mArtistDao.getAllArtist();
    }

    LiveData<List<Artist>> getAllArtist() {
        if (mAllArtist == null) {
            mAllArtist = mArtistDao.getAllArtist();
        }
        return mAllArtist;
    }

    public long insert(Artist artist) {
        return mArtistDao.insert(artist);
    }
}
