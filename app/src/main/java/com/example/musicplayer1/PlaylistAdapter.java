package com.example.musicplayer1;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

public class PlaylistAdapter extends BaseAdapter {

    private Context context;
    private int layout;
    private ArrayList<Playlist> listPlaylist;

    public PlaylistAdapter(Context context, int layout, ArrayList<Playlist> listPlaylist) {
        this.context = context;
        this.layout = layout;
        this.listPlaylist = new ArrayList<>();
        this.listPlaylist.addAll(listPlaylist);
    }


    @Override
    public int getCount() {
        return listPlaylist.size();
    }

    @Override
    public Object getItem(int position) {
        return listPlaylist.get(position);
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    private class ViewHolder{
        TextView txtPlaylistName;
        ImageButton btnDelete;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        ViewHolder viewHolder;
        if(convertView==null){
            viewHolder = new ViewHolder();
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = inflater.inflate(layout,null);
            viewHolder.txtPlaylistName = convertView.findViewById(R.id.textViewNamePlaylist);
            viewHolder.btnDelete = convertView.findViewById(R.id.buttonSettingPlaylist);
            convertView.setTag(viewHolder);
        }else{
            viewHolder = (ViewHolder) convertView.getTag();
        }
        final Playlist p = listPlaylist.get(position);
        viewHolder.txtPlaylistName.setText(p.getName());
        viewHolder.txtPlaylistName.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Toast.makeText(context,"Playlist: "+p.getName(),Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(context,ActivitySongOfPL.class);
                intent.putExtra("PLAYLIST",listPlaylist.get(position));
                MainActivity.mPosList = position;
                //MainActivity.instance.setListSongTempNow(position);
                context.startActivity(intent);
            }
        });
        viewHolder.btnDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(context, "Setting playlist",Toast.LENGTH_SHORT).show();
            }
        });
        return convertView;
    }
}
