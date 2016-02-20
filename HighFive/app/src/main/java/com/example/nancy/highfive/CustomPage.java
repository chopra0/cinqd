package com.example.nancy.highfive;

import android.content.ContentResolver;
import android.content.Context;
import android.net.Uri;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.koushikdutta.ion.Ion;

/**
 * Created by Nancy on 6/30/2015.
 */
public class CustomPage extends PagerAdapter {
    Context context;
    private int[] GalImages = new int[]{
            R.drawable.aboutgif3,
            R.drawable.aboutimg1,
            R.drawable.aboutimg3
    };

    private String[] GalImage = new String[]{
            "http://cinqd.com/HighFive/aboutgif.gif",
            "http://cinqd.com/HighFive/aboutimg1.png",
            "http://cinqd.com/HighFive/aboutimg3.png"
    };

    // int[] imageId = {R.drawable.images, R.drawable.cats, R.drawable.five_moose};
    CustomPage(Context context) {
        this.context = context;
    }


    public Object instantiateItem(ViewGroup container, int position) {
        // TODO Auto-generated method stub
        ImageView imageView = new ImageView(context);
        // int padding = context.getResources().getDimensionPixelSize(R.dimen.padding_medium);
        // imageView.setPadding(padding, padding, padding, padding);
        imageView.setScaleType(ImageView.ScaleType.CENTER_INSIDE);
        //    Uri uri = Uri.parse("android.resource://your.package.here/drawable/image_name");
    /*    Uri imageUri = Uri.parse(ContentResolver.SCHEME_ANDROID_RESOURCE + "://" +
                context.getResources().getResourcePackageName(GalImages[position]) + '/' +
                context.getResources().getResourceTypeName(GalImages[position]) + '/' +
                context.getResources().getResourceEntryName(GalImages[position]));*/
        Ion.with(context)
                .load(String.valueOf(GalImage[position]))
                .withBitmap()
                .placeholder(R.drawable.cinqdlogo1024)
                .error(R.drawable.dislike)
                .intoImageView(imageView);
        //  imageView.setImageResource(GalImages[position]);
        ((ViewPager) container).addView(imageView, 0);
        return imageView;


    }

    @Override
    public int getCount() {
        return GalImages.length;
    }

    @Override
    public boolean isViewFromObject(View view, Object object) {
        return view == ((ImageView) object);
    }

    public void destroyItem(ViewGroup container, int position, Object object) {
        // TODO Auto-generated method stub
        ((ViewPager) container).removeView((ImageView) object);
    }
}
