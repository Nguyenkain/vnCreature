package com.example.vncreatures.rest;

import java.util.ArrayList;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Gallery;

import com.example.vncreatures.common.Common;
import com.example.vncreatures.common.Common.CREATURE;
import com.example.vncreatures.common.ServerConfig;
import com.example.vncreatures.common.Utils;
import com.example.vncreatures.controller.ImageViewFlipperActivity;
import com.example.vncreatures.customItems.BitmapManager;
import com.example.vncreatures.customItems.GalleryImageAdapter;
import com.example.vncreatures.model.CategoryModel;
import com.example.vncreatures.model.Creature;
import com.example.vncreatures.model.CreatureModel;
import com.example.vncreatures.model.CreatureGroup;
import com.example.vncreatures.model.CreatureGroupListModel;
import com.example.vncreatures.model.NewsModel;

public class HrmService {
	private Callback mCallback = null;
	private GroupCallback mGroupCallback = null;
	private NewsCallback mNewsCallback = null;

	public interface Callback {
		public void onGetAllCreaturesDone(final CreatureModel creatureModel);

		public void onError();
	}

	public void setCallback(final Callback callback) {
		mCallback = callback;
	}

	public interface GroupCallback {
		public void onSuccess(final CreatureGroupListModel groupModel);

		public void onError();
	}

	public void setCallback(final GroupCallback callback) {
		mGroupCallback = callback;
	}

	public interface NewsCallback {
		public void onGetCatSuccess(final CategoryModel catModel);

		public void onGetNewsSuccess(final NewsModel newsModel);

		public void onError();
	}

	public void setCallback(final NewsCallback callback) {
		mNewsCallback = callback;
	}

	public boolean requestAllCreature(String page, String kingdom) {
		GetAllCreatureTask task = new GetAllCreatureTask();
		task.execute(page, kingdom);

		return true;
	}

	public boolean requestCreaturesByName(String name, String kingdom,
			String page, String familyId, String orderId, String classId) {
		GetCreaturesByNameTask task = new GetCreaturesByNameTask();
		task.execute(page, kingdom, name, familyId, orderId, classId);

		return true;
	}

	public boolean requestCreaturesById(String id) {
		GetCreaturesByIdTask task = new GetCreaturesByIdTask();
		task.execute(id);

		return true;
	}

	public boolean requestGetFamily(String kingdomId, String orderId,
			String classId) {
		GetFamilyTask task = new GetFamilyTask();
		task.execute(orderId, classId, kingdomId);
		return true;
	}

	public boolean requestGetOrder(String kingdomId, String familyId,
			String classId) {
		GetOrderTask task = new GetOrderTask();
		task.execute(familyId, classId, kingdomId);
		return true;
	}

	public boolean requestGetClass(String kingdomId, String orderId,
			String familyId) {
		GetClassTask task = new GetClassTask();
		task.execute(orderId, familyId, kingdomId);
		return true;
	}

	public boolean requestGetCategory() {
		GetCategoryTask task = new GetCategoryTask();
		task.execute();
		return true;
	}
	
	public boolean requestGetNews(String catId) {
		GetNewsTask task = new GetNewsTask();
		task.execute(catId);
		return true;
	}
	
	public boolean requestGetNewsDetail(String newsId) {
        GetNewsDetailTask task = new GetNewsDetailTask();
        task.execute(newsId);
        return true;
    }
	
	
	//END REQUEST

	/* download all image of a creature */
	public void downloadImages(Context context, String imgId, String loai,
			Gallery gallery) {
		BitmapDownloaderTask task = new BitmapDownloaderTask(context, gallery);
		String name = CREATURE.getEnumNameForValue(loai);
		task.execute(String.format(ServerConfig.IMAGE_PATH, name, imgId + "s"),
				String.format(ServerConfig.IMAGE_PATH, name, imgId + "_1s"),
				String.format(ServerConfig.IMAGE_PATH, name, imgId + "_2s"),
				String.format(ServerConfig.IMAGE_PATH, name, imgId + "_3s"),
				String.format(ServerConfig.IMAGE_PATH, name, imgId));
	}

	private class GetAllCreatureTask extends AsyncTask<String, Void, String> {
		@Override
		protected String doInBackground(String... params) {
			String page = params[0];
			String kingdom = params[1];
			String result = ServiceUtils.getAllCreatures(page, kingdom);
			return result;
		}

		@Override
		protected void onPostExecute(String result) {
			if (mCallback != null) {
				if (result == null) {
					mCallback.onError();
				} else {
					CreatureModel creatureModel = ServiceUtils
							.parseCreatureModelFromJSON(result);
					mCallback.onGetAllCreaturesDone(creatureModel);
				}
			}
		}
	}

	private class GetCreaturesByNameTask extends
			AsyncTask<String, Void, String> {
		@Override
		protected String doInBackground(String... params) {
			String page = params[0];
			String kingdom = params[1];
			String name = params[2];
			String familyId = params[3];
			String orderId = params[4];
			String classId = params[5];
			String result = ServiceUtils.getCreaturesByName(page, kingdom,
					name, familyId, orderId, classId);
			return result;
		}

		@Override
		protected void onPostExecute(String result) {
			if (mCallback != null) {
				if (result == null) {
					mCallback.onError();
				} else {
					CreatureModel creatureModel = ServiceUtils
							.parseCreatureModelFromJSON(result);
					mCallback.onGetAllCreaturesDone(creatureModel);
				}
			}
		}
	}

	private class GetCreaturesByIdTask extends AsyncTask<String, Void, String> {

		@Override
		protected String doInBackground(String... params) {
			String creatureId = params[0];
			String result = ServiceUtils.getCreaturesById(creatureId);
			return result;
		}

		@Override
		protected void onPostExecute(String result) {
			if (mCallback != null) {
				if (result == null) {
					mCallback.onError();
				} else {
					CreatureModel creatureModel = ServiceUtils
							.parseCreatureModelFromJSON(result);
					mCallback.onGetAllCreaturesDone(creatureModel);
				}
			}
		}
	}

	// download all image of a creature
	private class BitmapDownloaderTask extends
			AsyncTask<String, Void, ArrayList<Bitmap>> {
		private Gallery mGallery;
		private Context mContext;
		private Bitmap mBitmap = null;
		private ArrayList<String> mUrl = new ArrayList<String>();
		private ArrayList<Bitmap> mCreatureImage = new ArrayList<Bitmap>();

		public BitmapDownloaderTask(Context context, Gallery gallery) {
			mGallery = gallery;
			mContext = context;
		}

		@Override
		// Actual download method, run in the task thread
		protected ArrayList<Bitmap> doInBackground(String... params) {
			// params comes from the execute() call: params is the url.
			for (int i = 0; i < params.length; i++) {
				mBitmap = Utils.downloadBitmap(params[i]);
				if (mBitmap != null) {
					// mUrl.add(params[i]);
					mCreatureImage.add(mBitmap);
				}
			}
			BitmapManager.INSTANCE.setCreatureArrayBitmap(mCreatureImage);
			return mCreatureImage;
		}

		@Override
		// Once the image is downloaded, associates it to the imageView
		protected void onPostExecute(final ArrayList<Bitmap> arrayBitmap) {
			if (isCancelled()) {
				mGallery = null;
			}
			if (mGallery != null) {
				mGallery.setAdapter(new GalleryImageAdapter(mContext,
						arrayBitmap));
				mGallery.setOnItemClickListener(new OnItemClickListener() {

					@Override
					public void onItemClick(AdapterView<?> view, View v,
							int pos, long id) {
						Intent intent = new Intent();
						intent.setClass(mContext,
								ImageViewFlipperActivity.class);
						Bundle bundle = new Bundle();
						// bundle.putStringArrayList(
						// Common.CREATURE_URL_IMAGES_EXTRA,
						// (ArrayList<String>) mUrl);
						bundle.putInt(
								Common.CREATURE_URL_IMAGES_POSITION_EXTRA, pos);
						intent.putExtras(bundle);

						mContext.startActivity(intent);
					}
				});
			}
		}
	}

	// Get Family Task
	private class GetFamilyTask extends AsyncTask<String, Void, String> {

		@Override
		protected String doInBackground(String... params) {
			String orderId = params[0];
			String classId = params[1];
			String kingdomId = params[2];
			String result = ServiceUtils.getFamily(orderId, classId, kingdomId);
			return result;
		}

		@Override
		protected void onPostExecute(String result) {
			if (mGroupCallback != null) {
				if (result == "") {
					mGroupCallback.onError();
				} else {
					CreatureGroupListModel crGroupListModel = ServiceUtils
							.parseGroupModelFromJSON(result);
					mGroupCallback.onSuccess(crGroupListModel);
				}
			}
		}
	}

	// Get Order Task
	private class GetOrderTask extends AsyncTask<String, Void, String> {

		@Override
		protected String doInBackground(String... params) {
			String familyId = params[0];
			String classId = params[1];
			String kingdomId = params[2];
			String result = ServiceUtils.getOrder(familyId, classId, kingdomId);
			return result;
		}

		@Override
		protected void onPostExecute(String result) {
			if (mGroupCallback != null) {
				if (result == "") {
					mGroupCallback.onError();
				} else {
					CreatureGroupListModel crGroupListModel = ServiceUtils
							.parseGroupModelFromJSON(result);
					mGroupCallback.onSuccess(crGroupListModel);
				}
			}
		}
	}

	// Get Class Task
	private class GetClassTask extends AsyncTask<String, Void, String> {

		@Override
		protected String doInBackground(String... params) {
			String orderId = params[0];
			String familyId = params[1];
			String kingdomId = params[2];
			String result = ServiceUtils.getClassType(orderId, familyId,
					kingdomId);
			return result;
		}

		@Override
		protected void onPostExecute(String result) {
			if (mGroupCallback != null) {
				if (result == "") {
					mGroupCallback.onError();
				} else {
					CreatureGroupListModel crGroupListModel = ServiceUtils
							.parseGroupModelFromJSON(result);
					mGroupCallback.onSuccess(crGroupListModel);
				}
			}
		}
	}

	// Get Category Task
	private class GetCategoryTask extends AsyncTask<String, Void, String> {

		@Override
		protected String doInBackground(String... params) {
			String result = ServiceUtils.getCategory();
			return result;
		}

		@Override
		protected void onPostExecute(String result) {
			if (mNewsCallback != null) {
				if (result == "") {
					mNewsCallback.onError();
				} else {
					CategoryModel catModel = ServiceUtils
							.parseCategoryModelFromJSON(result);
					mNewsCallback.onGetCatSuccess(catModel);
				}
			}
		}
	}

	// Get News Task
	private class GetNewsTask extends AsyncTask<String, Void, String> {

		@Override
		protected String doInBackground(String... params) {
			String catId = params[0];
			String result = ServiceUtils.getNews(catId);
			return result;
		}

		@Override
		protected void onPostExecute(String result) {
			if (mNewsCallback != null) {
				if (result == "" || result == null) {
					mNewsCallback.onError();
				} else {
					NewsModel newsModel = ServiceUtils
							.parseNewsModelFromJSON(result);
					mNewsCallback.onGetNewsSuccess(newsModel);
				}
			}
		}
	}
	
	private class GetNewsDetailTask extends AsyncTask<String, Void, String> {

        @Override
        protected String doInBackground(String... params) {
            String newsId = params[0];
            String result = ServiceUtils.getNewsDetail(newsId);
            return result;
        }

        @Override
        protected void onPostExecute(String result) {
            if (mNewsCallback != null) {
                if (result == "" || result == null) {
                    mNewsCallback.onError();
                } else {
                    NewsModel newsModel = ServiceUtils
                            .parseNewsModelFromJSON(result);
                    mNewsCallback.onGetNewsSuccess(newsModel);
                }
            }
        }
    }
}