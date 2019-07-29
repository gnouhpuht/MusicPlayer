package com.phuong.musicplayer.fragment;

import android.annotation.SuppressLint;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.IBinder;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.Filter;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.phuong.musicplayer.R;
import com.phuong.musicplayer.adapter.AdapterMusic;
import com.phuong.musicplayer.model.ItemArtist;
import com.phuong.musicplayer.model.ItemMusic;
import com.phuong.musicplayer.inter_.IMusic;
import com.phuong.musicplayer.sevice.ServiceSearchMusic;

import java.util.ArrayList;

public class FragmentSearch extends Fragment implements IMusic {
    private ServiceSearchMusic serviceSearchMusic;
    private RecyclerView rcMusic;
    private AdapterMusic adapter;
    private ServiceConnection connection;
    private EditText etSearch;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @SuppressLint("WrongConstant")
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view=inflater.inflate(R.layout.fragment_search,container,false);
        rcMusic = view.findViewById(R.id.rc_music_search);
        rcMusic.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false));
        serviceSearchMusic= ServiceSearchMusic.getInstance();
        adapter=new AdapterMusic(serviceSearchMusic.getAllMusic(), this);
        rcMusic.setAdapter(adapter);
        etSearch=view.findViewById(R.id.et_search);
        etSearch.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                adapter.getFilter().filter(etSearch.getText().toString(), new Filter.FilterListener() {
                    @Override
                    public void onFilterComplete(int i) {
                        adapter.notifyDataSetChanged();
                    }
                });

            }
        });
        createConnectService();
        return view;
    }



    public void updateListMusic(ArrayList<ItemMusic> musicArrayList){
        adapter.updateMusic(musicArrayList);
    }








    @Override
    public int getCountItem() {
        if (serviceSearchMusic == null) {
            return 0;
        }
        return serviceSearchMusic.getAllMusic().size();
    }

    @Override
    public ItemMusic getData(int position) {
        return serviceSearchMusic.getAllMusic().get(position);
    }

    @Override
    public void onClick(int position) {

    }

    private void createConnectService() {
        connection = new ServiceConnection() {
            @Override
            public void onServiceConnected(ComponentName name, IBinder service) {
                ServiceSearchMusic.MyBinder myBinder = (ServiceSearchMusic.MyBinder) service;
                serviceSearchMusic = myBinder.getServiceSearchMusic();
                rcMusic.getAdapter().notifyDataSetChanged();
            }

            @Override
            public void onServiceDisconnected(ComponentName name) {

            }
        };

        Intent intent = new Intent();
        intent.setClass(getContext(), ServiceSearchMusic.class);
        getContext().bindService(intent, connection, Context.BIND_AUTO_CREATE);
    }
}
