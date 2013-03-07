package com.example.vncreatures.common;

import java.io.IOException;
import java.io.InputStream;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.client.methods.HttpGet;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.http.AndroidHttpClient;
import android.util.Log;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.Animation.AnimationListener;
import android.view.animation.AnimationUtils;

import com.example.vncreatures.R;
import com.example.vncreatures.rest.HrmService;
import com.example.vncreatures.rest.HrmService.CheckUrlCallback;

public class Utils {

    public static boolean testUrl(String url) {
        final AndroidHttpClient client = AndroidHttpClient
                .newInstance("Android");
        final HttpGet getRequest = new HttpGet(url);

        try {
            HttpResponse response = client.execute(getRequest);
            final int statusCode = response.getStatusLine().getStatusCode();
            if (statusCode != HttpStatus.SC_OK) {
                Log.w("ImageDownloader", "Error " + statusCode
                        + " while retrieving bitmap from " + url);
                return false;
            }

            final HttpEntity entity = response.getEntity();
            if (entity != null) {
                InputStream inputStream = null;
                try {
                    inputStream = entity.getContent();
                    return true;
                } finally {
                    if (inputStream != null) {
                        inputStream.close();
                    }
                    entity.consumeContent();
                }
            }
        } catch (Exception e) {
            // Could provide a more explicit error message for IOException or
            // IllegalStateException
            getRequest.abort();
            Log.w("ImageDownloader", "Error while retrieving bitmap from "
                    + url);
        } finally {
            if (client != null) {
                client.close();
            }
        }
        return false;
    }

    public static String convertStreamToString(InputStream is)
            throws IOException {
        String text = null;
        try {
            int size = is.available();
            byte[] buffer = new byte[size];
            is.read(buffer);
            is.close();
            text = new String(buffer);
        } catch (Exception e) {
        }
        return text;
    }

    public static Bitmap getUserPic(String userID) {
        String imageURL;
        Bitmap bitmap = null;
        imageURL = "http://graph.facebook.com/" + userID
                + "/picture?type=small";
        bitmap = downloadBitmap(imageURL);
        return bitmap;
    }

    public static Bitmap downloadBitmap(String url) {
        final AndroidHttpClient client = AndroidHttpClient
                .newInstance("Android");
        final HttpGet getRequest = new HttpGet(url);

        try {
            HttpResponse response = client.execute(getRequest);
            final int statusCode = response.getStatusLine().getStatusCode();
            if (statusCode != HttpStatus.SC_OK) {
                Log.w("ImageDownloader", "Error " + statusCode
                        + " while retrieving bitmap from " + url);
                return null;
            }

            final HttpEntity entity = response.getEntity();
            if (entity != null) {
                InputStream inputStream = null;
                try {
                    inputStream = entity.getContent();
                    final Bitmap bitmap = BitmapFactory
                            .decodeStream(inputStream);
                    return bitmap;
                } finally {
                    if (inputStream != null) {
                        inputStream.close();
                    }
                    entity.consumeContent();
                }
            }
        } catch (Exception e) {
            // Could provide a more explicit error message for IOException or
            // IllegalStateException
            getRequest.abort();
            Log.w("ImageDownloader", "Error while retrieving bitmap from "
                    + url);
        } finally {
            if (client != null) {
                client.close();
            }
        }
        return null;
    }

    public static void toogleLayout(View v) {
        if (v.getVisibility() == View.VISIBLE) {
            Utils.hideView(v);
        } else {
            Utils.displayView(v);
        }

    }

    public static void displayView(final View activateView) {

        final Animation fadeInFromTopAnimation = AnimationUtils.loadAnimation(
                activateView.getContext(), R.anim.fade_in_from_top);
        activateView.startAnimation(fadeInFromTopAnimation);
        activateView.setVisibility(View.VISIBLE);
    }

    public static void hideView(final View activateView) {
        // fade in from top
        if (activateView.getVisibility() == View.VISIBLE) {
            final Animation fadeOutFromTopAnimation = AnimationUtils
                    .loadAnimation(activateView.getContext(),
                            R.anim.fade_out_from_bottom);
            fadeOutFromTopAnimation
                    .setAnimationListener(new AnimationListener() {

                        @Override
                        public void onAnimationStart(Animation animation) {

                        }

                        @Override
                        public void onAnimationRepeat(Animation animation) {

                        }

                        @Override
                        public void onAnimationEnd(Animation animation) {
                            activateView.setVisibility(View.GONE);
                        }
                    });
            activateView.startAnimation(fadeOutFromTopAnimation);
        }
    }
}
