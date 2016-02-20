package com.example.nancy.highfive;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

/**
 * Created by Nancy on 7/20/2015.
 */
public class MatchFragment extends Fragment {

    private String TAG = "Match";

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.matchtab, container, false);

        Log.i(TAG, "tab1: onCreateView");

        return view;
    }

}
