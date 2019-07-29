package com.phuong.musicplayer.adapter;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.phuong.musicplayer.R;
import com.phuong.musicplayer.model.ItemMusic;
import com.phuong.musicplayer.inter_.IMusic;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

public class AdapterMusic extends RecyclerView.Adapter<AdapterMusic.MusicViewHolder> implements Filterable{
    private IMusic inter;
    private SimpleDateFormat format=new SimpleDateFormat("mm:ss");
    private List<ItemMusic> musicList;
    private List<ItemMusic> musicFullList;
    private final int VIEW_TYPE_ITEM = 0;
    private final int VIEW_TYPE_LOADING = 1;

    public AdapterMusic(List<ItemMusic> musicList,IMusic inter) {
        this.inter = inter;
        this.musicList = musicList;
        musicFullList=new ArrayList<>(musicList);
    }

    @NonNull
    @Override
    public MusicViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int viewType) {
        View view= LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.item_music,viewGroup,false);
        return new MusicViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final MusicViewHolder holder, int i) {
        final ItemMusic data=inter.getData(i);
//        Uri sArtworkUri = Uri.parse("content://media/external/audio/albumart");
//        Uri albumArtUri = ContentUris.withAppendedId(sArtworkUri,data.getAlbumId());
        holder.name.setText(data.getName()+"");
//        holder.imgMusic.setImageURI(albumArtUri);
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                data.setClick(true);
                inter.onClick(holder.getAdapterPosition());
            }
        });
        holder.duration.setText(format.format(data.getDuration()));
        holder.singer.setText(data.getSinger());
    }

    @Override
    public int getItemCount() {
        return inter.getCountItem();
    }

    @Override
    public Filter getFilter() {

        return musicFilter;

    }

    private Filter musicFilter=new Filter() {
        @Override
        protected FilterResults performFiltering(CharSequence charSequence) {
            List<ItemMusic> list=new ArrayList<>();
            if (charSequence==null||charSequence.length()==0){
                list.addAll(musicFullList);
            }else {
                String filterPattern=charSequence.toString().toLowerCase().trim();
                for (ItemMusic item:musicFullList) {
                    if (item.getName().toLowerCase().contains(filterPattern)){
                        list.add(item);
                    }
                }
            }
            FilterResults results=new FilterResults();
            results.values=list;
            Log.d("aaaaaaaaaaaaaaaaa", "performFiltering: "+list);
            return results;
        }

        @Override
        protected void publishResults(CharSequence charSequence, FilterResults filterResults) {
            musicList.clear();
            musicList.addAll((List)filterResults.values);
            notifyDataSetChanged();
        }
    };

    public void updateMusic(ArrayList<ItemMusic> musicArrayList) {
        this.musicList = musicArrayList;
        musicFullList=new ArrayList<>(musicArrayList);
    }





    static class MusicViewHolder extends RecyclerView.ViewHolder{
        private ImageView imgMusic;
        private TextView name;
        private TextView duration;
        private TextView singer;
        public MusicViewHolder(@NonNull View itemView) {
            super(itemView);
            name=itemView.findViewById(R.id.tv_name);
            duration=itemView.findViewById(R.id.tv_duration);
            singer =itemView.findViewById(R.id.tv_singer);
            imgMusic=itemView.findViewById(R.id.iv_img);
        }
    }
}
