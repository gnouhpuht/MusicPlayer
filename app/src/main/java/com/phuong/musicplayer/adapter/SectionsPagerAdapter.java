package com.phuong.musicplayer.adapter;

import android.content.Context;

import androidx.annotation.Nullable;
import androidx.annotation.StringRes;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;

import com.phuong.musicplayer.R;
import com.phuong.musicplayer.fragment.FragmentAlbum;
import com.phuong.musicplayer.fragment.FragmentGenres;
import com.phuong.musicplayer.fragment.FragmentHome;
import com.phuong.musicplayer.fragment.FragmentList;
import com.phuong.musicplayer.fragment.FragmentSong;


//import com.example.myapplication.ui.main.AlbumFragment;
//import com.example.myapplication.ui.main.GenresFragment;
//import com.example.myapplication.ui.main.ListSongFragment;
//import com.example.myapplication.ui.main.MusicHomeFragment;
//import com.example.myapplication.ui.main.PlaylistFragment;

/**
 * A [FragmentPagerAdapter] that returns a fragment corresponding to
 * one of the sections/tabs/pages.
 */
public class SectionsPagerAdapter extends FragmentPagerAdapter {

    @StringRes
    private static final int[] TAB_TITLES = new int[]{R.string.tab_text_1, R.string.tab_text_2, R.string.tab_text_3, R.string.tap_text_4};
    private final Context mContext;

    public SectionsPagerAdapter(FragmentHome context, FragmentManager fm) {
        super(fm);
        mContext = context.getContext();
    }

    @Override
    public Fragment getItem(int position) {
        // getItem is called to instantiate the fragment for the given page.
        // Return a MusicHomeFragment (defined as a static inner class below).
        switch (position) {
            case 0:
                FragmentList fragmentList = new FragmentList();
                return fragmentList;

            case 1:
                FragmentAlbum fragmentAlbum = new FragmentAlbum();
                return fragmentAlbum;
            case 2:
                FragmentSong fragmentSong = new FragmentSong();
                return fragmentSong;
            case 3:
                 FragmentGenres fragmentGenres = new FragmentGenres();
                return fragmentGenres;
            default:
                return null;
        }
    }

    @Nullable
    @Override
    public CharSequence getPageTitle(int position) {
        return mContext.getResources().getString(TAB_TITLES[position]);
    }

    @Override
    public int getCount() {
        return 4;
    }
}