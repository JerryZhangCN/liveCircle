package com.lvr.livecircle.utils;

import android.app.Activity;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.lzy.imagepicker.loader.ImageLoader;

import java.io.File;

/**
 * Created by peter on 28/12/2016.
 */

public class GlideImageLoader implements ImageLoader {
    @Override
    public void displayImage(Activity activity, String path, ImageView imageView, int width, int height) {
        Glide.with(activity)//
                .load(new File(path))//
                .into(imageView);
    }


    @Override
    public void clearMemoryCache() {

    }
}
