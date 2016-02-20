package model;

/**
 * Created by alouanemed on 09-02-2015.
 */

import android.graphics.drawable.Drawable;

import andtinder.model.CardModel;


public class CustomCardModel extends CardModel {

    private String img_url;



    private String video_url;

    private CardModel.OnCardDismissedListener mOnCardDismissedListener = null;

    private OnCardClickListener mOnCardClickListener = null;

    public CustomCardModel(String title, String img_url, String id, String video_url) {
        super(title, (Drawable) null, id, video_url);
        this.img_url = img_url;
        this.video_url = video_url;
    }

    public String getVideo_url() {
        return video_url;
    }

    public void setVideo_url(String video_url) {
        this.video_url = video_url;
    }

    public String getImg_url() {
        return img_url;
    }

    public void setImg_url(String img_url) {
        this.img_url = img_url;
    }
}