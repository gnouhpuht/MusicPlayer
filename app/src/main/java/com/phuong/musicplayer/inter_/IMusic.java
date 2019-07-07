package com.phuong.musicplayer.inter_;


import com.phuong.musicplayer.component.ItemMusic;

public interface IMusic {
    int getCountItem();
    ItemMusic getData(int position);
    void onClick(int position);
}
