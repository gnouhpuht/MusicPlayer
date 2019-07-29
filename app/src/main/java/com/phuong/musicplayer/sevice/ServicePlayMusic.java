package com.phuong.musicplayer.sevice;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.media.MediaPlayer;
import android.media.session.MediaController;
import android.media.session.MediaSession;
import android.media.session.MediaSessionManager;
import android.os.Binder;
import android.os.Build;
import android.os.IBinder;
import android.provider.MediaStore;
import android.provider.Settings;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.RequiresApi;
import androidx.core.app.NotificationCompat;

import com.phuong.musicplayer.R;
import com.phuong.musicplayer.model.ItemAlbum;
import com.phuong.musicplayer.model.ItemArtist;
import com.phuong.musicplayer.model.ItemMusic;
import com.phuong.musicplayer.inter_.Action1;
import com.phuong.musicplayer.model.ItemPlaylist;
import com.phuong.musicplayer.musicmanager.MusicManager;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ServicePlayMusic extends Service implements MediaPlayer.OnCompletionListener {
    private MusicManager musicManager;
    private int currentPosition;
    List<ItemMusic> itemMusics;
    private Map<String, Action1<MediaPlayer>> listCompleted;

    private MediaSession mSession;
    public static final String ACTION_PLAY="action_play";
    public static final String ACTION_PAUSE="action_pause";
    public static final String ACTION_REWIND="action_rewind";
    public static final String ACTION_PAST_FORWARD="action_past_forward";
    public static final String ACTION_NEXT="action_next";
    public static final String ACTION_PREVIOUS="action_previous";
    public static final String ACTION_STOP="action_stop";

    private MediaPlayer mediaPlayer;
    private MediaSessionManager mManager;
    private MediaController mController;

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    public void onCreate() {
        musicManager=new MusicManager();
        listCompleted = new HashMap<>();
        itemMusics=getAllMusic();

        super.onCreate();
    }

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

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        if (mManager==null){
            initMediaSession();
            Toast.makeText(this, "hiên thị",   Toast.LENGTH_SHORT).show();
        }
        handleIntent(intent);
        return super.onStartCommand(intent, flags, startId);
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    private void handleIntent(Intent intent){
        if (intent==null||intent.getAction()==null){
            return;
        }

        String action=intent.getAction();
        if (action.equalsIgnoreCase(ACTION_PLAY)){
            mController.getTransportControls().play();
        }else if (action.equalsIgnoreCase(ACTION_PAUSE)){
            mController.getTransportControls().pause();
        }else if (action.equalsIgnoreCase(ACTION_PAST_FORWARD)){
            mController.getTransportControls().fastForward();
        }else if (action.equalsIgnoreCase(ACTION_REWIND)){
            mController.getTransportControls().rewind();
        }else if (action.equalsIgnoreCase(ACTION_NEXT)){
            mController.getTransportControls().skipToNext();
        }else if (action.equalsIgnoreCase(ACTION_PREVIOUS)){
            mController.getTransportControls().skipToPrevious();
        }else if (action.equalsIgnoreCase(ACTION_STOP)){
            mController.getTransportControls().stop();
        }
    }


    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    private void initMediaSession() {
        mediaPlayer=new MediaPlayer();
        mSession = new MediaSession(getApplicationContext(), "example player session");
        mController=new MediaController(getApplicationContext(),mSession.getSessionToken());

        mSession.setCallback(new MediaSession.Callback() {
            @Override
            public void onPlay() {
                super.onPlay();
                musicManager.start();
                buildNotification(generateAction(R.drawable.ic_pause_black_24dp,"Pause",ACTION_PAUSE));
            }

            @Override
            public void onPause() {
                musicManager.pause();
                super.onPause();
                buildNotification(generateAction(R.drawable.ic_play_arrow_black_24dp,"Play",ACTION_PLAY));
            }


            @Override
            public void onSkipToNext() {
                super.onSkipToNext();

                buildNotification(generateAction(R.drawable.ic_pause_black_24dp,"Pause",ACTION_PAUSE));
            }

            @Override
            public void onFastForward() {
                super.onFastForward();
            }

            @Override
            public void onRewind() {
                super.onRewind();
            }

            @Override
            public void onStop() {
                super.onStop();
                NotificationManager notificationManager=(NotificationManager) getApplicationContext().getSystemService(NOTIFICATION_SERVICE);
                notificationManager.cancel(1);
                Intent intent=new Intent(getApplicationContext(),ServicePlayMusic.class);
                stopService(intent);
            }
        });

    }
    @Override
    public IBinder onBind(Intent intent) {

        return new MyBinder(this);
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    public boolean onUnbind(Intent intent) {
        mSession.release();
        return super.onUnbind(intent);
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    private Notification.Action generateAction(int icon, String title, String intentAction){
        Intent intent=new Intent(getApplicationContext(),MediaSessionManager.class);
        intent.setAction(intentAction);
        PendingIntent pendingIntent=PendingIntent.getService(getApplicationContext(),1,intent,0);
        return new Notification.Action.Builder(icon,title,pendingIntent).build();
    }



    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    private void buildNotification(Notification.Action action){

        Notification.MediaStyle style=new Notification.MediaStyle();
        Intent intent=new Intent(getApplicationContext(),ServicePlayMusic.class);
        intent.setAction(ACTION_PLAY);
        PendingIntent pendingIntent=PendingIntent.getService(getApplicationContext(),1,intent,0);
        Notification.Builder builder=new Notification.Builder(this)
                .setSmallIcon(R.drawable.ic_music_note_black_24dp)
                .setOngoing(true)
                .setLargeIcon(BitmapFactory.decodeResource(getResources(), R.drawable.img))
                .setContentTitle(itemMusics.get(getCurrentPosition()).getName())
                .setContentText(itemMusics.get(getCurrentPosition()).getSinger())
                .setContentIntent(pendingIntent)
                .setAutoCancel(true)
                .setUsesChronometer(false)
                .setStyle(style);

        builder.addAction(generateAction(R.drawable.ic_skip_previous_black_24dp,"Previous",ACTION_PREVIOUS));
        builder.addAction(generateAction(R.drawable.ic_play_arrow_black_24dp,"Play", ACTION_PLAY));
        builder.addAction(generateAction(R.drawable.ic_skip_next_black_24dp,"Play", ACTION_NEXT));
        builder.addAction(action);
        style.setShowActionsInCompactView(0,1,2,3);
        NotificationManager notificationManager=(NotificationManager) getSystemService(NOTIFICATION_SERVICE);
        if (Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O)
        {
            String channelId = "Your_channel_id";
            NotificationChannel channel = new NotificationChannel(
                    channelId,
                    "Channel human readable title", NotificationManager.IMPORTANCE_DEFAULT
                    );
            notificationManager.createNotificationChannel(channel);
            builder.setChannelId(channelId);
        }
//        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
//            String chanel_id = "3000";
//            CharSequence name = "Channel Name";
//            String description = "Chanel Description";
//            int importance = NotificationManager.IMPORTANCE_LOW;
//            NotificationChannel mChannel = new NotificationChannel(chanel_id, name, importance);
//            mChannel.setDescription(description);
//            mChannel.enableLights(true);
//            mChannel.setLightColor(Color.BLUE);
//            notificationManager.createNotificationChannel(mChannel);
//            builder = new Notification.Builder(this, chanel_id);
//        } else {
//            notificationManager.notify(0, builder.build());
//        }
        Toast.makeText(this, "hiên thị",   Toast.LENGTH_SHORT).show();
    }

    public void playMusic(int position) {

        musicManager.updatePath(itemMusics.get(position).getPath(), this);
        musicManager.start();
        currentPosition = position;
    }

    public void pause() {
        musicManager.pause();
    }

    public void stop() {
        musicManager.stop();

    }

    public void continueSong() {
        musicManager.start();
    }

    public boolean isPlaying() {
        if (musicManager.isPlaying()) {
            return true;
        }
        return false;
    }

    public int getDuration() {
        return musicManager.getDuration();
    }

    public void seekTo(int pos) {
        musicManager.seekTo(pos);
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



    public int getCurrentPosition() {
        return currentPosition;
    }


    public List<ItemMusic> getAllMusic() {
        List<ItemMusic> itemMusics = new ArrayList<>();
        Cursor cursor;
//        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N && !Settings.System.canWrite(getBaseContext())) {
//           cursor=getContentResolver().query(MediaStore.Audio.Media.EXTERNAL_CONTENT_URI,
//                    null, null, null, null);
//        }else
            cursor = getContentResolver().query(MediaStore.Audio.Media.EXTERNAL_CONTENT_URI,
                null, null, null, null);
//        cursor.getColumnNames();
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

    public List<ItemAlbum> getAllAlbum() {
        List<ItemAlbum> itemAlbums = new ArrayList<>();
        Cursor cursor = getContentResolver().query(MediaStore.Audio.Albums.EXTERNAL_CONTENT_URI,
                null, null, null, null);
        String nameAlbum = "album";
        int indexNameAlbum = cursor.getColumnIndex(nameAlbum);
        String nameArtist = "artist";
        int indexNameArtist = cursor.getColumnIndex(nameArtist);
        String numSong = "numsongs";
        int indexNumberSong= cursor.getColumnIndex(numSong);
        cursor.moveToFirst();


        while (!cursor.isAfterLast()) {
            String nameAlbums = cursor.getString(indexNameAlbum);
            String nameArtists = cursor.getString(indexNameArtist);
            String numberSong = cursor.getString(indexNumberSong);

            ItemAlbum itemAlbum = new ItemAlbum(nameAlbums, nameArtists, numberSong);
            Log.d("ssssssssss", "getAllAlbum: "+numberSong);
            itemAlbums.add(itemAlbum);
            cursor.moveToNext();

        }
        cursor.close();
        Log.d("aaaaaaaaaaaaaaaaaaaaaaa", "getAllAlbum: " + itemAlbums.size());
        return itemAlbums;
    }
    public List<ItemMusic> getAllListMusicInAlbum() {
        List<ItemMusic> itemMusicsAlbum = new ArrayList<>();
        String selection = MediaStore.Audio.Media.ALBUM_ID + "="+MediaStore.Audio.Albums.ALBUM_ID;


        Cursor cursor = getContentResolver().query(MediaStore.Audio.Media.EXTERNAL_CONTENT_URI,
               null, selection, null, null);

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
            itemMusicsAlbum.add(itemMusic);
            cursor.moveToNext();

        }
        cursor.close();
        Log.d("aaaaaaaaaaaaaaaaaaaaaaa", "getAllAlbum: " + itemMusicsAlbum.size());
        return itemMusicsAlbum;
    }
    public List<ItemArtist> getAllArtist() {
        List<ItemArtist> itemArtists = new ArrayList<>();
        Cursor cursor = getContentResolver().query(MediaStore.Audio.Albums.EXTERNAL_CONTENT_URI,
                null,
                null,
                null,
                null);
        String dataPath = "artist";
        int indexDataPath = cursor.getColumnIndex(dataPath);
        String nameAlbum = "artist";
        int indexDisplayName = cursor.getColumnIndex(nameAlbum);
        String singerAlbum = "numsongs";
        int indexSingerAlbum = cursor.getColumnIndex(singerAlbum);
        cursor.moveToFirst();


        while (!cursor.isAfterLast()) {
            String path = cursor.getString(indexDataPath);
            String name = cursor.getString(indexDisplayName);
            String singer = cursor.getString(indexSingerAlbum);

            ItemArtist itemArtist = new ItemArtist(name, path, singer);

            itemArtists.add(itemArtist);
            cursor.moveToNext();

        }
        cursor.close();

        return itemArtists;
    }
    public List<ItemMusic> getAllListMusicInArtist() {
        List<ItemMusic> itemMusicsAlbum = new ArrayList<>();
        String selection = MediaStore.Audio.Media.ALBUM_ID + "="+MediaStore.Audio.Albums.ALBUM_ID;


        Cursor cursor = getContentResolver().query(MediaStore.Audio.Media.EXTERNAL_CONTENT_URI,
                null, selection, null, null);

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
            itemMusicsAlbum.add(itemMusic);
            cursor.moveToNext();

        }
        cursor.close();
        Log.d("aaaaaaaaaaaaaaaaaaaaaaa", "getAllAlbum: " + itemMusicsAlbum.size());
        return itemMusicsAlbum;
    }
    public List<ItemPlaylist> getAllPlaylist() {
        List<ItemPlaylist> playlists = new ArrayList<>();
        Cursor cursor = getContentResolver().query(MediaStore.Audio.Playlists.EXTERNAL_CONTENT_URI,
                null,
                null,
                null,
                null);
        String data = "_data";
        int indexData = cursor.getColumnIndex(data);
        String namePlaylist = "name";
        int indexDisplayName = cursor.getColumnIndex(namePlaylist);
        cursor.moveToFirst();


        while (!cursor.isAfterLast()) {
            String path = cursor.getString(indexData);
            String name = cursor.getString(indexDisplayName);

            ItemPlaylist playlist=new ItemPlaylist(path, name);

            playlists.add(playlist);
            cursor.moveToNext();

        }
        cursor.close();

        return playlists;
    }
    public static class MyBinder extends Binder{
        private ServicePlayMusic servicePlayMusic;

        public MyBinder(ServicePlayMusic servicePlayMusic) {
            this.servicePlayMusic = servicePlayMusic;
        }

        public ServicePlayMusic getServicePlayMusic() {
            return servicePlayMusic;
        }
    }
}
