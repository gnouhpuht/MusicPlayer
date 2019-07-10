package com.phuong.musicplayer.component;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.media.AudioManager;
import android.telephony.PhoneStateListener;
import android.telephony.TelephonyManager;

import com.phuong.musicplayer.MyPhoneStateListener;
import com.phuong.musicplayer.musicmanager.MusicManager;

public class ServiceReceiver extends BroadcastReceiver implements AudioManager.OnAudioFocusChangeListener{
    private TelephonyManager telephony;
    private MusicManager musicManager;
    private AudioManager am = null;

    public ServiceReceiver(MusicManager musicManager) {
        this.musicManager = musicManager;
    }
    int result = am.requestAudioFocus(this, AudioManager.STREAM_MUSIC, AudioManager.AUDIOFOCUS_GAIN);
    @Override
    public void onReceive(Context context, Intent intent) {
        MyPhoneStateListener phoneListener = new MyPhoneStateListener(musicManager);
        telephony = (TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE);
        telephony.listen(phoneListener, PhoneStateListener.LISTEN_CALL_STATE);


        if (AudioManager.ACTION_AUDIO_BECOMING_NOISY.equals(intent.getAction())) {
            musicManager.pause();
            // Pause the playback
        }
    }

    public void onDestroy() {
        telephony.listen(null, PhoneStateListener.LISTEN_NONE);
    }




    // Request focus for music stream and pass AudioManager.OnAudioFocusChangeListener
    // implementation reference



    @Override
    public void onAudioFocusChange(int focusChange)
    {
        if(focusChange == AudioManager.AUDIOFOCUS_LOSS_TRANSIENT || focusChange == AudioManager.AUDIOFOCUS_LOSS_TRANSIENT_CAN_DUCK)
        {
            if (musicManager==null){
                return;
            }
            musicManager.pause();
            // Pause
        }
        else if(focusChange == AudioManager.AUDIOFOCUS_GAIN)
        {
            musicManager.start();
            // Resume
        }
        else if(focusChange == AudioManager.AUDIOFOCUS_LOSS)
        {
            if (musicManager==null){
                return;
            }
            musicManager.release();
            // Stop or pause depending on your need
        }
        else if(result == AudioManager.AUDIOFOCUS_REQUEST_GRANTED)
        {
            // Play
            musicManager.start();
        }
    }


}