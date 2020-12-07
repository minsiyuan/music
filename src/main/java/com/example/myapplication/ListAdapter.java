package com.example.myapplication;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.text.BreakIterator;
import java.util.List;

public class ListAdapter extends BaseAdapter {
    Context context;
    List<Song> list;
    LayoutInflater inflater=null;
    public ListAdapter(MainActivity mainActivity, List<Song> list){
        this.context=mainActivity;
        this.list=list;
        inflater = LayoutInflater.from(context);
    }
    @Override
    public int getCount() {
        return list.size();
    }

    @Override
    public Object getItem(int position) {
        return list.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    //定义holder对象
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        Myholder myholder;
        if(convertView==null){
            myholder=new Myholder();
            //引入布局
            convertView = inflater.inflate(R.layout.music, null);
            //实例化对象
            myholder.t_song = convertView.findViewById(R.id.local_song);
            myholder.t_singer = convertView.findViewById(R.id.local_singer);
            convertView.setTag(myholder);
        }else {
            myholder = (Myholder) convertView.getTag();
        }
        // 给控件赋值
        String string_song = list.get(position).getSong();
        if (string_song.length() >= 5
                && string_song.substring(string_song.length() - 4,
                string_song.length()).equals(".mp3")) {
            myholder.t_song.setText(string_song.substring(0,
                    string_song.length() - 4).trim());
        } else {
            myholder.t_song.setText(string_song.trim());
        }

        myholder.t_song.setText(list.get(position).song.toString());
        myholder.t_singer.setText(list.get(position).singer.toString());

        return convertView;
    }
    class Myholder {
        TextView  t_song, t_singer;
    }
    }
