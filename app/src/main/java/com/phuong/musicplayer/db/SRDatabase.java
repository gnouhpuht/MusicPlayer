package com.phuong.musicplayerapp.db;


import android.content.Context;
import android.os.AsyncTask;
import androidx.annotation.NonNull;
import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import androidx.sqlite.db.SupportSQLiteDatabase;
import com.phuong.musicplayerapp.db.dao.AlbumSongDao;
import com.phuong.musicplayerapp.db.dao.AlbumsDao;
import com.phuong.musicplayerapp.db.dao.ArtistDao;
import com.phuong.musicplayerapp.db.dao.ArtistSongDao;
import com.phuong.musicplayerapp.db.dao.FavoriteDao;
import com.phuong.musicplayerapp.db.dao.PlaylistDao;
import com.phuong.musicplayerapp.db.dao.PlaylistSongDao;
import com.phuong.musicplayerapp.db.dao.SongDao;
import com.phuong.musicplayerapp.models.AlbumSong;
import com.phuong.musicplayerapp.models.Albums;
import com.phuong.musicplayerapp.models.Artist;
import com.phuong.musicplayerapp.models.ArtistSong;
import com.phuong.musicplayerapp.models.Favorite;
import com.phuong.musicplayerapp.models.Playlist;
import com.phuong.musicplayerapp.models.PlaylistSong;
import com.phuong.musicplayerapp.models.Song;

@Database(entities = {Song.class, Albums.class, Artist.class, Playlist.class, AlbumSong.class, ArtistSong.class, PlaylistSong.class, Favorite.class}, version = 1, exportSchema = false)
public abstract class SRDatabase extends RoomDatabase {
    public abstract SongDao mSongDao();

    public abstract AlbumsDao mAlbumsDao();

    public abstract ArtistDao mArtistDao();

    public abstract PlaylistDao mPlaylistDao();

    public abstract AlbumSongDao mAlbumSongDao();

    public abstract ArtistSongDao mArtistSongDao();

    public abstract PlaylistSongDao mPlaylistSongDao();

    public abstract FavoriteDao mFavoriteDao();

    private static volatile SRDatabase INSTANCE;


    public static SRDatabase getDatabase(final Context context) {
        if (INSTANCE == null) {
            synchronized (SRDatabase.class) {
                if (INSTANCE == null) {
                    INSTANCE = Room.databaseBuilder(context.getApplicationContext(),
                            SRDatabase.class, "music_database")
                            .fallbackToDestructiveMigration()
                            .build();
                }
            }
        }
        return INSTANCE;
    }

    private static SRDatabase.Callback sCallback = new Callback() {
        @Override
        public void onOpen(@NonNull SupportSQLiteDatabase db) {
            super.onOpen(db);
            new PopulateDbAsync(INSTANCE).execute();
        }
    };

    private static class PopulateDbAsync extends AsyncTask<Void, Void, Void> {

        private final SongDao mSongDao;

        PopulateDbAsync(SRDatabase db) {
            mSongDao = db.mSongDao();
        }

        @Override
        protected Void doInBackground(Void... voids) {
            mSongDao.deleteAllSong();
            return null;
        }
    }
}
