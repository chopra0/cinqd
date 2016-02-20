package com.example.nancy.highfive;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.Typeface;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.preference.PreferenceManager;
import android.provider.MediaStore;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.app.ActionBarDrawerToggle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.FrameLayout;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

import andtinder.view.ProfilePictureView;

/**
 * Created by Nancy on 7/24/2015.
 */

public class MainScreenActivity extends ActionBarActivity {

    private String TAG = "MainScreenActivity";

    //Interfaces
    private DrawerLayout mDrawerLayout;
    private ActionBarDrawerToggle mDrawerToggle;
    private TabBarView mTabBarView;
    private MainScreenPagerAdapter mMainScreenPagerAdapter;
    private ViewPager mViewPager;
    private ListView mDrawerList;
    private static final int CAMERA_CAPTURE_IMAGE_REQUEST_CODE = 100;
    private static final int CAMERA_CAPTURE_VIDEO_REQUEST_CODE = 200;
    public static final int MEDIA_TYPE_IMAGE = 1;
    public static final int MEDIA_TYPE_VIDEO = 2;
    private Uri fileUri;
    SharedPreferences prefs1;
    SharedPreferences.Editor editor;
    TextView tv1;
    ProfilePictureView ppv1;

    //Data
    private int PAGE_COUNT = 2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Log.i(TAG, "MainScreenActivity: onCreate()");

        setContentView(R.layout.activity_main1);
        prefs1 = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
        editor = prefs1.edit();
        prefs1.edit().putString("loggedIn", "true").commit();

       /* ppv1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(MainScreenActivity.this,ProfileActivity.class);
                i.putExtra("id",prefs1.getString("id", ""));
                i.putExtra("username",prefs1.getString("userName", "Not Available"));
                startActivity(i);
            }
        }); */

        LayoutInflater inflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        DrawerLayout drawer = (DrawerLayout) inflater.inflate(R.layout.decor, null); // "null" is important.

        // HACK: "steal" the first child of decor view
        ViewGroup decor = (ViewGroup) getWindow().getDecorView();
        View child = decor.getChildAt(0);
        decor.removeView(child);
        FrameLayout container = (FrameLayout) drawer.findViewById(R.id.container); // This is the container we defined just now.
        container.addView(child);
        // Make the drawer replace the first child
        decor.addView(drawer);
        setUpCustomTabs();
        setPagerListener();
        setUpNavigationDrawer();
        tv1 = (TextView) findViewById(R.id.userName);
        ppv1 = (ProfilePictureView) findViewById(R.id.profilepicview);
        ppv1.setProfileId(prefs1.getString("id", ""));
        tv1.setText(prefs1.getString("userName", ""));
        ppv1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(MainScreenActivity.this,ProfileActivity.class);
                i.putExtra("id",prefs1.getString("id", ""));
                i.putExtra("username",prefs1.getString("userName", "Not Available"));
                startActivity(i);
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (prefs1.getString("loggedIn", "false").equals("false")) {
            Intent i=new Intent(MainScreenActivity.this,About.class);
            startActivity(i);
            finish();
        }
    }

    private void setUpCustomTabs() {
        LayoutInflater mLayoutInflater = (LayoutInflater) this.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        View customTabView = mLayoutInflater.inflate(R.layout.custom_tab_view, null);
        mTabBarView = (TabBarView) customTabView.findViewById(R.id.customTabBar);
        mTabBarView.setStripHeight(10);
        mTabBarView.setStripColor(Color.parseColor("#c42128"));

        getSupportActionBar().setDisplayOptions(ActionBar.DISPLAY_SHOW_CUSTOM);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setCustomView(customTabView);

        mMainScreenPagerAdapter = new MainScreenPagerAdapter(getSupportFragmentManager());
        mViewPager = (ViewPager) findViewById(R.id.pager);
        mViewPager.setAdapter(mMainScreenPagerAdapter);
        mTabBarView.setViewPager(mViewPager);
    }

    private void setUpNavigationDrawer() {
        mDrawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
        mDrawerLayout.setScrimColor(Color.TRANSPARENT);
        mDrawerList = (ListView) findViewById(R.id.drawerList2);
        ListAdapter adapter = (ListAdapter) (new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, getResources().getStringArray(R.array.nav_drawer_labels))
        {
            @Override
            public View getView(int position, View convertView, ViewGroup parent) {
                TextView textView = (TextView) super.getView(position, convertView, parent);
                int textColor = R.color.app_color;
                textView.setTextColor(Color.parseColor("#c42128"));
                textView.setTextSize(15);
                textView.setTypeface(Typeface.DEFAULT_BOLD);
                return textView;
            }
        });
        mDrawerList.setAdapter(adapter);
        mDrawerList.setClickable(true);

       /* mDrawerList.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                Log.d("heha", "" + i);
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });*/
        /*ItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Log.d("heha", "" + i);
            }
        });*/
        //  mDrawerList.setOnItemClickListener(new DrawerItemClickListener());
        //  mDrawerLayout.setDrawerListener((DrawerLayout.DrawerListener) MainScreenActivity.this);
        mDrawerToggle = new ActionBarDrawerToggle(this, mDrawerLayout, R.string.drawer_open, R.string.drawer_close) {

            @Override
            public void onDrawerOpened(View drawerView) {
                super.onDrawerOpened(drawerView);
                Log.d(TAG, "Drawer Opened");
            }

            @Override
            public void onDrawerClosed(View drawerView) {
                super.onDrawerClosed(drawerView);
                Log.d(TAG, "Drawer Closed");
            }


        };

        mDrawerLayout.setDrawerListener(mDrawerToggle);

        mDrawerLayout.post(new Runnable() {
            @Override
            public void run() {
                mDrawerToggle.syncState();
            }
        });
        mDrawerList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Log.e("yahan hu mai", "");
                displayView(i);
                mDrawerLayout.closeDrawers();
            }
        });
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
      //  Log.e("..............", "");
        if (mDrawerToggle.onOptionsItemSelected(item)) {
        //    Log.d("hahahahaha", "aisa blog likhega tu");
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private void setPagerListener() {
        mViewPager.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
                mTabBarView.onPageScrolled(position, positionOffset, positionOffsetPixels);
            }

            @Override
            public void onPageSelected(int position) {
                Log.i(TAG, "Page: " + position);
                mTabBarView.onPageSelected(position);
            }

            @Override
            public void onPageScrollStateChanged(int state) {
                mTabBarView.onPageScrollStateChanged(state);
            }

        });


    }


    private void displayView(int position) {
        //  Fragment fragment = null;
        String title = getString(R.string.app_name);
        switch (position) {

            case 0:
                // fragment = new DiscoverFragment();
                Intent intent = new Intent(MainScreenActivity.this, Discover.class);
                startActivity(intent);
                //  title = getString(R.string.title_dis);
                break;
            case 1:
                // fragment = new MessagesFragment();
                //title = getString(R.string.title_messages);
                Intent intent2 = new Intent(MainScreenActivity.this, Mysettings.class);
                startActivity(intent2);
                break;
            case 2:
                Intent i = new Intent(MediaStore.ACTION_VIDEO_CAPTURE);

                fileUri = getOutputMediaFileUri(MEDIA_TYPE_VIDEO);

                // set video quality
                i.putExtra(MediaStore.EXTRA_VIDEO_QUALITY, 0);

                i.putExtra(MediaStore.EXTRA_OUTPUT, fileUri); // set the image file
                // name

                i.putExtra(MediaStore.EXTRA_DURATION_LIMIT, 5);
                // start the video capture Intent
                startActivityForResult(i, CAMERA_CAPTURE_VIDEO_REQUEST_CODE);
                //  startActivity(intent3);
                break;
            default:
                break;
        }/* if (fragment != null) {
                FragmentManager fragmentManager = getSupportFragmentManager();
                FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                fragmentTransaction.replace(R.id.container_body, fragment);
                fragmentTransaction.commit(); */

        // set the toolbar title
        getSupportActionBar().setTitle(title);

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        // if the result is capturing Image
        if (requestCode == CAMERA_CAPTURE_IMAGE_REQUEST_CODE) {
            if (resultCode == RESULT_OK) {

                // successfully captured the image
                // launching upload activity
                launchUploadActivity(true);


            } else if (resultCode == RESULT_CANCELED) {

                // user cancelled Image capture
                Toast.makeText(getApplicationContext(),
                        "User cancelled image capture", Toast.LENGTH_SHORT)
                        .show();

            } else {
                // failed to capture image
                Toast.makeText(getApplicationContext(),
                        "Sorry! Failed to capture image", Toast.LENGTH_SHORT)
                        .show();
            }

        } else if (requestCode == CAMERA_CAPTURE_VIDEO_REQUEST_CODE) {
            if (resultCode == RESULT_OK) {

                // video successfully recorded
                // launching upload activity
                launchUploadActivity(false);

            } else if (resultCode == RESULT_CANCELED) {

                // user cancelled recording
                Toast.makeText(getApplicationContext(),
                        "User cancelled video recording", Toast.LENGTH_SHORT)
                        .show();

            } else {
                // failed to record video
                Toast.makeText(getApplicationContext(),
                        "Sorry! Failed to record video", Toast.LENGTH_SHORT)
                        .show();
            }
        }
    }

    private void launchUploadActivity(boolean isImage) {
        Intent i = new Intent(MainScreenActivity.this, UploadActivity.class);
        i.putExtra("filePath", fileUri.getPath());
        i.putExtra("isImage", isImage);
        startActivity(i);
    }

    public Uri getOutputMediaFileUri(int type) {
        return Uri.fromFile(getOutputMediaFile(type));
    }

    private static File getOutputMediaFile(int type) {

        // External sdcard location
        File mediaStorageDir = new File(
                Environment
                        .getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES),
                Config.IMAGE_DIRECTORY_NAME);


        // Create the storage directory if it does not exist
        if (!mediaStorageDir.exists()) {
            if (!mediaStorageDir.mkdirs()) {
                Log.d("msa", "Oops! Failed create "
                        + Config.IMAGE_DIRECTORY_NAME + " directory");
                return null;
            }
        }

        // Create a media file name
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss",
                Locale.getDefault()).format(new Date());
        File mediaFile;
        if (type == MEDIA_TYPE_IMAGE) {
            mediaFile = new File(mediaStorageDir.getPath() + File.separator
                    + "IMG_" + timeStamp + ".jpg");
        } else if (type == MEDIA_TYPE_VIDEO) {
            mediaFile = new File(mediaStorageDir.getPath() + File.separator
                    + "VID_" + timeStamp + ".mp4");
        } else {
            return null;
        }

        return mediaFile;
    }


    public class MainScreenPagerAdapter extends FragmentPagerAdapter implements TabBarView.IconTabProvider {

        private int[] tab_icons = {R.drawable.cinqdlogogrey,
                R.drawable.matchicongrey,
                R.drawable.ic_tab_rock_bg500,
                R.drawable.cinqdlogored,
                R.drawable.matchiconred,
                R.drawable.ic_tab_rock_white};

        public MainScreenPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int pos) {
            switch (pos) {
                case 0:
                    return new MainViewFragment();
                case 1:
                    return new MatchListActivity();
                //case 2:
                //  return new Tab3();
                default:
                    return null;
            }
        }

        @Override
        public int getCount() {
            return PAGE_COUNT;
        }

        @Override
        public int getPageIconResId(int position) {
            return tab_icons[position];
        }

        @Override
        public CharSequence getPageTitle(int position) {
            switch (position) {
                case 0:
                    return "tab1";
                case 1:
                    return "tab2";
                case 2:
                    return "tab3";
            }
            return null;
        }
    }
}