package com.example.vncreatures.controller;

import java.io.IOException;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;

import com.actionbarsherlock.view.Menu;
import com.actionbarsherlock.view.MenuItem;
import com.androidquery.AQuery;
import com.example.vncreatures.R;
import com.example.vncreatures.common.Common;
import com.example.vncreatures.common.ServerConfig;
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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        this.mView = new NewsDetailView(this, mModel);
        this.mAQuery = mView.getaQueryView();
        super.onCreate(savedInstanceState);
        getFromExtras();
        initUI();
    }

    @Override
    protected View createView() {
        return mView;
    }

    private void getFromExtras() {
        try {
            Bundle extras = getIntent().getExtras();
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
                setProgressBarIndeterminateVisibility(false);
                // Parse Image
                NewsItem newsItem = newsModel.get(0);
                String url = newsItem.getContent();
                Document jsDoc = null;
                try {
                    jsDoc = Jsoup.parse(url);

                    Elements imgs = jsDoc.select("img");
                    if (imgs != null) {
                        for (Element imgElement : imgs) {
                            String imgSrc = imgElement.attr("src");
                            String srcNew;
                            if(!imgSrc.contains("www.vncreatures.net"))
                                srcNew = "http://vncreatures.net/" + imgSrc;
                            else
                                srcNew = imgSrc;
                            imgElement.attr("src", srcNew);
                            imgElement
                                    .attr("style", "width:200px; height:auto");
                            imgElement.attr("width", "");
                            imgElement.attr("height", "");
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
                mModel.newsWebView.loadDataWithBaseURL(null, jsDoc.html(),
                        "text/html", "utf-8", null);
                aQuery.id(R.id.news_title_textView).text(
                        newsModel.get(0).getTitle());
            }

            @Override
            public void onGetCatSuccess(CategoryModel catModel) {

            }

            @Override
            public void onError() {

            }
        });
        service.requestGetNewsDetail(mNewsId);
        setProgressBarIndeterminateVisibility(true);
    }

    @Override
    public void onClick(View v) {
        super.onClick(v);
    }

    @Override
    protected int indentifyTabPosition() {
        return R.id.tabNews_button;
    }

}
