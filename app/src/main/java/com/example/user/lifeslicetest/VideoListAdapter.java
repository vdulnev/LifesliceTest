package com.example.user.lifeslicetest;

import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.support.annotation.NonNull;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.util.List;

/**
 * Created by User on 26.01.2017.
 */

public class VideoListAdapter extends ArrayAdapter<Record> {

    private int selectedItem = -1;

    public VideoListAdapter(Context context, int resource, List<Record> objects) {
        super(context, resource, objects);
    }

    @NonNull
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        Log.d("lifeslicetest", "getView: " + position);
        View v;
        if (convertView == null) {
            LayoutInflater l = (LayoutInflater)getContext().getSystemService
                    (Context.LAYOUT_INFLATER_SERVICE);
            v = l.inflate(R.layout.list_item, null);
            Holder h = new Holder();
            h.avatar = (ImageView) v.findViewById(R.id.avatar);
            h.userName = (TextView) v.findViewById(R.id.userName);
            h.thumbNail = (ImageView) v.findViewById(R.id.thumbnail);
            h.progressBar = (ProgressBar) v.findViewById(R.id.progress_bar);
            v.setTag(h);
        } else {
            v = convertView;
        }
        Holder h = (Holder) v.getTag();
        h.setData(position);
        return v;
    }



    public int getSelectedItem() {
        return selectedItem;
    }

    public void setSelectedItem(int selectedItem) {
        this.selectedItem = selectedItem;
    }

    public class Holder {
        ImageView avatar;
        TextView userName;
        ImageView thumbNail;
        ProgressBar progressBar;

        void setData(int position) {
            Record r = getItem(position);
            if (r != null) {
                Picasso.with(getContext()).load(r.getAvatarUrl()).into(avatar);
                userName.setText(r.getUsername());
                if (position == selectedItem) {
                    userName.setTextColor(Color.BLUE);
                    progressBar.setVisibility(View.VISIBLE);
                } else {
                    userName.setTextColor(Color.BLACK);
                    progressBar.setVisibility(View.GONE);
                }
                Picasso.with(getContext()).load(r.getThumbnailUrl()).into(thumbNail);
            }
        }
    }
}
