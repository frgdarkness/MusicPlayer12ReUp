package com.example.musicplayer1;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.widget.Toast;

public class MediaReceiver extends BroadcastReceiver {
    private MusicService musicService;
    private Intent musicIntent;
    private ServiceConnection serviceConnection;
    @Override
    public void onReceive(Context context, Intent intent) {
        //String message = intent.getStringExtra("toastMessage");
        //Toast.makeText(context,"nhu lol",Toast.LENGTH_SHORT).show();

        String action = intent.getAction();
        //if(action.equals("test"))
            //MainActivity.instance.show();

        if(action.equals("PREV")){
            Toast.makeText(context, action, Toast.LENGTH_SHORT).show();
            ActivityPlay.instance.prevSongUIP();
        }else if(action.equals("PLAY")){
            Toast.makeText(context, action, Toast.LENGTH_SHORT).show();
            ActivityPlay.instance.pauseUIP();
            //MainActivity.instance.pauseSongMain();
            //ActivityPlaying.instance.changeCover();
        }else if(action.equals("NEXT")){
            Toast.makeText(context, action, Toast.LENGTH_SHORT).show();
            ActivityPlay.instance.nextSong();
            //MainActivity.instance.nextSongMain();
            //ActivityPlaying.instance.changeCover();
        }else if(action.equals("EXIT")){
            Toast.makeText(context, action, Toast.LENGTH_SHORT).show();
            MainActivity.instance.exitApp();
        }
        /*
        if(action.equals("PLAY")) {
            Toast.makeText(context, action, Toast.LENGTH_SHORT).show();
            MainActivity.instance.update();
        }else if(action.equals("PREV")){
            Toast.makeText(context, action, Toast.LENGTH_SHORT).show();
        }else{
            Toast.makeText(context, action, Toast.LENGTH_SHORT).show();
        }
        */

        //if(action.equals("Pause")){
        //    String s="1";
        //}

    }
}
