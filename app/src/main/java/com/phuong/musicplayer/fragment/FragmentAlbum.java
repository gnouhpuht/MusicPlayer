package com.phuong.musicplayer.fragment;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.IBinder;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.phuong.musicplayer.R;
import com.phuong.musicplayer.adapter.AdapterAlbum;
import com.phuong.musicplayer.component.ServiceMusic;
import com.phuong.musicplayer.inter_.IAlbum;
import com.phuong.musicplayer.item.ItemAlbum;

public class FragmentAlbum extends Fragment implements IAlbum {
    private RecyclerView rcAlbum;
    private ServiceMusic serviceMusic;
    private ServiceConnection connection;
    private AdapterAlbum adapterAlbum;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view=inflater.inflate(R.layout.fragment_album,container,false);
        rcAlbum=view.findViewById(R.id.rc_album);
        rcAlbum.setLayoutManager(new GridLayoutManager(getContext(),2));
        adapterAlbum=new AdapterAlbum(this);
        rcAlbum.setAdapter(adapterAlbum);


        createConnectService();
        return view;
    }


    private void createConnectService() {
        connection = new ServiceConnection() {
            @Override
            public void onServiceConnected(ComponentName name, IBinder service) {
                ServiceMusic.MyBinder myBinder = (ServiceMusic.MyBinder) service;
                serviceMusic = myBinder.getServiceMusic();
                rcAlbum.getAdapter().notifyDataSetChanged();
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
    public int getCountItemAlbum() {
        if (serviceMusic==null){
            return 0;
        }
        return serviceMusic.getAllAlbum().size();
    }

    @Override
    public ItemAlbum getDataAlbum(int position) {
        return serviceMusic.getAllAlbum().get(position);
    }

    @Override
    public void onClick(int position) {
        FragmentManager listMusic = getActivity().getSupportFragmentManager();
        FragmentTransaction transactionSearch = listMusic.beginTransaction();
        Fragment fListMusic = listMusic.findFragmentByTag(FragmentHome.class.getName());
        FragmentListInAlbum fragmentListInAlbum = new FragmentListInAlbum();
        transactionSearch.hide(fListMusic);
        transactionSearch.add(R.id.fl_home, fragmentListInAlbum, FragmentListInAlbum.class.getName());
//                fragmentSearch.updateListMusic(serviceMusic.getAllMusic());
        transactionSearch.addToBackStack(null);
        transactionSearch.commit();
    }
}
