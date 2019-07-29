package com.phuong.musicplayer;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.content.Intent;
import android.os.Bundle;

import com.phuong.musicplayer.fragment.FragmentHome;
import com.phuong.musicplayer.fragment.FragmentPlay;
import com.phuong.musicplayer.musicmanager.MusicManager;
import com.phuong.musicplayer.sevice.ServiceNotification;
import com.phuong.musicplayer.sevice.ServicePlayMusic;

public class MainActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        inits();

        Intent intent=new Intent(getApplicationContext(), ServiceNotification.class);
        intent.setAction(ServiceNotification.ACTION_PLAY);
        startService(intent);
    }
    private void inits() {
        FragmentManager manager = getSupportFragmentManager();
        FragmentTransaction transaction = manager.beginTransaction();
        Fragment fragment = manager.findFragmentByTag(FragmentPlay.class.getName());
        if (fragment !=null){
            transaction.remove(fragment);
        }
        FragmentHome fragmentHome = new FragmentHome();
        transaction.add(R.id.fl_home, fragmentHome, FragmentHome.class.getName());
        transaction.commit();
    }

    public void createPlaying(MusicManager musicManager, int position){
        FragmentManager manager = getSupportFragmentManager();
        FragmentTransaction transaction = manager.beginTransaction();
        Fragment fragment = manager.findFragmentByTag(FragmentHome.class.getName());
        FragmentPlay fragmentPlay = new FragmentPlay();
        fragmentPlay.getMusicManager(musicManager,position);
        transaction.hide(fragment);
        transaction.add(R.id.fl_home, fragmentPlay, FragmentPlay.class.getName());
        transaction.addToBackStack(null);
        transaction.commit();
    }


}
