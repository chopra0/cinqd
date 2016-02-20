/**
 * AndTinder v0.1 for Android
 *
 * @Author: Enrique López Mañas <eenriquelopez@gmail.com>
 * http://www.lopez-manas.com
 * <p>
 * TAndTinder is a native library for Android that provide a
 * Tinder card like effect. A card can be constructed using an
 * image and displayed with animation effects, dismiss-to-like
 * and dismiss-to-unlike, and use different sorting mechanisms.
 * <p>
 * AndTinder is compatible with API Level 13 and upwards
 * @copyright: Enrique López Mañas
 * @license: Apache License 2.0
 */

package andtinder.model;

import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;

public class CardModel {

    private String title;
    private Drawable cardImageDrawable;
    public String id;
    public String video_url;


    public OnCardDismissedListener mOnCardDismissedListener = null;

    public OnCardClickListener mOnCardClickListener = null;

    public interface OnCardDismissedListener {
        void onLike(CardModel card);

        void onDislike(CardModel card);
    }

    public interface OnCardClickListener {
        void onCardClick();
    }


    public CardModel(String title, Drawable drawable, String id, String video_url) {
        this(null, (Drawable) null);
        this.title = title;
        this.id = id;
        this.video_url = video_url;
    }

    public CardModel(String title, Drawable cardImage) {
        this.title = title;
        this.cardImageDrawable = cardImage;
    }

    public CardModel(String title, Bitmap cardImage, String id, String video_url) {
        this.title = title;
        this.id = id;
        this.video_url = video_url;
        this.cardImageDrawable = new BitmapDrawable(null, cardImage);
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getVideo_url() {
        return video_url;
    }

    public void setVideo_url(String video_url) {
        this.video_url = video_url;
    }


    public Drawable getCardImageDrawable() {
        return cardImageDrawable;
    }

    public void setCardImageDrawable(Drawable cardImageDrawable) {
        this.cardImageDrawable = cardImageDrawable;
    }

    public void setOnCardDismissedListener(OnCardDismissedListener listener) {
        this.mOnCardDismissedListener = listener;
        id = getId();
    }

    public OnCardDismissedListener getOnCardDismissedListener() {
        return this.mOnCardDismissedListener;

    }


    public void setOnClickListener(OnCardClickListener listener) {
        this.mOnCardClickListener = listener;
    }

    public OnCardClickListener getOnClickListener() {
        return this.mOnCardClickListener;
    }
}