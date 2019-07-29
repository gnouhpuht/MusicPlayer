package com.phuong.musicplayer.inter_;


import com.phuong.musicplayer.model.ItemArtist;

public interface IArtist {
    int getCountItemArtist();
    ItemArtist getData(int position);
    void onClick(int position);
}
