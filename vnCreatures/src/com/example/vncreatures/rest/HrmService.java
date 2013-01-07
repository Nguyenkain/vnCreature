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
import com.example.vncreatures.model.Creature;
import com.example.vncreatures.model.CreatureModel;
import com.example.vncreatures.model.CreatureGroup;
import com.example.vncreatures.model.CreatureGroupListModel;

public class HrmService {
	private Callback mCallback = null;
	private GroupCallback mGroupCallback = null;

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

	public boolean requestAllCreature(String page) {
		GetAllCreatureTask task = new GetAllCreatureTask();
		task.execute(page);

		return true;
	}

	public boolean requestCreaturesByName(String name, String page,
			String familyId, String orderId, String classId) {
		GetCreaturesByNameTask task = new GetCreaturesByNameTask();
		task.execute(page, name, familyId, orderId, classId);

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

	protected String getAllCreatures(String page) {
		String result = "";
		String request = String.format(ServerConfig.GET_ALL_CREATURE);
		RestClient client = new RestClient(request);
		client.addParam("getAllNameCreature", "");
		client.addParam("format", "json");
		client.addParam("page", page);
		client.addParam("recordPerPage", ServerConfig.NUM_PER_PAGE);

		try {
			client.execute(RestClient.RequestMethod.GET);
			result = client.getResponse();
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}

		return result;
	}

	protected String getCreaturesByName(String page, String name,
			String familyId, String orderId, String classId) {
		String result = "";
		String request = String.format(ServerConfig.GET_ALL_CREATURE_BY_NAME2);
		RestClient client = new RestClient(request);
		client.addParam("format", "json");
		client.addParam("creatureName", name);
		client.addParam("page", page);
		client.addParam("recordPerPage", ServerConfig.NUM_PER_PAGE);
		client.addParam("family", familyId);
		client.addParam("order", orderId);
		client.addParam("class", classId);

		try {
			client.execute(RestClient.RequestMethod.GET);
			result = client.getResponse();
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}

		return result;
	}

	protected String getCreaturesById(String id) {
		String result = "";
		String request = String.format(ServerConfig.GET_CREATURE_BY_ID);
		RestClient client = new RestClient(request);
		client.addParam("getCreatureById", "");
		client.addParam("format", "json");
		client.addParam("id", id);

		try {
			client.execute(RestClient.RequestMethod.GET);
			result = client.getResponse();
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}

		return result;
	}

	protected String getFamily(String orderId, String classId, String kingdomId) {
		String result = "";

		String request = String.format(ServerConfig.GET_FAMILY);
		RestClient client = new RestClient(request);
		client.addParam("format", "json");
		client.addParam("kingdom", kingdomId);
		client.addParam("class", classId);
		client.addParam("order", orderId);

		try {
			client.execute(RestClient.RequestMethod.GET);
			result = client.getResponse();
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}

		return result;
	}

	protected String getOrder(String familyId, String classId, String kingdomId) {
		String result = "";

		String request = String.format(ServerConfig.GET_ORDER);
		RestClient client = new RestClient(request);
		client.addParam("format", "json");
		client.addParam("kingdom", kingdomId);
		client.addParam("class", classId);
		client.addParam("family", familyId);

		try {
			client.execute(RestClient.RequestMethod.GET);
			result = client.getResponse();
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}

		return result;
	}

	protected String getClassType(String orderId, String familyId,
			String kingdomId) {
		String result = "";

		String request = String.format(ServerConfig.GET_CLASS);
		RestClient client = new RestClient(request);
		client.addParam("format", "json");
		client.addParam("kingdom", kingdomId);
		client.addParam("order", orderId);
		client.addParam("family", familyId);

		try {
			client.execute(RestClient.RequestMethod.GET);
			result = client.getResponse();
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}

		return result;
	}

	public CreatureModel parseCreatureModelFromJSON(String data) {
		if (data == null || data == "")
			return null;

		CreatureModel creatureModel = new CreatureModel();

		if (data.indexOf('{') > -1)
			data = data.substring(data.indexOf('{'));
		if (data.lastIndexOf('}') > -1)
			data = data.substring(0, data.lastIndexOf('}') + 1);

		try {
			JSONObject creaturesObj = new JSONObject(data);
			if (!creaturesObj.has("creatures"))
				return null;

			JSONArray creatureListObj = creaturesObj.getJSONArray("creatures");
			for (int i = 0; i < creatureListObj.length(); i++) {
				Creature creature = new Creature();
				JSONObject creatureObj = creatureListObj.getJSONObject(i);
				String stringVal = "", key = "";
				int intVal = -1;
				key = "creature";
				if (!creatureObj.has(key))
					continue;
				creatureObj = creatureObj.getJSONObject(key);

				key = "ID";
				stringVal = creatureObj.has(key) ? creatureObj.getString(key)
						: "";
				creature.setId(stringVal);

				key = "Viet";
				stringVal = creatureObj.has(key) ? creatureObj.getString(key)
						: "";
				creature.setvName(stringVal);

				key = "Latin";
				stringVal = creatureObj.has(key) ? creatureObj.getString(key)
						: "";
				creature.setLatin(stringVal);

				key = "Img";
				stringVal = creatureObj.has(key) ? creatureObj.getString(key)
						: "";
				creature.setImageId(stringVal);

				key = "Loai";
				stringVal = creatureObj.has(key) ? creatureObj.getString(key)
						: "";
				creature.setLoai(stringVal);

				key = "Description";
				stringVal = creatureObj.has(key) ? creatureObj.getString(key)
						: "";
				stringVal = stringVal.replace("charset=windows-1252", " ");
				creature.setDescription(stringVal);

				key = "AuthorName";
				stringVal = creatureObj.has(key) ? creatureObj.getString(key)
						: "";
				creature.setAuthor(stringVal);

				// Add to the model
				creatureModel.add(creature);
			}

		} catch (JSONException e) {
			e.printStackTrace();
		}

		return creatureModel;
	}

	// Parse GroupModel
	protected CreatureGroupListModel parseGroupModelFromJSON(String data) {
		if (data == null || data == "")
			return null;

		CreatureGroupListModel crGroupListModel = new CreatureGroupListModel();

		if (data.indexOf('{') > -1)
			data = data.substring(data.indexOf('{'));
		if (data.lastIndexOf('}') > -1)
			data = data.substring(0, data.lastIndexOf('}') + 1);

		try {
			JSONObject creaturesObj = new JSONObject(data);
			if (!creaturesObj.has("groups"))
				return null;

			JSONArray creatureListObj = creaturesObj.getJSONArray("groups");
			for (int i = 0; i < creatureListObj.length(); i++) {
				CreatureGroup creatureGroup = new CreatureGroup();
				JSONObject creatureObj = creatureListObj.getJSONObject(i);
				String stringVal = "", key = "";
				int intVal = -1;
				key = "group";
				if (!creatureObj.has(key))
					continue;
				creatureObj = creatureObj.getJSONObject(key);

				key = "ID";
				stringVal = creatureObj.has(key) ? creatureObj.getString(key)
						: "";
				creatureGroup.setId(stringVal);

				key = "Viet";
				stringVal = creatureObj.has(key) ? creatureObj.getString(key)
						: "";
				creatureGroup.setViet(stringVal);

				key = "Latin";
				stringVal = creatureObj.has(key) ? creatureObj.getString(key)
						: "";
				creatureGroup.setLatin(stringVal);

				// Add to the model
				crGroupListModel.add(creatureGroup);
			}

		} catch (JSONException e) {
			e.printStackTrace();
		}

		return crGroupListModel;
	}

	private class GetAllCreatureTask extends AsyncTask<String, Void, String> {
		@Override
		protected String doInBackground(String... params) {
			String page = params[0];
			String result = getAllCreatures(page);
			return result;
		}

		@Override
		protected void onPostExecute(String result) {
			if (mCallback != null) {
				if (result == null) {
					mCallback.onError();
				} else {
					CreatureModel creatureModel = parseCreatureModelFromJSON(result);
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
			String name = params[1];
			String familyId = params[2];
			String orderId = params[3];
			String classId = params[4];
			String result = getCreaturesByName(page, name, familyId, orderId,
					classId);
			return result;
		}

		@Override
		protected void onPostExecute(String result) {
			if (mCallback != null) {
				if (result == null) {
					mCallback.onError();
				} else {
					CreatureModel creatureModel = parseCreatureModelFromJSON(result);
					mCallback.onGetAllCreaturesDone(creatureModel);
				}
			}
		}
	}

	private class GetCreaturesByIdTask extends AsyncTask<String, Void, String> {

		@Override
		protected String doInBackground(String... params) {
			String creatureId = params[0];
			String result = getCreaturesById(creatureId);
			return result;
		}

		@Override
		protected void onPostExecute(String result) {
			if (mCallback != null) {
				if (result == null) {
					mCallback.onError();
				} else {
					CreatureModel creatureModel = parseCreatureModelFromJSON(result);
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
			String result = getFamily(orderId, classId, kingdomId);
			return result;
		}

		@Override
		protected void onPostExecute(String result) {
			if (mGroupCallback != null) {
				if (result == "") {
					mGroupCallback.onError();
				} else {
					CreatureGroupListModel crGroupListModel = parseGroupModelFromJSON(result);
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
			String result = getOrder(familyId, classId, kingdomId);
			return result;
		}

		@Override
		protected void onPostExecute(String result) {
			if (mGroupCallback != null) {
				if (result == "") {
					mGroupCallback.onError();
				} else {
					CreatureGroupListModel crGroupListModel = parseGroupModelFromJSON(result);
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
			String result = getClassType(orderId, familyId, kingdomId);
			return result;
		}

		@Override
		protected void onPostExecute(String result) {
			if (mGroupCallback != null) {
				if (result == "") {
					mGroupCallback.onError();
				} else {
					CreatureGroupListModel crGroupListModel = parseGroupModelFromJSON(result);
					mGroupCallback.onSuccess(crGroupListModel);
				}
			}
		}
	}
}