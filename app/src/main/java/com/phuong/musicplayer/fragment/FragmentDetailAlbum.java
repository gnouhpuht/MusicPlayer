package com.phuong.musicplayer.fragment;

import android.annotation.SuppressLint;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.IBinder;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.phuong.musicplayer.MainActivity;
import com.phuong.musicplayer.R;
import com.phuong.musicplayer.adapter.AdapterDetailAlbum;
import com.phuong.musicplayer.adapter.AdapterMusic;
import com.phuong.musicplayer.inter_.IDetailAlbum;
import com.phuong.musicplayer.inter_.IMusic;
import com.phuong.musicplayer.model.ItemMusic;
import com.phuong.musicplayer.models.AlbumSong;
import com.phuong.musicplayer.models.Albums;
import com.phuong.musicplayer.models.Playlist;
import com.phuong.musicplayer.models.PlaylistSong;
import com.phuong.musicplayer.models.Song;
import com.phuong.musicplayer.sevice.ServicePlayMusic;
import com.phuong.musicplayer.ultils.Constant;
import com.phuong.musicplayer.view.AlbumDetailViewModel;
import com.phuong.musicplayer.view.AlbumViewModel;
import com.phuong.musicplayer.view.PlaylistDetailViewModel;
import com.phuong.musicplayer.view.SongViewModel;

import java.util.ArrayList;
import java.util.List;

public class FragmentListInAlbum extends Fragment implements IDetailAlbum {
    private RecyclerView rcMusic;
    private ServicePlayMusic servicePlayMusic;
    private ServiceConnection connection;
    private AdapterDetailAlbum adapter;
    private List<AlbumSong> albumSongs;
    private Context mContext;
    private AlbumDetailViewModel albumDetailViewModel;
    private SongViewModel songViewModel;
    private AlbumViewModel albumViewModel;
    private Albums mAlbums;
    private ArrayList<Song> mListSongs;
    private ArrayList<Long> mListSongByAlbumId;
    private Playlist mPlaylist;
    private PlaylistDetailViewModel mDetailViewModel;
    private ImageView mImvAlbums;
    private ImageButton mImvBack;

    @Override
    public void onAttach(Context context) {
        this.mContext=context;
        super.onAttach(context);
    }
    public static FragmentListInAlbum getInstance(Albums albums) {
        FragmentListInAlbum fragment = new FragmentListInAlbum();
        fragment.mAlbums = albums;
        return fragment;
    }
    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        mListSongs =new ArrayList<>();
        albumDetailViewModel= ViewModelProviders.of(this).get(AlbumDetailViewModel.class);
        albumViewModel= ViewModelProviders.of(this).get(AlbumViewModel.class);
        songViewModel= ViewModelProviders.of(this).get(SongViewModel.class);
        mDetailViewModel= ViewModelProviders.of(this).get(PlaylistDetailViewModel.class);
        Glide.with(mContext)
                .applyDefaultRequestOptions(RequestOptions.placeholderOf(R.drawable.ic_headphones))
                .load(mAlbums.getAlbumArt())
                .into(mImvAlbums);
        albumDetailViewModel.getSongByAlbumId(mAlbums.getId()).observe(FragmentListInAlbum.this, new Observer<List<Long>>() {
            @Override
            public void onChanged(List<Long> longs) {
                if (longs != null) {
                    mListSongByAlbumId = new ArrayList<>(longs);
                    mListSongs.clear();
                    new AsyncTask<Long, Void, List<Song>>() {
                        @Override
                        protected List<Song> doInBackground(Long... longs) {
                            for (long id : mListSongByAlbumId) {
                                List<Song> listSongById = songViewModel.getSongById(id);
                                if (listSongById.size() > 0) {
                                    mListSongs.add(listSongById.get(0));
                                }
                            }
                            return null;
                        }

                        @Override
                        protected void onPostExecute(List<Song> songs) {
                            super.onPostExecute(songs);
                            adapter.notifyDataSetChanged();
                        }
                    }.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
                }
            }
        });
        super.onCreate(savedInstanceState);
    }

    @SuppressLint("WrongConstant")
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view=LayoutInflater.from(container.getContext()).inflate(R.layout.fragment_list_in_album,container,false);
        rcMusic = view.findViewById(R.id.rc_list_in_album);
        rcMusic.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false));
        adapter=new AdapterDetailAlbum(new ArrayList<Song>(), this);
        rcMusic.setAdapter(adapter);
        mImvAlbums = view.findViewById(R.id.iv_list_album);
        mImvBack = view.findViewById(R.id.btn_back_album);

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

        return mListSongs.size();
    }

    @Override
    public Song getData(int position) {
        return mListSongs.get(position);
    }

    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
    @Override
    public void onClick(int position) {
        servicePlayMusic.playMusic(position);

        FragmentActivity ac = getActivity();
        ((MainActivity) ac).createPlaying(servicePlayMusic.getMusicManager(), position);
    }

    @Override
    public void onAddItem(Song song) {
        final boolean isAdd = getActivity().getIntent().getBooleanExtra(Constant.TYPE_ADD_SONG, false);
        mPlaylist = (Playlist) getActivity().getIntent().getSerializableExtra(Constant.TYPE_PLAYLIST);
        if (isAdd) {
            final long idSong = song.getId();
            final long idPlaylist = mPlaylist.getId();
            new AsyncTask<Void, Void, Void>() {
                @Override
                protected Void doInBackground(Void... voids) {
                    PlaylistSong playlistSong = new PlaylistSong();
                    playlistSong.setSongId(idSong);
                    playlistSong.setPlaylistId(idPlaylist);
                    mDetailViewModel.insert(playlistSong);
                    return null;
                }
            }.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
        }
    }
}
