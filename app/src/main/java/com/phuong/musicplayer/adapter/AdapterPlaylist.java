package com.phuong.musicplayer.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.phuong.musicplayer.R;
import com.phuong.musicplayer.inter_.IPlaylist;
import com.phuong.musicplayer.model.ItemPlaylist;

public class AdapterPlaylist extends RecyclerView.Adapter<AdapterPlaylist.PlaylistViewHolder> {
    private IPlaylist inter;

    public AdapterPlaylist(IPlaylist inter) {
        this.inter = inter;
    }

    @NonNull
    @Override
    public PlaylistViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view= LayoutInflater.from(parent.getContext()).inflate(R.layout.item_artist,parent,false);
        return new PlaylistViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final PlaylistViewHolder holder, int position) {
        ItemPlaylist data=inter.getDataAlbum(position);
        holder.namePlaylist.setText(data.getNamePlaylist());
        holder.numberSong.setText(data.getNumberSong());
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                inter.onClick(holder.getAdapterPosition());
            }
        });
    }

    @Override
    public int getItemCount() {
        return inter.getCountPlaylist();
    }


    static  class PlaylistViewHolder extends RecyclerView.ViewHolder{
        private TextView namePlaylist;
        private TextView numberSong;

        public PlaylistViewHolder(@NonNull View itemView) {
            super(itemView);
            namePlaylist=itemView.findViewById(R.id.tv_name_artist);
            numberSong=itemView.findViewById(R.id.tv_number_song_artist);
        }
    }
}
