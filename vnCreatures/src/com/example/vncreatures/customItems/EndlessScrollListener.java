package com.example.vncreatures.customItems;

import android.widget.AbsListView;
import android.widget.AbsListView.OnScrollListener;

import com.example.vncreatures.common.ServerConfig;
import com.example.vncreatures.model.CreatureModel;
import com.example.vncreatures.rest.HrmService;
import com.example.vncreatures.rest.HrmService.Callback;

public class EndlessScrollListener implements OnScrollListener {
	private int visibleThreshold = Integer.parseInt(ServerConfig.NUM_PER_PAGE);
	private int currentPage = 1;
	private int previousTotal = 0;
	private boolean loading = true;
	private String name = null;
	private CreaturesListAdapter adapter = null;

	public EndlessScrollListener() {
	}

	public EndlessScrollListener(CreaturesListAdapter adapter) {
		this.adapter = adapter;
	}

	public EndlessScrollListener(CreaturesListAdapter adapter, String name) {
		this.adapter = adapter;
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
				&& (totalItemCount - visibleItemCount) <= (firstVisibleItem + visibleThreshold)) {
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
				service.requestCreaturesByName(this.name,
						String.valueOf(currentPage + 1));
			} else
				service.requestAllCreature(String.valueOf(currentPage + 1));
			loading = true;
		}
	}

	@Override
	public void onScrollStateChanged(AbsListView view, int scrollState) {
	}
}
