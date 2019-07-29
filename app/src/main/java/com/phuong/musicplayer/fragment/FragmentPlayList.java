package com.phuong.musicplayer.fragment;

import android.annotation.SuppressLint;

import android.content.ServiceConnection;
import android.os.Bundle;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.phuong.musicplayer.R;

import com.phuong.musicplayer.adapter.AdapterPlaylist;
import com.phuong.musicplayer.controll.DialogPlaylist;

import com.phuong.musicplayer.sevice.ServicePlayMusic;


public class FragmentPlayList extends Fragment  {
    private RecyclerView rcPlaylist;
    private ServicePlayMusic servicePlayMusic;
    private ServiceConnection connection;
    private AdapterPlaylist adapter;
    private Button createPlaylist;

    @SuppressLint("WrongConstant")
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view=inflater.inflate(R.layout.fragment_playlist,container,false);
        rcPlaylist = view.findViewById(R.id.rc_playlist);
        rcPlaylist.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false));
//        adapter=new AdapterPlaylist(this);
        rcPlaylist.setAdapter(adapter);

        createPlaylist=view.findViewById(R.id.btn_playlist);
        createPlaylist.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openDialog();
            }
        });
//        createConnectService();
        return view;
    }

    private void openDialog() {
        DialogPlaylist dialogPlaylist=new DialogPlaylist();
        dialogPlaylist.show(getActivity().getSupportFragmentManager(),"playlist dialog");
    }




//    private void createConnectService() {
//        connection = new ServiceConnection() {
//            @Override
//            public void onServiceConnected(ComponentName name, IBinder service) {
//                ServicePlayMusic.MyBinder myBinder = (ServicePlayMusic.MyBinder) service;
//                servicePlayMusic = myBinder.getServicePlayMusic();
//                rcPlaylist.getAdapter().notifyDataSetChanged();
//            }
//
//            @Override
//            public void onServiceDisconnected(ComponentName name) {
//
//            }
//        };
//
//        Intent intent = new Intent();
//        intent.setClass(getContext(), ServicePlayMusic.class);
//        getContext().bindService(intent, connection, Context.BIND_AUTO_CREATE);
//    }


//    @Override
//    public void applyTexts(String createPlaylist) {
//
//    }

//    @Override
//    public int getCountPlaylist() {
//        if (servicePlayMusic==null){
//            return 0;
//        }
//        return servicePlayMusic.getAllPlaylist().size();
//    }
//
//    @Override
//    public ItemPlaylist getDataAlbum(int position) {
//        return servicePlayMusic.getAllPlaylist().get(position);
//    }
//
//    @Override
//    public void onClick(int position) {
//
//    }
}
