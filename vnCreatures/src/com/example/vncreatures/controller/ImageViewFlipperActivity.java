package com.example.vncreatures.controller;

import it.sephiroth.android.library.imagezoom.ImageViewTouch;

import java.util.ArrayList;
import java.util.List;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.actionbarsherlock.view.Menu;
import com.actionbarsherlock.view.MenuItem;
import com.androidquery.AQuery;
import com.example.vncreatures.R;
import com.example.vncreatures.common.Common;
import com.example.vncreatures.customItems.BitmapManager;
import com.example.vncreatures.view.ImageViewTouchViewPager;

public class ImageViewFlipperActivity extends AbstractActivity {
    private int mPosition = -1;
    private ArrayList<String> mCreatureImage = new ArrayList<String>();

    @Override
    public void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        // Hide tabbar
        super.showTabbar(false);

        AQuery aQuery = new AQuery(this);

        ArrayList<String> listDelete = new ArrayList<String>();
        try {
            Bundle extras = getIntent().getExtras();
            if (extras != null) {
                mPosition = extras.getInt(Common.CREATURE_URL_IMAGES_POSITION);
                mCreatureImage = extras
                        .getStringArrayList(Common.CREATURE_URL_IMAGES_LIST);
                for (String urlTest : mCreatureImage) {
                    Bitmap placeholder = aQuery.getCachedImage(urlTest);
                    if (placeholder.getWidth() < 10) {
                        listDelete.add(urlTest);
                    }
                }

            }
        } catch (Exception e) {
        }
        for (String urlDel : listDelete) {
            mCreatureImage.remove(urlDel);
        }
        ImageViewTouchViewPager viewPager = (ImageViewTouchViewPager) findViewById(R.id.view_pager);
        ImagePagerAdapter adapter = new ImagePagerAdapter();
        viewPager.setAdapter(adapter);
        viewPager.setCurrentItem(mPosition);
    }

    @Override
    protected View createView() {
        return getLayoutInflater().inflate(R.layout.creature_image_flipper,
                null);
    }

    @Override
    protected void onResume() {
        // Transition
        overridePendingTransition(R.anim.push_left_in, R.anim.push_left_out);
        super.onResume();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        setTitle(getString(R.string.view_image));
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
        case android.R.id.home:
            finish();
            break;

        default:
            break;
        }
        return super.onOptionsItemSelected(item);
    }

    private class ImagePagerAdapter extends PagerAdapter {

        @Override
        public int getCount() {
            return mCreatureImage.size();
        }

        @Override
        public boolean isViewFromObject(View view, Object object) {
            return view == ((ImageViewTouch) object);
        }

        @Override
        public Object instantiateItem(ViewGroup container, int position) {
            Context context = ImageViewFlipperActivity.this;
            AQuery aQuery = new AQuery(context);
            ImageViewTouch imageView = new ImageViewTouch(context, null);
            aQuery.id(imageView).image(mCreatureImage.get(position), true,
                    true, 0, AQuery.GONE);
            imageView.setFitToScreen(true);
            imageView.setPadding(10, 0, 10, 0);
            ((ViewPager) container).addView(imageView, 0);
            return imageView;
        }

        @Override
        public void destroyItem(ViewGroup container, int position, Object object) {
            ((ViewPager) container).removeView((ImageView) object);
        }

    }

    @Override
    protected int indentifyTabPosition() {
        return R.id.tabHome_button;
    }
}
