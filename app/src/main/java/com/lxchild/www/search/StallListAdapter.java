package com.lxchild.www.search;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.lxchild.www.smartpark.R;

import java.util.ArrayList;

/**
 * Created by LXChild on 2015/6/3.
 */
public class StallListAdapter extends BaseAdapter {

    private String TAG = StallListLoader.class.getSimpleName();
    private LayoutInflater inflater;
    private ArrayList<StallEntity> stallInfos;

    public StallListAdapter(Context cxt) {
        this.inflater = LayoutInflater.from(cxt);
    }

    public void setData(ArrayList<StallEntity> data) {
        this.stallInfos = data;
    }

    @Override
    public int getCount() {
        return stallInfos != null ? stallInfos.size() : 0;
    }

    @Override
    public StallEntity getItem(int position) {
        return stallInfos.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        musicInfoHolder holder;
        if (convertView == null) {
            convertView = inflater.inflate(R.layout.layout_stalllist_item, parent, false);
            holder = new musicInfoHolder();
            holder.img = (ImageView) convertView.findViewById(R.id.iv_tag);
            holder.name = (TextView) convertView.findViewById(R.id.tv_name);
            holder.pos = (TextView) convertView.findViewById(R.id.tv_pos);

            convertView.setTag(holder);
        } else {
            holder = (musicInfoHolder) convertView.getTag();
        }

        holder.img.setImageResource(R.mipmap.ic_launcher);
        holder.name.setText(stallInfos.get(position).getId() + "");
        holder.pos.setText(stallInfos.get(position).getStatus());

        return convertView;
    }

    private class musicInfoHolder {
        ImageView img;
        TextView name;
        TextView pos;
    }
}
