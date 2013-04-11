package com.vncreatures.customItems;

import android.widget.AbsListView;
import android.widget.AbsListView.OnScrollListener;

import com.vncreatures.common.ServerConfig;
import com.vncreatures.model.CategoryModel;
import com.vncreatures.model.CreatureModel;
import com.vncreatures.model.NewsModel;
import com.vncreatures.rest.HrmService;
import com.vncreatures.rest.HrmService.Callback;
import com.vncreatures.rest.HrmService.NewsCallback;

public class EndlessScrollListener implements OnScrollListener {
    private int visibleThreshold = Integer.parseInt(ServerConfig.NUM_PER_PAGE);
    private int currentPage = 1;
    private int previousTotal = 0;
    private boolean loading = true;
    private String name = null;
    private String familyId = null;
    private String orderId = null;
    private String classId = null;
    private String kingdom = null;
    private CreaturesListAdapter adapter = null;

    public EndlessScrollListener() {
    }

    public EndlessScrollListener(CreaturesListAdapter adapter, String kingdom) {
        this.adapter = adapter;
        this.kingdom = kingdom;
    }

    public EndlessScrollListener(CreaturesListAdapter adapter, String name,
            String kingdomId, String familyid, String orderId, String classId) {
        this.adapter = adapter;
        this.orderId = orderId;
        this.familyId = familyid;
        this.classId = classId;
        this.kingdom = kingdomId;
        this.name = name;
    }

    @Override
    public void onScroll(AbsListView view, int firstVisibleItem,
            int visibleItemCount, int totalItemCount) {
        if (loading) {
            if (totalItemCount > previousTotal) {
                loading = false;
                previousTotal = totalItemCount;
                currentPage++;
            }
        } else if (!loading
                && (totalItemCount - visibleItemCount - 1) <= (firstVisibleItem + visibleThreshold)) {
            // I load the next page of gigs using a background task,
            // but you can call any function here.
            HrmService service = new HrmService();
            service.setCallback(new Callback() {

                @Override
                public void onGetAllCreaturesDone(CreatureModel creatureModel) {
                    CreatureModel model = adapter.getCreatureModel();
                    model.addAll(creatureModel);
                    adapter.setCreatureModel(model);
                    adapter.notifyDataSetChanged();
                }

                @Override
                public void onError() {
                    // TODO Auto-generated method stub

                }
            });
            if (this.name != null) {
                service.requestCreaturesByName(this.name, this.kingdom,
                        String.valueOf(currentPage + 1), this.familyId,
                        this.orderId, this.classId);
            } else
                service.requestAllCreature(String.valueOf(currentPage + 1),
                        kingdom);
            loading = true;
        }
    }

    @Override
    public void onScrollStateChanged(AbsListView view, int scrollState) {
    }

    public static class EndlessScrollNewsListener implements OnScrollListener {
        private int visibleThreshold = Integer.parseInt(ServerConfig.NUM_PER_PAGE);
        private int currentPage = 1;
        private int previousTotal = 0;
        private boolean loading = true;
        private String mCatId = null;
        private NewsListAdapter mAdapter = null;

        public EndlessScrollNewsListener(NewsListAdapter adapter, String catId) {
            super();
            this.mCatId = catId;
            this.mAdapter = adapter;
        }

        @Override
        public void onScroll(AbsListView view, int firstVisibleItem,
                int visibleItemCount, int totalItemCount) {
            if (loading) {
                if (totalItemCount > previousTotal) {
                    loading = false;
                    previousTotal = totalItemCount;
                    currentPage++;
                }
            } else if (!loading
                    && (totalItemCount - visibleItemCount - 1) <= (firstVisibleItem + visibleThreshold)) {
                // I load the next page of gigs using a background task,
                // but you can call any function here.
                HrmService service = new HrmService();
                service.setCallback(new NewsCallback() {

                    @Override
                    public void onGetNewsSuccess(NewsModel newsModel) {
                        NewsModel nModel = mAdapter.getNewsModel();
                        nModel.addAll(newsModel);
                        mAdapter.setNewsModel(nModel);
                        mAdapter.notifyDataSetChanged();
                    }

                    @Override
                    public void onGetCatSuccess(CategoryModel catModel) {

                    }

                    @Override
                    public void onError() {

                    }
                });
                service.requestGetNews(this.mCatId,
                        String.valueOf(currentPage));
                loading = true;
            }
        }

        @Override
        public void onScrollStateChanged(AbsListView view, int scrollState) {

        }

    }
}
