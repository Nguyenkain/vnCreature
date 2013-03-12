package com.example.vncreatures.rest;

import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLConnection;
import java.net.UnknownHostException;
import java.util.ArrayList;

import android.content.Context;
import android.os.AsyncTask;
import android.widget.ImageView;

import com.example.vncreatures.model.CategoryModel;
import com.example.vncreatures.model.CreatureGroupListModel;
import com.example.vncreatures.model.CreatureModel;
import com.example.vncreatures.model.NewsModel;
import com.example.vncreatures.model.ProvinceModel;
import com.example.vncreatures.model.discussion.FacebookUser;
import com.example.vncreatures.model.discussion.ReportModel;
import com.example.vncreatures.model.discussion.Report;
import com.example.vncreatures.model.discussion.Thread;
import com.example.vncreatures.model.discussion.ThreadModel;

public class HrmService {
    private Callback mCallback = null;
    private GroupCallback mGroupCallback = null;
    private NewsCallback mNewsCallback = null;
    private ProvinceCallback mProvinceCallback = null;
    private PostTaskCallback mPostTaskCallback = null;
    private ThreadTaskCallback mThreadTaskCallback = null;
    private ReportTypeCallback mReportTypeCallback = null;
    private ThreadImageTaskCallback mThreadImageTaskCallback = null;
    private CheckUrlCallback mCheckUrlCallback;

    public interface Callback {
        public void onGetAllCreaturesDone(final CreatureModel creatureModel);

        public void onError();
    }

    public interface PostTaskCallback {
        public void onSuccess(final String result);

        public void onError();
    }

    public void setCallback(final PostTaskCallback callback) {
        mPostTaskCallback = callback;
    }

    public interface ThreadTaskCallback {
        public void onSuccess(final ThreadModel threadModel);

        public void onError();
    }

    public void setCallback(final ThreadTaskCallback callback) {
        mThreadTaskCallback = callback;
    }
    
    public interface ThreadImageTaskCallback {
        public void onSuccess(final ThreadModel threadModel);

        public void onError();
    }

    public void setCallback(final ThreadImageTaskCallback callback) {
    	mThreadImageTaskCallback = callback;
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

    public interface CheckUrlCallback {
        public void onSuccess(final boolean result);

        public void onImagesVerify(final ArrayList<String> listImages);

        public void onError();
    }

    public void setCallback(final CheckUrlCallback callback) {
        mCheckUrlCallback = callback;
    }

    public interface ProvinceCallback {
        public void onSuccess(final ProvinceModel provinceModel);

        public void onError();
    }

    public void setCallback(final ProvinceCallback callback) {
        mProvinceCallback = callback;
    }

    public interface NewsCallback {
        public void onGetCatSuccess(final CategoryModel catModel);

        public void onGetNewsSuccess(final NewsModel newsModel);

        public void onError();
    }

    public void setCallback(final NewsCallback callback) {
        mNewsCallback = callback;
    }

    public interface ReportTypeCallback {
        public void onSuccess(final ReportModel reportModel);

        public void onError();
    }

    public void setCallback(final ReportTypeCallback callback) {
        mReportTypeCallback = callback;
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

    public boolean requestGetNews(String catId, String page) {
        GetNewsTask task = new GetNewsTask();
        task.execute(catId, page);
        return true;
    }

    public boolean requestGetNewsDetail(String newsId) {
        GetNewsDetailTask task = new GetNewsDetailTask();
        task.execute(newsId);
        return true;
    }

    public boolean requestGetProvince(String creatureId) {
        GetProvinceTask task = new GetProvinceTask();
        task.execute(creatureId);
        return true;
    }

    public boolean requestGetNationalPark(String nationParkId) {
        GetNationalParkTask task = new GetNationalParkTask();
        task.execute(nationParkId);
        return true;
    }

    public boolean requestGetAllThread() {
        GetThreadTask task = new GetThreadTask();
        task.execute("");
        return true;
    }

    public boolean requestGetThreadById(String threadId) {
        GetThreadTask task = new GetThreadTask();
        task.execute(threadId);
        return true;
    }
    
    public boolean requestGetThreadImageById(String threadId) {
        GetThreadImageTask task = new GetThreadImageTask();
        task.execute(threadId);
        return true;
    }

    public boolean requestGetPostByThreadId(String threadId) {
        GetPostTask task = new GetPostTask();
        task.execute(threadId);
        return true;
    }

    public boolean requestGetNotification(String userId) {
        GetNotificationTask task = new GetNotificationTask();
        task.execute(userId);
        return true;
    }

    public boolean requestGetPictureProfile(Context context, ImageView imv,
            String profileId) {
        GetUrlProfilePic task = new GetUrlProfilePic(context, imv);
        task.execute(profileId);
        return true;
    }

    public boolean requestGetReportType() {
        GetReportTypeTask task = new GetReportTypeTask();
        task.execute();
        return true;
    }

    public boolean requestGetSuggestion(String title) {
        GetSuggestionTask task = new GetSuggestionTask();
        task.execute(title);
        return true;
    }

    public boolean requestCheckUrl(String url) {
        CheckUrlAsyncTask task = new CheckUrlAsyncTask();
        task.execute(url);
        return true;
    }
    
    public boolean requestVerifyImages(ArrayList<String> listImages) {
        CheckUrlAsyncTask task = new CheckUrlAsyncTask(listImages);
        task.execute();
        return true;
    }

    // POST TO SERVER

    public boolean requestAddUser(FacebookUser fb) {
        AddUserTask task = new AddUserTask(fb);
        task.execute();
        return true;
    }

    public boolean requestAddThread(Thread thread) {
        AddThreadTask task = new AddThreadTask(thread);
        task.execute();
        return true;
    }

    public boolean requestAddPost(Thread thread) {
        AddPostTask task = new AddPostTask(thread);
        task.execute();
        return true;
    }

    public boolean requestUpdateNotification(Thread thread) {
        UpdateNotificationTask task = new UpdateNotificationTask(thread);
        task.execute();
        return true;
    }

    public boolean requestAddReport(Report reportType) {
        AddReportTask task = new AddReportTask(reportType);
        task.execute();
        return true;
    }

    // END REQUEST

    // END REQUEST

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

        private volatile boolean running = true;

        @Override
        protected String doInBackground(String... params) {
            while (running) {
                String familyId = params[0];
                String classId = params[1];
                String kingdomId = params[2];
                String result = ServiceUtils.getOrder(familyId, classId,
                        kingdomId);
                return result;
            }
            return null;
        }

        @Override
        protected void onCancelled() {
            running = false;
            super.onCancelled();
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

        private volatile boolean running = true;

        @Override
        protected String doInBackground(String... params) {
            while (running) {
                String result = ServiceUtils.getCategory();
                return result;
            }
            return null;
        }

        @Override
        protected void onCancelled() {
            running = false;
            super.onCancelled();
        }

        @Override
        protected void onPostExecute(String result) {
            if (mNewsCallback != null) {
                if (result == "" || result == null) {
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
    public class GetNewsTask extends AsyncTask<String, Void, String> {

        private volatile boolean running = true;

        @Override
        protected String doInBackground(String... params) {
            while (running) {
                String catId = params[0];
                String page = params[1];
                String result = ServiceUtils.getNews(catId, page);
                return result;
            }
            return null;

        }

        @Override
        protected void onCancelled() {
            running = false;
            super.onCancelled();
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

    private class GetProvinceTask extends AsyncTask<String, Void, String> {

        @Override
        protected String doInBackground(String... params) {
            String creatureId = params[0];
            String result = ServiceUtils.getProvince(creatureId);
            return result;
        }

        @Override
        protected void onPostExecute(String result) {
            if (mProvinceCallback != null) {
                if (result == "" || result == null) {
                    mProvinceCallback.onError();
                } else {
                    ProvinceModel provinceModel = ServiceUtils
                            .parseProvinceModelFromJSON(result);
                    mProvinceCallback.onSuccess(provinceModel);
                }
            }
        }
    }

    private class GetNationalParkTask extends AsyncTask<String, Void, String> {

        @Override
        protected String doInBackground(String... params) {
            String nationalParkId = params[0];
            String result = ServiceUtils.getNationalPark(nationalParkId);
            return result;
        }

        @Override
        protected void onPostExecute(String result) {
            if (mProvinceCallback != null) {
                if (result == "" || result == null) {
                    mProvinceCallback.onError();
                } else {
                    ProvinceModel provinceModel = ServiceUtils
                            .parseProvinceModelFromJSON(result);
                    mProvinceCallback.onSuccess(provinceModel);
                }
            }
        }
    }

    public class GetThreadTask extends AsyncTask<String, Void, String> {

        private volatile boolean running = true;

        @Override
        protected String doInBackground(String... params) {
            while (running) {

                String result = null;
                if (!params[0].equalsIgnoreCase("")) {
                    String id = params[0];
                    result = ServiceUtils.getThreadById(id);
                } else {
                    result = ServiceUtils.getAllThread();
                }
                return result;
            }
            return null;
        }

        @Override
        protected void onCancelled() {
            running = false;
            super.onCancelled();
        }

        @Override
        protected void onPostExecute(String result) {
            if (mThreadTaskCallback != null) {
                if (result == "" || result == null) {
                    mThreadTaskCallback.onError();
                } else {
                    ThreadModel threadModel = ServiceUtils
                            .parseThreadModelFromJSON(result);
                    mThreadTaskCallback.onSuccess(threadModel);
                }
            }
        }
    }
    
    public class GetThreadImageTask extends AsyncTask<String, Void, String> {

        private volatile boolean running = true;

        @Override
        protected String doInBackground(String... params) {
            while (running) {

                String result = null;
                if (!params[0].equalsIgnoreCase("")) {
                    String id = params[0];
                    result = ServiceUtils.getThreadImageById(id);
                } 
                return result;
            }
            return null;
        }

        @Override
        protected void onCancelled() {
            running = false;
            super.onCancelled();
        }

        @Override
        protected void onPostExecute(String result) {
            if (mThreadImageTaskCallback != null) {
                if (result == "" || result == null) {
                	mThreadImageTaskCallback.onError();
                } else {
                    ThreadModel threadModel = ServiceUtils
                            .parseThreadModelFromJSON(result);
                    mThreadImageTaskCallback.onSuccess(threadModel);
                }
            }
        }
    }

    public class GetPostTask extends AsyncTask<String, Void, String> {

        private volatile boolean running = true;

        @Override
        protected String doInBackground(String... params) {
            while (running) {
                String id = params[0];
                String result = ServiceUtils.getPostByThreadId(id);
                return result;
            }
            return null;
        }

        @Override
        protected void onCancelled() {
            running = false;
            super.onCancelled();
        }

        @Override
        protected void onPostExecute(String result) {
            if (mThreadTaskCallback != null) {
                if (result == "" || result == null) {
                    mThreadTaskCallback.onError();
                } else {
                    ThreadModel threadModel = ServiceUtils
                            .parseThreadModelFromJSON(result);
                    mThreadTaskCallback.onSuccess(threadModel);
                }
            }
        }
    }

    public class GetSuggestionTask extends AsyncTask<String, Void, String> {

        private volatile boolean running = true;

        @Override
        protected String doInBackground(String... params) {
            while (running) {
                String title = params[0];
                String result = ServiceUtils.getSuggestion(title);
                return result;
            }
            return null;
        }

        @Override
        protected void onCancelled() {
            running = false;
            super.onCancelled();
        }

        @Override
        protected void onPostExecute(String result) {
            if (mThreadTaskCallback != null) {
                if (result == "" || result == null) {
                    mThreadTaskCallback.onError();
                } else {
                    ThreadModel threadModel = ServiceUtils
                            .parseThreadModelFromJSON(result);
                    mThreadTaskCallback.onSuccess(threadModel);
                }
            }
        }
    }

    public class GetNotificationTask extends AsyncTask<String, Void, String> {

        private volatile boolean running = true;

        @Override
        protected String doInBackground(String... params) {
            while (running) {
                String userId = params[0];
                String result = ServiceUtils.getNotification(userId);
                return result;
            }
            return null;
        }

        @Override
        protected void onCancelled() {
            running = false;
            super.onCancelled();
        }

        @Override
        protected void onPostExecute(String result) {
            if (mThreadTaskCallback != null) {
                if (result == "" || result == null) {
                    mThreadTaskCallback.onError();
                } else {
                    ThreadModel threadModel = ServiceUtils
                            .parseThreadModelFromJSON(result);
                    mThreadTaskCallback.onSuccess(threadModel);
                }
            }
        }
    }

    public class GetUrlProfilePic extends AsyncTask<String, Void, String> {

        private volatile boolean running = true;
        private Context mContext = null;
        private ImageView mImageView = null;

        public GetUrlProfilePic(Context mContext, ImageView mImageView) {
            super();
            this.mContext = mContext;
            this.mImageView = mImageView;
        }

        @Override
        protected String doInBackground(String... params) {
            while (running) {
                String id = params[0];
                String result = ServiceUtils.getProfilePictureUrl(id);
                return result;
            }
            return null;
        }

        @Override
        protected void onCancelled() {
            running = false;
            super.onCancelled();
        }

        @Override
        protected void onPostExecute(String result) {
            if (mThreadTaskCallback != null) {
                if (result == "" || result == null) {
                    mThreadTaskCallback.onError();
                } else {
                    ThreadModel threadModel = ServiceUtils
                            .parseThreadModelFromJSON(result);
                    mThreadTaskCallback.onSuccess(threadModel);
                }
            }
        }
    }

    public class GetReportTypeTask extends AsyncTask<String, Void, String> {

        private volatile boolean running = true;

        @Override
        protected String doInBackground(String... params) {
            while (running) {
                String result = ServiceUtils.getReportType();
                return result;
            }
            return null;
        }

        @Override
        protected void onCancelled() {
            running = false;
            super.onCancelled();
        }

        @Override
        protected void onPostExecute(String result) {
            if (mReportTypeCallback != null) {
                if (result == "" || result == null) {
                    mReportTypeCallback.onError();
                } else {
                    ReportModel reportModel = ServiceUtils
                            .parseReportModelFromJSON(result);
                    mReportTypeCallback.onSuccess(reportModel);
                }
            }
        }
    }

    private class CheckUrlAsyncTask extends AsyncTask<String, Void, Boolean> {

        ArrayList<String> listImages;

        public CheckUrlAsyncTask(ArrayList<String> images) {
            this.listImages = images;
        }

        public CheckUrlAsyncTask() {
            listImages = new ArrayList<String>();
        }

        @Override
        protected Boolean doInBackground(String... params) {
            if (listImages.size() > 0) {
                ArrayList<String> temp = (ArrayList<String>) listImages.clone();
                for (String image : listImages) {
                    try {
                        URL url = new URL(image);
                        URLConnection urlConnection = url.openConnection();

                        HttpURLConnection.setFollowRedirects(false);
                        HttpURLConnection httpURLConnection = (HttpURLConnection) urlConnection;
                        httpURLConnection.setRequestMethod("HEAD");

                        if (httpURLConnection.getResponseCode() == HttpURLConnection.HTTP_OK) {
                            System.out.println("URL Exist");
                        } else {

                            System.out.println("URL not Exists");
                            temp.remove(image);
                        }
                    } catch (UnknownHostException unknownHostException) {
                        System.out.println("UnkownHost");
                        System.out.println(unknownHostException);
                        temp.remove(image);
                    } catch (Exception e) {
                        System.out.println(e);
                        temp.remove(image);
                    }
                }
                listImages = temp;
                return true;
            } else {
                String urlName = params[0];
                try {
                    URL url = new URL(urlName);
                    URLConnection urlConnection = url.openConnection();

                    HttpURLConnection.setFollowRedirects(false);
                    HttpURLConnection httpURLConnection = (HttpURLConnection) urlConnection;
                    httpURLConnection.setRequestMethod("HEAD");

                    if (httpURLConnection.getResponseCode() == HttpURLConnection.HTTP_OK) {
                        System.out.println("URL Exist");
                        return true;
                    } else {

                        System.out.println("URL not Exists");
                        return false;
                    }
                } catch (UnknownHostException unknownHostException) {
                    System.out.println("UnkownHost");
                    System.out.println(unknownHostException);
                    return false;
                } catch (Exception e) {
                    System.out.println(e);
                    return false;
                }
            }
        }

        @Override
        protected void onPostExecute(Boolean result) {
            if (mCheckUrlCallback != null) {
                if (listImages.size() > 0) {
                    mCheckUrlCallback.onImagesVerify(listImages);
                } else {
                    mCheckUrlCallback.onSuccess(result);
                }
            }
        }

    }

    // POST TO SERVER TASK
    private class AddUserTask extends AsyncTask<String, Void, String> {

        FacebookUser mUser;

        public AddUserTask(final FacebookUser fb) {
            this.mUser = fb;
        }

        @Override
        protected String doInBackground(String... params) {
            String result = ServiceUtils.addUser(mUser);
            return result;
        }

        @Override
        protected void onPostExecute(String result) {
            if (mPostTaskCallback != null) {
                if (result == "" || result == null) {
                    mPostTaskCallback.onError();
                } else {
                    mPostTaskCallback.onSuccess(result);
                }
            }
        }
    }

    private class AddThreadTask extends AsyncTask<String, Void, String> {

        Thread mThread = null;

        public AddThreadTask(final Thread thread) {
            this.mThread = thread;
        }

        @Override
        protected String doInBackground(String... params) {
            String result = ServiceUtils.addThread(mThread);
            return result;
        }

        @Override
        protected void onPostExecute(String result) {
            if (mPostTaskCallback != null) {
                if (result == "" || result == null) {
                    mPostTaskCallback.onError();
                } else {
                    mPostTaskCallback.onSuccess(result);
                }
            }
        }
    }

    private class AddPostTask extends AsyncTask<String, Void, String> {

        Thread mThread = null;

        public AddPostTask(final Thread thread) {
            this.mThread = thread;
        }

        @Override
        protected String doInBackground(String... params) {
            String result = ServiceUtils.addPost(mThread);
            return result;
        }

        @Override
        protected void onPostExecute(String result) {
            if (mThread.getPost_id() != null) {
                if (mPostTaskCallback != null) {
                    if (result == "" || result == null) {
                        mPostTaskCallback.onError();
                    } else {
                        mPostTaskCallback.onSuccess(result);
                    }
                }
            } else {
                if (mThreadTaskCallback != null) {
                    if (result == "" || result == null) {
                        mThreadTaskCallback.onError();
                    } else {
                        mThreadTaskCallback.onSuccess(ServiceUtils
                                .parseThreadModelFromJSON(result));
                    }
                }
            }
        }
    }

    private class UpdateNotificationTask extends
            AsyncTask<String, Void, String> {

        Thread mThread = null;

        public UpdateNotificationTask(final Thread thread) {
            this.mThread = thread;
        }

        @Override
        protected String doInBackground(String... params) {
            String result = ServiceUtils.updateNotification(mThread);
            return result;
        }

        @Override
        protected void onPostExecute(String result) {
            if (mPostTaskCallback != null) {
                if (result == "" || result == null) {
                    mPostTaskCallback.onError();
                } else {
                    mPostTaskCallback.onSuccess(result);
                }
            }
        }
    }

    private class AddReportTask extends AsyncTask<String, Void, String> {

        Report mReportType = null;

        public AddReportTask(final Report reportType) {
            this.mReportType = reportType;
        }

        @Override
        protected String doInBackground(String... params) {
            String result = ServiceUtils.addReport(mReportType);
            return result;
        }

        @Override
        protected void onPostExecute(String result) {
            if (mPostTaskCallback != null) {
                if (result == "" || result == null) {
                    mPostTaskCallback.onError();
                } else {
                    mPostTaskCallback.onSuccess(result);
                }
            }
        }
    }
}
