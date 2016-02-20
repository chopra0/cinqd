package com.example.nancy.highfive;

import android.app.Activity;
import android.os.Bundle;

/**
 * Created by Nancy on 6/24/2015.
 */
public class YourVideos extends Activity {
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.videos);
        overridePendingTransition(R.anim.left_trans_in, R.anim.left_trans_out);
    }
    public void onBackPressed() {
        super.onBackPressed();
        overridePendingTransition(R.anim.right_trans_in, R.anim.right_trans_out);
    }
}
