package com.example.vncreatures.controller;

import it.sephiroth.android.library.imagezoom.ImageViewTouch;

import java.io.File;
import java.net.URI;
import java.util.ArrayList;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.actionbarsherlock.view.Menu;
import com.actionbarsherlock.view.MenuItem;
import com.actionbarsherlock.widget.ShareActionProvider;
import com.androidquery.AQuery;
import com.example.vncreatures.R;
import com.example.vncreatures.common.Common;
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
            Bundle extras = new Bundle();
            if(savedInstanceState != null) {
                extras = savedInstanceState;
            }
            else {
                extras = getIntent().getExtras();
            }
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
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putInt(Common.CREATURE_URL_IMAGES_POSITION, mPosition);
        outState.putStringArrayList(Common.CREATURE_URL_IMAGES_LIST, mCreatureImage);
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

        // Inflate your menu.
        getSupportMenuInflater().inflate(R.menu.share_action, menu);
        // Set file with share history to the provider and set the share intent.
        MenuItem actionItem = menu
                .findItem(R.id.menu_item_share_action_provider_action_bar);
        ShareActionProvider actionProvider = (ShareActionProvider) actionItem
                .getActionProvider();
        actionProvider
                .setShareHistoryFileName(ShareActionProvider.DEFAULT_SHARE_HISTORY_FILE_NAME);
        // Note that you can set/change the intent any time,
        // say when the user has selected an image.
        actionProvider.setShareIntent(createShareIntent());

        return super.onCreateOptionsMenu(menu);
    }

    @Override
    protected Intent createShareIntent() {
        AQuery aQuery = new AQuery(this);
        String url = mCreatureImage.get(mPosition);
        File file = aQuery.makeSharedFile(url, "creature.png");
        if (file != null) {
            Intent shareIntent = new Intent(Intent.ACTION_SEND);
            shareIntent.setType("image/*");
            shareIntent.putExtra(Intent.EXTRA_STREAM, Uri.fromFile(file));
            return shareIntent;
        }
        return super.createShareIntent();
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
