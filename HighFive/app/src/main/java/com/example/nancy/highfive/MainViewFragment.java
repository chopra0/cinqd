package com.example.nancy.highfive;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.Toast;

import com.skyfishjy.library.RippleBackground;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
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

import andtinder.model.CardModel;
import andtinder.model.Orientations;
import andtinder.view.CardContainer;
import andtinder.view.ProfilePictureView;
import gpsTracking.highfive.GPSTracker;
import model.CustomCardModel;


public class MainViewFragment extends Fragment implements View.OnClickListener {


    private CardContainer mCardContainer;
    private static final String TAG = "MainActivity";
    ImageView imageView;
    RoundImage roundedImage;
    ProfilePictureView ppv;
    SharedPreferences prefs1;
    SharedPreferences.Editor editor;
    static String url = "http://cinqd.com/HighFive/get_data.php";
    private static final String TAG_AGE = "age";
    private static final String TAG_NAME = "Name";
    private static final String TAG_PIC = "profile_pic";
    InputStream is = null;
    String result = "";
    String line = null;
    int code;
    int like;
    String cid;
    String id;
    String gender = "male";
    GPSTracker gps;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.fragment_main_view, container, false);
        prefs1 = PreferenceManager.getDefaultSharedPreferences(getActivity());
        editor = prefs1.edit();
        rootView.findViewById(R.id.pass_button).setOnClickListener(this);
        rootView.findViewById(R.id.fave_button).setOnClickListener(this);
        ppv = (ProfilePictureView) rootView.findViewById(R.id.profilepic2);
        // for circular view
        final RippleBackground rippleBackground = (RippleBackground) rootView.findViewById(R.id.content);

        rippleBackground.startRippleAnimation();

        ppv.setProfileId(prefs1.getString("id", ""));
        ppv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(getActivity(),ProfileActivity.class);
                i.putExtra("id",prefs1.getString("id", ""));
                i.putExtra("username", prefs1.getString("userName", "Not Available"));
                startActivity(i);
            }
        });
        mCardContainer = (CardContainer) rootView.findViewById(R.id.card_container);
        mCardContainer.setOrientation(Orientations.Orientation.Ordered);


        return rootView;
    }

    @Override
    public void onResume() {
        super.onResume();
        int i = 0;
        i = locationFinder();
        if (i == 1 && prefs1.getString("match","no").equals("no")) {
            new LoadAllProducts().execute();
        }else{
            editor.putString("match","no").commit();
        }

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.pass_button:

                mCardContainer.missCard(5000, 100, mCardContainer.getTopCard());

                break;
            case R.id.fave_button:
                mCardContainer.missCard(-5000, 100, mCardContainer.getTopCard());

                break;

        }
    }

    private void cardDisliked(String id) {
        Log.i("Swipeable Cards", "I dislike the card");
        like = 0;
        Log.e("dislike", id);
        new TheTask().execute(id);
        //  Toast.makeText(getActivity(), "DisLike", Toast.LENGTH_SHORT).show();

    }

    private void cardLiked(String id) {
        Log.i("Swipeable Cards", "I like the card");
        //  Toast.makeText(getActivity(), id, Toast.LENGTH_SHORT).show();
        like = 1;
        Log.e("like", id);
        new TheTask().execute(id);

        //   Toast.makeText(getActivity(), "Like", Toast.LENGTH_SHORT).show();
    }


    class LoadAllProducts extends AsyncTask<String, String, JSONArray> {

        /**
         * Before starting background thread Show Progress Dialog
         */
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        /**
         * getting All products from url
         */
        protected JSONArray doInBackground(String... args) {
            // Building Parameters

            ArrayList<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>();

            nameValuePairs.add(new BasicNameValuePair("liker_id", prefs1.getString("id", "")));


            //   Toast.makeText(getActivity(),date.substring(0,1),Toast.LENGTH_SHORT).show();


            try {
                HttpClient httpclient = new DefaultHttpClient();
                HttpPost httppost = new HttpPost(url);
                httppost.setEntity(new UrlEncodedFormEntity(nameValuePairs));
                HttpResponse response = httpclient.execute(httppost);
                HttpEntity entity = response.getEntity();
                is = entity.getContent();
                Log.e("pass 1", "connection success ");
            } catch (Exception e) {
                Log.e("Fail 1", e.toString());
                Toast.makeText(getActivity(), "Invalid IP Address",
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
            Log.e("result hai", result);
            JSONArray jArray = null;
            try {
                Log.e("1", "here");
                jArray = new JSONArray(result);
                Log.e("1", "here");
            } catch (JSONException e) {
                e.printStackTrace();
            }
            return jArray;


        }

        /**
         * After completing background task Dismiss the progress dialog
         * *
         */
        protected void onPostExecute(final JSONArray json1) {

            if (getActivity() != null)
                getActivity().runOnUiThread(new Runnable() {
                    public void run() {
                        CustomAdapter adapter = new CustomAdapter(getActivity());
                        for (int i = 0; i < json1.length(); i++) {
                            JSONObject c = null;
                            try {
                                c = json1.getJSONObject(i);
                                String age = c.getString(TAG_AGE);
                                String title = c.getString(TAG_NAME);
                                title = title + ", " + age;
                                String profile = c.getString(TAG_PIC);
                                String id = c.getString("id");
                                String sex = c.getString("Sex");
                                String video=c.getString("video");
                                Log.e("sex", prefs1.getString("displayGender", ""));
                                if (prefs1.getString("displayGender", "").equals("1")) {
                                    gender = "male";
                                } else if (prefs1.getString("displayGender", "").equals("2")) {
                                    gender = "female";
                                } else if (prefs1.getString("displayGender", "").equals("3")) {
                                    gender = sex;
                                }

                                if (!id.equals(prefs1.getString("id", ""))) {
                                    final CustomCardModel cardModel = new CustomCardModel(title, profile, id, video);
                                    cardModel.setOnClickListener(new CardModel.OnCardClickListener() {
                                        @Override
                                        public void onCardClick() {
                                            Log.i("Swipeable Cards", "I am pressing the card");
                                            cid = cardModel.getId();
                                            // Toast.makeText(getActivity(), cardModel.getId(), Toast.LENGTH_SHORT).show();
                                        }
                                    });

                                    cardModel.setOnCardDismissedListener(new CardModel.OnCardDismissedListener() {
                                        @Override
                                        public void onLike(CardModel card) {
                                            // Toast.makeText(getActivity(), card.getId(), Toast.LENGTH_SHORT).show();

                                            //   Toast.makeText(getActivity(), cardModel.getId(), Toast.LENGTH_SHORT).show();
                                            cardLiked(cardModel.getId());
                                            new Match().execute(cardModel.getId());
                                        }

                                        @Override
                                        public void onDislike(CardModel card) {
                                            //  Toast.makeText(getActivity(), card.getId(), Toast.LENGTH_SHORT).show();
                                            //   Toast.makeText(getActivity(), cardModel.getId(), Toast.LENGTH_SHORT).show();
                                            //   Toast.makeText(getActivity(), cardModel.getId(), Toast.LENGTH_SHORT).show();
                                            cardDisliked(cardModel.getId());
                                        }
                                    });

                                    adapter.add(cardModel);

                                    mCardContainer.setAdapter(adapter);
                                }

                            } catch (JSONException e1) {
                                e1.printStackTrace();
                            }
                        }
                    }
                });

        }

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
                    //     Toast.makeText(getActivity(), "Inserted Successfully",
                    //            Toast.LENGTH_SHORT).show();
                } else {
                    //     Toast.makeText(getActivity(), "Sorry, Try Again",
                    //             Toast.LENGTH_LONG).show();
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

            nameValuePairs.add(new BasicNameValuePair("liker_id", prefs1.getString("id", "")));
            nameValuePairs.add(new BasicNameValuePair("liked_id", params[0]));
            nameValuePairs.add(new BasicNameValuePair("like", String.valueOf(like)));

            //   Toast.makeText(getActivity(),date.substring(0,1),Toast.LENGTH_SHORT).show();

            try {
                HttpClient httpclient = new DefaultHttpClient();
                HttpPost httppost = new HttpPost("http://cinqd.com/HighFive/set_like.php");
                httppost.setEntity(new UrlEncodedFormEntity(nameValuePairs));
                HttpResponse response = httpclient.execute(httppost);
                HttpEntity entity = response.getEntity();
                is = entity.getContent();
                Log.e("pass 1", "connection success ");
            } catch (Exception e) {
                Log.e("Fail 1", e.toString());
                Toast.makeText(getActivity(), "Invalid IP Address",
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


    class Match extends AsyncTask<String, String, String> {

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
                // JSONObject json_data;
                Log.e("json_data", result);
                JSONArray jArray = new JSONArray(result);

                if (jArray.length() == 0) {
                       Toast.makeText(getActivity(), "Not a match",
                             Toast.LENGTH_SHORT).show();
                } else {
                    JSONObject json_data = jArray.getJSONObject(0);
                //       Toast.makeText(getActivity(), "Match, " + json_data.getString("liker_id"), Toast.LENGTH_SHORT).show();
                    Intent i = new Intent(getActivity(), MatchActivity.class);
                    i.putExtra("pic1", prefs1.getString("id", ""));
                    i.putExtra("pic2", values[0]);
                    startActivity(i);
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

            nameValuePairs.add(new BasicNameValuePair("liker_id", prefs1.getString("id", "")));
            nameValuePairs.add(new BasicNameValuePair("liked_id", params[0]));


            try {
                HttpClient httpclient = new DefaultHttpClient();
                HttpPost httppost = new HttpPost("http://cinqd.com/HighFive/match_details.php");
                httppost.setEntity(new UrlEncodedFormEntity(nameValuePairs));
                HttpResponse response = httpclient.execute(httppost);
                HttpEntity entity = response.getEntity();
                is = entity.getContent();
                Log.e("pass 1", "connection success ");
            } catch (Exception e) {
                Log.e("Fail 1", e.toString());
                Toast.makeText(getActivity(), "Invalid IP Address",
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

            publishProgress(params[0]);

            return null;
        }
    }

    public int locationFinder() {
        gps = new GPSTracker(getActivity());

        // check if GPS enabled
        if (gps.canGetLocation()) {
            double latitude = gps.getLatitude();
            double longitude = gps.getLongitude();
            editor.putString("lat", String.valueOf(10.0));
            editor.putString("long", String.valueOf(longitude));
            editor.putString("tracked", "true");
            return 1;
            //   Toast.makeText(getApplicationContext(), "Your Location is - \nLat: " + latitude + "\nLong: " + longitude, Toast.LENGTH_LONG).show();
            //   Toast.makeText(getApplicationContext(), "" + distance(latitude, longitude, latitude + 3, longitude + 4), Toast.LENGTH_SHORT).show();
        } else {
            // can't get location
            // GPS or Network is not enabled
            // Ask user to enable GPS/network in settings
            gps.showSettingsAlert();
            return 0;
        }
    }

    private double deg2rad(double deg) {
        return (deg * Math.PI / 180.0);
    }

    private double rad2deg(double rad) {
        return (rad * 180.0 / Math.PI);
    }


}