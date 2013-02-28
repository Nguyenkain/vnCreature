package com.example.vncreatures.controller;

import java.io.ByteArrayOutputStream;
import java.util.ArrayList;

import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnDismissListener;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.provider.MediaStore;
import android.support.v4.app.Fragment;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.actionbarsherlock.app.SherlockFragment;
import com.actionbarsherlock.view.Menu;
import com.actionbarsherlock.view.MenuInflater;
import com.actionbarsherlock.view.MenuItem;
import com.androidquery.AQuery;
import com.example.vncreatures.R;
import com.example.vncreatures.common.Common;
import com.example.vncreatures.customItems.Base64;
import com.example.vncreatures.customItems.Base64.InputStream;
import com.example.vncreatures.customItems.ThreadListAdapter;
import com.example.vncreatures.customItems.ThreadListAdapter.Callback;
import com.example.vncreatures.model.discussion.Thread;
import com.example.vncreatures.model.discussion.ThreadModel;
import com.example.vncreatures.rest.HrmService;
import com.example.vncreatures.rest.HrmService.PostTaskCallback;
import com.example.vncreatures.rest.HrmService.ThreadTaskCallback;
import com.markupartist.android.widget.PullToRefreshListView;
import com.markupartist.android.widget.PullToRefreshListView.OnRefreshListener;
import com.mobsandgeeks.saripaar.Rule;
import com.mobsandgeeks.saripaar.Validator;
import com.mobsandgeeks.saripaar.Validator.ValidationListener;
import com.mobsandgeeks.saripaar.annotation.Required;
import com.mobsandgeeks.saripaar.annotation.TextRule;

public class ThreadFragment extends SherlockFragment implements OnClickListener {
	InputStream inputStream;
	private final int IMAGE_MAX_SIZE = 450;

	private ProgressDialog m_ProgressDialog = null;
	private Runnable viewOrders;
	private Context mContext;
	private View mView;
	private AQuery mAQueryComPoseView;
	private ThreadListAdapter mAdapter;
	private Dialog mComposeWindow;
	PullToRefreshListView mListView;
	SharedPreferences pref;
	private ArrayList<QueueItem> mQueueItems = new ArrayList<QueueItem>();

	// Post Thread Item
	Validator validator;
	@Required(order = 1, message = Common.TITLE_MESSAGE)
	@TextRule(order = 4, minLength = 8, message = Common.MINLENGTH_MESSAGE)
	private EditText mTitleEditText = null;

	@Required(order = 2, message = Common.CONTENT_MESSAGE)
	@TextRule(order = 3, minLength = 8, message = Common.MINLENGTH_MESSAGE)
	private EditText mContentEditText = null;

	public ThreadFragment() {
	}

	public ThreadFragment(Context context) {
		mContext = context;
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		mView = inflater.inflate(R.layout.thread_layout, null);

		// get preference
		pref = PreferenceManager.getDefaultSharedPreferences(getActivity());

		initLayout();
		initList();
		setHasOptionsMenu(true);
		return mView;
	}

	@Override
	public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
		super.onCreateOptionsMenu(menu, inflater);
		// Inflate menu
		getSherlockActivity().getSupportMenuInflater().inflate(
				R.menu.dicussion_menu, menu);

	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		Fragment frag = null;
		switch (item.getItemId()) {
		case R.id.menu_item_refresh:
			initList();
			break;
		case R.id.menu_item_post:
			// frag = new PostThreadFragment();
			initPopupWindow();
			break;
		default:
			break;
		}
		if (frag != null) {
			switchFragment(frag);
		}
		return super.onOptionsItemSelected(item);
	}

	@SuppressWarnings("deprecation")
	private void initPopupWindow() {

		try {
			// We need to get the instance of the LayoutInflater, use the
			// context of this activity
			LayoutInflater inflater = (LayoutInflater) getActivity()
					.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
			// Inflate the view from a predefined XML layout
			View layout = inflater.inflate(R.layout.compose_layout, null);
			mComposeWindow = new Dialog(getActivity(),
					R.style.Theme_D1NoTitleDim);

			// init UI
			mAQueryComPoseView = new AQuery(layout);
			mTitleEditText = mAQueryComPoseView.id(R.id.title_editText)
					.getEditText();
			mContentEditText = mAQueryComPoseView
					.id(R.id.post_content_EditText).getEditText();
			if (!mQueueItems.isEmpty()) {
				Bitmap bitmap = BitmapFactory
						.decodeFile(mQueueItems.get(0).path);
				mAQueryComPoseView.id(R.id.image_upload).image(bitmap);
			}
			Button sendButton = mAQueryComPoseView.id(R.id.send_button)
					.getButton();

			String userName = pref.getString(Common.USER_NAME, null);
			String fbId = pref.getString(Common.FB_ID, null);
			if (userName != null && fbId != null) {
				userName = userName.replace("\n", "").replace("\r", "").trim();
				fbId = fbId.replace("\n", "").replace("\r", "").trim();
			}
			String url = "http://graph.facebook.com/" + fbId
					+ "/picture?type=small";
			mAQueryComPoseView
					.id(R.id.avatar_imageView)
					.image(url)
					.image(url, true, true, 0, R.drawable.no_thumb, null,
							AQuery.FADE_IN_NETWORK);
			mAQueryComPoseView.id(R.id.username_textView).text(userName);

			// init validator
			validator = new Validator(this);
			ValidateListener listener = new ValidateListener((View) sendButton);
			validator.setValidationListener(listener);

			// init Event
			sendButton.setOnClickListener(this);
			mAQueryComPoseView.id(R.id.cancel_button).clicked(this);
			mAQueryComPoseView.id(R.id.photo_button).clicked(this);

			/* blur background */
			WindowManager.LayoutParams lp = mComposeWindow.getWindow()
					.getAttributes();
			lp.dimAmount = 0.0f;
			lp.gravity = Gravity.CENTER;
			mComposeWindow.getWindow().setAttributes(lp);
			mComposeWindow.getWindow().addFlags(
					WindowManager.LayoutParams.FLAG_BLUR_BEHIND);

			// set view
			mComposeWindow.setOnDismissListener(new OnDismissListener() {

				@Override
				public void onDismiss(DialogInterface dialog) {
					mQueueItems.clear();
				}
			});
			mComposeWindow.setContentView(layout);
			mComposeWindow.setCanceledOnTouchOutside(true);
			mComposeWindow.getWindow().setSoftInputMode(
					WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE);
			mComposeWindow.show();

		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	// the meat of switching the above fragment
	private void switchFragment(Fragment fragment) {
		if (getActivity() == null)
			return;

		if (getActivity() instanceof DiscussionActivity) {
			DiscussionActivity dca = (DiscussionActivity) getActivity();
			dca.switchContent(fragment);
		}
	}

	private void initLayout() {
		mListView = (PullToRefreshListView) mView
				.findViewById(R.id.thread_lisview);
		mListView.setOnRefreshListener(new OnRefreshListener() {

			@Override
			public void onRefresh() {
				initList();
			}
		});
	}

	private void initList() {
		HrmService service = new HrmService();
		service.setCallback(new ThreadTaskCallback() {

			@Override
			public void onSuccess(ThreadModel threadModel) {
				if (threadModel != null && threadModel.count() > 0) {
					mAdapter = new ThreadListAdapter(getActivity(), threadModel);
					mListView.setAdapter(mAdapter);
					mListView.onRefreshComplete();
					getSherlockActivity()
							.setSupportProgressBarIndeterminateVisibility(false);
					mAdapter.setCallback(new Callback() {

						@Override
						public void onClick(Thread thread) {
							pref.edit()
									.putString(Common.THREAD_ID,
											thread.getThread_id()).commit();
							Fragment fragment = new ThreadDetailFragment();
							switchFragment(fragment);
						}
					});
				}
			}

			@Override
			public void onError() {

			}
		});
		getSherlockActivity()
				.setSupportProgressBarIndeterminateVisibility(true);
		service.requestGetAllThread();
	}

	private final class ValidateListener implements ValidationListener {

		private View mView;

		public ValidateListener(View v) {
			this.mView = v;
		}

		@Override
		public void preValidation() {
			// TODO Auto-generated method stub

		}

		@Override
		public void onSuccess() {
			postNewThread(mView);
		}

		@Override
		public void onFailure(View failedView, Rule<?> failedRule) {
			String message = failedRule.getFailureMessage();
			mView.setEnabled(true);

			if (failedView instanceof EditText) {
				failedView.requestFocus();
				((EditText) failedView).setError(message);
			} else {
				Toast.makeText(getActivity(), message, Toast.LENGTH_SHORT)
						.show();
			}
		}

		@Override
		public void onValidationCancelled() {
			// TODO Auto-generated method stub

		}

	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.send_button:
			v.setEnabled(false);
			validator.validateAsync();
			break;
		case R.id.cancel_button:
			mComposeWindow.dismiss();
			mComposeWindow = null;
			break;
		case R.id.photo_button:
			Intent intent = new Intent(getSherlockActivity(),
					AndroidCustomGalleryActivity.class);
			startActivityForResult(intent, Common.SELECT_PICTURE);
			break;
		default:
			break;
		}
	}

	public void onActivityResult(int requestCode, int resultCode, Intent data) {
		if (resultCode == android.app.Activity.RESULT_OK) {
			if (requestCode == Common.SELECT_PICTURE) {
				String newText = data.getStringExtra("selectImages");
				initialize(newText);
				Bitmap bitmap = decodeFilePath(mQueueItems.get(0).path);
				mAQueryComPoseView.id(R.id.image_upload).image(bitmap);
				viewOrders = new Runnable() {
					@Override
					public void run() {
						upload();
					}
				};
				java.lang.Thread thread = new java.lang.Thread(null,
						viewOrders, "Background");
				thread.start();
				m_ProgressDialog = ProgressDialog.show(getSherlockActivity(),
						"Please wait...", "Uploading image ...", true, true);
			}
		}
	}

	public void initialize(String ids) {
		mQueueItems.clear();

		String[] arrIds = ids.split(",");
		for (String item : arrIds) {
			QueueItem queueItem = new QueueItem();
			queueItem.media_id = Long.parseLong(item);

			final String[] columns = { MediaStore.Images.Media.DATA };
			final String orderBy = MediaStore.Images.Media._ID;
			Cursor imagecursor = mContext.getContentResolver().query(
					MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
					columns,
					MediaStore.Images.Media._ID + " = " + queueItem.media_id
							+ "", null, orderBy);
			int count = imagecursor.getCount();
			for (int i = 0; i < count; i++) {
				imagecursor.moveToPosition(i);
				int dataColumnIndex = imagecursor
						.getColumnIndex(MediaStore.Images.Media.DATA);
				queueItem.path = imagecursor.getString(dataColumnIndex);
				System.out.println("queueItem.path: " + queueItem.path);
			}
			imagecursor.close();

			mQueueItems.add(queueItem);
		}
	}

	class QueueItem {
		String path;
		long media_id;
	}

	private void upload() {
		for (QueueItem queue : mQueueItems) {
			String selectedImagePath = queue.path;
			Bitmap bitmap = decodeFilePath(selectedImagePath);

			ByteArrayOutputStream stream = new ByteArrayOutputStream();
			bitmap.compress(Bitmap.CompressFormat.JPEG, 90, stream);
			byte[] byte_arr = stream.toByteArray();
			String image_str = Base64.encodeBytes(byte_arr);
			ArrayList<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>();
			nameValuePairs.add(new BasicNameValuePair("image", image_str));
			nameValuePairs.add(new BasicNameValuePair("path", queue.media_id
					+ ".jpg"));
			try {
				HttpClient httpclient = new DefaultHttpClient();
				HttpPost httppost = new HttpPost(
						"http://113.164.1.45/webservice/base.php");
				httppost.setEntity(new UrlEncodedFormEntity(nameValuePairs));
				httpclient.execute(httppost);
			} catch (Exception e) {
				System.out.println("Error in http connection " + e.toString());
			}
		}
		getSherlockActivity().runOnUiThread(returnRes);
	}

	private Runnable returnRes = new Runnable() {

		@Override
		public void run() {
			m_ProgressDialog.dismiss();
		}
	};

	private Bitmap decodeFilePath(String path) {
		Bitmap b = null;
		try {
			// Decode image size
			BitmapFactory.Options o = new BitmapFactory.Options();
			o.inJustDecodeBounds = true;

			BitmapFactory.decodeFile(path, o);

			int scale = 1;
			if (o.outHeight > IMAGE_MAX_SIZE || o.outWidth > IMAGE_MAX_SIZE) {
				scale = (int) Math.pow(
						2,
						(int) Math.round(Math.log(IMAGE_MAX_SIZE
								/ (double) Math.max(o.outHeight, o.outWidth))
								/ Math.log(0.5)));
			}

			// Decode with inSampleSize
			BitmapFactory.Options o2 = new BitmapFactory.Options();
			o2.inSampleSize = scale;
			b = BitmapFactory.decodeFile(path, o2);
		} catch (Exception e) {
			System.out.println("Error in decodeFile: " + e.toString());
		}
		return b;
	}

	private void postNewThread(final View v) {
		String title = mTitleEditText.getText().toString();
		String content = mContentEditText.getText().toString();
		String userid = pref.getString(Common.USER_ID, null);
		Thread thread = new Thread();
		thread.setThread_title(title);
		thread.setThread_content(content);
		thread.setUser_id(userid);

		if (userid != null) {
			HrmService service = new HrmService();
			service.requestAddThread(thread);
			service.setCallback(new PostTaskCallback() {

				@Override
				public void onSuccess(String result) {
					v.setEnabled(true);
					mComposeWindow.dismiss();
					mComposeWindow = null;
					initList();
				}

				@Override
				public void onError() {

				}
			});
		}
	}
}
