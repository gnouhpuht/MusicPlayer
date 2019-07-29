package com.phuong.musicplayer.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.phuong.musicplayer.R;
import com.phuong.musicplayer.inter_.IArtist;
import com.phuong.musicplayer.model.ItemArtist;

public class AdapterArtist extends RecyclerView.Adapter<AdapterArtist.ArtistViewHolder>{
    private IArtist inter;

    public AdapterArtist(IArtist inter) {
        this.inter = inter;
    }

    @NonNull
    @Override
    public ArtistViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view= LayoutInflater.from(parent.getContext()).inflate(R.layout.item_artist,parent,false);
        return new ArtistViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final ArtistViewHolder holder, int position) {
        ItemArtist data=inter.getData(position);
        holder.nameArtist.setText(data.getNameArtist());
        holder.numberSong.setText(data.getSinger());
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                inter.onClick(holder.getAdapterPosition());
            }
        });
    }

    @Override
    public int getItemCount() {
        return inter.getCountItemArtist();
    }

    static class ArtistViewHolder extends RecyclerView.ViewHolder{
        private ImageView imgArtist;
        private TextView nameArtist, numberSong;

        public ArtistViewHolder(@NonNull View itemView) {
            super(itemView);
            nameArtist=itemView.findViewById(R.id.tv_name_artist);
            numberSong=itemView.findViewById(R.id.tv_number_song_artist);
        }
    }
}












