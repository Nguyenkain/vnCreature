package com.example.vncreatures.controller;

import java.io.IOException;
import java.util.ArrayList;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.webkit.WebChromeClient;
import android.widget.AdapterView;
import android.widget.Toast;
import android.widget.AdapterView.OnItemClickListener;

import com.actionbarsherlock.view.Menu;
import com.actionbarsherlock.view.MenuItem;
import com.androidquery.AQuery;
import com.example.vncreatures.R;
import com.example.vncreatures.common.Common;
import com.example.vncreatures.common.ServerConfig;
import com.example.vncreatures.common.Common.CREATURE;
import com.example.vncreatures.customItems.GalleryImageAdapter;
import com.example.vncreatures.model.CategoryModel;
import com.example.vncreatures.model.NewsItem;
import com.example.vncreatures.model.NewsModel;
import com.example.vncreatures.model.NewsViewModel;
import com.example.vncreatures.rest.HrmService;
import com.example.vncreatures.rest.HrmService.NewsCallback;
import com.example.vncreatures.view.NewsDetailView;

public class NewsDetailActivity extends AbstractActivity implements
        OnClickListener {

    private NewsDetailView mView;
    private NewsViewModel mModel = new NewsViewModel();
    private AQuery mAQuery;
    private String mNewsId;
    private ArrayList<String> mListImages;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        this.mView = new NewsDetailView(this, mModel);
        this.mAQuery = mView.getaQueryView();
        super.onCreate(savedInstanceState);
        getFromExtras(savedInstanceState);
        initUI();
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putString(Common.NEWS_EXTRA, mNewsId);
    }

    @Override
    protected View createView() {
        return mView;
    }

    private void getFromExtras(Bundle savedInstanceState) {
        try {
            Bundle extras = new Bundle();
            if (savedInstanceState != null) {
                extras = savedInstanceState;
            } else {
                extras = getIntent().getExtras();
            }
            if (extras != null) {
                mNewsId = extras.getString(Common.NEWS_EXTRA);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        setTitle(R.string.news);
        // Inflate menu
        getSupportMenuInflater().inflate(R.menu.detail_menu, menu);
        menu.removeItem(R.id.menu_item_map);
        return super.onCreateOptionsMenu(menu);

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
        case android.R.id.home:
            finish();
            break;
        case R.id.menu_item_refresh:
            initUI();
            break;

        default:
            break;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onResume() {
        // Transition
        overridePendingTransition(R.anim.push_left_in, R.anim.push_right_out);
        super.onResume();
    }

    private void initUI() {
        final AQuery aQuery = new AQuery(mView);
        HrmService service = new HrmService();
        service.setCallback(new NewsCallback() {

            @Override
            public void onGetNewsSuccess(NewsModel newsModel) {
                setSupportProgressBarIndeterminateVisibility(false);
                // Parse Image
                NewsItem newsItem = newsModel.get(0);
                String url = newsItem.getContent();
                Document jsDoc = null;

                try {
                    jsDoc = Jsoup.parse(url);

                    Elements imgs = jsDoc.select("img");
                    if (imgs != null) {
                        mListImages = new ArrayList<String>();
                        for (Element imgElement : imgs) {

                            String imgSrc = imgElement.attr("src");
                            String srcNew;
                            if (!imgSrc.contains("www.vncreatures.net"))
                                srcNew = "http://vncreatures.net/" + imgSrc;
                            else
                                srcNew = imgSrc;
                            imgElement.attr("src", srcNew);
                            imgElement
                                    .attr("style", "width:300px; height:auto");
                            int position = imgs.indexOf(imgElement);
                            imgElement.attr("width", "");
                            imgElement.attr("height", "");
                            imgElement.attr("onclick",
                                    "interface.changeIntoView(" + position
                                            + ")");
                            mListImages.add(srcNew);
                        }
                    }
                    Element element = jsDoc.select("td[colspan=3]").first();
                    element.html("");

                } catch (Exception e) {
                    e.printStackTrace();
                }

                mModel.newsWebView.getSettings().setSupportZoom(false);
                mModel.newsWebView.getSettings().setDefaultTextEncodingName(
                        "utf-8");
                mModel.newsWebView.getSettings().setJavaScriptEnabled(true);
                mModel.newsWebView.setWebChromeClient(new WebChromeClient());
                mModel.newsWebView.setHorizontalScrollBarEnabled(false);

                // Add java
                mModel.newsWebView.addJavascriptInterface(new JsInterface(),
                        "interface");

                mModel.newsWebView.getSettings().setDefaultFontSize(18);
                mModel.newsWebView.loadDataWithBaseURL(null, jsDoc.html(),
                        "text/html", "utf-8", null);
                aQuery.id(R.id.news_title_textView).text(
                        newsModel.get(0).getTitle());

                // Get Image
                getImage();
            }

            @Override
            public void onGetCatSuccess(CategoryModel catModel) {

            }

            @Override
            public void onError() {

            }
        });
        service.requestGetNewsDetail(mNewsId);
        setSupportProgressBarIndeterminateVisibility(true);
    }

    private void getImage() {
        final GalleryImageAdapter adapter = new GalleryImageAdapter(this,
                mListImages);
        mModel.newsGallery.setAdapter(adapter);

        mModel.newsGallery.setOnItemClickListener(new OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> view, View v, int pos,
                    long id) {
                Intent intent = new Intent();
                intent.setClass(NewsDetailActivity.this,
                        ImageViewFlipperActivity.class);
                Bundle bundle = new Bundle();
                bundle.putStringArrayList(Common.CREATURE_URL_IMAGES_LIST,
                        adapter.getListImages());
                bundle.putInt(Common.CREATURE_URL_IMAGES_POSITION, pos);
                intent.putExtras(bundle);

                startActivity(intent);
            }
        });
    }

    @Override
    public void onClick(View v) {
        super.onClick(v);
    }

    @Override
    protected int indentifyTabPosition() {
        return R.id.tabNews_button;
    }

    public final class JsInterface {
        public JsInterface() {
        }

        public void changeIntoView(int pos) {
            Intent intent = new Intent();
            intent.setClass(NewsDetailActivity.this,
                    ImageViewFlipperActivity.class);
            Bundle bundle = new Bundle();
            bundle.putStringArrayList(Common.CREATURE_URL_IMAGES_LIST,
                    mListImages);
            bundle.putInt(Common.CREATURE_URL_IMAGES_POSITION, pos);
            intent.putExtras(bundle);

            startActivity(intent);
        }
    }

}
