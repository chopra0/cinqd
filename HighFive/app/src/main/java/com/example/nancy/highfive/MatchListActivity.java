package com.example.nancy.highfive;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v4.app.ListFragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

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
import java.util.HashMap;


public class MatchListActivity extends ListFragment {

    static String url = "http://cinqd.com/HighFive/get_data.php";
    static String Name = "Name";
    static String Img_url = "id";
    InputStream is = null;
    String result = "";
    String line = null;
    int code;
    String id;
    ListView lv;
    SharedPreferences prefs1;
    SharedPreferences.Editor editor;
    // JSONArray matchlist = null;
    ArrayList<HashMap<String, String>> jsonlist;
    ArrayList<String> idarray,namearray;
    // private static final String TAG_CONTACTS = "match1";

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        prefs1 = PreferenceManager.getDefaultSharedPreferences(getActivity());
        editor = prefs1.edit();
        //  editor.putString("id", "");
        // editor.commit();
        View rootView = inflater.inflate(R.layout.activity_match_list, container, false);
        //  setContentView(R.layout.activity_match_list);
        jsonlist = new ArrayList<HashMap<String, String>>();
        idarray=new ArrayList<String>();
        namearray=new ArrayList<String>();
        return rootView;
    }


    @Override
    public void onResume() {
        super.onResume();
        new GetContacts().execute();
    }

    private class GetContacts extends AsyncTask<String, String, JSONArray> {

        /*@Override
         protected Void doInBackground(Void... voids) {

             JSONParser jparser = new JSONParser();
             JSONArray json = jparser.getJSONFromUrl(url);
             // al.removeAll(al);
             // al2.removeAll(al2);
             jsonlist.removeAll(jsonlist);
             for (int i = 0; i < json.length(); i++) {
                 try {
                     JSONObject c = json.getJSONObject(i);
                     String title = c.getString(Liker1);
                     String author = c.getString(Liker2);
                     // String date_created = c.getString(DATE);
                     // String summary = c.getString(SUMMARY);
                     //String body = c.getString(BODY);
                     HashMap<String, String> map = new HashMap<String, String>();

                     // al2.add(title);
                     //al.add(body);
                     map.put(Liker1, title);
                     map.put(Liker2, author);
                     // map.put(DATE, date_created);
                     // map.put(SUMMARY, summary);
                     jsonlist.add(map);
                 } catch (JSONException e) {
                     e.printStackTrace();
                 }
             }
             ;
             return null;
         } */
        @Override
        protected void onProgressUpdate(String... values) {
            super.onProgressUpdate(values);
           /* try {
                JSONObject json_data;
                json_data = result.get;
              /*code = (json_data.getInt("code"));
                if (code == 1) {
                    Toast.makeText(getActivity(), "Inserted Successfully",
                            Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(getActivity(), "Sorry, Try Again",
                            Toast.LENGTH_LONG).show();
                }
            } catch (Exception e) {
                Log.e("Fail 3", e.toString());
            }*/
        }


        protected void onPreExecute() {
            super.onPreExecute();
        }

        protected JSONArray doInBackground(String... params) {
            ArrayList<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>();
            nameValuePairs.add(new BasicNameValuePair("id", prefs1.getString("id", "")));

            try {
                HttpClient httpclient = new DefaultHttpClient();
                HttpPost httppost = new HttpPost("http://cinqd.com/HighFive/get_match_list.php");
                httppost.setEntity(new UrlEncodedFormEntity(nameValuePairs));
                HttpResponse response = httpclient.execute(httppost);
                HttpEntity entity = response.getEntity();
                is = entity.getContent();
                Log.e("pass2 1", "connection success ");
            } catch (Exception e) {
                Log.e("Fail 1", e.toString());
                // Toast.makeText(getApplicationContext(), "Invalid IP Address", Toast.LENGTH_LONG).show();
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
                Log.e("pass2 2", "connection success ");
            } catch (Exception e) {
                Log.e("Fail2 2", e.toString());
            }

            //  publishProgress();
            JSONArray jArray = null;
            try {
                jArray = new JSONArray(result);
                Log.d("yolo", jArray.toString());
            } catch (JSONException e) {
                e.printStackTrace();
            }
            jsonlist.removeAll(jsonlist);
            idarray.removeAll(idarray);
            for (int i = 0; i < jArray.length(); i++) {
                try {
                    JSONObject c = jArray.getJSONObject(i);
                    String name = c.getString("Name");
                    String img_url = c.getString("id");
                    Log.d("name", name);
                    HashMap<String, String> map = new HashMap<String, String>();
                    map.put("Name", name);
                    map.put("Img", img_url);
                    idarray.add(img_url);
                    namearray.add(name);
                    Log.d("name:",name);
                    jsonlist.add(map);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
            return jArray;
        }


        protected void onPostExecute(JSONArray json1) {
            //LazyAdapter adapter = new LazyAdapter(MatchListActivity.this, jsonlist);
            //lv=getListView();
            // lv.setAdapter(adapter);
            //  for (int i = 0; i < json1.length(); i++) {
            /*  JSONObject c = null;
                ListAdapter adapter = new SimpleAdapter(getActivity(), jsonlist,
                       R.layout.match_row, new String[] { Name,Img_url}, new int[] { R.id.mname,R.id.pic});
               setListAdapter(adapter);
               lv = getListView();*/
            LazyAdapter adapter = new LazyAdapter(getActivity(), jsonlist);
            lv = getListView();
            lv.setAdapter(adapter);
            lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                    Intent intent = new Intent(getActivity(),ProfileActivity.class);
                    String mid=idarray.get(i);
                    String mname=namearray.get(i);
                    intent.putExtra("id",mid);
                    intent.putExtra("username",mname);
                    startActivity(intent);
                }
            });
            lv.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
                @Override
                public boolean onItemLongClick(AdapterView<?> adapterView, View view, int i, long l) {
                    displayAlert(i);
                    return true;
                }
            });
        }
    }

    public void displayAlert(final int pos) {
        AlertDialog.Builder alertDialog = new AlertDialog.Builder(getActivity());

        // Setting Dialog Title
        alertDialog.setTitle("UNMATCH!");

        // Setting Dialog Message
        alertDialog.setMessage("ARE YOU SURE YOU WANT TO UNMATCH?");

        // On pressing Settings button
        alertDialog.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                // Intent intent = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
                // getActivity().startActivity(intent);
                String unlike_id= idarray.get(pos);
                new TheTask().execute(unlike_id);
            }
        });

        // on pressing cancel button
        alertDialog.setNegativeButton("No", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();

            }
        });

        // Showing Alert Message
        alertDialog.show();
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
                Log.e("result dekho",result);
                json_data = new JSONObject(result);
                code = (json_data.getInt("code"));
                if (code == 1) {
                    Toast.makeText(getActivity(), "Unmatched Successfully",
                            Toast.LENGTH_SHORT).show();
                    onResume();
                } else {
                    Toast.makeText(getActivity(), "Sorry, Try Again",
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

            nameValuePairs.add(new BasicNameValuePair("id2", params[0]));
            nameValuePairs.add(new BasicNameValuePair("id", prefs1.getString("id", "")));

            try {
                HttpClient httpclient = new DefaultHttpClient();
                HttpPost httppost = new HttpPost("http://cinqd.com/HighFive/deleteMatch.php");
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
}