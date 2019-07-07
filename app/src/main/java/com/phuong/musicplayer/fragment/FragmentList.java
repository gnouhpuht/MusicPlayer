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


import com.phuong.musicplayer.Constants;
import com.phuong.musicplayer.MainActivity;
import com.phuong.musicplayer.R;
import com.phuong.musicplayer.adapter.AdapterMusic;
import com.phuong.musicplayer.component.ItemMusic;
import com.phuong.musicplayer.inter_.IMusic;
import com.phuong.musicplayer.service.ServiceMusic;



public class FragmentList extends Fragment implements IMusic {
    private RecyclerView rcMusic;
    private ServiceMusic serviceMusic;
    private ServiceConnection connection;
    private AdapterMusic adapter;

    @SuppressLint("WrongConstant")
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_list, container, false);
        rcMusic = view.findViewById(R.id.rc_music);
        rcMusic.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false));
        adapter=new AdapterMusic(this);
        rcMusic.setAdapter(adapter);

        startStart();
        createConnectService();
        return view;
    }



    private void startStart() {
        Intent intent=new Intent();
        intent.setClass(getContext(),ServiceMusic.class);
        intent.setAction(Constants.ACTION.STARTFOREGROUND_ACTION);
        getContext().startService(intent);
//        Intent serviceIntent = new Intent(FragmentList.this, ServiceMusic.class);
//        serviceIntent.setAction(Constants.ACTION.STARTFOREGROUND_ACTION);
//        startService(serviceIntent);
    }

    private void createConnectService() {
        connection = new ServiceConnection() {
            @Override
            public void onServiceConnected(ComponentName name, IBinder service) {
                ServiceMusic.MyBinder myBinder = (ServiceMusic.MyBinder) service;
                serviceMusic = myBinder.getServiceMusic();
                rcMusic.getAdapter().notifyDataSetChanged();
            }

            @Override
            public void onServiceDisconnected(ComponentName name) {

            }
        };

        Intent intent = new Intent();
        intent.setClass(getContext(), ServiceMusic.class);
        getContext().bindService(intent, connection, Context.BIND_AUTO_CREATE);
    }

    @Override
    public int getCountItem() {
        if (serviceMusic == null) {
            return 0;
        }
        return serviceMusic.getAllMusic().size();

    }

    @Override
    public ItemMusic getData(int position) {
        return serviceMusic.getAllMusic().get(position);
    }

    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
    @Override
    public void onClick(int position) {
        serviceMusic.playMusic(position);

        FragmentActivity ac = getActivity();
        ((MainActivity) ac).createPlaying(serviceMusic.getMusicManager(), position);
    }
}
