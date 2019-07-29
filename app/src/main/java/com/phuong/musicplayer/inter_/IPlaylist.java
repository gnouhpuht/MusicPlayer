package com.phuong.musicplayer.inter_;


import com.phuong.musicplayer.model.ItemPlaylist;

public interface IPlaylist {
    int getCountPlaylist();
    ItemPlaylist getDataAlbum(int position);
    void onClick(int position);
}
