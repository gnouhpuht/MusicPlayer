package com.phuong.musicplayer.fragment;

import android.annotation.SuppressLint;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Build;
import android.os.Bundle;
import android.os.IBinder;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.phuong.musicplayer.MainActivity;
import com.phuong.musicplayer.R;
import com.phuong.musicplayer.adapter.AdapterMusic;
import com.phuong.musicplayer.inter_.IMusic;
import com.phuong.musicplayer.model.ItemMusic;
import com.phuong.musicplayer.models.Song;
import com.phuong.musicplayer.sevice.ServicePlayMusic;

import java.util.ArrayList;

public class FragmentListInArtist extends Fragment implements IMusic {
    private RecyclerView rcMusic;
    private ServicePlayMusic servicePlayMusic;
    private ServiceConnection connection;
    private AdapterMusic adapter;


    @SuppressLint("WrongConstant")
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view=LayoutInflater.from(container.getContext()).inflate(R.layout.fragment_list_in_artist,container,false);
//        rcMusic = view.findViewById(R.id.rc_list_in_artist);
//        rcMusic.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false));
//        adapter=new AdapterMusic(new ArrayList<ItemMusic>(), this);
//        rcMusic.setAdapter(adapter);

        createConnectService();
        return view;
    }

    private void createConnectService() {
        connection = new ServiceConnection() {
            @Override
            public void onServiceConnected(ComponentName name, IBinder service) {
                ServicePlayMusic.MyBinder myBinder = (ServicePlayMusic.MyBinder) service;
                servicePlayMusic = myBinder.getServicePlayMusic();
                rcMusic.getAdapter().notifyDataSetChanged();
            }

            @Override
            public void onServiceDisconnected(ComponentName name) {

            }
        };

        Intent intent = new Intent();
        intent.setClass(getContext(), ServicePlayMusic.class);
        getContext().bindService(intent, connection, Context.BIND_AUTO_CREATE);
    }

    @Override
    public int getCountItem() {
        if (servicePlayMusic==null){
            return 0;
        }
        return servicePlayMusic.getAllListMusicInArtist().size();
    }

    @Override
    public Song getData(int position) {
        return null;
//        return servicePlayMusic.getAllListMusicInArtist().get(position);
//        return null;
    }

    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
    @Override
    public void onClick(int position) {
        servicePlayMusic.playMusic(position);

        FragmentActivity ac = getActivity();
        ((MainActivity) ac).createPlaying(servicePlayMusic.getMusicManager(), position);
    }
}
