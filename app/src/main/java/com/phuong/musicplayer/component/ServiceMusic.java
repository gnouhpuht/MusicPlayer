package com.phuong.musicplayer.component;

import android.app.Notification;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Intent;
import android.database.Cursor;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.os.Binder;
import android.os.Build;
import android.os.IBinder;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.RemoteViews;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.core.app.NotificationCompat;

import com.phuong.musicplayer.Constants;
import com.phuong.musicplayer.MainActivity;
import com.phuong.musicplayer.R;
import com.phuong.musicplayer.item.ItemAlbum;
import com.phuong.musicplayer.item.ItemArtist;
import com.phuong.musicplayer.item.ItemMusic;
import com.phuong.musicplayer.inter_.Action1;
import com.phuong.musicplayer.musicmanager.MusicManager;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ServiceMusic extends Service implements MediaPlayer.OnCompletionListener  {

    private List<ItemMusic> itemMusics;
    private List<ItemAlbum> itemAlbums;
    private MusicManager musicManager;
    private int currentPosition;
    private Map<String, Action1<MediaPlayer>> listCompleted;


    public MusicManager getMusicManager() {
        return musicManager;
    }

    public void setMusicManager(MusicManager musicManager) {
        this.musicManager = musicManager;
    }

    public void register(String key, Action1<MediaPlayer> action) {
        listCompleted.put(key, action);
    }

    public void unregister(String key) {
        listCompleted.remove(key);
    }

    @Override
    public void onCreate() {
        musicManager = new MusicManager();
        listCompleted = new HashMap<>();
        itemMusics = getAllMusic();
        itemAlbums=getAllAlbum();
        super.onCreate();


    }

    public int getDuration() {
        return musicManager.getDuration();
    }

    public int getCurrentPosition() {
        return currentPosition;
    }

    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {

        super.onStartCommand(intent, flags, startId);
        if (intent == null) {
            return START_STICKY;
        }
        if (intent.getAction().equals(Constants.ACTION.STARTFOREGROUND_ACTION)) {

            showNotification(getCurrentPosition());
//            Toast.makeText(this, "Service Started", Toast.LENGTH_SHORT).show();

        } else if (intent.getAction().equals(Constants.ACTION.PREV_ACTION)) {
            if (currentPosition <= 0) {
                currentPosition = getAllMusic().size();
            }
            currentPosition = currentPosition - 1;
            playMusic(currentPosition);
            Toast.makeText(this, "Clicked Previous", Toast.LENGTH_SHORT).show();
            Log.i(LOG_TAG, "Clicked Previous");
        } else if (intent.getAction().equals(Constants.ACTION.PLAY_ACTION)) {

//            RemoteViews views = new RemoteViews(getPackageName(), R.layout.big_notify);
//            views.setImageViewBitmap(R.id.iv_img_notify, Constants.getDefaultAlbumArt(this));
//            if (musicManager.isPlaying()){
//                views.setImageViewResource(R.id.btn_play_notify, R.drawable.ic_pause_black_24dp);
//            }else {
//                views.setImageViewResource(R.id.btn_play_notify, R.drawable.ic_play_arrow_black_24dp);
//            }

            Toast.makeText(this, "Clicked Play", Toast.LENGTH_SHORT).show();
            Log.i(LOG_TAG, "Clicked Play");
        } else if (intent.getAction().equals(Constants.ACTION.NEXT_ACTION)) {
            if (currentPosition >= getAllMusic().size() - 1) {
                currentPosition = 0;
            }
            currentPosition = currentPosition + 1;
            playMusic(currentPosition);
            Toast.makeText(this, "Clicked Next", Toast.LENGTH_SHORT).show();
            Log.i(LOG_TAG, "Clicked Next");
        }
//        else if (intent.getAction().equals(Constants.ACTION.STOPFOREGROUND_ACTION)) {
//            Log.i(LOG_TAG, "Received Stop Foreground Intent");
//            Toast.makeText(this, "Service Stoped", Toast.LENGTH_SHORT).show();
//            stopForeground(true);
//            stopSelf();
//        }
        return START_STICKY;

    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return new MyBinder(this);
    }


    public List<ItemMusic> getAllMusic() {
        List<ItemMusic> itemMusics=new ArrayList<>();
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
        Log.d("aaaaaaaaaaaaaaaaa", "getAllMusic: "+itemMusics.size());
        return itemMusics;
    }

    public List<ItemAlbum> getAllAlbum() {
        List<ItemAlbum> itemAlbums=new ArrayList<>();
//        Cursor cursor = getApplicationContext().getContentResolver().query(
//                MediaStore.Audio.Albums.getContentUri("external"),
//                new String[] {
//                        MediaStore.Audio.Albums.ARTIST,
//                        MediaStore.Audio.Albums._ID,
//                        MediaStore.Audio.Albums.NUMBER_OF_SONGS,
//                        MediaStore.Audio.Albums.ALBUM},
//                null, null,
//                MediaStore.Audio.Albums.ALBUM + " ASC");
        Cursor cursor = getContentResolver().query(MediaStore.Audio.Albums.EXTERNAL_CONTENT_URI,
                null, null, null, null);
        String dataPath = "album_art";
        int indexDataPath = cursor.getColumnIndex(dataPath);
        String nameAlbum = "artist";
        int indexDisplayName = cursor.getColumnIndex(nameAlbum);
        String singerAlbum = "numsongs";
        int indexSingerAlbum = cursor.getColumnIndex(singerAlbum);
        cursor.moveToFirst();



        while (!cursor.isAfterLast()) {
            String path = cursor.getString(indexDataPath);
            String name = cursor.getString(indexDisplayName);
            String singer=cursor.getString(indexSingerAlbum);

            ItemAlbum itemAlbum=new ItemAlbum(name,path,singer);

            itemAlbums.add(itemAlbum);
            cursor.moveToNext();

        }
        cursor.close();
        Log.d("aaaaaaaaaaaaaaaaaaaaaaa", "getAllAlbum: "+itemAlbums.size());
        return itemAlbums;
    }


    public List<ItemAlbum> getAllListMusicInAlbum() {
        List<ItemAlbum> itemAlbums=new ArrayList<>();
//        Cursor cursor = getApplicationContext().getContentResolver().query(
//                MediaStore.Audio.Albums.getContentUri("external"),
//                new String[] {
//                        MediaStore.Audio.Albums.ARTIST,
//                        MediaStore.Audio.Albums._ID,
//                        MediaStore.Audio.Albums.NUMBER_OF_SONGS,
//                        MediaStore.Audio.Albums.ALBUM},
//                null, null,
//                MediaStore.Audio.Albums.ALBUM + " ASC");
        Cursor cursor = getContentResolver().query(MediaStore.Audio.Albums.EXTERNAL_CONTENT_URI,
                null, null, null, null);
        String dataPath = "album_art";
        int indexDataPath = cursor.getColumnIndex(dataPath);
        String nameAlbum = "artist";
        int indexDisplayName = cursor.getColumnIndex(nameAlbum);
        String singerAlbum = "numsongs";
        int indexSingerAlbum = cursor.getColumnIndex(singerAlbum);
        cursor.moveToFirst();



        while (!cursor.isAfterLast()) {
            String path = cursor.getString(indexDataPath);
            String name = cursor.getString(indexDisplayName);
            String singer=cursor.getString(indexSingerAlbum);

            ItemAlbum itemAlbum=new ItemAlbum(name,path,singer);

            itemAlbums.add(itemAlbum);
            cursor.moveToNext();

        }
        cursor.close();
        Log.d("aaaaaaaaaaaaaaaaaaaaaaa", "getAllAlbum: "+itemAlbums.size());
        return itemAlbums;
    }

    public List<ItemArtist> getAllArtist() {
        List<ItemArtist> itemArtists=new ArrayList<>();
//        Cursor cursor = getApplicationContext().getContentResolver().query(
//                MediaStore.Audio.Albums.getContentUri("external"),
//                new String[] {
//                        MediaStore.Audio.Albums.ARTIST,
//                        MediaStore.Audio.Albums._ID,
//                        MediaStore.Audio.Albums.NUMBER_OF_SONGS,
//                        MediaStore.Audio.Albums.ALBUM},null, null,
//                MediaStore.Audio.Albums.ALBUM + " ASC");
        Cursor cursor = getContentResolver().query(MediaStore.Audio.Artists.EXTERNAL_CONTENT_URI,
                null, null, null, null);
        String dataPath = "artist";
        int indexDataPath = cursor.getColumnIndex(dataPath);
        String nameAlbum = "number_of_albums";
        int indexDisplayName = cursor.getColumnIndex(nameAlbum);
//        String singerAlbum = "artist";
//        int indexSingerAlbum = cursor.getColumnIndex(singerAlbum);
        cursor.moveToFirst();



        while (!cursor.isAfterLast()) {
            String path = cursor.getString(indexDataPath);
            String name = cursor.getString(indexDisplayName);
//            String singer=cursor.getString(indexSingerAlbum);

            ItemArtist itemArtist=new ItemArtist(name,path);

            itemArtists.add(itemArtist);
            cursor.moveToNext();

        }
        cursor.close();

        return itemArtists;
    }

    public static class MyBinder extends Binder {
        private ServiceMusic serviceMusic;

        public MyBinder(ServiceMusic serviceMusic) {
            this.serviceMusic = serviceMusic;
        }

        public ServiceMusic getServiceMusic() {
            return serviceMusic;
        }
    }

    public void continueSong() {
        musicManager.start();
    }

    @Override
    public void onCompletion(MediaPlayer mp) {
        currentPosition = (currentPosition + 1) % itemMusics.size();
        musicManager.updatePath(itemMusics.get(currentPosition).getPath(), this);

        musicManager.start();
        for (String s : listCompleted.keySet()) {
            listCompleted.get(s).call(musicManager.getMedia());
        }
    }

    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
    public void playMusic(int position) {

        musicManager.updatePath(itemMusics.get(position).getPath(), this);
        musicManager.start();
        currentPosition = position;
        showNotification(position);
    }


    public boolean isPlaying() {
        if (musicManager.isPlaying()) {
            return true;
        }
        return false;
    }

    private Notification status;
    private final String LOG_TAG = "NotificationService";

    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
    private void showNotification(int position) {
// Using RemoteViews to bind custom layouts into Notification
        RemoteViews views = new RemoteViews(getPackageName(), R.layout.big_notify);
        views.setImageViewBitmap(R.id.iv_img_notify, Constants.getDefaultAlbumArt(this));

// showing default album image
        views.setViewVisibility(R.id.iv_img_notify, View.VISIBLE);

        Intent notificationIntent = new Intent(this, MainActivity.class);
        notificationIntent.setAction(Constants.ACTION.MAIN_ACTION);
        notificationIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0, notificationIntent, 0);

        Intent previousIntent = new Intent(this, ServiceMusic.class);
        previousIntent.setAction(Constants.ACTION.PREV_ACTION);
        PendingIntent ppreviousIntent = PendingIntent.getService(this, 0, previousIntent, 0);

        Intent playIntent = new Intent(this, ServiceMusic.class);
        playIntent.setAction(Constants.ACTION.PLAY_ACTION);
        PendingIntent pplayIntent = PendingIntent.getService(this, 0, playIntent, 0);

        Intent nextIntent = new Intent(this, ServiceMusic.class);
        nextIntent.setAction(Constants.ACTION.NEXT_ACTION);
        PendingIntent pnextIntent = PendingIntent.getService(this, 0, nextIntent, 0);

//        Intent closeIntent = new Intent(this, ServiceMusic.class);
//        closeIntent.setAction(Constants.ACTION.STOPFOREGROUND_ACTION);
//        PendingIntent pcloseIntent = PendingIntent.getService(this, 0, closeIntent, 0);

        views.setOnClickPendingIntent(R.id.btn_play_notify, pplayIntent);

        views.setOnClickPendingIntent(R.id.btn_next_notify, pnextIntent);

        views.setOnClickPendingIntent(R.id.btn_back_notify, ppreviousIntent);

//        views.setOnClickPendingIntent(R.id.btn_close_notify, pcloseIntent);

        views.setImageViewResource(R.id.btn_play_notify, R.drawable.ic_pause_black_24dp);

        views.setTextViewText(R.id.tv_name_notify, itemMusics.get(position).getName());

        views.setTextViewText(R.id.tv_singer_notify, itemMusics.get(position).getSinger());

        status = new NotificationCompat.Builder(this).build();
        status.contentView = views;
        status.bigContentView = views;
        status.flags = Notification.FLAG_ONGOING_EVENT;
        status.icon = R.drawable.ic_album_black_24dp;
        status.contentIntent = pendingIntent;
        startForeground(Constants.NOTIFICATION_ID.FOREGROUND_SERVICE, status);
    }



}
