package com.phuong.musicplayer.musicmanager;

import android.media.AudioManager;
import android.media.MediaPlayer;
import android.util.Log;
import android.widget.MediaController;
import java.io.IOException;


public class MusicManager implements MediaController.MediaPlayerControl {
    private MediaPlayer mediaPlayer;
    private int currentPosition;


    public MusicManager() {

    }



    public void updatePath(String path, MediaPlayer.OnCompletionListener cmp){
        if (mediaPlayer!=null){
            mediaPlayer.release();
        }
        mediaPlayer=new MediaPlayer();
        try {
            mediaPlayer.setDataSource(path);
            mediaPlayer.setOnCompletionListener(cmp);
            mediaPlayer.prepare();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    public void start(){
        if (mediaPlayer==null){
            return;
        }
        mediaPlayer.start();
    }

    public void pause (){
        if (mediaPlayer==null){
            return;
        }
        mediaPlayer.pause();
    }
    public void stop(){
        if (mediaPlayer==null){
            return;
        }
        mediaPlayer.stop();
    }
    public void release(){
        if (mediaPlayer==null){
            return;
        }
        mediaPlayer.release();
    }


    public int getCurrent(){
        if (mediaPlayer==null){
            return 0;
        }
        return  mediaPlayer.getCurrentPosition();
    }
    //thời lượng phát
    public int getDuration(){
        if (mediaPlayer==null){
            return 0;
        }
        return mediaPlayer.getDuration();
    }

    @Override
    public int getCurrentPosition() {
        if (mediaPlayer==null){
            return 0;
        }
        return mediaPlayer.getCurrentPosition();
    }

    @Override
    public void seekTo(int pos) {
        if (mediaPlayer!=null)
        mediaPlayer.seekTo(pos);
    }

    @Override
    public boolean isPlaying() {
        if (mediaPlayer.isPlaying()){
            return false;
        }
        return true;
    }

    @Override
    public int getBufferPercentage() {
        return 0;
    }

    @Override
    public boolean canPause() {
        return false;
    }

    @Override
    public boolean canSeekBackward() {
        return false;
    }

    @Override
    public boolean canSeekForward() {
        return false;
    }

    @Override
    public int getAudioSessionId() {
        return 0;
    }

    public void playMusic(int position, String url) {
        updateUrl(url);
        currentPosition = position;
    }
    public MediaPlayer getMedia(){
        return mediaPlayer;
    }

    public void updateUrl(String url){
        if (mediaPlayer!=null&& mediaPlayer.isPlaying()){
            mediaPlayer.release();
        }
        Log.e("ssssđfsg","đagđgg:         "+url);
        mediaPlayer=new MediaPlayer();
        mediaPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);
        try {
            mediaPlayer.setDataSource(url);
            mediaPlayer.prepareAsync();
            mediaPlayer.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
                @Override
                public void onPrepared(MediaPlayer mp) {
                    mediaPlayer.start();
                }
            });
        } catch (IOException e) {
            e.printStackTrace();
        }
    }



}
