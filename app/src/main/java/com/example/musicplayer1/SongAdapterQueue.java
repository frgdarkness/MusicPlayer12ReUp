package com.example.musicplayer1;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

public class SongAdapterQueue extends RecyclerView.Adapter<SongAdapterQueue.ViewHolder> {

    private Context aContext;
    private ArrayList<Song> aListSong;

    public SongAdapterQueue(Context context, ArrayList<Song> aListSong) {
        this.aContext = context;
        this.aListSong = aListSong;
    }

    @NonNull
    @Override
    public SongAdapterQueue.ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        return new ViewHolder(LayoutInflater.from(aContext).
                inflate(R.layout.list_song_queue,viewGroup,false));
    }

    @Override
    public void onBindViewHolder(@NonNull SongAdapterQueue.ViewHolder viewHolder, int i) {
        Song currentSong = aListSong.get(i);
        viewHolder.bindTo(currentSong);
    }

    @Override
    public int getItemCount() {
        return aListSong.size();
    }

    class ViewHolder extends  RecyclerView.ViewHolder implements View.OnClickListener{
        private TextView aTextTitleSong;
        private TextView aTextArtistSong;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            this.aTextTitleSong = itemView.findViewById(R.id.aTextTitleSong);
            this.aTextArtistSong = itemView.findViewById(R.id.aTextArtist);

            itemView.setOnClickListener(this);
        }

        void bindTo(Song currentSong){
            aTextTitleSong.setText(currentSong.getTitle());
            aTextArtistSong.setText(currentSong.getArctis());
        }

        @Override
        public void onClick(View v) {
            Toast.makeText(aContext,""+getAdapterPosition(),Toast.LENGTH_SHORT).show();
            ActivityPlay.instance.posSong = getAdapterPosition();
            ActivityPlay.instance.callPlayAtFromUIP();
            //MainActivity.getInstance().creatMedia(getAdapterPosition());
            //MainActivity.getInstance().startMedia();
        }
    }


}
