package com.example.musicplayer1;

import android.app.Dialog;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.PopupMenu;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

public class StringAdapter1 extends BaseAdapter {

    private Context context;
    private int layout;
    private ArrayList<String> listString;

    public StringAdapter1(Context context, int layout, ArrayList<String> listString1) {
        this.context = context;
        this.layout = layout;
        this.listString = new ArrayList<>();
        listString.addAll(listString1);
    }

    @Override
    public int getCount() {
        return listString.size();
    }

    @Override
    public Object getItem(int position) {
        return listString.get(position);
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    private class ViewHolder{
        TextView txtTitle;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        final ViewHolder viewHolder;
        if(convertView==null){
            viewHolder = new ViewHolder();
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = inflater.inflate(layout,null);
            viewHolder.txtTitle = convertView.findViewById(R.id.textViewStringRow);
            convertView.setTag(viewHolder);
        }else{
            viewHolder = (ViewHolder) convertView.getTag();
        }
        viewHolder.txtTitle.setText(listString.get(position));

        return convertView;
    }

    public void setClickSetting(){

    }

}
