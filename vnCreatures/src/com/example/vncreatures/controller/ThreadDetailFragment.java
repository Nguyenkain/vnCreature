package com.example.vncreatures.controller;

import java.util.ArrayList;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
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
import com.example.vncreatures.common.Utils;
import com.example.vncreatures.customItems.DiscussionQuickAction;
import com.example.vncreatures.customItems.PostListAdapter;
import com.example.vncreatures.customItems.PostListAdapter.Callback;
import com.example.vncreatures.customItems.eventbus.BusProvider;
import com.example.vncreatures.customItems.eventbus.NotificationUpdateEvent;
import com.example.vncreatures.model.discussion.ReportModel;
import com.example.vncreatures.model.discussion.Report;
import com.example.vncreatures.model.discussion.Thread;
import com.example.vncreatures.model.discussion.ThreadModel;
import com.example.vncreatures.rest.HrmService;
import com.example.vncreatures.rest.HrmService.PostTaskCallback;
import com.example.vncreatures.rest.HrmService.ReportTypeCallback;
import com.example.vncreatures.rest.HrmService.ThreadTaskCallback;
import com.mobsandgeeks.saripaar.Rule;
import com.mobsandgeeks.saripaar.Validator;
import com.mobsandgeeks.saripaar.Validator.ValidationListener;
import com.mobsandgeeks.saripaar.annotation.Required;

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
    private Report mReportType;

    SharedPreferences pref;

    @Required(order = 1, message = Common.CONTENT_MESSAGE)
    private EditText mPostContent = null;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.thread_detail_layout, null);

        setHasOptionsMenu(true);

        // get preference
        pref = PreferenceManager.getDefaultSharedPreferences(getActivity());
        if (savedInstanceState != null) {
            mUserId = savedInstanceState.getString(Common.USER_ID, null);
            if (mUserId != null) {
                pref.edit().putString(Common.USER_ID, mUserId).commit();
                pref.edit().putString(Common.THREAD_ID, mThreadId).commit();
            }
        }
        mThreadId = pref.getString(Common.THREAD_ID, null);
        mUserId = pref.getString(Common.USER_ID, null);

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
        if (userid != null) {
            outState.putString(Common.USER_ID, userid);
        }
        if (mThreadId != null) {
            outState.putString(Common.THREAD_ID, mThreadId);
        }
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
        if (mThreadId != null) {
            service.requestGetThreadById(mThreadId);
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
                mMode = getSherlockActivity().startActionMode(
                        new AnActionModeOfEpicProportions(thread, contentEdit,
                                content));
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
                doneButton.setOnClickListener(new View.OnClickListener() {

                    @Override
                    public void onClick(View v) {
                        if (!contentEdit.getText().toString()
                                .equalsIgnoreCase(content.getText().toString())) {
                            final Thread newThread = new Thread();
                            newThread.setPost_id(thread.getPost_id());
                            newThread.setPost_content(contentEdit.getText()
                                    .toString());
                            HrmService service = new HrmService();
                            service.setCallback(new PostTaskCallback() {

                                @Override
                                public void onSuccess(String result) {
                                    initCommentData();
                                    mMode.finish();
                                }

                                @Override
                                public void onError() {
                                    // TODO Auto-generated method stub

                                }
                            });
                            service.requestAddPost(newThread);
                        } else {
                            mMode.finish();
                        }

                    }
                });
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
        final EditText contentEdit = aQuery.id(R.id.thread_content_EditText)
                .getEditText();
        final TextView content = aQuery.id(R.id.content_textView).getTextView();
        switch (v.getId()) {
        case R.id.comment_button:
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
        case R.id.action_button:
            quickAction.onShowBar(v);
            quickAction.setCallback(new DiscussionQuickAction.Callback() {

                @Override
                public void onQuickActionClicked(QuickActionWidget widget,
                        int position) {
                    switch (position) {
                    case 0:
                        content.setVisibility(View.GONE);
                        contentEdit.setVisibility(View.VISIBLE);
                        contentEdit.requestFocus();
                        mMode = getSherlockActivity().startActionMode(
                                new AnActionModeOfEpicProportions(mThread,
                                        contentEdit, content));
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
                                        if (!contentEdit
                                                .getText()
                                                .toString()
                                                .equalsIgnoreCase(
                                                        content.getText()
                                                                .toString())) {
                                            Thread newThread = new Thread();
                                            newThread.setThread_id(mThread
                                                    .getThread_id());
                                            newThread
                                                    .setThread_content(contentEdit
                                                            .getText()
                                                            .toString());
                                            HrmService service = new HrmService();
                                            service.setCallback(new PostTaskCallback() {

                                                @Override
                                                public void onSuccess(
                                                        String result) {
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
                                            service.requestAddThread(newThread);
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

}
