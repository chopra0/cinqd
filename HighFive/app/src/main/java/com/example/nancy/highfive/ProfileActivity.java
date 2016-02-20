package com.example.nancy.highfive;

import android.media.MediaPlayer;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.facebook.login.widget.ProfilePictureView;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by Nancy on 7/18/2015.
 */
public class ProfileActivity extends ActionBarActivity {
    private MediaPlayer mediaPlayer;
    private SurfaceHolder vidHolder;
    private SurfaceView vidSurface;
    InputStream is = null;
    String result = "";
    String line = null;
    int code;
    String pname, psex, page, pid, pvideo;
    ArrayList<HashMap<String, String>> jsonlist;
    TextView username, sex;

    String vidAddress = "https://archive.org/download/ksnn_compilation_master_the_internet/ksnn_compilation_master_the_internet_512kb.mp4";
    String vid2 = "http://lafeimme.com/uploads/VID_20150626_031017.mp4";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.profile);
        Bundle bundle = getIntent().getExtras();
        String id = bundle.getString("id");
        //String name = bundle.getString("username");
        new TheTask().execute(id);
        //vidSurface = (SurfaceView) findViewById(R.id.surfaceView);
        //vidHolder = vidSurface.getHolder();
        // vidHolder.addCallback(this);
        ProfilePictureView ppv1 = (ProfilePictureView) findViewById(R.id.profilepicview1);
        ppv1.setProfileId(id);
        username = (TextView) findViewById(R.id.name);
        sex = (TextView) findViewById(R.id.textView2);
        //username.setText(pname+" "+page+" "+psex);
        Button b = (Button) findViewById(R.id.button2);
        b.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //upload activity
            }
        });
    }

    /*@Override
    public void surfaceCreated(SurfaceHolder surfaceHolder) {
        try {
            mediaPlayer = new MediaPlayer();
            mediaPlayer.setDisplay(vidHolder);
            mediaPlayer.setDataSource(vid2);
            mediaPlayer.prepare();
            mediaPlayer.setOnPreparedListener(this);
            mediaPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    @Override
    public void surfaceChanged(SurfaceHolder surfaceHolder, int i, int i1, int i2) {

    }

    @Override
    public void surfaceDestroyed(SurfaceHolder surfaceHolder) {

    }

    @Override
    public void onPrepared(MediaPlayer mediaPlayer) {
        mediaPlayer.start();
    }*/


    class TheTask extends AsyncTask<String, String, JSONArray> {

        @Override
        protected void onPostExecute(JSONArray result1) {
            // TODO Auto-generated method stub
            super.onPostExecute(result1);
            try {
                JSONObject result = result1.getJSONObject(0);
                pname = result.getString("Name");
                pid = result.getString("id");
                psex = result.getString("Sex");
                page = result.getString("age");
                pvideo = result.getString("video");
                Log.d("name", pname + page);
                updateprofie(pname, page, psex);

            } catch (JSONException e) {
                e.printStackTrace();
            }


            // update textview here

        }

        @Override
        protected void onProgressUpdate(String... values) {
            super.onProgressUpdate(values);
            try {
                JSONObject json_data;
                json_data = new JSONObject(result);
                code = (json_data.getInt("code"));
                if (code == 1) {
                    //     Toast.makeText(getActivity(), "Inserted Successfully",
                    //            Toast.LENGTH_SHORT).show();
                } else {
                    //     Toast.makeText(getActivity(), "Sorry, Try Again",
                    //             Toast.LENGTH_LONG).show();
                }
            } catch (Exception e) {
                Log.e("Fail p3", e.toString());
            }
        }

        @Override
        protected void onPreExecute() {
            // TODO Auto-generated method stub
            super.onPreExecute();
        }

        @Override
        protected JSONArray doInBackground(String... params) {
            ArrayList<BasicNameValuePair> nameValuePairs = new ArrayList<>();

            nameValuePairs.add(new BasicNameValuePair("liker_id", params[0]));

            //   Toast.makeText(getActivity(),date.substring(0,1),Toast.LENGTH_SHORT).show();

            try {
                HttpClient httpclient = new DefaultHttpClient();
                HttpPost httppost = new HttpPost("http://cinqd.com/HighFive/view_profile.php");
                httppost.setEntity(new UrlEncodedFormEntity(nameValuePairs));
                HttpResponse response = httpclient.execute(httppost);
                HttpEntity entity = response.getEntity();
                is = entity.getContent();
                Log.e("pass 1", "connection success ");
            } catch (Exception e) {
                Log.e("Fail 1", e.toString());
                // Toast.makeText("","Invalid IP Address", Toast.LENGTH_LONG).show();
            }

            try {
                BufferedReader reader = new BufferedReader
                        (new InputStreamReader(is, "iso-8859-1"), 8);
                StringBuilder sb = new StringBuilder();
                while ((line = reader.readLine()) != null) {
                    sb.append(line + "\n");
                }
                is.close();
                result = sb.toString();
                Log.e("pass 2", "connection success ");
            } catch (Exception e) {
                Log.e("Fail p2", e.toString());
            }

            // publishProgress();
            JSONArray jArray = null;
            try {
                jArray = new JSONArray(result);
                Log.d("yolop", jArray.toString());
            } catch (JSONException e) {
                e.printStackTrace();
            }
            //  jsonlist.removeAll(jsonlist);


            return jArray;

        }
    }

    private void updateprofie(String pname, String page, String psex) {
        username.setText(pname + "," + page);
        if (psex.equals("male")) {
            sex.setText("Male");
        }
        if (psex.equals("female")) {
            sex.setText("Female");
        }

    }
}