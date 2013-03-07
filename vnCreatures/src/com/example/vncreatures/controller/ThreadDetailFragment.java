package com.example.vncreatures.controller;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;

import org.json.JSONException;
import org.json.JSONObject;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Resources;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.actionbarsherlock.app.SherlockFragment;
import com.actionbarsherlock.view.ActionMode;
import com.actionbarsherlock.view.Menu;
import com.actionbarsherlock.view.MenuInflater;
import com.actionbarsherlock.view.MenuItem;
import com.alhneiti.QuickAction.QuickActionWidget;
import com.androidquery.AQuery;
import com.example.vncreatures.R;
import com.example.vncreatures.common.Common;
import com.example.vncreatures.common.ServerConfig;
import com.example.vncreatures.common.Utils;
import com.example.vncreatures.customItems.DiscussionQuickAction;
import com.example.vncreatures.customItems.PostListAdapter;
import com.example.vncreatures.customItems.PostListAdapter.Callback;
import com.example.vncreatures.customItems.eventbus.BusProvider;
import com.example.vncreatures.customItems.eventbus.NotificationUpdateEvent;
import com.example.vncreatures.model.discussion.Report;
import com.example.vncreatures.model.discussion.ReportModel;
import com.example.vncreatures.model.discussion.Thread;
import com.example.vncreatures.model.discussion.ThreadModel;
import com.example.vncreatures.rest.HrmService;
import com.example.vncreatures.rest.HrmService.PostTaskCallback;
import com.example.vncreatures.rest.HrmService.ReportTypeCallback;
import com.example.vncreatures.rest.HrmService.ThreadImageTaskCallback;
import com.example.vncreatures.rest.HrmService.ThreadTaskCallback;
import com.facebook.FacebookRequestError;
import com.facebook.HttpMethod;
import com.facebook.LoggingBehavior;
import com.facebook.Request;
import com.facebook.RequestAsyncTask;
import com.facebook.Response;
import com.facebook.Session;
import com.facebook.SessionState;
import com.facebook.Settings;
import com.facebook.UiLifecycleHelper;
import com.mobsandgeeks.saripaar.Rule;
import com.mobsandgeeks.saripaar.Validator;
import com.mobsandgeeks.saripaar.Validator.ValidationListener;
import com.mobsandgeeks.saripaar.annotation.Required;
import com.mobsandgeeks.saripaar.annotation.TextRule;

public class ThreadDetailFragment extends SherlockFragment implements
        OnClickListener {
    AQuery mAQuery;
    Validator validator;
    ActionMode mMode;
    PostListAdapter adapter;

    private String mThreadId = null;
    private String mUserId = null;
    private ListView mListView = null;
    private EditText mReportContent = null;
    private Thread mThread;
    private View mView;
    private View mLoadingView;
    private Dialog mReportWindow;
    private Dialog mEditWindow;
    private Report mReportType;

    SharedPreferences pref;
    UiLifecycleHelper uiHelper;
    private Session.StatusCallback statusCallback = new SessionStatusCallback();

    private static final List<String> PERMISSIONS = Arrays
            .asList("publish_actions");
    private static final String PENDING_PUBLISH_KEY = "pendingPublishReauthorization";
    private boolean pendingPublishReauthorization = false;

    @Required(order = 1, message = Common.CONTENT_MESSAGE)
    @TextRule(order = 2, minLength = 8, message = Common.MINLENGTH_MESSAGE)
    private EditText mPostContent = null;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.thread_detail_layout, null);

        setHasOptionsMenu(true);

        // get preference
        pref = PreferenceManager.getDefaultSharedPreferences(getActivity());
        if (savedInstanceState != null) {
            pendingPublishReauthorization = savedInstanceState.getBoolean(
                    PENDING_PUBLISH_KEY, false);
            mUserId = savedInstanceState.getString(Common.USER_ID, null);
            if (mUserId != null) {
                pref.edit().putString(Common.USER_ID, mUserId).commit();
                pref.edit().putString(Common.THREAD_ID, mThreadId).commit();
            }
        }
        mThreadId = pref.getString(Common.THREAD_ID, null);
        mUserId = pref.getString(Common.USER_ID, null);

        // init session
        uiHelper = new UiLifecycleHelper(getActivity(), statusCallback);
        Settings.addLoggingBehavior(LoggingBehavior.INCLUDE_ACCESS_TOKENS);

        // init ListView
        mListView = (ListView) view.findViewById(R.id.post_listView);
        View threadDetail = inflater.inflate(
                R.layout.thread_detail_footer_header, null);
        mLoadingView = inflater.inflate(R.layout.post_item_loading, null);
        mListView.addHeaderView(threadDetail);

        // init UI
        mAQuery = new AQuery(threadDetail);
        mPostContent = (EditText) view.findViewById(R.id.post_editText);

        // init event
        mAQuery.id(R.id.comment_button).clicked(this);
        mAQuery.id(R.id.action_button).clicked(this);
        mAQuery.id(R.id.share_button).clicked(this);
        mAQuery.id(R.id.report_button).clicked(this);

        // Event
        Button button = (Button) view.findViewById(R.id.post_button);
        // init validator
        validator = new Validator(this);
        ValidateListener listener = new ValidateListener((View) button);
        validator.setValidationListener(listener);
        button.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                mPostContent = (EditText) mView
                        .findViewById(R.id.post_editText);
                v.setEnabled(false);
                validator.validateAsync();
            }
        });

        initData();

        mView = view;
        return view;
    }

    private void updateNotification() {
        HrmService service = new HrmService();
        Thread thread = new Thread();
        thread.setThread_id(mThreadId);
        thread.setUser_id(mUserId);

        service.requestUpdateNotification(thread);
        service.setCallback(new PostTaskCallback() {

            @Override
            public void onSuccess(String result) {
                BusProvider.getInstance().post(new NotificationUpdateEvent());
            }

            @Override
            public void onError() {

            }
        });
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        String userid = pref.getString(Common.USER_ID, null);
        outState.putBoolean(PENDING_PUBLISH_KEY, pendingPublishReauthorization);
        if (userid != null) {
            outState.putString(Common.USER_ID, userid);
        }
        if (mThreadId != null) {
            outState.putString(Common.THREAD_ID, mThreadId);
        }
        uiHelper.onSaveInstanceState(outState);
    }

    private void initData() {
        // Update notification
        updateNotification();
        mAQuery.id(R.id.progressBar1).visible();
        mAQuery.id(R.id.thread_layout).gone();

        HrmService service = new HrmService();
        service.setCallback(new ThreadTaskCallback() {

            @Override
            public void onSuccess(ThreadModel threadModel) {
                if (threadModel != null) {
                    mThread = threadModel.get(0);
                    if (mThread != null) {
                        mAQuery.id(R.id.username_textView).text(
                                mThread.getName());
                        mAQuery.id(R.id.topic_title_TextView).text(
                                mThread.getThread_title());
                        mAQuery.id(R.id.time_textView).text(
                                mThread.getThread_created_time());
                        mAQuery.id(R.id.content_textView).text(
                                mThread.getThread_content());
                        mAQuery.id(R.id.thread_content_EditText).text(
                                mThread.getThread_content());
                        mAQuery.id(R.id.share_button).clicked(
                                ThreadDetailFragment.this);
                        if (mThread.getLast_modified_time() != null) {
                            mAQuery.id(R.id.thread_last_modified_time_textView)
                                    .visible()
                                    .text(String.format(
                                            getString(R.string.last_modified),
                                            mThread.getLast_modified_time()));
                        }
                        String url = "http://graph.facebook.com/"
                                + mThread.getUser_avatar()
                                + "/picture?type=small";
                        mAQuery.id(R.id.avatar_imageView).image(url, true,
                                true, 0, R.drawable.no_thumb, null,
                                AQuery.FADE_IN_NETWORK);

                        mAQuery.id(R.id.progressBar1).gone();
                        mAQuery.id(R.id.thread_layout).visible();

                        // init Thread edit
                        final DiscussionQuickAction quickAction = new DiscussionQuickAction(
                                getSherlockActivity());
                        mUserId = mUserId.replace("\n", "").replace("\t", "");
                        if (mUserId.equalsIgnoreCase(mThread.getUser_id())) {
                            mAQuery.id(R.id.action_button).visible();
                        } else {
                            mAQuery.id(R.id.report_button).visible();
                        }

                    }
                }
            }

            @Override
            public void onError() {

            }
        });
        service.setCallback(new ThreadImageTaskCallback() {

			@Override
			public void onSuccess(ThreadModel threadModel) {
				if (threadModel != null) {
					LinearLayout linear = (LinearLayout) mAQuery.id(
							R.id.thread_image_layout).getView();
					linear.removeAllViewsInLayout();
					final Thread thread = new Thread();
					thread.setThread_image(new ArrayList<String>());
					for (int i = 0; i < threadModel.count(); i++) {
						thread.getThread_image().add(ServerConfig.ROOT + threadModel.get(i).getImage_link());
					}
					for (int i = 0; i < thread.getThread_image().size(); i++) {
						final int position;
						position = i;
						ImageView img = new ImageView(getSherlockActivity());
						img.setVisibility(View.VISIBLE);
						img.setPadding(10, 10, 10, 10);
						img.setAdjustViewBounds(true);
						img.setMaxHeight(100);
						img.setMaxWidth(100);
						mAQuery.id(img)
		                .progress(R.id.progressBar1)
		                .image(thread.getThread_image().get(i), true, true, 0, AQuery.GONE, null,
		                        AQuery.FADE_IN_NETWORK);
						img.setClickable(true);
						img.setOnClickListener(new OnClickListener() {

							@Override
							public void onClick(View v) {
								Intent intent = new Intent();
								intent.setClass(getSherlockActivity(),
										ImageViewFlipperActivity.class);
								Bundle bundle = new Bundle();
								bundle.putStringArrayList(
										Common.CREATURE_URL_IMAGES_LIST,
										thread.getThread_image());
								bundle.putInt(
										Common.CREATURE_URL_IMAGES_POSITION, position);
								intent.putExtras(bundle);

								startActivity(intent);
							}
						});
						linear.addView(img);
					}
				}
			}

			@Override
			public void onError() {

			}
		});
        if (mThreadId != null) {
            service.requestGetThreadById(mThreadId);
            service.requestGetThreadImageById(mThreadId);
            getSherlockActivity().setSupportProgressBarIndeterminateVisibility(
                    true);
            initCommentData();
        }
    }

    private void initCommentData() {
        HrmService service = new HrmService();
        ThreadModel model = new ThreadModel();
        adapter = new PostListAdapter(getActivity(), model);
        mListView.setAdapter(adapter);
        service.setCallback(new ThreadTaskCallback() {

            @Override
            public void onSuccess(ThreadModel threadModel) {
                if (threadModel != null) {
                    adapter.setModel(threadModel);
                    adapter.notifyDataSetChanged();
                    getSherlockActivity()
                            .setSupportProgressBarIndeterminateVisibility(false);
                }
            }

            @Override
            public void onError() {

            }
        });
        service.requestGetPostByThreadId(mThreadId);

        adapter.setCallback(new Callback() {

            @Override
            public void onEditThread(final Thread thread,
                    final EditText contentEdit, final TextView content) {

                initEditWindow(thread, content.getText().toString());

                /*
                 * mMode = getSherlockActivity().startActionMode( new
                 * AnActionModeOfEpicProportions(thread, contentEdit, content));
                 * int doneButtonId = Resources.getSystem().getIdentifier(
                 * "action_mode_close_button", "id", "android"); View doneButton
                 * = null; if (doneButtonId != 0) { doneButton =
                 * getSherlockActivity().findViewById( doneButtonId); } else {
                 * doneButton = getSherlockActivity().findViewById(
                 * R.id.abs__action_mode_close_button); }
                 * doneButton.setOnClickListener(new View.OnClickListener() {
                 * 
                 * @Override public void onClick(View v) { if
                 * (!contentEdit.getText().toString()
                 * .equalsIgnoreCase(content.getText().toString())) { final
                 * Thread newThread = new Thread();
                 * newThread.setPost_id(thread.getPost_id());
                 * newThread.setPost_content(contentEdit.getText() .toString());
                 * HrmService service = new HrmService();
                 * service.setCallback(new PostTaskCallback() {
                 * 
                 * @Override public void onSuccess(String result) {
                 * initCommentData(); mMode.finish(); }
                 * 
                 * @Override public void onError() { // TODO Auto-generated
                 * method stub
                 * 
                 * } }); service.requestAddPost(newThread); } else {
                 * mMode.finish(); }
                 * 
                 * } });
                 */
            }

            @Override
            public void onDeleteThread(final Thread thread) {
                new AlertDialog.Builder(getSherlockActivity())
                        .setIcon(android.R.drawable.ic_dialog_alert)
                        .setTitle(R.string.delete)
                        .setMessage(R.string.delete_message)
                        .setPositiveButton(R.string.confirm,
                                new DialogInterface.OnClickListener() {

                                    @Override
                                    public void onClick(DialogInterface dialog,
                                            int which) {
                                        HrmService service = new HrmService();
                                        service.setCallback(new PostTaskCallback() {

                                            @Override
                                            public void onSuccess(String result) {
                                                initCommentData();
                                            }

                                            @Override
                                            public void onError() {

                                            }
                                        });
                                        service.requestAddPost(thread);
                                    }
                                }).setNegativeButton(R.string.cancel, null)
                        .show();
            }
        });
    }

    private void postNewComment(final View v) {
        getSherlockActivity()
                .setSupportProgressBarIndeterminateVisibility(true);
        mListView.addFooterView(mLoadingView);
        mListView.setSelection(mListView.getCount() - 1);
        String content = mPostContent.getText().toString();
        String userid = pref.getString(Common.USER_ID, null);
        Thread thread = new Thread();
        thread.setPost_content(content);
        thread.setUser_id(userid);
        thread.setThread_id(mThreadId);
        if (userid != null) {
            HrmService service = new HrmService();
            service.requestAddPost(thread);
            /*
             * service.setCallback(new PostTaskCallback() {
             * 
             * @Override public void onSuccess(String result) {
             * mListView.removeFooterView(mLoadingView);
             * mPostContent.setText(""); getSherlockActivity()
             * .setSupportProgressBarIndeterminateVisibility(true);
             * initCommentData(); v.setEnabled(true); }
             * 
             * @Override public void onError() {
             * 
             * } });
             */
            service.setCallback(new ThreadTaskCallback() {

                @Override
                public void onSuccess(ThreadModel threadModel) {
                    mListView.removeFooterView(mLoadingView);
                    mPostContent.setText("");
                    getSherlockActivity()
                            .setSupportProgressBarIndeterminateVisibility(false);
                    adapter.setModel(threadModel);
                    adapter.notifyDataSetChanged();
                    mListView.setSelection(mListView.getCount() - 1);
                    v.setEnabled(true);
                }

                @Override
                public void onError() {

                }
            });
        }
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        Fragment frag = null;
        AQuery aQuery = new AQuery(mView);
        switch (item.getItemId()) {
        case R.id.menu_item_refresh:
            initData();
            break;
        case android.R.id.home:
            frag = new ThreadFragment();
            break;
        case R.id.menu_item_post:
            Utils.toogleLayout(aQuery.id(R.id.postText_layout).getView());
            break;
        default:
            break;
        }
        if (frag != null) {
            switchFragment(frag);
        }
        return super.onOptionsItemSelected(item);
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
            postNewComment(mView);
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

    private final class ValidateEditListener implements ValidationListener {

        private View mView;
        private Thread mThread;
        private String mContent;

        public ValidateEditListener(View v, Thread thread, String content) {
            this.mView = v;
            this.mThread = thread;
            this.mContent = content;
        }

        public void setContent(String content) {
            this.mContent = content;
        }

        @Override
        public void preValidation() {
            // TODO Auto-generated method stub

        }

        @Override
        public void onSuccess() {
            if (mThread.getPost_id() != null) {
                final Thread newThread = new Thread();
                newThread.setPost_id(this.mThread.getPost_id());
                newThread.setPost_content(this.mContent);
                HrmService service = new HrmService();
                service.setCallback(new PostTaskCallback() {

                    @Override
                    public void onSuccess(String result) {
                        initCommentData();
                        mView.setEnabled(true);
                        mEditWindow.dismiss();
                        mEditWindow = null;
                    }

                    @Override
                    public void onError() {

                    }
                });
                service.requestAddPost(newThread);
            } else {
                HrmService service = new HrmService();
                service.setCallback(new PostTaskCallback() {

                    @Override
                    public void onSuccess(String result) {
                        initData();
                        mMode.finish();
                    }

                    @Override
                    public void onError() {
                        // TODO
                        // Auto-generated
                        // method
                        // stub

                    }
                });
                service.requestAddThread(mThread);
            }
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

    private final class AnActionModeOfEpicProportions implements
            ActionMode.Callback {
        EditText mContentEdit;
        TextView mContent;
        Thread mThread;

        public AnActionModeOfEpicProportions(Thread thread,
                EditText contentEdit, TextView content) {
            this.mContentEdit = contentEdit;
            this.mContent = content;
            this.mThread = thread;
        }

        @Override
        public boolean onCreateActionMode(ActionMode mode, Menu menu) {
            // Used to put dark icons on light action bar
            boolean isLight = Common.THEME == R.style.Theme_Sherlock_Light;

            menu.add("Cancel")
                    .setIcon(
                            isLight ? R.drawable.navigation_cancel_inverse
                                    : R.drawable.navigation_cancel)
                    .setShowAsAction(MenuItem.SHOW_AS_ACTION_ALWAYS);

            return true;
        }

        @Override
        public boolean onPrepareActionMode(ActionMode mode, Menu menu) {
            return false;
        }

        @Override
        public boolean onActionItemClicked(ActionMode mode, MenuItem item) {
            if (item.getTitle().toString().equalsIgnoreCase("Cancel")) {
                mode.finish();
            }
            return true;
        }

        @Override
        public void onDestroyActionMode(ActionMode mode) {
            mContent.setVisibility(View.VISIBLE);
            mContentEdit.setVisibility(View.GONE);
        }
    }

    @Override
    public void onClick(View v) {
        AQuery aQuery = new AQuery(mView);
        DiscussionQuickAction quickAction = new DiscussionQuickAction(
                getSherlockActivity());

        final TextView content = aQuery.id(R.id.content_textView).getTextView();
        switch (v.getId()) {
        case R.id.comment_button:
            mPostContent = (EditText) mView.findViewById(R.id.post_editText);
            aQuery.id(R.id.postText_layout).visible();
            break;
        case R.id.report_button:
            initPopupWindow();
            break;
        case R.id.cancel_button:
            mReportWindow.dismiss();
            break;
        case R.id.send_button:
            v.setEnabled(false);
            postNewReport(v);
            break;
        case R.id.share_button:
            sharePost(mThread);
            break;
        case R.id.action_button:
            mPostContent = aQuery.id(R.id.thread_content_EditText)
                    .getEditText();
            quickAction.onShowBar(v);
            quickAction.setCallback(new DiscussionQuickAction.Callback() {

                @Override
                public void onQuickActionClicked(QuickActionWidget widget,
                        int position) {
                    switch (position) {
                    case 0:
                        content.setVisibility(View.GONE);
                        mPostContent.setVisibility(View.VISIBLE);
                        mPostContent.requestFocus();
                        mMode = getSherlockActivity().startActionMode(
                                new AnActionModeOfEpicProportions(mThread,
                                        mPostContent, content));
                        int doneButtonId = Resources.getSystem().getIdentifier(
                                "action_mode_close_button", "id", "android");
                        View doneButton = null;
                        if (doneButtonId != 0) {
                            doneButton = getSherlockActivity().findViewById(
                                    doneButtonId);
                        } else {
                            doneButton = getSherlockActivity().findViewById(
                                    R.id.abs__action_mode_close_button);
                        }
                        doneButton
                                .setOnClickListener(new View.OnClickListener() {

                                    @Override
                                    public void onClick(View v) {
                                        if (!mPostContent
                                                .getText()
                                                .toString()
                                                .equalsIgnoreCase(
                                                        content.getText()
                                                                .toString())) {
                                            Thread newThread = new Thread();
                                            newThread.setThread_id(mThread
                                                    .getThread_id());
                                            newThread
                                                    .setThread_content(mPostContent
                                                            .getText()
                                                            .toString());
                                            Validator validator = new Validator(
                                                    ThreadDetailFragment.this);
                                            ValidateEditListener listener = new ValidateEditListener(
                                                    v, newThread, mPostContent
                                                            .getText()
                                                            .toString());
                                            validator
                                                    .setValidationListener(listener);
                                            validator.validateAsync();
                                        } else {
                                            mMode.finish();
                                        }

                                    }
                                });
                        break;
                    case 1:
                        new AlertDialog.Builder(getSherlockActivity())
                                .setIcon(android.R.drawable.ic_dialog_alert)
                                .setTitle(R.string.delete)
                                .setMessage(R.string.delete_message)
                                .setPositiveButton(R.string.confirm,
                                        new DialogInterface.OnClickListener() {

                                            @Override
                                            public void onClick(
                                                    DialogInterface dialog,
                                                    int which) {
                                                Thread newThread = new Thread();
                                                newThread
                                                        .setThread_id(mThreadId);
                                                HrmService service = new HrmService();
                                                service.setCallback(new PostTaskCallback() {

                                                    @Override
                                                    public void onSuccess(
                                                            String result) {
                                                        Fragment frag = new ThreadFragment();
                                                        switchFragment(frag);
                                                    }

                                                    @Override
                                                    public void onError() {

                                                    }
                                                });
                                                service.requestAddThread(newThread);
                                            }
                                        })
                                .setNegativeButton(R.string.cancel, null)
                                .show();
                        break;
                    case 2:
                        initPopupWindow();
                        break;

                    default:
                        break;
                    }

                }
            });

            break;

        default:
            break;
        }
    }

    private void initPopupWindow() {

        try {
            // We need to get the instance of the LayoutInflater, use the
            // context of this activity
            LayoutInflater inflater = (LayoutInflater) getActivity()
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            // Inflate the view from a predefined XML layout
            View layout = inflater.inflate(R.layout.report_layout, null);
            mReportWindow = new Dialog(getActivity(),
                    R.style.Theme_D1NoTitleDim);

            // init UI
            AQuery aQView = new AQuery(layout);
            final Spinner reportTypeChoose = aQView
                    .id(R.id.report_type_spinner).getSpinner();
            Button sendButton = aQView.id(R.id.send_button).getButton();
            mReportContent = aQView.id(R.id.report_content_EditText)
                    .getEditText();

            HrmService service = new HrmService();
            service.setCallback(new ReportTypeCallback() {

                @Override
                public void onSuccess(ReportModel reportModel) {
                    ArrayList<Report> listReport = reportModel
                            .getReportTypeList();
                    ArrayAdapter<Report> adapter = new ArrayAdapter<Report>(
                            getActivity(),
                            android.R.layout.simple_spinner_dropdown_item,
                            listReport);
                    reportTypeChoose.setAdapter(adapter);
                }

                @Override
                public void onError() {

                }
            });
            service.requestGetReportType();

            reportTypeChoose
                    .setOnItemSelectedListener(new OnItemSelectedListener() {

                        @Override
                        public void onItemSelected(AdapterView<?> adapter,
                                View view, int pos, long id) {
                            mReportType = (Report) adapter
                                    .getItemAtPosition(pos);
                        }

                        @Override
                        public void onNothingSelected(AdapterView<?> arg0) {

                        }
                    });

            // init Event
            sendButton.setOnClickListener(this);
            aQView.id(R.id.cancel_button).clicked(this);

            /* blur background */
            WindowManager.LayoutParams lp = mReportWindow.getWindow()
                    .getAttributes();
            lp.dimAmount = 0.0f;
            lp.gravity = Gravity.CENTER;
            mReportWindow.getWindow().setAttributes(lp);
            mReportWindow.getWindow().addFlags(
                    WindowManager.LayoutParams.FLAG_BLUR_BEHIND);

            // set view
            mReportWindow.setContentView(layout);
            mReportWindow.setCanceledOnTouchOutside(true);
            mReportWindow.getWindow().setSoftInputMode(
                    WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE);
            mReportWindow.show();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void postNewReport(final View v) {
        Report report = new Report();
        report.setThread_id(mThreadId);
        report.setUser_id(mUserId);
        report.setReport_type_id(mReportType.getReport_type_id());
        report.setComment(mReportContent.getText().toString());
        HrmService service = new HrmService();
        service.requestAddReport(report);
        service.setCallback(new PostTaskCallback() {

            @Override
            public void onSuccess(String result) {
                v.setEnabled(true);
                result = result.replace("\n", "");
                String message;
                if (result.equalsIgnoreCase("ok")) {
                    message = getString(R.string.report_done);
                } else {
                    message = getString(R.string.report_existed);
                }
                new AlertDialog.Builder(getSherlockActivity())
                        .setIcon(android.R.drawable.ic_dialog_alert)
                        .setTitle(R.string.report).setMessage(message)
                        .setPositiveButton(R.string.confirm, null).show();
                mReportWindow.dismiss();
            }

            @Override
            public void onError() {

            }
        });

    }

    @SuppressWarnings("deprecation")
    private void initEditWindow(final Thread thread, final String content) {

        try {
            // We need to get the instance of the LayoutInflater, use the
            // context of this activity
            LayoutInflater inflater = (LayoutInflater) getActivity()
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            // Inflate the view from a predefined XML layout
            View layout = inflater.inflate(R.layout.compose_layout, null);
            mEditWindow = new Dialog(getActivity(), R.style.Theme_D1NoTitleDim);

            // init UI
            AQuery aQView = new AQuery(layout);
            aQView.id(R.id.title_editText).gone();
            aQView.id(R.id.button_layout).gone();
            mPostContent = aQView.id(R.id.post_content_EditText).getEditText();
            mPostContent.setText(content);
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

            // init validator
            final Validator editValidator = new Validator(this);
            final ValidateEditListener listener = new ValidateEditListener(
                    (View) sendButton, thread, content);
            editValidator.setValidationListener(listener);

            // init Event
            sendButton.setOnClickListener(new OnClickListener() {

                @Override
                public void onClick(View v) {
                    listener.setContent(mPostContent.getText().toString());
                    v.setEnabled(false);
                    editValidator.validateAsync();
                }
            });
            aQView.id(R.id.cancel_button).clicked(new OnClickListener() {

                @Override
                public void onClick(View v) {
                    mEditWindow.dismiss();
                    mEditWindow = null;
                }
            });

            /* blur background */
            WindowManager.LayoutParams lp = mEditWindow.getWindow()
                    .getAttributes();
            lp.dimAmount = 0.0f;
            lp.gravity = Gravity.CENTER;
            mEditWindow.getWindow().setAttributes(lp);
            mEditWindow.getWindow().addFlags(
                    WindowManager.LayoutParams.FLAG_BLUR_BEHIND);

            // set view
            mEditWindow.setContentView(layout);
            mEditWindow.setCanceledOnTouchOutside(true);
            mEditWindow.getWindow().setSoftInputMode(
                    WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE);
            mEditWindow.show();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void sharePost(Thread thread) {
        final Session session = Session.getActiveSession();

        if (session != null) {

            // Check for publish permissions
            List<String> permissions = session.getPermissions();
            if (!isSubsetOf(PERMISSIONS, permissions)) {
                pendingPublishReauthorization = true;
                Session.NewPermissionsRequest newPermissionsRequest = new Session.NewPermissionsRequest(
                        this, PERMISSIONS);
                session.requestNewPublishPermissions(newPermissionsRequest);
                return;
            }

            final Bundle postParams = new Bundle();
            postParams.putString("name", getString(R.string.share_title));
            postParams.putString("caption",
                    getString(R.string.share_user, thread.getThread_title() ,thread.getName()));
            postParams.putString("description", thread.getThread_content());
            postParams.putString("link", getString(R.string.share_link));
            postParams
                    .putString("picture",
                            "https://raw.github.com/fbsamples/ios-3.x-howtos/master/Images/iossdk_logo.png");

            final Request.Callback callback = new Request.Callback() {
                public void onCompleted(Response response) {
                    JSONObject graphResponse = response.getGraphObject()
                            .getInnerJSONObject();
                    String postId = null;
                    try {
                        postId = graphResponse.getString("id");
                    } catch (JSONException e) {
                        Log.i("Facebook", "JSON error " + e.getMessage());
                    }
                    FacebookRequestError error = response.getError();
                    String share = getString(R.string.share_complete);
                    if (error != null) {
                        Toast.makeText(getActivity().getApplicationContext(),
                                error.getErrorMessage(), Toast.LENGTH_SHORT)
                                .show();
                    } else {
                        Toast.makeText(getActivity().getApplicationContext(),
                                share, Toast.LENGTH_LONG).show();
                    }
                    getSherlockActivity()
                            .setSupportProgressBarIndeterminateVisibility(false);
                }
            };

            new AlertDialog.Builder(getSherlockActivity())
                    .setIcon(android.R.drawable.ic_dialog_alert)
                    .setTitle(R.string.share)
                    .setMessage(R.string.share_message)
                    .setPositiveButton(R.string.confirm,
                            new DialogInterface.OnClickListener() {

                                @Override
                                public void onClick(DialogInterface dialog,
                                        int which) {
                                    Request request = new Request(session,
                                            "me/feed", postParams,
                                            HttpMethod.POST, callback);
                                    getSherlockActivity()
                                            .setSupportProgressBarIndeterminateVisibility(
                                                    true);

                                    RequestAsyncTask task = new RequestAsyncTask(
                                            request);
                                    task.execute();
                                }
                            }).setNegativeButton(R.string.cancel, null).show();

        }
    }

    private boolean isSubsetOf(Collection<String> subset,
            Collection<String> superset) {
        for (String string : subset) {
            if (!superset.contains(string)) {
                return false;
            }
        }
        return true;
    }

    private class SessionStatusCallback implements Session.StatusCallback {
        @Override
        public void call(Session session, SessionState state,
                Exception exception) {
        }
    }

}
