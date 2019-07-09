package com.phuong.musicplayer.inter_;


import com.phuong.musicplayer.item.ItemMusic;

public interface IMusic {
    int getCountItem();
    ItemMusic getData(int position);
    void onClick(int position);
}
