package com.example.musicplayer1;

import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;

import java.util.ArrayList;
import java.util.Collections;

public class QueueActivity extends AppCompatActivity {
    SongAdapterQueue songAdapterQueue;
    ArrayList<Song> listSong;
    ArrayList<Song> listSongA;
    RecyclerView recyclerViewSongNow;
    public static int position;
    ImageButton btnExit;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_queue);
        btnExit = (ImageButton) findViewById(R.id.buttonExitActiQueue);
        listSongA = ActivityPlay.instance.listSongTemp;
        listSong = new ArrayList<>();
        for(Song i:listSongA)
            listSong.add(i);
        recyclerViewSongNow = (RecyclerView) findViewById(R.id.recycleViewSong);
        recyclerViewSongNow.setLayoutManager(new LinearLayoutManager(this));
        songAdapterQueue = new SongAdapterQueue(this,listSong);
        recyclerViewSongNow.setAdapter(songAdapterQueue);
        position = MusicService.posSongNow;

        btnExit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });


        ItemTouchHelper helper = new ItemTouchHelper(new ItemTouchHelper.SimpleCallback(ItemTouchHelper.LEFT|ItemTouchHelper.RIGHT|ItemTouchHelper.UP|ItemTouchHelper.DOWN,ItemTouchHelper.LEFT|ItemTouchHelper.RIGHT) {
            @Override
            public boolean onMove(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, @NonNull RecyclerView.ViewHolder viewHolder1) {
                int from = viewHolder.getAdapterPosition();
                int to = viewHolder1.getAdapterPosition();
                Collections.swap(listSong,from,to);
                songAdapterQueue.notifyItemMoved(from,to);
                position = to;
                //MainActivity.instance.swapSongInListSongTemp(from,to);
                ActivityPlay.instance.swapSongInListSongTemp(from,to);
                return true;
            }

            @Override
            public void onSwiped(@NonNull RecyclerView.ViewHolder viewHolder, int i) {
                int posMove = viewHolder.getAdapterPosition();
                listSong.remove(posMove);
                songAdapterQueue.notifyItemRemoved(viewHolder.getAdapterPosition());
                MainActivity.instance.removeSongInListSongTemp(posMove);
                ActivityPlay.instance.removeSongInListSongTemp(posMove);
                /*if(viewHolder.getAdapterPosition()==position){
                    //position++;
                    //if(position>(listSong.size()-1))
                    //    position = 0;
                    listSong.remove(viewHolder.getAdapterPosition());
                    songAdapterQueue.notifyItemRemoved(viewHolder.getAdapterPosition());
                    MainActivity.instance.setListSongTemp(listSong);
                    ActivityPlay.instance.pauseUIP();
                    //media.stop();
                    //creatMedia();
                    //media.start();
                    //btnPlay.setImageResource(R.drawable.ic_pause2);
                }else {

                    listSong.remove(posMove);
                    MainActivity.instance.removeSongInListSongTemp(posMove);
                    ActivityPlay.instance.removeSongInListSongTemp(posMove);
                    songAdapterQueue.notifyItemRemoved(viewHolder.getAdapterPosition());
                }
                */
            }
        });
        helper.attachToRecyclerView(recyclerViewSongNow);
    }
}
