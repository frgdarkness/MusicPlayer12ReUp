package com.example.musicplayer1;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class SongAdapter extends BaseAdapter {
    private Context context;
    private int layout;
    private List<Song> songList,songListSave;


    public SongAdapter(Context context, int layout, List<Song> songList) {
        this.context = context;
        this.layout = layout;
        this.songList = songList;
        this.songListSave = new ArrayList<>();
        this.songListSave.addAll(songList);
    }

    @Override
    public int getCount() {
        return songList.size();
    }

    @Override
    public Object getItem(int position) {
        return songList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    private class ViewHolder{
        TextView songTitle2,songArtist2;
        ImageView songCover2;
        ImageButton btnSetting2;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        ViewHolder viewHolder;
        if(convertView==null){
            viewHolder = new ViewHolder();
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = inflater.inflate(layout,null);
            viewHolder.songTitle2 = (TextView) convertView.findViewById(R.id.textSongTitleSongRow);
            viewHolder.songArtist2 = (TextView) convertView.findViewById(R.id.textSongArtistSongRow);
            viewHolder.songCover2 = (ImageView)  convertView.findViewById(R.id.imageViewCoverSongRow);
            viewHolder.btnSetting2 = (ImageButton) convertView.findViewById(R.id.buttonSettingSongRow);
            convertView.setTag(viewHolder);
        }else{
            viewHolder = (ViewHolder) convertView.getTag();
        }

        final Song s = songList.get(position);
        viewHolder.songArtist2.setText(s.getArctis());
        viewHolder.songTitle2.setText(s.getTitle());
        viewHolder.songCover2.setImageBitmap(getBitmapByByte(s.getByteImage()));
        viewHolder.btnSetting2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(context,"Setting: "+s.getTitle(),Toast.LENGTH_SHORT).show();
            }
        });
        viewHolder.songTitle2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                MainActivity.instance.playAllSongAt(position);
            }
        });
        viewHolder.songCover2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                MainActivity.instance.playAllSongAt(position);
            }
        });
        viewHolder.songArtist2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                MainActivity.instance.playAllSongAt(position);
            }
        });
        return convertView;
    }

    public void filter(String charText) {
        charText = charText.toLowerCase(Locale.getDefault());
        songList.clear();
        if (charText.length() == 0) {
            songList.addAll(songListSave);
        } else {
            for (Song wp : songListSave) {
                if (wp.getTitle().toLowerCase(Locale.getDefault()).contains(charText)) {
                    songList.add(wp);
                }
            }
        }
        notifyDataSetChanged();
    }

    public Bitmap getBitmapByByte(byte[] a){

        Bitmap bm = BitmapFactory.decodeByteArray(a, 0, a.length);
        return bm;
    }
}
