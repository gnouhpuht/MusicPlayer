package com.phuong.musicplayer.inter_;


import com.phuong.musicplayer.model.ItemArtist;
import com.phuong.musicplayer.model.ItemMusic;

public interface IMusic {
    int getCountItem();
    ItemMusic getData(int position);
    void onClick(int position);
}
