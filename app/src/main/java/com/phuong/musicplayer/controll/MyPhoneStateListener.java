package com.phuong.musicplayer.controll;

import android.telephony.PhoneStateListener;
import android.telephony.TelephonyManager;

import com.phuong.musicplayer.musicmanager.MusicManager;

public class MyPhoneStateListener extends PhoneStateListener {
    public static Boolean phoneRinging = false;
    private MusicManager musicManager;

    public MyPhoneStateListener(MusicManager musicManager) {
        this.musicManager = musicManager;
    }

    public void onCallStateChanged(int state, String incomingNumber) {

        switch (state) {
            case TelephonyManager.CALL_STATE_IDLE:
//                Log.d("DEBUG", "IDLE");
                phoneRinging = false;
                break;
            case TelephonyManager.CALL_STATE_OFFHOOK:
//                Log.d("DEBUG", "OFFHOOK");
                phoneRinging = false;
                break;
            case TelephonyManager.CALL_STATE_RINGING:
//                Log.d("DEBUG", "RINGING");
                if (musicManager==null){
                    return;
                }
                musicManager.pause();
                phoneRinging = true;
                break;
        }


    }

}
