package com.phuong.musicplayer.inter_;

import com.phuong.musicplayer.model.ItemAlbum;

public interface IAlbum {
    int getCountItemAlbum();
    ItemAlbum getDataAlbum(int position);
    void onClick(int position);
}
