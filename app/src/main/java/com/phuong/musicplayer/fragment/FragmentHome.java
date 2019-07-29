package com.phuong.musicplayer.fragment;

import android.annotation.SuppressLint;
import android.content.ComponentName;
import android.content.Intent;
import android.content.ServiceConnection;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.IBinder;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
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
import com.phuong.musicplayer.inter_.Action1;
import com.phuong.musicplayer.model.ItemMusic;
import com.phuong.musicplayer.inter_.IMusic;
import com.phuong.musicplayer.sevice.ServicePlayMusic;
import com.sothree.slidinguppanel.SlidingUpPanelLayout;


import static android.content.Context.BIND_AUTO_CREATE;

public class FragmentHome extends Fragment implements View.OnClickListener, IMusic , Action1<MediaPlayer> {
    private ServicePlayMusic servicePlayMusic;
    private DrawerLayout drawerLayout;
    private ImageButton open;
    private AdapterMusic adapter;
    private FragmentSearch fragmentSearch;
    private ServiceConnection connection;
    private ImageButton search;
    private ImageView next , back, play;
    private TextView name, singer;
    private FloatingActionButton flbSearch;
    private SlidingUpPanelLayout mLayout;
    private int currentPosition;
    private LinearLayout linearLayout;
//    private BottomSheetBehavior bottomSheet = null;


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        createConnectService();

    }

    @SuppressLint("WrongConstant")
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_main, container, false);
        viewPager(view);
        mapping(view);
        mLayout=view.findViewById(R.id.sliding_layout);
        linearLayout=view.findViewById(R.id.dragview);

//        mLayout =view.findViewById(R.id.sliding_layout);
//        mLayout.addPanelSlideListener(new SlidingUpPanelLayout.PanelSlideListener() {
//            @Override
//            public void onPanelSlide(View panel, float slideOffset) {
//
//            }
//
//            @Override
//            public void onPanelStateChanged(View panel, SlidingUpPanelLayout.PanelState previousState, SlidingUpPanelLayout.PanelState newState) {
                getActivity().getSupportFragmentManager().beginTransaction().add(R.id.fl_play,new FragmentPlay(),FragmentPlay.class.getName()).commit();
//
//            }
//        });
//        mLayout.setFadeOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                mLayout.setVisibility(View.GONE);
//                mLayout.setPanelState(SlidingUpPanelLayout.PanelState.COLLAPSED);
//            }
//        });

//        rcMusic = view.findViewById(R.id.rc_music_search);
//        rcMusic.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false));
//        adapter = new AdapterMusic(this);
//        rcMusic.setAdapter(adapter);
//        ListView listView=view.findViewById(R.id.list);
//        listView.setAdapter(new ArrayAdapter<String>(getContext(),android.R.layout.simple_list_item_1,new String[]{"Copy","Paste","Cut","Delete","Convent","Open"}));

//        getActivity().getSupportFragmentManager().beginTransaction().add(R.id.fl_play,new FragmentPlay(),
//                FragmentPlay.class.getName()).commit();

//        bottomSheet = BottomSheetBehavior.from( mLayout);
//
//        bottomSheet.setHideable(true);
//        bottomSheet.setState(BottomSheetBehavior.STATE_HIDDEN);
        return view;
    }

    private void viewPager(View view){
        SectionsPagerAdapter sectionsPagerAdapter = new SectionsPagerAdapter(this, getActivity().getSupportFragmentManager());
        ViewPager viewPager = view.findViewById(R.id.view_pager);
        viewPager.setAdapter(sectionsPagerAdapter);
        TabLayout tabs = view.findViewById(R.id.tapbar);
        tabs.setupWithViewPager(viewPager);
        tabs.setSelectedTabIndicatorColor(getResources().getColor(R.color.white));
        tabs.setTabTextColors(getResources().getColor(R.color.normal_text), getResources().getColor(R.color.white));
        viewPager.setOffscreenPageLimit(4);
    }

    private void mapping(View view){
        RelativeLayout rl_menu = view.findViewById(R.id.rl_menu);
        rl_menu.setOnClickListener(null);

        drawerLayout = view.findViewById(R.id.dl_main);
        open = view.findViewById(R.id.tb_open_menu);
        open.setOnClickListener(this);

        search=view.findViewById(R.id.btn_search);
        search.setOnClickListener(this);
        flbSearch=view.findViewById(R.id.fab_search);
        flbSearch.setOnClickListener(this);

        next=view.findViewById(R.id.btn_next_notify);
        play=view.findViewById(R.id.btn_play_notify);
        back=view.findViewById(R.id.btn_back_notify);

        next.setOnClickListener(this);
        play.setOnClickListener(this);
        back.setOnClickListener(this);

        name=view.findViewById(R.id.tv_name_notify);
        singer=view.findViewById(R.id.tv_singer_notify);


    }
    private void createConnectService() {
        connection = new ServiceConnection() {
            @Override
            public void onServiceConnected(ComponentName componentName, IBinder iBinder) {
                ServicePlayMusic.MyBinder myBinder = (ServicePlayMusic.MyBinder) iBinder;
                servicePlayMusic = myBinder.getServicePlayMusic();
                upSongInfo(servicePlayMusic.getCurrentPosition());
                servicePlayMusic.register(FragmentHome.class.getName(), FragmentHome.this);

            }


            @Override
            public void onServiceDisconnected(ComponentName componentName) {

            }
        };
        //gui thong diep ket noi den service
        Intent intent = new Intent();
        intent.setClass(this.getContext(), ServicePlayMusic.class);
        getContext().bindService(intent, connection, BIND_AUTO_CREATE);
    }

    public void upSongInfo(int position){
        name.setText(getSong(position).getName());
        singer.setText(getSong(position).getSinger());

    }
    @Override
    public void onClick(View view) {
        currentPosition=servicePlayMusic.getCurrentPosition();
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
            case R.id.btn_back_notify:
                if (currentPosition <= 0) {
                    currentPosition = servicePlayMusic.getAllMusic().size();
                }
                currentPosition = currentPosition - 1;
                if ( servicePlayMusic != null ){
                    upSongInfo(currentPosition);
                    servicePlayMusic.playMusic(currentPosition);
                }
                play.setImageResource(R.drawable.ic_pause_black_24dp);
                break;
            case R.id.btn_next_notify:
                if (currentPosition >= servicePlayMusic.getAllMusic().size() - 1) {
                    currentPosition = -1;
                }
                currentPosition = currentPosition +1;
                if ( servicePlayMusic != null ){
                    upSongInfo(currentPosition);
                    servicePlayMusic.playMusic(currentPosition);
                }
                play.setImageResource(R.drawable.ic_pause_black_24dp);
                break;
            case R.id.btn_play_notify:
                if (servicePlayMusic.isPlaying()) {
                    play.setImageResource(R.drawable.ic_pause_black_24dp);
                    servicePlayMusic.continueSong();

                } else {
                    play.setImageResource(R.drawable.ic_play_arrow_black_24dp);
                    servicePlayMusic.getMusicManager().pause();
                }
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
        if (servicePlayMusic == null) {
            return 0;
        }
        return servicePlayMusic.getAllMusic().size();
    }

    @Override
    public ItemMusic getData(int position) {
        return servicePlayMusic.getAllMusic().get(position);
    }

    @Override
    public void onClick(int position) {

    }



    public ItemMusic getSong(int position){
        return servicePlayMusic.getAllMusic().get(position);
    }

    @Override
    public void call(MediaPlayer mediaPlayer) {
        upSongInfo(servicePlayMusic.getCurrentPosition());
    }

    @Override
    public void onDestroyView() {
        if (servicePlayMusic != null ){
            servicePlayMusic.unregister(FragmentPlay.class.getName());
        }
        super.onDestroyView();
    }




//    public void showAboutStore(View V){
//        bottomSheet.setState(BottomSheetBehavior.STATE_EXPANDED);
//    }
//
//    private BottomSheetBehavior.BottomSheetCallback bottomSheetCallBack = new BottomSheetBehavior.BottomSheetCallback() {
//        @Override
//        public void onStateChanged(@NonNull View view, int newState) {
//            if(newState == BottomSheetBehavior.STATE_HIDDEN) {
//
//            }
//        }
//
//        @Override
//        public void onSlide(@NonNull View view, float v) {
//
//        }
//    };
}
