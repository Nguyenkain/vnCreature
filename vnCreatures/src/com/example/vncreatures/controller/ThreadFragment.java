package com.example.vncreatures.controller;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.util.ArrayList;

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
import android.net.Uri;
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
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.Toast;

import com.actionbarsherlock.app.SherlockFragment;
import com.actionbarsherlock.view.Menu;
import com.actionbarsherlock.view.MenuInflater;
import com.actionbarsherlock.view.MenuItem;
import com.androidquery.AQuery;
import com.example.vncreatures.R;
import com.example.vncreatures.common.Common;
import com.example.vncreatures.customItems.Base64;
import com.example.vncreatures.customItems.SuggestListAdapter;
import com.example.vncreatures.customItems.ThreadListAdapter;
import com.example.vncreatures.customItems.ThreadListAdapter.Callback;
import com.example.vncreatures.customItems.eventbus.BusProvider;
import com.example.vncreatures.customItems.eventbus.NotificationUpdateEvent;
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

	private final int IMAGE_MAX_SIZE = 450;

	private View mView;
	private ThreadListAdapter mAdapter;
	private Dialog mComposeWindow;
	private Dialog mSuggestWindow;
	PullToRefreshListView mListView;
	SharedPreferences pref;
	private boolean mState = true;
	private ProgressDialog mProgressDialog = null;
	private Runnable mViewOrders;
	private Context mContext;
	private AQuery mAQueryComPoseView;
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

		// init suggest popup
		String title = pref.getString(Common.SUGGEST_TITLE_PREF, null);
		if (title != null) {
			initSuggestPopupWindow(true);
		}

		initLayout();
		initList();
		setHasOptionsMenu(true);

		return mView;
	}

	@Override
	public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
		super.onCreateOptionsMenu(menu, inflater);

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
			// initPopupWindow();
			initSuggestPopupWindow(false);
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
	private void initPopupWindow(String titleText) {

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
			pref.edit().putString(Common.SUGGEST_TITLE_PREF, titleText)
					.commit();
			String title = pref.getString(Common.SUGGEST_TITLE_PREF, "");
			mTitleEditText.setText(title);
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
			mComposeWindow.setContentView(layout);
			mComposeWindow.setCanceledOnTouchOutside(true);
			mComposeWindow.getWindow().setSoftInputMode(
					WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE);
			mComposeWindow.show();
			mComposeWindow.setOnDismissListener(new OnDismissListener() {

				@Override
				public void onDismiss(DialogInterface dialog) {
					mQueueItems.clear();
				}
			});

		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	private void initSuggestPopupWindow(boolean isOpenned) {

		try {
			// We need to get the instance of the LayoutInflater, use the
			// context of this activity
			LayoutInflater inflater = (LayoutInflater) getActivity()
					.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
			// Inflate the view from a predefined XML layout
			View layout = inflater.inflate(R.layout.suggest_layout, null);
			mSuggestWindow = new Dialog(getActivity(),
					R.style.Theme_D1NoTitleDim);

			// init UI
			final AQuery aQView = new AQuery(layout);
			mTitleEditText = aQView.id(R.id.title_editText).getEditText();
			String title = pref.getString(Common.SUGGEST_TITLE_PREF, "");
			mTitleEditText.setText(title);
			final ListView suggestListview = aQView.id(R.id.suggest_listview)
					.getListView();
			Button sendButton = aQView.id(R.id.send_button).getButton();

			String userName = pref.getString(Common.USER_NAME, null);
			String fbId = pref.getString(Common.FB_ID, null);
			if (userName != null && fbId != null) {
				userName = userName.replace("\n", "").replace("\r", "").trim();
				fbId = fbId.replace("\n", "").replace("\r", "").trim();
			}
			String url = "http://graph.facebook.com/" + fbId
					+ "/picture?type=small";
			aQView.id(R.id.avatar_imageView)
					.image(url)
					.image(url, true, true, 0, R.drawable.no_thumb, null,
							AQuery.FADE_IN_NETWORK);
			aQView.id(R.id.username_textView).text(userName);

			// init Event
			aQView.id(R.id.cancel_button).clicked(new OnClickListener() {

				@Override
				public void onClick(View v) {
					if (aQView.id(R.id.suggest_layout).getView()
							.getVisibility() == View.GONE) {
						pref.edit().remove(Common.SUGGEST_TITLE_PREF).commit();
						mSuggestWindow.dismiss();
						mSuggestWindow = null;
					} else {
						Button bt = (Button) v;
						bt.setText(getString(R.string.cancel));
						aQView.id(R.id.suggest_layout).gone();
					}
				}
			});
			aQView.id(R.id.send_button).clicked(new OnClickListener() {

				@Override
				public void onClick(View v) {
					if (aQView.id(R.id.suggest_layout).getView()
							.getVisibility() == View.GONE) {
						aQView.id(R.id.cancel_button).text(
								getString(R.string.delete));
						HrmService service = new HrmService();
						aQView.id(R.id.loading_layout).visible();
						aQView.id(R.id.suggest_layout).gone();
						service.requestGetSuggestion(mTitleEditText.getText()
								.toString());
						ThreadModel model = new ThreadModel();
						final SuggestListAdapter adapter = new SuggestListAdapter(
								getActivity(), model);
						suggestListview.setAdapter(adapter);
						service.setCallback(new ThreadTaskCallback() {

							@Override
							public void onSuccess(
									final ThreadModel threadItemModel) {
								adapter.setModel(threadItemModel);
								adapter.notifyDataSetChanged();
								aQView.id(R.id.loading_layout).gone();
								aQView.id(R.id.suggest_layout).visible();
								pref.edit()
										.putString(
												Common.SUGGEST_TITLE_PREF,
												mTitleEditText.getText()
														.toString()).commit();
								suggestListview
										.setOnItemClickListener(new OnItemClickListener() {

											@Override
											public void onItemClick(
													AdapterView<?> arg0,
													View arg1, int arg2,
													long arg3) {
												Thread threadItem = threadItemModel
														.get(arg2);
												pref.edit()
														.putString(
																Common.THREAD_ID,
																threadItem
																		.getThread_id())
														.commit();
												mState = false;
												mSuggestWindow.dismiss();
												Fragment fragment = new ThreadDetailFragment();
												switchFragment(fragment);
											}
										});
							}

							@Override
							public void onError() {

							}
						});
					} else {
						mSuggestWindow.dismiss();
						mSuggestWindow = null;
						initPopupWindow(mTitleEditText.getText().toString());
					}
				}
			});

			/* blur background */
			WindowManager.LayoutParams lp = mSuggestWindow.getWindow()
					.getAttributes();
			lp.dimAmount = 0.0f;
			lp.gravity = Gravity.CENTER;
			mSuggestWindow.getWindow().setAttributes(lp);
			mSuggestWindow.getWindow().addFlags(
					WindowManager.LayoutParams.FLAG_BLUR_BEHIND);

			// set view
			mSuggestWindow.setContentView(layout);
			mSuggestWindow.setCanceledOnTouchOutside(true);
			mSuggestWindow.getWindow().setSoftInputMode(
					WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE);
			mSuggestWindow.show();

			mSuggestWindow.setOnDismissListener(new OnDismissListener() {

				@Override
				public void onDismiss(DialogInterface dialog) {
					if (mState) {
						pref.edit().remove(Common.SUGGEST_TITLE_PREF).commit();
					}
				}
			});

			if (isOpenned) {
				aQView.id(R.id.send_button).click();
			}

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

		// Update notification
		BusProvider.getInstance().post(new NotificationUpdateEvent());

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
			pref.edit().remove(Common.SUGGEST_TITLE_PREF).commit();
			validator.validateAsync();
			break;
		case R.id.cancel_button:
			pref.edit().remove(Common.SUGGEST_TITLE_PREF).commit();
			mComposeWindow.dismiss();
			mComposeWindow = null;
			break;
		case R.id.photo_button:
			Intent intent = new Intent(getSherlockActivity(),
					CustomGalleryActivity.class);
			startActivityForResult(intent, Common.SELECT_PICTURE);
			break;
		default:
			break;
		}
	}

	public void onActivityResult(int requestCode, int resultCode, Intent data) {
		if (resultCode == android.app.Activity.RESULT_OK) {
			if (requestCode == Common.SELECT_PICTURE) {
				String selectedImage = data.getStringExtra("selectImages");
				initialize(selectedImage);
				LinearLayout linear = (LinearLayout) mAQueryComPoseView.id(
						R.id.post_image_layout).getView();
				linear.removeAllViewsInLayout();
				for (QueueItem queue : mQueueItems) {
					final String path = queue.path;
					Bitmap bitmap = decodeFilePath(path);
					ImageView img = new ImageView(getSherlockActivity());
					img.setVisibility(View.VISIBLE);
					img.setPadding(10, 10, 10, 10);
					img.setAdjustViewBounds(true);
					img.setMaxHeight(100);
					img.setMaxWidth(100);
					img.setImageBitmap(bitmap);
					img.setClickable(true);
					img.setOnClickListener(new OnClickListener() {

						@Override
						public void onClick(View v) {
							Intent intent = new Intent();
							intent.setAction(Intent.ACTION_VIEW);
							File file = new File(path);
							intent.setDataAndType(Uri.fromFile(file), "image/*");
							startActivityForResult(intent,
									Common.SELECT_PICTURE);
							startActivity(intent);
						}
					});
					linear.addView(img);
				}
				// mViewOrders = new Runnable() {
				// @Override
				// public void run() {
				// upload();
				// }
				// };
				// java.lang.Thread thread1 = new java.lang.Thread(null,
				// mViewOrders, "Background");
				// thread1.start();
				// mProgressDialog = ProgressDialog.show(getSherlockActivity(),
				// "Please wait...", "Uploading image ...", true, true);
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
			}
			imagecursor.close();

			mQueueItems.add(queueItem);
		}
	}

	class QueueItem {
		String path;
		long media_id;
	}

	// private void upload() {
	// for (QueueItem queue : mQueueItems) {
	// String selectedImagePath = queue.path;
	// Bitmap bitmap = decodeFilePath(selectedImagePath);
	//
	// ByteArrayOutputStream stream = new ByteArrayOutputStream();
	// bitmap.compress(Bitmap.CompressFormat.JPEG, 90, stream);
	// byte[] byte_arr = stream.toByteArray();
	// String image_str = Base64.encodeBytes(byte_arr);
	// ArrayList<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>();
	// nameValuePairs.add(new BasicNameValuePair("image", image_str));
	// nameValuePairs.add(new BasicNameValuePair("path", queue.media_id
	// + ".jpg"));
	// try {
	// HttpClient httpclient = new DefaultHttpClient();
	// HttpPost httppost = new HttpPost(
	// "http://113.164.1.45/webservice/base.php");
	// httppost.setEntity(new UrlEncodedFormEntity(nameValuePairs));
	// httpclient.execute(httppost);
	// } catch (Exception e) {
	// System.out.println("Error in http connection " + e.toString());
	// }
	// }
	// getSherlockActivity().runOnUiThread(returnRes);
	// }
	//
	// private Runnable returnRes = new Runnable() {
	//
	// @Override
	// public void run() {
	// mProgressDialog.dismiss();
	// }
	// };

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
		for (QueueItem queue : mQueueItems) {
			String selectedImagePath = queue.path;
			Bitmap bitmap = decodeFilePath(selectedImagePath);

			ByteArrayOutputStream stream = new ByteArrayOutputStream();
			bitmap.compress(Bitmap.CompressFormat.JPEG, 90, stream);
			byte[] byte_arr = stream.toByteArray();
			String image_str = Base64.encodeBytes(byte_arr);
			thread.getThread_image().add(image_str);
		}

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
