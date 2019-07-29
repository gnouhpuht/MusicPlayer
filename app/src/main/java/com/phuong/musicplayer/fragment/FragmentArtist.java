package com.phuong.musicplayer.fragment;

import android.annotation.SuppressLint;
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
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.phuong.musicplayer.R;
import com.phuong.musicplayer.adapter.AdapterArtist;
import com.phuong.musicplayer.inter_.IArtist;
import com.phuong.musicplayer.model.ItemArtist;
import com.phuong.musicplayer.sevice.ServicePlayMusic;


public class FragmentArtist extends Fragment implements IArtist {
    private RecyclerView rcArtist;
    private ServicePlayMusic servicePlayMusic;
    private ServiceConnection connection;
    private AdapterArtist adapterArtist;

    @SuppressLint("WrongConstant")
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view=inflater.inflate(R.layout.fragment_artist,container,false);
        rcArtist=view.findViewById(R.id.rc_artist);
        rcArtist.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL,false));
        adapterArtist=new AdapterArtist(this);
        rcArtist.setAdapter(adapterArtist);

        createConnectService();
        return view;
    }




    private void createConnectService() {
        connection = new ServiceConnection() {
            @Override
            public void onServiceConnected(ComponentName name, IBinder service) {
                ServicePlayMusic.MyBinder myBinder = (ServicePlayMusic.MyBinder) service;
                servicePlayMusic = myBinder.getServicePlayMusic();
                rcArtist.getAdapter().notifyDataSetChanged();
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
    public int getCountItemArtist() {
        if (servicePlayMusic==null){
            return 0;
        }
        return servicePlayMusic.getAllArtist().size();
    }

    @Override
    public ItemArtist getData(int position) {
//        return null;
        return servicePlayMusic.getAllArtist().get(position);
    }

    @Override
    public void onClick(int position) {
        FragmentManager listMusic = getActivity().getSupportFragmentManager();
        FragmentTransaction transactionSearch = listMusic.beginTransaction();
        Fragment fListMusic = listMusic.findFragmentByTag(FragmentHome.class.getName());
        FragmentListInArtist fragmentListInArtist = new FragmentListInArtist();
        transactionSearch.hide(fListMusic);
        transactionSearch.add(R.id.fl_home, fragmentListInArtist, FragmentListInArtist.class.getName());
//                fragmentSearch.updateListMusic(serviceMusic.getAllMusic());
        transactionSearch.addToBackStack(null);
        transactionSearch.commit();
    }
}
