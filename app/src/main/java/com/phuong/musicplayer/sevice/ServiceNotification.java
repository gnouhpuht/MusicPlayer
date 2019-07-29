package com.phuong.musicplayer.sevice;


import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.BitmapFactory;
import android.media.MediaPlayer;
import android.media.session.MediaController;
import android.media.session.MediaSession;
import android.media.session.MediaSessionManager;
import android.os.Binder;
import android.os.Build;
import android.os.IBinder;
import android.provider.MediaStore;

import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;

import com.phuong.musicplayer.R;
import com.phuong.musicplayer.inter_.Action1;
import com.phuong.musicplayer.model.ItemMusic;
import com.phuong.musicplayer.musicmanager.MusicManager;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


public class ServiceNotification extends Service implements MediaPlayer.OnCompletionListener {
    private List<ItemMusic> itemMusics;
    private MusicManager musicManager;
    private int currentPosition;
    private Map<String, Action1<MediaPlayer>> listCompleted;

    private MediaSession mSession;
    public static final String ACTION_PLAY="action_play";
    public static final String ACTION_PAUSE="action_pause";
    public static final String ACTION_REWIND="action_rewind";
    public static final String ACTION_PAST_FORWARD="action_past_forward";
    public static final String ACTION_NEXT="action_next";
    public static final String ACTION_PREVIOUS="action_previous";
    public static final String ACTION_STOP="action_stop";

    private MediaPlayer mMediaPlayer;
    private MediaSessionManager mManager;
    private MediaController mController;



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
    public void onCreate() {
        itemMusics=getAllMusic();
        musicManager=new MusicManager();
        listCompleted=new HashMap<>();
//        mSession = new MediaSession(this, "MusicService");
//        mSession.setCallback(new MediaSession.Callback() {
//
//        });
//        mSession.setFlags(MediaSession.FLAG_HANDLES_MEDIA_BUTTONS | MediaSession.FLAG_HANDLES_TRANSPORT_CONTROLS);
        getAllMusic();

        super.onCreate();
    }
    public int getDuration() {
        return musicManager.getDuration();
    }

    public int getCurrentPosition() {
        return currentPosition;
    }
    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        if (mManager==null){
            initMediaSession();
        }
        handleIntent(intent);
        return super.onStartCommand(intent, flags, startId);
//        return START_NOT_STICKY;
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
        mMediaPlayer=new MediaPlayer();
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
                Intent intent=new Intent(getApplicationContext(),ServiceNotification.class);
                stopService(intent);
            }
        });

    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return new MyBinder(this) ;
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
        Intent intent=new Intent(getApplicationContext(), ServiceNotification.class);
        intent.setAction(ACTION_PLAY);
        PendingIntent pendingIntent=PendingIntent.getService(getApplicationContext(),1,intent,0);
        Notification.Builder builder=new Notification.Builder(this)
                .setSmallIcon(R.drawable.ic_music_note_black_24dp)
                .setOngoing(true)
                .setLargeIcon(BitmapFactory.decodeResource(getResources(), R.drawable.img))
                .setContentTitle(itemMusics.get(getCurrentPosition()).getName())
                .setContentText(itemMusics.get(getCurrentPosition()).getSinger())
                .setContentIntent(pendingIntent)
                .setUsesChronometer(false)
                .setStyle(style);

        builder.addAction(generateAction(R.drawable.ic_skip_previous_black_24dp,"Previous",ACTION_PREVIOUS));
        builder.addAction(generateAction(R.drawable.ic_play_arrow_black_24dp,"Play", ACTION_PLAY));
        builder.addAction(generateAction(R.drawable.ic_skip_next_black_24dp,"Play", ACTION_NEXT));
        builder.addAction(action);
//        builder.addAction(generateAction(R.drawable.ic_fast_forward_black_24dp,"Previous",ACTION_PAST_FORWARD));
//        builder.addAction(generateAction(R.drawable.ic_skip_next_black_24dp,"Rewind", ACTION_NEXT));
        style.setShowActionsInCompactView(0,1,2,3);
        NotificationManager notificationManager=(NotificationManager) getSystemService(NOTIFICATION_SERVICE);
        notificationManager.notify(1,builder.build());
    }

    public List<ItemMusic> getAllMusic(){
        List<ItemMusic> itemMusics=new ArrayList<>();
        Cursor cursor=getContentResolver().query(MediaStore.Audio.Media.EXTERNAL_CONTENT_URI,
                null,null,null,null);

        String dataPath="_data";
        int indexDataPath=cursor.getColumnIndex(dataPath);
        String displayName="_display_name";
        int indexDisplayName=cursor.getColumnIndex(displayName);
        String duration="duration";
        int indexDuration=cursor.getColumnIndex(duration);
        String artist="artist";
        int indexArtist=cursor.getColumnIndex(artist);
        cursor.moveToFirst();


        while (!cursor.isAfterLast()){
            String path=cursor.getString(indexDataPath);
            String name=cursor.getString(indexDisplayName);
            String artistData=cursor.getString(indexArtist);
            long durationData=cursor.getLong(indexDuration);

            ItemMusic itemMusic=new ItemMusic();
            itemMusic.setPath(path);
            itemMusic.setName(name);
            itemMusic.setSinger(artistData);
            itemMusic.setDuration(durationData);
            itemMusics.add(itemMusic);
            cursor.moveToNext();

        }
        cursor.close();
        return itemMusics;
    }


    public List<ItemMusic> getItemMusic() {
        return itemMusics;
    }

    public void play(int position) {
        musicManager.updatePath(itemMusics.get(position).getPath(),this);
        musicManager.start();
        currentPosition=position;
    }



    public static class MyBinder extends Binder {
        private ServiceNotification serviceMusic;

        public MyBinder(ServiceNotification serviceMusic) {
            this.serviceMusic = serviceMusic;
        }

        public ServiceNotification getServiceMusic() {
            return serviceMusic;
        }
    }

    public void continueSong() {
        musicManager.start();
    }
    @Override
    public void onCompletion(MediaPlayer mp) {
        currentPosition=(currentPosition+1)%itemMusics.size();
        musicManager.updatePath(itemMusics.get(currentPosition).getPath(),this);;
        musicManager.start();
        for (String s : listCompleted.keySet()) {
            listCompleted.get(s).call(musicManager.getMedia());
        }
    }
    public void playMusic(int position) {

        musicManager.updatePath(
                itemMusics.get(position).getPath(), this);
        musicManager.start();
        currentPosition = position;
    }
}
