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
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.Filter;
import android.widget.SearchView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.view.MenuItemCompat;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.phuong.musicplayer.R;
import com.phuong.musicplayer.adapter.AdapterMusic;
import com.phuong.musicplayer.item.ItemMusic;
import com.phuong.musicplayer.inter_.IMusic;
import com.phuong.musicplayer.component.ServiceMusic;

import java.util.ArrayList;

public class FragmentSearch extends Fragment implements IMusic {
    private ServiceMusic serviceMusic;
    private RecyclerView rcMusic;
    private AdapterMusic adapter;
    private ServiceConnection connection;
    private EditText etSearch;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        setHasOptionsMenu(true);
    }

    @SuppressLint("WrongConstant")
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view=inflater.inflate(R.layout.fragment_search,container,false);
        rcMusic = view.findViewById(R.id.rc_music_search);
        rcMusic.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false));
        adapter=new AdapterMusic(new ArrayList<ItemMusic>(), this);
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
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        inflater = getActivity().getMenuInflater();
        inflater.inflate(R.menu.menu, menu);
        MenuItem searchViewItem = menu.findItem(R.id.app_bar_search);
        final SearchView searchView = (SearchView) MenuItemCompat.getActionView(searchViewItem);
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                searchView.clearFocus();
//                if(list.contains(query)){
//                    adapter.getFilter().filter(query);
//                }else{
//                    Toast.makeText(MainActivity.this, "No Match found",Toast.LENGTH_LONG).show();
//                }
                return false;

            }

            @Override
            public boolean onQueryTextChange(String newText) {
                adapter.getFilter().filter(newText);
                return false;
            }
        });
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

    @Override
    public void onClick(int position) {

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
}
