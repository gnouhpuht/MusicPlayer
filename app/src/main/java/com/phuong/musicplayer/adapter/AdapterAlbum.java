package com.phuong.musicplayer.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.phuong.musicplayer.R;
import com.phuong.musicplayer.inter_.IAlbum;
import com.phuong.musicplayer.model.ItemAlbum;

public class AdapterAlbum extends RecyclerView.Adapter<AdapterAlbum.AlbumViewHolder>{
    private IAlbum inter;

    public AdapterAlbum(IAlbum inter) {
        this.inter = inter;
    }

    @NonNull
    @Override
    public AlbumViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view= LayoutInflater.from(parent.getContext()).inflate(R.layout.item_album,parent,false);
        return new AlbumViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final AlbumViewHolder holder, int position) {
        ItemAlbum data=inter.getDataAlbum(position);
        holder.nameAlbum.setText(data.getNameAlbum());
        holder.numberSong.setText(data.getNumberSong());
//        holder.imgAlbum.setImageResource((int) data.getAlbumId());

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                inter.onClick(holder.getAdapterPosition());
            }
        });
    }

    @Override
    public int getItemCount() {
        return inter.getCountItemAlbum();
    }

    static class AlbumViewHolder extends RecyclerView.ViewHolder{
        private ImageView imgAlbum;
        private TextView nameAlbum;
        private TextView numberSong;

        public AlbumViewHolder(@NonNull View itemView) {
            super(itemView);

            imgAlbum=itemView.findViewById(R.id.iv_album);
            nameAlbum=itemView.findViewById(R.id.tv_name_album);
            numberSong =itemView.findViewById(R.id.tv_number_song_album);
        }
    }


}
