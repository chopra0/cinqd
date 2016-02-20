package com.example.nancy.highfive;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import com.facebook.login.LoginManager;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;

/**
 * Created by Nancy on 7/3/2015.
 */
public class Mysettings extends ActionBarActivity {
    Toolbar mToolbar;
    Button privacy, terms, logout, delete;
    SharedPreferences sp = null;
    SharedPreferences.Editor editor;
    String result;
    int code;
    String line = null;
    InputStream is = null;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.mysettings);
        // setTheme(android.R.style.Theme_Holo);

        logout = (Button) findViewById(R.id.button3);
        delete = (Button) findViewById(R.id.button5);
        privacy = (Button) findViewById(R.id.button);
        terms = (Button) findViewById(R.id.button4);
        sp = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
        editor = sp.edit();
        mToolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(mToolbar);
        mToolbar.setNavigationIcon(R.drawable.abc_ic_ab_back_mtrl_am_alpha);
        //getSupportActionBar().setDisplayShowHomeEnabled(true);
        mToolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

        logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                LoginManager.getInstance().logOut();
                sp.edit().putString("loggedIn", "false").commit();
                finish();
            }
        });

        delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                LoginManager.getInstance().logOut();
                sp.edit().putString("loggedIn", "false").commit();
                new TheTask().execute();
                sp.edit().putString("loggedIn", "false").commit();
                finish();
            }
        });

        terms.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i=new Intent(Mysettings.this,Terms.class);
                startActivity(i);
            }
        });

        privacy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i=new Intent(Mysettings.this,Privacy.class);
                startActivity(i);
            }
        });

    }


    public void onBackPressed() {
        super.onBackPressed();
        overridePendingTransition(R.anim.left_trans_in, R.anim.left_trans_out);

    }

    class TheTask extends AsyncTask<String, String, String> {

        private ProgressDialog dialog;

        @Override
        protected void onPostExecute(String result) {
            // TODO Auto-generated method stub
            super.onPostExecute(result);

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
                    Toast.makeText(getApplicationContext(), "Deleted Successfully",
                            Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(getApplicationContext(), "Sorry, Try Again",
                            Toast.LENGTH_LONG).show();
                }
            } catch (Exception e) {
                Log.e("Fail 3", e.toString());
            }
        }

        @Override
        protected void onPreExecute() {
            // TODO Auto-generated method stub
            super.onPreExecute();

        }

        @Override
        protected String doInBackground(String... params) {
            ArrayList<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>();

            nameValuePairs.add(new BasicNameValuePair("id", sp.getString("id", " ")));

            try {
                HttpClient httpclient = new DefaultHttpClient();
                HttpPost httppost = new HttpPost("http://cinqd.com/HighFive/deleteAccount.php");
                httppost.setEntity(new UrlEncodedFormEntity(nameValuePairs));
                HttpResponse response = httpclient.execute(httppost);
                HttpEntity entity = response.getEntity();
                is = entity.getContent();
                Log.e("pass 1", "connection success ");
            } catch (Exception e) {
                Log.e("Fail 1", e.toString());
                Toast.makeText(getApplicationContext(), "Invalid IP Address",
                        Toast.LENGTH_LONG).show();
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
                Log.e("Fail 2", e.toString());
            }

            publishProgress();

            return null;
        }
    }

}



