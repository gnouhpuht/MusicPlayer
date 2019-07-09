package com.phuong.musicplayer.fragment;

import android.annotation.SuppressLint;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.RelativeLayout;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.viewpager.widget.ViewPager;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.tabs.TabLayout;
import com.phuong.musicplayer.R;
import com.phuong.musicplayer.adapter.AdapterMusic;
import com.phuong.musicplayer.adapter.SectionsPagerAdapter;
import com.phuong.musicplayer.item.ItemMusic;
import com.phuong.musicplayer.inter_.IMusic;
import com.phuong.musicplayer.component.ServiceMusic;

public class FragmentHome extends Fragment implements View.OnClickListener, IMusic {
    private ServiceMusic serviceMusic;
    private DrawerLayout drawerLayout;
    private ImageButton open;
    private AdapterMusic adapter;
    private FragmentSearch fragmentSearch;
//    private RecyclerView rcMusic;
    private ServiceConnection connection;
    private ImageButton search;
    private FloatingActionButton flbSearch;


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        createConnectService();
    }

    @SuppressLint("WrongConstant")
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_main, container, false);

        SectionsPagerAdapter sectionsPagerAdapter = new SectionsPagerAdapter(this, getActivity().getSupportFragmentManager());
        ViewPager viewPager = view.findViewById(R.id.view_pager);
        viewPager.setAdapter(sectionsPagerAdapter);
        TabLayout tabs = view.findViewById(R.id.tapbar);
        tabs.setupWithViewPager(viewPager);
        tabs.setSelectedTabIndicatorColor(getResources().getColor(R.color.white));
        tabs.setTabTextColors(getResources().getColor(R.color.normal_text), getResources().getColor(R.color.white));

        RelativeLayout rl_menu = view.findViewById(R.id.rl_menu);
        rl_menu.setOnClickListener(null);

        drawerLayout = view.findViewById(R.id.dl_main);
        open = view.findViewById(R.id.tb_open_menu);
        open.setOnClickListener(this);

        search=view.findViewById(R.id.btn_search);
        search.setOnClickListener(this);
        flbSearch=view.findViewById(R.id.fab_search);
        flbSearch.setOnClickListener(this);

//        rcMusic = view.findViewById(R.id.rc_music_search);
//        rcMusic.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false));
//        adapter = new AdapterMusic(this);
//        rcMusic.setAdapter(adapter);
        return view;
    }

    @Override
    public void onClick(View view) {

        switch (view.getId()) {
            case R.id.tb_open_menu:
                drawerLayout.openDrawer(Gravity.LEFT);
                break;
            case R.id.btn_search:
                FragmentManager manager = getActivity().getSupportFragmentManager();
                FragmentTransaction transaction = manager.beginTransaction();
                Fragment fragment = manager.findFragmentByTag(FragmentHome.class.getName());
                fragmentSearch = new FragmentSearch();
                transaction.hide(fragment);
                transaction.add(R.id.fl_home, fragmentSearch, FragmentSearch.class.getName());
                transaction.addToBackStack(null);
                transaction.commit();
                break;
            case R.id.fab_search:
                FragmentManager search = getActivity().getSupportFragmentManager();
                FragmentTransaction transactionSearch = search.beginTransaction();
                Fragment fSearch = search.findFragmentByTag(FragmentHome.class.getName());
                fragmentSearch = new FragmentSearch();
                transactionSearch.hide(fSearch);
                transactionSearch.add(R.id.fl_home, fragmentSearch, FragmentSearch.class.getName());
//                fragmentSearch.updateListMusic(serviceMusic.getAllMusic());
                transactionSearch.addToBackStack(null);
                transactionSearch.commit();
                break;
            default:
                break;

        }
    }

//    @Override
//    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
//        super.onCreateOptionsMenu(menu, inflater);
//        inflater = getActivity().getMenuInflater();
//        inflater.inflate(R.menu.menu, menu);
//        MenuItem searchViewItem = menu.findItem(R.id.app_bar_search);
////        final SearchView searchView = (SearchView) MenuItemCompat.getActionView(searchViewItem);
//        final SearchView searchView = (SearchView) searchViewItem.getActionView();
//        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
//            @Override
//            public boolean onQueryTextSubmit(String query) {
//                searchView.clearFocus();
//                return false;
//
//            }
//
//            @Override
//            public boolean onQueryTextChange(String newText) {
//                adapter.getFilter().filter(newText);
//                return false;
//            }
//        });
//    }
//
//    @Override
//    public void onPrepareOptionsMenu(Menu menu) {
////        MenuInflater menuInflater = getActivity().getMenuInflater();
////        menuInflater.inflate(R.menu.menu, menu);
////
////       return super.onCreateOptionsMenu(menu,menuInflater);
//        super.onPrepareOptionsMenu(menu);
//    }
//
//
//
//    @Override
//    public boolean onOptionsItemSelected(MenuItem item) {
//        int id = item.getItemId();
//        if (id == R.id.app_bar_search) {
//            return true;
//        }
//        return super.onOptionsItemSelected(item);
//    }

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

//    private void createConnectService() {
//        connection = new ServiceConnection() {
//            @Override
//            public void onServiceConnected(ComponentName name, IBinder service) {
//                ServiceMusic.MyBinder myBinder = (ServiceMusic.MyBinder) service;
//                serviceMusic = myBinder.getServiceMusic();
//                rcMusic.getAdapter().notifyDataSetChanged();
//            }
//
//            @Override
//            public void onServiceDisconnected(ComponentName name) {
//
//            }
//        };
//
//        Intent intent = new Intent();
//        intent.setClass(getContext(), ServiceMusic.class);
//        getContext().bindService(intent, connection, Context.BIND_AUTO_CREATE);
//    }
}
