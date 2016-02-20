package com.example.nancy.highfive;

import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.SurfaceTexture;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.preference.PreferenceManager;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Surface;
import android.view.TextureView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.facebook.login.widget.ProfilePictureView;
import com.koushikdutta.ion.Ion;

import java.io.IOException;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.List;

import andtinder.model.CardModel;
import andtinder.view.CardStackAdapter;
import model.CustomCardModel;


public final class CustomAdapter extends CardStackAdapter implements TextureView.SurfaceTextureListener, MediaPlayer.OnBufferingUpdateListener, MediaPlayer.OnCompletionListener, MediaPlayer.OnPreparedListener, MediaPlayer.OnVideoSizeChangedListener {

    private static final String TAG = "CustomAdapter";
    private MediaPlayer mMediaPlayer;
    CustomCardModel modelT;
    Surface s;
    SharedPreferences prefs1;
    SharedPreferences.Editor editor;
    String check = "";
    String vid = "";
    SurfaceTexture sur;
    Thread thread;

    public CustomAdapter(Context mContext) {
        super(mContext);
    }

    private List<CardModel> listNextRdv = new ArrayList<CardModel>();

    @Override
    public void onSurfaceTextureAvailable(SurfaceTexture surface, int width, int height) {
        sur = surface;
        s = new Surface(surface);
        Log.e("comp..........", "");
        try {
            mMediaPlayer = new MediaPlayer();
            mMediaPlayer.setSurface(s);
//            mMediaPlayer.setDataSource(String.valueOf(uri));
////            mMediaPlayer.prepare();
//            mMediaPlayer.prepareAsync();
/*
            try {
                mMediaPlayer.prepare();
            } catch (IOException e) {
                e.printStackTrace();
            }
  */
            mMediaPlayer.setOnBufferingUpdateListener(this);
            mMediaPlayer.setOnCompletionListener(this);
            mMediaPlayer.setOnPreparedListener(this);
            mMediaPlayer.setOnVideoSizeChangedListener(this);
            mMediaPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);

        } catch (IllegalArgumentException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (SecurityException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (IllegalStateException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }

    @Override
    public void onSurfaceTextureSizeChanged(SurfaceTexture surface, int width, int height) {
    }

    @Override
    public boolean onSurfaceTextureDestroyed(SurfaceTexture surface) {
        Log.e("he........", "");
        return false;
    }

    @Override
    public void onSurfaceTextureUpdated(SurfaceTexture surface) {
    }

    @Override
    public void onBufferingUpdate(MediaPlayer mp, int percent) {
    }

    @Override
    public void onCompletion(MediaPlayer mp) {
    }

    @Override
    public void onPrepared(MediaPlayer mp) {
        mp.start();
        //mMediaPlayer.setLooping(true);
    }

    @Override
    public void onVideoSizeChanged(MediaPlayer mp, int width, int height) {
    }

    public static class ViewHolder {
        public TextView card_Title;
        public ImageView gifImageView;
        public TextureView texture;

    }

    @Override
    public View getCardView(int position, final CustomCardModel model, View convertView, ViewGroup parent) {
        final ViewHolder holder;
        java.net.URI uri = null;
        prefs1 = PreferenceManager.getDefaultSharedPreferences(getContext());
        editor = prefs1.edit();
        //  editor.putString("onetimedone", "false").commit();
        //User user = (User) getItem(position);
        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.gif_card, parent, false);
            assert convertView != null;

            holder = new ViewHolder();
            holder.card_Title = (TextView) convertView.findViewById(R.id.title);
            holder.gifImageView = (ImageView) convertView.findViewById(R.id.image_g);
            holder.texture = (TextureView) convertView.findViewById(R.id.texture_v);

            modelT = model;
            if (!model.getVideo_url().equals("null")) {
//                vid = "http://scribzoo.com/AndroidFileUpload/uploads/VID_20150715_163845.mp4";
                /*
                StringBuilder sb = new StringBuilder();
                String str1 = "http://scribzoo.com/AndroidFileUpload/uploads/";
                String str2 = model.getVideo_url();
                vid = sb.append(str1).append(str2).toString();
                */
                try {
                    uri = new java.net.URI("http://cinqd.com/AndroidFileUpload/uploads/" + model.getVideo_url());
                } catch (URISyntaxException e) {
                    e.printStackTrace();
                }

//                vid = "http://scribzoo.com/AndroidFileUpload/uploads/" + model.getVideo_url();
                Log.e("url.....", String.valueOf(uri));
                holder.texture.setSurfaceTextureListener(this);
//                if (vid == "http://scribzoo.com/AndroidFileUpload/uploads/");
            }

            final java.net.URI finalUri = uri;
            holder.gifImageView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (model.getVideo_url().equals("null")) {
                        Toast.makeText(getContext(), "No video uploaded by user", Toast.LENGTH_SHORT).show();
                    } else if (!check.equals(model.getVideo_url())) {
                        Log.e("1.................", model.getVideo_url());
                        check = model.getVideo_url();
                        Toast.makeText(getContext(), "Loading Video...", Toast.LENGTH_LONG).show();
                        try {
                            mMediaPlayer.setDataSource(String.valueOf(finalUri));

                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                        mMediaPlayer.prepareAsync();
                        // mMediaPlayer.setDataSource(modelT.getImg_url());
/*
                        thread = new Thread() {
                            @Override
                            public void run() {

                                // mMediaPlayer.start();
                            }
                        };
                        thread.start();*/
                    } else {
                        Toast.makeText(getContext(), "Wait Loading....", Toast.LENGTH_SHORT);
                    }
                }
            });
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        if (!TextUtils.isEmpty(model.getTitle())) {
            holder.card_Title.setText(model.getTitle());
            holder.card_Title.setVisibility(View.VISIBLE);
        } else {
            holder.card_Title.setVisibility(View.GONE);
        }

    //    holder.gifImageView.setProfileId(model.getId());
        Ion.with(getContext())
                .load(model.getImg_url())
                .withBitmap()
                .placeholder(R.drawable.cinqdlogo1024)
                .error(R.drawable.dislike)
                .intoImageView(holder.gifImageView);
/*
        model.setOnCardDismissedListener(new CardModel.OnCardDismissedListener() {
            @Override
            public void onLike(CardModel card) {
                if (mMediaPlayer.isPlaying()) {
                    mMediaPlayer.stop();
                }
             //   thread.currentThread().interrupt();
                Log.e("L:destroying", "destroyed");
            }

            @Override
            public void onDislike(CardModel card) {
                if (mMediaPlayer.isPlaying()) {
                    mMediaPlayer.stop();
                }
               // thread.currentThread().interrupt();
                Log.e("D:destroying", "destroyed");
                //  holder.texture.destroyDrawingCache();
                //  System.gc();
            }
        });
/*
        try {
            // Start the MediaController
            MediaController mediacontroller = new MediaController(getContext());
            mediacontroller.setAnchorView(holder.video);
            // Get the URL from String VideoURL
            Uri video = Uri.parse("http://lafeimme.com/uploads/VID_20150626_031017.mp4");
            holder.video.setFocusable(true);
            holder.video.setVideoURI(video);
            holder.video.setMediaController(mediacontroller);
        } catch (Exception e) {
            Log.e("Error", e.getMessage());
            e.printStackTrace();
        }
        holder.video.requestFocus();
        holder.video.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
            // Close the progress bar and play the video
            public void onPrepared(MediaPlayer mp) {
                holder.video.start();
                holder.gifImageView.setVisibility(View.GONE);
            }
        });

*/
        return convertView;
    }
}