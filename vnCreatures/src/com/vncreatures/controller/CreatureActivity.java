package com.vncreatures.controller;

import java.util.ArrayList;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;

import com.actionbarsherlock.view.Menu;
import com.actionbarsherlock.view.MenuItem;
import com.vncreatures.R;
import com.vncreatures.common.Common;
import com.vncreatures.common.ServerConfig;
import com.vncreatures.common.Common.CREATURE;
import com.vncreatures.customItems.GalleryImageAdapter;
import com.vncreatures.model.Creature;
import com.vncreatures.model.CreatureDescriptionViewModel;
import com.vncreatures.model.CreatureModel;
import com.vncreatures.rest.HrmService;
import com.vncreatures.rest.HrmService.Callback;
import com.vncreatures.rest.HrmService.CheckUrlCallback;
import com.vncreatures.view.CreatureDescriptionView;

public class CreatureActivity extends AbstractActivity implements
        OnClickListener {

    private CreatureDescriptionViewModel mCreatureDescriptionViewModel = new CreatureDescriptionViewModel();
    private CreatureDescriptionView mCreatureDescriptionView = null;
    private Creature mCreature = null;

    private String mCreatureId = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        mCreatureDescriptionView = new CreatureDescriptionView(this,
                mCreatureDescriptionViewModel);

        super.onCreate(savedInstanceState);

        try {
            Bundle extras = new Bundle();
            if (savedInstanceState != null) {
                extras = savedInstanceState;
            } else {
                extras = getIntent().getExtras();
            }
            if (extras != null) {
                mCreatureId = extras.getString(Common.CREATURE_EXTRA);
            }
        } catch (Exception e) {
        }

        getCreatureById();
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putString(Common.CREATURE_EXTRA, mCreatureId);
    }

    private void getImage() {
        ArrayList<String> listImage = new ArrayList<String>();
        String name = CREATURE.getEnumNameForValue(mCreature.getKingdom());
        listImage.add(String.format(ServerConfig.IMAGE_PATH, name,
                mCreature.getImageId()));
        listImage.add(String.format(ServerConfig.IMAGE_PATH, name,
                mCreature.getImageId() + "s"));
        listImage.add(String.format(ServerConfig.IMAGE_PATH, name,
                mCreature.getImageId() + "_1s"));
        listImage.add(String.format(ServerConfig.IMAGE_PATH, name,
                mCreature.getImageId() + "_2s"));
        listImage.add(String.format(ServerConfig.IMAGE_PATH, name,
                mCreature.getImageId() + "_3s"));
        HrmService service = new HrmService();
        service.setCallback(new CheckUrlCallback() {

            @Override
            public void onSuccess(boolean result) {

            }

            @Override
            public void onImagesVerify(ArrayList<String> listImages) {
                mCreature.setCreatureImages(listImages);
                final GalleryImageAdapter adapter = new GalleryImageAdapter(
                        CreatureActivity.this, mCreature);
                adapter.setListImages((ArrayList<String>) listImages.clone());
                mCreatureDescriptionViewModel.galleryImage.setAdapter(adapter);
                mCreatureDescriptionViewModel.galleryImage
                        .setOnItemClickListener(new OnItemClickListener() {

                            @Override
                            public void onItemClick(AdapterView<?> view,
                                    View v, int pos, long id) {
                                Intent intent = new Intent();
                                intent.setClass(CreatureActivity.this,
                                        ImageViewFlipperActivity.class);
                                Bundle bundle = new Bundle();
                                bundle.putStringArrayList(
                                        Common.CREATURE_URL_IMAGES_LIST,
                                        adapter.getListImages());
                                bundle.putInt(
                                        Common.CREATURE_URL_IMAGES_POSITION,
                                        pos);
                                intent.putExtras(bundle);

                                startActivity(intent);
                            }
                        });
            }

            @Override
            public void onError() {

            }
        });
        service.requestVerifyImages(listImage);
        /*
         * mCreature.setCreatureImages(listImage); final GalleryImageAdapter
         * adapter = new GalleryImageAdapter(this, mCreature);
         * adapter.setListImages((ArrayList<String>) listImage.clone());
         * mCreatureDescriptionViewModel.galleryImage.setAdapter(adapter);
         */

    }

    @Override
    protected View createView() {
        return mCreatureDescriptionView;
    }

    @Override
    protected void onResume() {
        // Transition
        overridePendingTransition(R.anim.push_left_in, R.anim.push_right_out);
        super.onResume();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        setTitle(getString(R.string.detail));

        // XXX: For now, ShareActionProviders must be displayed on the action
        // bar
        // Set file with share history to the provider and set the share intent.
        // MenuItem overflowItem =
        // menu.findItem(R.id.menu_item_share_action_provider_overflow);
        // ShareActionProvider overflowProvider =
        // (ShareActionProvider) overflowItem.getActionProvider();
        // overflowProvider.setShareHistoryFileName(
        // ShareActionProvider.DEFAULT_SHARE_HISTORY_FILE_NAME);
        // Note that you can set/change the intent any time,
        // say when the user has selected an image.
        // overflowProvider.setShareIntent(createShareIntent());

        // Inflate menu
        getSupportMenuInflater().inflate(R.menu.detail_menu, menu);

        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
        case android.R.id.home:
            finish();
            break;
        case R.id.menu_item_refresh:
            getCreatureById();
            break;
        case R.id.menu_item_map:
            Intent intent = new Intent(CreatureActivity.this,
                    MapCreatureActivity.class);
            intent.putExtra(Common.CREATURE_EXTRA, mCreatureId);
            startActivity(intent);

        default:
            break;
        }
        return super.onOptionsItemSelected(item);
    }

    protected void getCreatureById() {
        HrmService service = new HrmService();
        service.setCallback(new Callback() {

            @Override
            public void onGetAllCreaturesDone(CreatureModel creatureModel) {
                setSupportProgressBarIndeterminateVisibility(false);
                Creature creature = creatureModel.get(0);
                mCreatureDescriptionView.setContent(creature);
                mCreature = creatureModel.get(0);
                getImage();
            }

            @Override
            public void onError() {

            }
        });
        service.requestCreaturesById(mCreatureId);
        setSupportProgressBarIndeterminateVisibility(true);
    }

    @Override
    public void onClick(View v) {
        super.onClick(v);
    }

    @Override
    protected int indentifyTabPosition() {
        return R.id.tabHome_button;
    }

}
