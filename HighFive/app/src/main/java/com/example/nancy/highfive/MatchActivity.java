package com.example.nancy.highfive;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Typeface;
import android.os.AsyncTask;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

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
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URL;
import java.util.ArrayList;

import andtinder.view.ProfilePictureView;


public class MatchActivity extends Activity {

    ProfilePictureView iv1, iv2;
    Animation an1, an2;
    Button b1;
    Intent i;
    InputStream is = null;
    String result = "";
    String line = null;
    int code;
    ShareExternalServer appUtil;
    String regId, nId;
    SharedPreferences prefs1;
    SharedPreferences.Editor editor;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        prefs1 = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
        editor = prefs1.edit();
        regId = prefs1.getString("regid", "");
        Log.e("aaja", regId);
        setContentView(R.layout.activity_match);
        iv1 = (ProfilePictureView) findViewById(R.id.imageView3);
        iv2 = (ProfilePictureView) findViewById(R.id.imageView4);
        b1 = (Button) findViewById(R.id.button6);
        an1 = AnimationUtils.loadAnimation(getBaseContext(), R.anim.flyin);
        iv1.startAnimation(an1);
        an2 = AnimationUtils.loadAnimation(getBaseContext(), R.anim.flyin);
        iv2.startAnimation(an2);
        TextView tx = (TextView) findViewById(R.id.textView4);
        Typeface custom_font = Typeface.createFromAsset(getAssets(), "font/Filxgirl.TTF");
        tx.setTypeface(custom_font);
        i = getIntent();
        iv1.setProfileId(i.getStringExtra("pic1"));
        iv2.setProfileId(i.getStringExtra("pic2"));
        nId = i.getStringExtra("pic2");
        Log.e("AAJAAAAA", nId);
        b1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(MatchActivity.this,ProfileActivity.class);
                intent.putExtra("id",nId);
                startActivity(intent);
                finish();
            }
        });
        editor.putString("match", "yes").commit();
        new TheTask().execute();
        //  new ShareRegidTask().execute();

        String url = "http://cinqd.com/gcm/gcm.php?push=true&ID=" + nId + "&message=Match";

        URL obj = null;
        try {
            obj = new URL(url);

            Log.e("gcm url now", obj.toString());
            HttpURLConnection con = (HttpURLConnection) obj.openConnection();

            // optional default is GET
            con.setRequestMethod("GET");

            //add request header
            //   con.setRequestProperty("User-Agent", USER_AGENT);

//            int responseCode = con.getResponseCode();
//        System.out.println("\nSending 'GET' request to URL : " + url);
//        System.out.println("Response Code : " + responseCode);

            //           BufferedReader in = new BufferedReader(
//                    new InputStreamReader(con.getInputStream()));
/*            String inputLine;
//            StringBuffer response = new StringBuffer();

            while ((inputLine = in.readLine()) != null) {
                response.append(inputLine);
            }
            in.close();
  */
        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (ProtocolException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        //print result
        //  System.out.println(response.toString());


        /*
        URL url = null;
        try {
            url = new URL("http://cinqd.com/gcm/gcm.php?push=true&ID="+nId+"&message=Match");
            Log.e("gcm url", String.valueOf(url));
            URLConnection conn = url.openConnection();
            conn.connect();
        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
*/

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_match, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    class TheTask extends AsyncTask<String, String, String> {

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
                Log.e("result......", result);
                json_data = new JSONObject(result);
                code = (json_data.getInt("code"));
                if (code == 1) {
                    Toast.makeText(getApplicationContext(), "Inserted Successfully",
                            Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(getApplicationContext(), "Sorry, Try Again",
                            Toast.LENGTH_LONG).show();
                }
            } catch (Exception e) {
                Log.e("Fail 3................", e.toString());
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
            nameValuePairs.add(new BasicNameValuePair("first", i.getStringExtra("pic1")));
            nameValuePairs.add(new BasicNameValuePair("second", i.getStringExtra("pic2")));

            try {
                HttpClient httpclient = new DefaultHttpClient();
                HttpPost httppost = new HttpPost("http://cinqd.com/HighFive/set_match.php");
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

    class ShareRegidTask extends AsyncTask<String, String, String> {
        @Override
        protected String doInBackground(String... params) {

            String result = appUtil.shareRegIdWithAppServer(nId, regId);
            Log.e("pls", result);
            return result;
        }


        protected void onPostExecute(String result) {
            // shareRegidTask = null;
            Toast.makeText(getApplicationContext(), result,
                    Toast.LENGTH_LONG).show();

        }
    }
}
