package com.example.nancy.highfive;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.CheckBox;
import android.widget.SeekBar;
import android.widget.TextView;

/*
import com.hannesdorfmann.swipeback.Position;
import com.hannesdorfmann.swipeback.SwipeBack;
*/

public class Discover extends ActionBarActivity implements SeekBar.OnSeekBarChangeListener {
    CheckBox cb1, cb2;
    private Toolbar mToolbar;
    SeekBar dis;
    TextView tv2, seek_text;

    SharedPreferences sp;
    SharedPreferences.Editor editor;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.discover);
        sp = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
        editor = sp.edit();
        // setTheme(android.R.style.Theme_Holo);
  /*      SwipeBack.attach(this, Position.LEFT)
                .setContentView(R.layout.discover)
                .setSwipeBackView(R.layout.swipeback_default);
*/
        mToolbar = (Toolbar) findViewById(R.id.toolbar);
        //  mToolbar.setTitle("Discovery Preference");
        dis = (SeekBar) findViewById(R.id.seekBar);
        setSupportActionBar(mToolbar);
        mToolbar.setNavigationIcon(R.drawable.abc_ic_ab_back_mtrl_am_alpha);
        //getSupportActionBar().setDisplayShowHomeEnabled(true);
        mToolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

        overridePendingTransition(R.anim.right_trans_in, R.anim.right_trans_out);
        cb1 = (CheckBox) findViewById(R.id.checkBox);
        cb2 = (CheckBox) findViewById(R.id.checkBox2);
        tv2 = (TextView) findViewById(R.id.editText);
        seek_text = (TextView) findViewById(R.id.seekdis);
/*
        if (sp.getString("displayGender", "").equals("1")) {
            cb1.setChecked(true);
            tv2.setText("Men");
        } else if (sp.getString("displayGender", "").equals("2")) {
            cb2.setChecked(true);
            tv2.setText("Female");
        } else {
            cb1.setChecked(true);
            cb2.setChecked(true);
            tv2.setText("Men, Women");
        }
*/
        if (sp.getBoolean("firstRun", true)) {
            if (sp.getString("displayGender", "").equals("1")) {
                cb1.setChecked(true);
                tv2.setText("Men");
            } else if (sp.getString("displayGender", "").equals("2")) {
                cb2.setChecked(true);
                tv2.setText("Female");
            } else {
                cb1.setChecked(true);
                cb2.setChecked(true);
                tv2.setText("Men, Women");
            }
            seek_text.setText("10");
            dis.setProgress(10);
            sp.edit().putBoolean("firstRun", false).commit();
            save();
        } else {
            load();
        }


        cb1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (cb1.isChecked() && cb2.isChecked()) {
                    tv2.setText("Men, Women");
                    editor.putString("diplayGender", "3").commit();
                } else if (!(cb1.isChecked())) {
                    tv2.setText("Women");
                    cb2.setChecked(true);
                    editor.putString("diplayGender", "2").commit();
                } else if (cb1.isChecked()) {
                    tv2.setText("Men");
                    editor.putString("diplayGender", "1").commit();
                }
                save();
            }
        });
        cb2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (cb1.isChecked() && cb2.isChecked()) {
                    tv2.setText("Men, Women");
                    editor.putString("diplayGender", "3").apply();
                } else if (!(cb2.isChecked())) {
                    tv2.setText("Men");
                    cb1.setChecked(true);
                    editor.putString("diplayGender", "1").apply();
                } else if (cb2.isChecked()) {
                    tv2.setText("Women");
                    editor.putString("diplayGender", "2").apply();
                }
                save();
            }
        });
        dis.setOnSeekBarChangeListener(this);
        dis.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int i, boolean b) {
                seek_text.setText("" + i);
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                save();
            }
        });

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        // menu.findItem(R.id.action_settings1).setVisible(true);
        return true;
    }

    public void finishActivity(View v) {
        super.onBackPressed();
    }


    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        if (id == android.R.id.home) {
            super.onBackPressed();
            overridePendingTransition(R.anim.left_trans_in, R.anim.left_trans_out);
            return true;

        }
        //noinspection SimplifiableIfStatement

        return super.onOptionsItemSelected(item);
    }

    private void save() {
        String s = tv2.getText().toString();
        int s2 = Integer.parseInt(seek_text.getText().toString());
        editor.putString("keys", s);
        editor.putInt("keys2", s2);
        editor.commit();
    }

    private void load() {

        String name = sp.getString("keys", null);
        int disin = sp.getInt("keys2", 0);
        cb1.setChecked(false);
        cb2.setChecked(false);
        tv2.setText(name);
        Log.d("seek", "dis=" + disin);
        seek_text.setText("" + disin);
        if (name.equals("Men, Women")) {
            cb1.setChecked(true);
            cb2.setChecked(true);
        } else if (name.equals("Men")) {
            cb1.setChecked(true);
        } else if (name.equals("Women")) {
            cb2.setChecked(true);
        } else if (name.equals(null)) {
            cb1.setChecked(true);
        }
        dis.setProgress(disin);

    }

    public void onBackPressed() {
        super.onBackPressed();
        overridePendingTransition(R.anim.left_trans_in, R.anim.left_trans_out);
    }


    @Override
    public void onProgressChanged(SeekBar seekBar, int i, boolean b) {

    }

    @Override
    public void onStartTrackingTouch(SeekBar seekBar) {

    }

    @Override
    public void onStopTrackingTouch(SeekBar seekBar) {

    }
}



