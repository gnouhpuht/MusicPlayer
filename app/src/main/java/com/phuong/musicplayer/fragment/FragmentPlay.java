package com.phuong.musicplayer.fragment;

import android.content.ComponentName;
import android.content.ContentUris;
import android.content.Intent;
import android.content.ServiceConnection;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.SeekBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.fragment.app.Fragment;


import com.phuong.musicplayer.R;
import com.phuong.musicplayer.item.ItemMusic;
import com.phuong.musicplayer.inter_.Action1;
import com.phuong.musicplayer.musicmanager.MusicManager;
import com.phuong.musicplayer.component.ServiceMusic;

import java.text.SimpleDateFormat;
import java.util.Random;

import static android.content.Context.BIND_AUTO_CREATE;

public class FragmentPlay extends Fragment implements Action1<MediaPlayer>, View.OnClickListener {
    private ImageButton btnPlay, btnNext, btnBack, btnRepeat, btnShuffle;
    private MusicManager musicManager;
    private ServiceConnection connection;
    private ServiceMusic serviceMusic;
    private SeekBar seekBar;
    private TextView tvDuration,tvTitle,tvArtist,ivAvata,tvCurrentTime;
    private int total;
    private boolean isTouchSeek;
    private Runnable runMusic;
    private Handler handler;
    private int position;
    private SimpleDateFormat format = new SimpleDateFormat("mm:ss");
    private ImageView imageView;


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        createConnectService();
    }


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view=inflater.inflate(R.layout.play_music,container,false);
        mapping(view);
        initActionSeekBar();
        initRun();
        upDateTimeSong();
        makeAnimation(R.anim.rotate);
        return view;
    }

    private void upDateTimeSong() {
        if (handler != null ){
            handler.removeCallbacks(runMusic);
        }
        handler = new Handler();
        handler.postDelayed(runMusic, 100);
    }

    private void initRun() {
        runMusic = new Runnable() {
            @Override
            public void run() {
                if (serviceMusic.getMusicManager() == null) {
                    return;
                }
                if (!isTouchSeek)
                    tvCurrentTime.setText(format.format(serviceMusic.getMusicManager().getCurrentPosition()));
                // update pro
                seekBar.setProgress( serviceMusic.getMusicManager().getCurrentPosition());
                handler.postDelayed(this, 500);
            }
        };
    }

    private void initActionSeekBar() {
        seekBar.setOnSeekBarChangeListener
                (new SeekBar.OnSeekBarChangeListener() {
                     @Override
                     public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {

                     }

                     @Override
                     public void onStartTrackingTouch(SeekBar seekBar) {
                         isTouchSeek = true;
                     }

                     @Override
                     public void onStopTrackingTouch(SeekBar seekBar) {
                         serviceMusic.getMusicManager().seekTo(seekBar.getProgress());
                         isTouchSeek = false;
                     }
                 }
                );
    }

    private void mapping(View view) {
        btnPlay=view.findViewById(R.id.btn_play);
        btnBack=view.findViewById(R.id.btn_back);
        btnNext=view.findViewById(R.id.btn_next);
        btnRepeat=view.findViewById(R.id.btn_repeat);
        btnShuffle=view.findViewById(R.id.btn_shuffle);
        seekBar=view.findViewById(R.id.seekbar);
        tvDuration=view.findViewById(R.id.tv_duration);
        tvTitle=view.findViewById(R.id.tv_namemusic);
        tvCurrentTime=view.findViewById(R.id.tv_runtime);
        imageView=view.findViewById(R.id.iv_content);

        btnShuffle.setOnClickListener(this);
        btnRepeat.setOnClickListener(this);
        btnNext.setOnClickListener(this);
        btnBack.setOnClickListener(this);
        btnPlay.setOnClickListener(this);

    }

    public ItemMusic getSong(int position){
        return serviceMusic.getAllMusic().get(position);
    }

    public void getMusicManager(MusicManager mediaMusicManager, int position){
        this.musicManager = mediaMusicManager;
        this.position = position;
    }

    private void createConnectService() {
        connection = new ServiceConnection() {
            @Override
            public void onServiceConnected(ComponentName componentName, IBinder iBinder) {
                ServiceMusic.MyBinder myBinder = (ServiceMusic.MyBinder) iBinder;
                serviceMusic = myBinder.getServiceMusic();
                seekBar.setMax(serviceMusic.getDuration());
                tvDuration.setText(format.format(serviceMusic.getDuration()));
                total = serviceMusic.getDuration();
                upSongInfo(serviceMusic.getCurrentPosition());
                serviceMusic.register(FragmentPlay.class.getName(), FragmentPlay.this);

            }

            @Override
            public void onServiceDisconnected(ComponentName componentName) {

            }
        };
        //gui thong diep ket noi den service
        Intent intent = new Intent();
        intent.setClass(this.getContext(), ServiceMusic.class);
        getContext().bindService(intent, connection, BIND_AUTO_CREATE);
    }

    public void upSongInfo(int position){
        tvTitle.setText(getSong(position).getName());
//        tvArtist.setText(getSong(position).getArtists());
        tvDuration.setText(format.format(getSong(position).getDuration()));
        Uri sArtworkUri = Uri
                .parse("content://media/external/audio/albumart");
        Uri albumArtUri = ContentUris.withAppendedId(sArtworkUri,
                getSong(position).getAlbumId());
//        ivAvata.setImageURI(albumArtUri);
    }
    @Override
    public void onDestroyView() {
        if (serviceMusic != null ){
            serviceMusic.unregister(FragmentPlay.class.getName());
        }
        super.onDestroyView();
    }

    @Override
    public void call(MediaPlayer mediaPlayer) {
        upSongInfo(serviceMusic.getCurrentPosition());
    }

    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
    @Override
    public void onClick(View view) {
        position = serviceMusic.getCurrentPosition();
        switch (view.getId()) {
            case R.id.btn_play:

                if (serviceMusic.isPlaying()) {
                    btnPlay.setBackgroundResource(R.drawable.pause);
                    serviceMusic.continueSong();
                    makeAnimation(R.anim.rotate);
                } else {
                    btnPlay.setBackgroundResource(R.drawable.imgplay);
                    serviceMusic.getMusicManager().pause();
                    imageView.clearAnimation();
                }
                upDateTimeSong();
                break;
            case R.id.btn_next:
                if (position >= serviceMusic.getAllMusic().size() - 1) {
                    position = -1;
                }
                position = position + 1;
                if ( serviceMusic != null ){
                    upSongInfo(position);
                    serviceMusic.playMusic(position);
                }
                btnPlay.setBackgroundResource(R.drawable.pause);
                makeAnimation(R.anim.rotate);

                break;
            case R.id.btn_back:
                if (position <= 0) {
                    position = serviceMusic.getAllMusic().size();
                }

                position = position - 1;
                if ( serviceMusic != null ){
                    upSongInfo(position);
                    serviceMusic.playMusic(position);
                }
                btnPlay.setBackgroundResource(R.drawable.pause);
                makeAnimation(R.anim.rotate);
                break;
            case R.id.btn_shuffle:
                Random rd=new Random();
                position = rd.nextInt(serviceMusic.getAllMusic().size() - 1) + 1;
                if ( serviceMusic != null ){
                    upSongInfo(position);
                    serviceMusic.playMusic(position);
                }
                btnPlay.setBackgroundResource(R.drawable.pause);
                makeAnimation(R.anim.rotate);
                break;
            case R.id.btn_repeat:
                if ( serviceMusic != null ){
                    upSongInfo(position);
                    serviceMusic.playMusic(position);
                }
                btnPlay.setBackgroundResource(R.drawable.pause);
                makeAnimation(R.anim.rotate);
                break;
        }
    }

    private void makeAnimation(int id) {
        //tai animation tu xml len
        Animation animation = AnimationUtils.loadAnimation(getContext(), id);
        //cai dat animation cho view
        imageView.setAnimation(animation);
        imageView.startAnimation(animation);
    }
}
