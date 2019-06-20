package com.example.musicplayer1;

import android.app.Notification;
import android.app.PendingIntent;
import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.ContentUris;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.ServiceConnection;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Binder;
import android.os.IBinder;
import android.os.PowerManager;
import android.provider.MediaStore;
import android.support.annotation.Nullable;
import android.support.v4.app.NotificationCompat;
import android.support.v4.app.NotificationManagerCompat;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v4.media.session.MediaSessionCompat;
import android.util.Log;

import java.io.IOException;

public class MusicService extends Service {
    public static final String CHANNEL_1_ID = "channel1";
    public static final String CHANNEL_2_ID = "channel2";
    public static int timeSongNow = 0;
    public static boolean isPlay = false;
    public static int posSongNow=0;
    public static int posListNow=-1;
    public static int isRepeat=1;
    public static int isRandom=1;
    private static final int NOTIFICATION_ID = 7;
    MediaPlayer media;
    Song songTemp;
    public static int mediaServiceSessionID=-1;
    private MediaSessionCompat mediaSession;
    private NotificationManagerCompat notificationManager;

    public class ServiceBinder extends Binder {
        MusicService getService() {
            return MusicService.this;
        }
    }

    private final IBinder mBinder = new ServiceBinder();
    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return mBinder;
    }


    @Override
    public void unbindService(ServiceConnection conn) {
        super.unbindService(conn);
    }

    @Override
    public boolean onUnbind(Intent intent) {
        return super.onUnbind(intent);
    }

    @Override
    public void onCreate() {
        super.onCreate();
        Log.i("Demo", "onCreate: ");
        MediaPlayer mp = new MediaPlayer();
        mp.setAudioStreamType(AudioManager.STREAM_MUSIC);
        mp.setWakeMode(getApplicationContext(), PowerManager.PARTIAL_WAKE_LOCK);
        //mp.setOnPreparedListener(this);
        //mp.setOnCompletionListener(this);
        this.media= mp;
        //prepareSong();
        //creatMedia();
        notificationManager = NotificationManagerCompat.from(this);
        mediaSession = new MediaSessionCompat(this, "tag");

        media.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
            @Override
            public void onCompletion(MediaPlayer mp) {
                ActivityPlay.instance.nextSong();
            }
        });

        //creatNoti();
        //sendPrev();
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        Log.i("Demo", "onStartCommand: ");

        if(media==null) {
            MediaPlayer mp = new MediaPlayer();
            mp.setAudioStreamType(AudioManager.STREAM_MUSIC);
            this.media= mp;
        }
        else
            Log.i("Demo", "Media not null");
        return START_STICKY;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        Log.i("Demo", "onDestroy: ");
        stopForeground(true);
        if (media != null) {
            try {
                media.stop();
                media.release();
            } finally {
                media = null;
            }
        }
    }

    public Bitmap getBitmapByByte(byte[] a){

        Bitmap bm = BitmapFactory.decodeByteArray(a, 0, a.length);
        return bm;
    }

    public void pause(){
        Log.i("Demo", "pause: ");
        if(media.isPlaying()) {
            media.pause();
            creatNotiBySongPause();
            isPlay=false;
        }
        else {
            media.start();
            creatNotiBySongPlay();
            isPlay=true;
        }
    }

    public void setTimeSongAgain(){
        timeSongNow = media.getCurrentPosition()/1000;
        Log.i("Demo", "setTimeSongAgain: "+timeSongNow);
    }

    public void creatNotiBySongPause() {

        Bitmap artwork = getBitmapByByte(songTemp.getByteImage());
        if(artwork==null)
            artwork = BitmapFactory.decodeResource(getResources(),R.drawable.disc);

        Intent activityIntent = new Intent(this, ActivityPlay.class);
        PendingIntent contentIntent = PendingIntent.getActivity(this,
                0, activityIntent, 0);

        Intent broadcastIntent = new Intent(this, MediaReceiver.class);
        //broadcastIntent.putExtra("toastMessage", message);
        PendingIntent actionIntent = PendingIntent.getBroadcast(this,
                0, broadcastIntent, PendingIntent.FLAG_UPDATE_CURRENT);

        Intent intentNext = new Intent(this, MediaReceiver.class);
        intentNext.setAction("NEXT");
        PendingIntent pendingIntentNext = PendingIntent.getBroadcast(this,0,intentNext,PendingIntent.FLAG_UPDATE_CURRENT);

        Intent intentPlay = new Intent(this, MediaReceiver.class);
        intentPlay.setAction("PLAY");
        PendingIntent pendingIntentPlay = PendingIntent.getBroadcast(this,0,intentPlay,PendingIntent.FLAG_UPDATE_CURRENT);

        Intent intentPrev = new Intent(this, MediaReceiver.class);
        intentPrev.setAction("Prev");
        PendingIntent pendingIntentPrev = PendingIntent.getBroadcast(this,0,intentPrev,PendingIntent.FLAG_UPDATE_CURRENT);


        Notification notification = new NotificationCompat.Builder(this, CHANNEL_2_ID)
                .setSmallIcon(R.drawable.ic_disc)
                .setContentTitle(songTemp.getTitle())
                .setContentText(songTemp.getArctis())
                .setLargeIcon(artwork)
                //.addAction(R.drawable.ic_like, "Dislike", actionIntent)

                .addAction(R.drawable.ic_prev, "Previous", pendingIntentPrev)
                .addAction(R.drawable.ic_play, "Pause", pendingIntentPlay)

                .addAction(R.drawable.ic_next, "Next", pendingIntentNext)
                .addAction(R.drawable.ic_exit,"Exit",pendingIntentPlay)
                //.addAction(R.drawable.ic_unlike, "Like", actionIntent)
                .setStyle(new android.support.v4.media.app.NotificationCompat.MediaStyle()
                        .setShowActionsInCompactView(0, 1, 2)
                        .setMediaSession(mediaSession.getSessionToken()))
                .setSubText("Sub Text")
                .setPriority(NotificationCompat.PRIORITY_LOW)
                .setContentIntent(contentIntent)
                .build();

        notificationManager.notify(2, notification);
        Log.i("Demo", "creatNotiBySong: ");

    }

    public void creatNotiBySongPlay() {

        Bitmap artwork = getBitmapByByte(songTemp.getByteImage());
        if(artwork==null)
            artwork = BitmapFactory.decodeResource(getResources(),R.drawable.disc);
        Intent activityIntent = new Intent(this, ActivityPlay.class);
        PendingIntent contentIntent = PendingIntent.getActivity(this,
                0, activityIntent, 0);

        Intent broadcastIntent = new Intent(this, MediaReceiver.class);
        //broadcastIntent.putExtra("toastMessage", message);
        PendingIntent actionIntent = PendingIntent.getBroadcast(this,
                0, broadcastIntent, PendingIntent.FLAG_UPDATE_CURRENT);

        Intent intentNext = new Intent(this, MediaReceiver.class);
        intentNext.setAction("NEXT");

        PendingIntent pendingIntentNext = PendingIntent.getBroadcast(this,0,intentNext,PendingIntent.FLAG_UPDATE_CURRENT);

        Intent intentPlay = new Intent(this, MediaReceiver.class);
        intentPlay.setAction("PLAY");
        PendingIntent pendingIntentPlay = PendingIntent.getBroadcast(this,0,intentPlay,PendingIntent.FLAG_UPDATE_CURRENT);

        Intent intentPrev = new Intent(this, MediaReceiver.class);
        intentPrev.setAction("PREV");
        PendingIntent pendingIntentPrev = PendingIntent.getBroadcast(this,0,intentPrev,PendingIntent.FLAG_UPDATE_CURRENT);

        Intent intentExit = new Intent(this,MediaReceiver.class);
        intentExit.setAction("EXIT");
        PendingIntent pendingIntentExit = PendingIntent.getBroadcast(this,0,intentExit,PendingIntent.FLAG_UPDATE_CURRENT);

        Notification notification = new NotificationCompat.Builder(this, CHANNEL_2_ID)
                .setSmallIcon(R.drawable.ic_disc)
                .setContentTitle(songTemp.getTitle())
                .setContentText(songTemp.getArctis())
                .setLargeIcon(artwork)
                //.addAction(R.drawable.ic_like, "Dislike", actionIntent)

                .addAction(R.drawable.ic_prev, "Previous", pendingIntentPrev)
                .addAction(R.drawable.ic_pause, "Pause", pendingIntentPlay)
                .addAction(R.drawable.ic_next, "Next", pendingIntentNext)
                .addAction(R.drawable.ic_exit,"Exit",pendingIntentExit)
                //.addAction(R.drawable.ic_unlike, "Like", actionIntent)
                .setStyle(new android.support.v4.media.app.NotificationCompat.MediaStyle()
                        .setShowActionsInCompactView(0, 1, 2)
                        .setMediaSession(mediaSession.getSessionToken()))
                .setSubText("Sub Text")
                .setPriority(NotificationCompat.PRIORITY_LOW)
                .setContentIntent(contentIntent)
                .build();

        notificationManager.notify(2, notification);
        Log.i("Demo", "creatNotiBySong: ");

    }

    public void sendSeekbar(){
        Intent sendListIntent = new Intent("send_reply_seekbar");
        //int n = media.getDuration();
        int m = media.getCurrentPosition();
        //int[] a = {m,n};
        sendListIntent.putExtra("SEEKBAR",m);
        LocalBroadcastManager.getInstance(this)
                .sendBroadcast(sendListIntent);
        Log.i("Demo", "sendSeekbar: ");
    }

    public void seekTo(int p){
        media.seekTo(p);
        timeSongNow = p/1000;
    }

    public void playSong(Song s){
        timeSongNow=0;
        isPlay=true;
        songTemp = s;
        Log.i("Demo", "playSongBySong: ");
        media.reset();
        long id = s.getId();
        Uri songUri = ContentUris.withAppendedId(MediaStore.Audio.Media.EXTERNAL_CONTENT_URI, id);
        try {
            media.setDataSource(getApplicationContext(), songUri);
            media.prepare();
        } catch (IOException e) {
            Log.e("MusicPlayService", "I/O error");
            media.reset(); // Reset again to idle state
        } catch (IllegalArgumentException e) {
            Log.e("MusicPlayService", "Argument error");
            media.reset(); // Reset again to idle state
        }
        media.start();
        mediaServiceSessionID = media.getAudioSessionId();
        creatNotiBySongPlay();
    }

    private BroadcastReceiver mainReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();
            if(action.equals("send_request_seekbar")){
                Log.i("Demo", "Service nhan duoc yeu cau send seekbar");
                sendSeekbar();
            }else if(action.equals("abc")){
                //txtMain.setText("PREV");
            }

        }
    };

    private void setupMainBroadcastReceiver() {
        IntentFilter mpPreparedIntentFilter2 = new IntentFilter("send_request_seekbar");
        LocalBroadcastManager.getInstance(this)
                .registerReceiver(mainReceiver, mpPreparedIntentFilter2);

    }

    public int getCurrentTimeSong(){
        if(media!=null && media.isPlaying())
            return media.getCurrentPosition();
        else
            return 0;
    }

    public void stopFG(){
        stopForeground(true);
    }

    public int getSessionID(){
        return media.getAudioSessionId();
    }

    public void closeNoti(){
        notificationManager.cancel(2);
    }
}
