package com.phuong.musicplayer.sevice;

import android.app.Service;
import android.content.Intent;
import android.database.Cursor;
import android.os.Binder;
import android.os.IBinder;
import android.provider.MediaStore;
import android.util.Log;

import com.phuong.musicplayer.model.ItemMusic;
import com.phuong.musicplayer.musicmanager.MusicManager;

import java.util.ArrayList;
import java.util.List;

public class ServiceSearchMusic extends Service {
    private List<ItemMusic> itemMusics;
    private MusicManager musicManager;
    private int currentPosition;


    @Override
    public void onCreate() {
        itemMusics=getAllMusic();
        super.onCreate();
    }

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    public List<ItemMusic> getAllMusic() {
        List<ItemMusic> itemMusics = new ArrayList<>();
        Cursor cursor = getContentResolver().query(MediaStore.Audio.Media.EXTERNAL_CONTENT_URI,
                null, null, null, null);
        String dataPath = "_data";
        int indexDataPath = cursor.getColumnIndex(dataPath);
        String displayName = "_display_name";
        int indexDisplayName = cursor.getColumnIndex(displayName);
        String duration = "duration";
        int indexDuration = cursor.getColumnIndex(duration);
        String artist = "artist";
        int indexArtist = cursor.getColumnIndex(artist);
        cursor.moveToFirst();


        while (!cursor.isAfterLast()) {
            String path = cursor.getString(indexDataPath);
            String name = cursor.getString(indexDisplayName);
            String artistData = cursor.getString(indexArtist);
            long durationData = cursor.getLong(indexDuration);

            ItemMusic itemMusic = new ItemMusic();
            itemMusic.setPath(path);
            itemMusic.setName(name);
            itemMusic.setSinger(artistData);
            itemMusic.setDuration(durationData);
            itemMusics.add(itemMusic);
            cursor.moveToNext();

        }
        cursor.close();
        Log.d("aaaaaaaaaaaaaaaaa", "getAllMusic: " + itemMusics.size());
        return itemMusics;
    }

    public static class MyBinder extends Binder {
        private ServiceSearchMusic serviceSearchMusic;

        public MyBinder(ServiceSearchMusic serviceSearchMusic) {
            this.serviceSearchMusic = serviceSearchMusic;
        }

        public ServiceSearchMusic getServiceSearchMusic() {
            return serviceSearchMusic;
        }
    }
    public static ServiceSearchMusic mServiceMusic ;
    public static ServiceSearchMusic getInstance() {

        return mServiceMusic;
    }

}
