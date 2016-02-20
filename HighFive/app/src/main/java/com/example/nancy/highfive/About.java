package com.example.nancy.highfive;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.widget.Toast;

import com.facebook.AccessToken;
import com.facebook.AccessTokenTracker;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.FacebookSdk;
import com.facebook.GraphRequest;
import com.facebook.GraphResponse;
import com.facebook.Profile;
import com.facebook.ProfileTracker;
import com.facebook.login.LoginResult;
import com.facebook.login.widget.LoginButton;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;

import gpsTracking.highfive.GPSTracker;


public class About extends Activity {
    ViewPager viewPager;
    LoginButton loginButton;
    GPSTracker gps;
    private CallbackManager callbackManager;
    private AccessTokenTracker accessTokenTracker;
    private ProfileTracker profileTracker;
    InputStream is = null;
    String result = "";
    String line = null;
    SharedPreferences prefs1;
    SharedPreferences.Editor editor;
    int code;
    JSONObject obj;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        prefs1 = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
        editor = prefs1.edit();
        editor.putString("lat", "0");
        editor.putString("long", "0");
        editor.putString("tracked", "flase");
        editor.commit();
        FacebookSdk.sdkInitialize(getApplicationContext());
        setContentView(R.layout.viewpager);
        callbackManager = CallbackManager.Factory.create();


        if (prefs1.getString("loggedIn", "false").equals("true")) {
            locationFinder();
            Intent i = new Intent(About.this, MainScreenActivity.class);
            startActivity(i);
            finish();
        }


        accessTokenTracker = new AccessTokenTracker() {
            @Override
            protected void onCurrentAccessTokenChanged(AccessToken oldToken, AccessToken newToken) {
                //  if (isResumed()) {
                //  }

                if (newToken != null) {

                } else {

                }
            }
        };


        profileTracker = new ProfileTracker() {
            @Override
            protected void onCurrentProfileChanged(Profile oldProfile, Profile newProfile) {
                displayMessage(newProfile);
            }
        };

        accessTokenTracker.startTracking();
        profileTracker.startTracking();

        ViewPager viewPager = (ViewPager) findViewById(R.id.viewPager);
        CustomPage adapter = new CustomPage(this);
        viewPager.setAdapter(adapter);
        loginButton = (LoginButton) findViewById(R.id.login_button);
        loginButton.setReadPermissions(Arrays.asList("public_profile, email, user_birthday, user_friends"));
        loginButton.registerCallback(callbackManager, callback);

    }

    @Override
    protected void onPause() {
        super.onPause();
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        callbackManager.onActivityResult(requestCode, resultCode, data);
    }

    private void displayMessage(Profile profile) {
        if (profile != null) {
        }
    }

    private FacebookCallback<LoginResult> callback = new FacebookCallback<LoginResult>() {
        @Override
        public void onSuccess(LoginResult loginResult) {
            GraphRequest request = GraphRequest.newMeRequest(AccessToken.getCurrentAccessToken(),
                    new GraphRequest.GraphJSONObjectCallback() {
                        @Override
                        public void onCompleted(JSONObject object, GraphResponse response) {
                            try {
                                editor.putString("userName", object.getString("name"));
                                editor.putString("email", object.getString("email"));
                                editor.putString("gender", object.getString("gender"));
                                editor.putString("profilepic", Profile.getCurrentProfile().getProfilePictureUri(300, 300).toString());
                                editor.putString("link", object.getString("link"));
                                editor.putString("id", object.getString("id"));
                                Log.e("1", object.getString("birthday"));
                                String date = object.getString("birthday");
                                Calendar dob = Calendar.getInstance();
                                Calendar today = Calendar.getInstance();

                                dob.set(Integer.parseInt(date.substring(6, 10)), Integer.parseInt(date.substring(0, 2)), Integer.parseInt(date.substring(3, 5)));

                                int age = today.get(Calendar.YEAR) - dob.get(Calendar.YEAR);

                                if (today.get(Calendar.DAY_OF_YEAR) < dob.get(Calendar.DAY_OF_YEAR)) {
                                    age--;
                                }

                                Integer ageInt = new Integer(age);
                                String ageS = ageInt.toString();

                                editor.putString("loggedIn", "true");
                                editor.putString("age", ageS);
                                if (object.getString("gender").equals("male")) {
//                                    Toast.makeText(getApplicationContext(), "About me hu and value hai" + 2, Toast.LENGTH_SHORT).show();
                                    editor.putString("displayGender", "2");
                                } else {
//                                    Toast.makeText(getApplicationContext(), "About me hu and value hai" + 2, Toast.LENGTH_SHORT).show();
                                    editor.putString("displayGender", "1");
                                }

                                //GPS location is done by this
                                //  locationFinder();

                                editor.commit();
                                obj = object;
                                new TheTask().execute(obj.toString());
                                Intent i = new Intent(About.this, MainScreenActivity.class);
                                startActivity(i);

                            } catch (JSONException e) {
                                e.printStackTrace();
                            }


                        }

                    });

            request.executeAsync();
            finish();
        }


        @Override
        public void onCancel() {

        }

        @Override
        public void onError(FacebookException e) {

        }

    };

    @Override
    public void onStop() {
        super.onStop();

        accessTokenTracker.stopTracking();
        profileTracker.stopTracking();
    }

    @Override
    public void onResume() {
        super.onResume();
        locationFinder();
        Profile profile = Profile.getCurrentProfile();
        displayMessage(profile);
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
                json_data = new JSONObject(result);
                code = (json_data.getInt("code"));
                if (code == 1) {
//                    Toast.makeText(getApplicationContext(), "Inserted Successfully",
//                            Toast.LENGTH_SHORT).show();
                } else {
//                    Toast.makeText(getApplicationContext(), "Sorry, Try Again",
//                            Toast.LENGTH_LONG).show();
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

            try {
                nameValuePairs.add(new BasicNameValuePair("id", obj.getString("id")));
                nameValuePairs.add(new BasicNameValuePair("profilepic", Profile.getCurrentProfile().getProfilePictureUri(300, 300).toString()));
                nameValuePairs.add(new BasicNameValuePair("name", obj.getString("name")));
                nameValuePairs.add(new BasicNameValuePair("link", obj.getString("link")));
                nameValuePairs.add(new BasicNameValuePair("sex", obj.getString("gender")));
                nameValuePairs.add(new BasicNameValuePair("lat", prefs1.getString("lat", "0")));
                nameValuePairs.add(new BasicNameValuePair("long", prefs1.getString("long", "0")));
                nameValuePairs.add(new BasicNameValuePair("tracked", prefs1.getString("tracked", "false")));
                nameValuePairs.add(new BasicNameValuePair("age", prefs1.getString("age", "n/a")));
                Log.e("reg_id.....", prefs1.getString("regid", ""));
                nameValuePairs.add(new BasicNameValuePair("reg_id", prefs1.getString("regid", "")));
            } catch (JSONException e) {
                e.printStackTrace();
            }

            try {
                HttpClient httpclient = new DefaultHttpClient();
                HttpPost httppost = new HttpPost("http://cinqd.com/HighFive/set_data.php");
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

    public void locationFinder() {
        gps = new GPSTracker(About.this);

        // check if GPS enabled
        if (gps.canGetLocation()) {
            double latitude = gps.getLatitude();
            double longitude = gps.getLongitude();
            editor.putString("lat", String.valueOf(10.0));
            editor.putString("long", String.valueOf(longitude));
            editor.putString("tracked", "true");
            //   Toast.makeText(getApplicationContext(), "Your Location is - \nLat: " + latitude + "\nLong: " + longitude, Toast.LENGTH_LONG).show();
            //   Toast.makeText(getApplicationContext(), "" + distance(latitude, longitude, latitude + 3, longitude + 4), Toast.LENGTH_SHORT).show();
        } else {
            // can't get location
            // GPS or Network is not enabled
            // Ask user to enable GPS/network in settings
            gps.showSettingsAlert();
        }
    }

    private double deg2rad(double deg) {
        return (deg * Math.PI / 180.0);
    }

    private double rad2deg(double rad) {
        return (rad * 180.0 / Math.PI);
    }

    private double distance(double lat1, double lon1, double lat2, double lon2) {

        double theta = lon1 - lon2;

        double dist = Math.sin(deg2rad(lat1)) * Math.sin(deg2rad(lat2)) + Math.cos(deg2rad(lat1)) * Math.cos(deg2rad(lat2)) * Math.cos(deg2rad(theta));

        dist = Math.acos(dist);

        dist = rad2deg(dist);

        dist = dist * 60 * 1.1515;

        dist = dist * 1.609344;  //distance in km

        return (dist);

    }

}
