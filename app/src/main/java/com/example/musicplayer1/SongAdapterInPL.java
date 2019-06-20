package com.example.musicplayer1;

import android.app.Dialog;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.PopupMenu;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class SongAdapterInPL extends BaseAdapter {
    private Context context;
    private int layout;
    private List<Song> songList,songListSave;
    private int posthisPL;

    public SongAdapterInPL(Context context, int layout, List<Song> songList, int pos) {
        this.context = context;
        this.layout = layout;
        this.songList = songList;
        this.songListSave = new ArrayList<>();
        this.songListSave.addAll(songList);
        this.posthisPL = pos;
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
        final SongAdapterInPL.ViewHolder viewHolder;
        if(convertView==null){
            viewHolder = new SongAdapterInPL.ViewHolder();
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = inflater.inflate(layout,null);
            viewHolder.songTitle2 = (TextView) convertView.findViewById(R.id.textSongTitleSongRow);
            viewHolder.songArtist2 = (TextView) convertView.findViewById(R.id.textSongArtistSongRow);
            viewHolder.songCover2 = (ImageView)  convertView.findViewById(R.id.imageViewCoverSongRow);
            viewHolder.btnSetting2 = (ImageButton) convertView.findViewById(R.id.buttonSettingSongRow);
            convertView.setTag(viewHolder);
        }else{
            viewHolder = (SongAdapterInPL.ViewHolder) convertView.getTag();
        }

        final Song s = songList.get(position);
        viewHolder.songArtist2.setText(s.getArctis());
        viewHolder.songTitle2.setText(s.getTitle());
        viewHolder.songCover2.setImageBitmap(getBitmapByByte(s.getByteImage()));
        viewHolder.btnSetting2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Toast.makeText(context,"Setting: "+s.getTitle(),Toast.LENGTH_SHORT).show();
                PopupMenu menu = new PopupMenu(context, viewHolder.btnSetting2);
                menu.getMenuInflater().inflate(R.menu.menu_song_2,menu.getMenu());
                menu.show();
                menu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                    @Override
                    public boolean onMenuItemClick(MenuItem item) {
                        switch (item.getItemId()){
                            case R.id.menuSongAdd2:
                                //Toast.makeText(context,"Add to playlist",Toast.LENGTH_SHORT).show();
                                setDialogSetting(position);
                                break;
                            case R.id.menuSongDel2:
                                Toast.makeText(context,"Deleted! "+posthisPL,Toast.LENGTH_SHORT).show();
                                MainActivity.instance.delSongOfPL(position,posthisPL);
                                songList.clear();
                                songList.addAll(MainActivity.instance.listPlaylist.get(posthisPL).getListSong());
                                SongAdapterInPL.this.notifyDataSetChanged();
                        }
                        return false;
                    }
                });
            }
        });
        viewHolder.songTitle2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //MainActivity.instance.playAllSongAt(position);
                setClick(position);
            }
        });
        viewHolder.songCover2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //MainActivity.instance.playAllSongAt(position);
                setClick(position);
            }
        });
        viewHolder.songArtist2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //MainActivity.instance.playAllSongAt(position);
                setClick(position);
            }
        });
        return convertView;
    }

    public void setClick(int pos){
        MainActivity.instance.setListSongTempInPL();
        MainActivity.instance.playAllSongAt(pos);
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

    public void setDialogSetting(final int pos){
        final Dialog dialog = new Dialog(context);
        dialog.setContentView(R.layout.dialog_setting_song_1);
        ListView lvDialog = dialog.findViewById(R.id.listViewDiaglogPlaylist);
        final ArrayList<String> listS = new ArrayList<>();
        ArrayList<Playlist> apl = MainActivity.instance.listPlaylist;
        for(Playlist i:apl)
            listS.add(i.getName());
        /*
        listS.add("all of the stars");
        listS.add("Ashes");
        listS.add("Perfect");
        listS.add("A sky full of star");
        listS.add("When i look at you");
        */
        //ArrayAdapter adapterC = new ArrayAdapter(context,android.R.layout.simple_list_item_1,listS);
        StringAdapter1 adapterC = new StringAdapter1(context,R.layout.string_row_1,listS);
        lvDialog.setAdapter(adapterC);
        dialog.show();
        lvDialog.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Toast.makeText(context, songList.get(pos).getTitle()+" - pos of listview in dialog: "+position,Toast.LENGTH_SHORT).show();
                MainActivity.instance.addSongToPL(songList.get(pos),position);
                dialog.cancel();
            }
        });

    }

}
