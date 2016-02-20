package com.example.nancy.highfive;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.preference.PreferenceManager;
import android.text.TextUtils;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import com.google.android.gms.gcm.GoogleCloudMessaging;

import java.io.IOException;

import gcm.*;
import gcm.Config;


public class Splash extends Activity {

    GoogleCloudMessaging gcm;
    Context context;
    String regId;
    private static final long SPLASH_TIME_OUT = 4000;
    private static final int PLAY_SERVICES_RESOLUTION_REQUEST = 9000;
    private static final String TAG = "Splash";
    public static final String REG_ID = "regId";
    private static final String APP_VERSION = "appVersion";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
        if (TextUtils.isEmpty(regId)) {
            regId = registerGCM();
//            Log.d("RegisterActivity", "GCM RegId: " + regId);
        } else {
//            Toast.makeText(getApplicationContext(),
//                    "Already Registered with GCM Server!",
//                    Toast.LENGTH_LONG).show();
        }
        new Handler().postDelayed(new Runnable() {

            /*
             * Showing splash screen with a timer. This will be useful when you
             * want to show case your app logo / company
             */

            @Override
            public void run() {
                // This method will be executed once the timer is over
                // Start your app main activity
                Intent i = new Intent(Splash.this, About.class);
                startActivity(i);

                // close this activity
                finish();
            }
        }, SPLASH_TIME_OUT);
    }




    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_splash, menu);
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
    public String registerGCM() {

        gcm = GoogleCloudMessaging.getInstance(this);
        regId = getRegistrationId(context);

        if (TextUtils.isEmpty(regId)) {


            new RegisterInBackground().execute();

//            Log.d("RegisterActivity",
//                    "registerGCM - successfully registered with GCM server - regId: "
//                            + regId);
        } else {
//            Toast.makeText(getApplicationContext(),
//                    "RegId already available. RegId: " + regId,
//                    Toast.LENGTH_LONG).show();
        }
        return regId;
    }
    private String getRegistrationId(Context context) {
        final SharedPreferences prefs = getSharedPreferences(
                Splash.class.getSimpleName(), Context.MODE_PRIVATE);
        String registrationId = prefs.getString(REG_ID, "");
        if (registrationId.isEmpty()) {
//            Log.i(TAG, "Registration not found.");
            return "";
        }
        int registeredVersion = prefs.getInt(APP_VERSION, Integer.MIN_VALUE);
        int currentVersion = getAppVersion(context);
        if (registeredVersion != currentVersion) {
//            Log.i(TAG, "App version changed.");
            return "";
        }
        return registrationId;
    }

    private static int getAppVersion(Context context) {
        try {
            PackageInfo packageInfo = context.getPackageManager().getPackageInfo(context.getPackageName(), 0);
            return packageInfo.versionCode;
        } catch (PackageManager.NameNotFoundException e) {
//            Log.d("RegisterActivity",
//                    "I never expected this! Going down, going down!" + e);
            throw new RuntimeException(e);
        }
    }


    /*  private void storeRegistrationId(Context context, String regId) {
          final SharedPreferences prefs = getSharedPreferences(
                  MainActivity.class.getSimpleName(), Context.MODE_PRIVATE);
          int appVersion = getAppVersion(context);
          Log.i(TAG, "Saving regId on app version " + appVersion);
          SharedPreferences.Editor editor = prefs.edit();
          editor.putString(REG_ID, regId);
          editor.putInt(APP_VERSION, appVersion);
          editor.commit();
      } */
    class RegisterInBackground extends AsyncTask<String, String, String>


    {
        @Override
        protected String doInBackground(String... params) {
            String msg = "";
            //  final SharedPreferences prefs = getShared
            //         Ma.class.getSimpleName(), Context.MODE_PRIVATE);
            final SharedPreferences  prefs = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
            try {
                if (gcm == null) {
                    gcm = GoogleCloudMessaging.getInstance(context);

                }
                regId = gcm.register(Config.GOOGLE_PROJECT_ID);
                Log.d("RegisterActivity", "registerInBackground - regId: "
                        + regId);
                msg = "Device registered, registration ID=" + regId;
                SharedPreferences.Editor editor = prefs.edit();
                editor.putString("regid", regId);
                editor.commit();
                Log.e("yahan reg id hai?",prefs.getString("regid","nooooooooooooooooooooooo"));

                // storeRegistrationId(context, regId);
            } catch (IOException ex) {
                msg = "Error :" + ex.getMessage();
                Log.d("RegisterActivity", "Error: " + msg);
            }
            Log.d("RegisterActivity", "AsyncTask completed: " + msg);
            return msg;
        }


        protected void onPostExecute(String msg) {
//            Toast.makeText(getApplicationContext(),
//                    "Registered with GCM Server." + msg, Toast.LENGTH_LONG)
//                    .show();
        }

    }
}

