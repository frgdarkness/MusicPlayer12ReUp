package com.example.musicplayer1;

import android.content.ContentResolver;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.MediaMetadataRetriever;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.Log;

import java.io.File;
import java.util.ArrayList;

/**
 * Created by ducth on 3/16/2018.
 */

@SuppressWarnings("unchecked")
public class MyMedia {
    private ContentResolver contentResolver;
    private Context context;
    private static final String musicExtensions = "mp3";
    public static int countFailed = 0;
    ArrayList<Lyric> listLyric;

    public MyMedia(Context context) {
        this.context = context;
        contentResolver = context.getContentResolver();
    }

    public static Uri getAlbumartURI(Long album_id) {
        return ContentUris.withAppendedId(Uri.parse("content://media/external/audio/albumart"), album_id.longValue());
    }

    public ArrayList<Song> getAllListSong() {
        ArrayList<Song> mlistSongForArtist = new ArrayList<>();
        getLyric();
        String[] projection = new String[]{"_id", "artist", "title", "_data", "duration", "album", "album_id"};
        Cursor musicCursor = context.getContentResolver().query(MediaStore.Audio.Media.EXTERNAL_CONTENT_URI,
                projection, "is_music != 0", null, "title ASC");
        if (musicCursor == null || !musicCursor.moveToFirst()) {
            return mlistSongForArtist;
        }
        do {
            Song songs = new Song();
            long idMedia = musicCursor.getLong(musicCursor.getColumnIndexOrThrow("_id"));
            String mArtists = musicCursor.getString(musicCursor.getColumnIndexOrThrow("artist"));
            long timeDuration = musicCursor.getLong(musicCursor.getColumnIndexOrThrow("duration"));
            String songPath = musicCursor.getString(musicCursor.getColumnIndexOrThrow("_data"));
            String nameSong = musicCursor.getString(musicCursor.getColumnIndexOrThrow("title"));
            String album = musicCursor.getString(musicCursor.getColumnIndexOrThrow("album"));
            long idalbum = musicCursor.getLong(musicCursor.getColumnIndexOrThrow("album_id"));
            songs.setDuration(Long.valueOf(timeDuration));
            songs.setArctis(mArtists);
            songs.setData(songPath);
            songs.setTitle(nameSong);
            songs.setId(Long.valueOf(idMedia));
            songs.setAlbum(album);
            songs.setIdAlbum(Long.valueOf(idalbum));
            songs.setByteImage(getAlbumart(songPath));
            songs.setTimeTotal((int)timeDuration);
            Bitmap bm = getCover(songPath);
            for(Lyric i:listLyric){
                Log.i("SetLyric", i.getTitle()+" - "+nameSong);
                if(nameSong.equals(i.getTitle())) {
                    songs.setLyric(i.getLyric());
                    break;
                }
                else songs.setLyric(R.string.no_lyric);
            }
            if(bm!=null) {
                //songs.setCover(getCover(songPath));
                mlistSongForArtist.add(songs);
            }else
                countFailed++;
        } while (musicCursor.moveToNext());
        return mlistSongForArtist;
    }

    public byte[] getAlbumart(String path) {
        MediaMetadataRetriever mmr = new MediaMetadataRetriever();
        byte[] rawArt = null;
        try {
            mmr.setDataSource(context, Uri.parse(path));
            rawArt = mmr.getEmbeddedPicture();
        } catch (Exception e) {

        }
        return rawArt;
    }

    public Bitmap getCover(String path){
        MediaMetadataRetriever mmr = new MediaMetadataRetriever();
        Bitmap bm = null;
        byte[] rawArt = null;
        try {
            mmr.setDataSource(context, Uri.parse(path));
            rawArt = mmr.getEmbeddedPicture();
            bm = BitmapFactory.decodeByteArray(rawArt, 0, rawArt.length);
        } catch (Exception e) {

        }
        return bm;
    }

    public Bitmap getBitmapByByte(byte[] a){

        Bitmap bm = BitmapFactory.decodeByteArray(a, 0, a.length);
        return bm;
    }

    public boolean deleteSong(String path) {
        File file = new File(path);
        final String where = MediaStore.MediaColumns.DATA + "=?";
        final String[] selectionArgs = new String[]{
                file.getAbsolutePath()
        };
        final Uri filesUri = MediaStore.Files.getContentUri("external");
        contentResolver.delete(filesUri, where, selectionArgs);
        if (file.exists()) {
            contentResolver.delete(filesUri, where, selectionArgs);
            return true;
        }
        return false;
    }

    public void scanAllMusicFileInExternal(Context context, ArrayList<String> musicScannedList, File scanFile) {
        try {
            File[] files = scanFile.listFiles();
            for (File itemFile : files) {
                if (itemFile.isDirectory()) {
                    if (!itemFile.getName().equals("ZING MP3")) {
                        scanAllMusicFileInExternal(context, musicScannedList, itemFile);
                    }
                } else if (itemFile.getName().toLowerCase().equals(".nomedia")) {
                    deleteFile(itemFile.getPath());
                } else if (itemFile.getName().lastIndexOf(".") != -1) {
                    String extension = itemFile.getName().toLowerCase().substring(itemFile.getName().lastIndexOf("."));
                    extension = extension.replace(".", "");
                    if (extension.equals(musicExtensions)) {
                        if (!musicScannedList.contains(itemFile.getPath())/* && (getAudioFileDuration(itemFile.getPath()) >= limitDuration)*/) {
                            scanFile(context, itemFile.getPath(), true);
                            /*musicScannedList.add(itemFile.getPath());
                            MusicFolderEntity entity = new MusicFolderEntity(itemFile.getParent());
                            if (!musicFolderList.contains(entity)) {
                                addNewMusicFolder(itemFile.getParent(), musicFolderList);
                            }*/
                        }
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void scanFile(Context context, String path, boolean b) {
        context.sendBroadcast(new Intent(Intent.ACTION_MEDIA_MOUNTED, Uri.parse("file://"
                + Environment.getExternalStorageDirectory())));
    }

    private void deleteFile(String path) {
        File file = new File(path);
        file.delete();
    }


    public void setRingstone(Song song) {
        File file = new File(song.getData());

        ContentValues values = new ContentValues();
        values.put(MediaStore.MediaColumns.DATA, file.getAbsolutePath());
        values.put(MediaStore.MediaColumns.TITLE, "ringstone_" + file.getName());
        values.put(MediaStore.MediaColumns.SIZE, file.length());
        values.put(MediaStore.MediaColumns.MIME_TYPE, "audio/mp3");
        values.put(MediaStore.Audio.AudioColumns.ARTIST, context.getString(R.string.app_name));
        values.put(MediaStore.Audio.AudioColumns.IS_RINGTONE, true);
        values.put(MediaStore.Audio.AudioColumns.IS_NOTIFICATION, false);
        values.put(MediaStore.Audio.AudioColumns.IS_ALARM, false);
        values.put(MediaStore.Audio.AudioColumns.IS_MUSIC, true);

        Uri uri = MediaStore.Audio.Media.getContentUriForPath(file.getAbsolutePath());
        contentResolver.delete(uri, MediaStore.MediaColumns.DATA + "=\"" + file.getAbsolutePath() + "\"", null);

        Uri newUri = contentResolver.insert(uri, values);

        RingtoneManager.setActualDefaultRingtoneUri(
                context,
                RingtoneManager.TYPE_RINGTONE,
                newUri
        );
    }

    public void getLyric(){
        listLyric = new ArrayList<>();
        //String s = String.valueOf(R.string.A_Sky_Full_Of_Stars);
        listLyric.add(new Lyric("A Sky Full Of Stars",R.string.A_Sky_Full_Of_Stars));
        listLyric.add(new Lyric("May It Be",R.string.May_It_Be));
        listLyric.add(new Lyric("When I Look At You",R.string.When_I_Look_At_You));
        listLyric.add(new Lyric("Ashes",R.string.ashes_lyric));
        listLyric.add(new Lyric("Perfect",R.string.Perfect));
    }

}
