package com.example.nancy.highfive;


import android.app.Activity;
import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.HashMap;

import andtinder.view.ProfilePictureView;

class ViewHolder {
    public TextView name;
    public ProfilePictureView profilePictureView;
}

public class LazyAdapter extends BaseAdapter {

    private Activity activity;
    private ArrayList<HashMap<String, String>> data;
    private static LayoutInflater inflater;
    MatchListActivity matchListActivity;
    ViewHolder holder;

    public LazyAdapter(Activity a, ArrayList<HashMap<String, String>> d) {
        activity = a;
        data = d;
          inflater = (LayoutInflater) activity.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        Log.d("here i m ", "1");
    }

    public int getCount() {
        return data.size();
    }

    public Object getItem(int position) {
        return position;
    }

    public long getItemId(int position) {
        return position;
    }

    public View getView(int position, View convertView, ViewGroup parent) {
        View vi = convertView;
        if (convertView == null) {
            vi = inflater.inflate(R.layout.match_row, null);
        }
      //  holder = new ViewHolder();
      //  holder.name = (TextView) convertView.findViewById(R.id.mname);
      //  holder.profilePictureView = (ProfilePictureView) convertView.findViewById(R.id.pic);
     //   convertView.setTag(holder);
        Log.d("here", "2");
         TextView name = (TextView)vi.findViewById(R.id.mname); // title
     ProfilePictureView pic=(ProfilePictureView)vi.findViewById(R.id.pic); // thumb image
        HashMap<String, String> jsonlist = new HashMap<String, String>();
        jsonlist = data.get(position);
        // Setting all values in listview
        Log.e("lolo",jsonlist.toString());
        name.setText(jsonlist.get(MatchListActivity.Name));
        Log.d("here lazy", MatchListActivity.Name);
        pic.setProfileId(jsonlist.get("Img"));
        return vi;
    }
}

