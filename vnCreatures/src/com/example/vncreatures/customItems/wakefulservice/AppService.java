/***
  Copyright (c) 2009-11 CommonsWare, LLC
  
  Licensed under the Apache License, Version 2.0 (the "License"); you may
  not use this file except in compliance with the License. You may obtain
  a copy of the License at
    http://www.apache.org/licenses/LICENSE-2.0
  Unless required by applicable law or agreed to in writing, software
  distributed under the License is distributed on an "AS IS" BASIS,
  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
  See the License for the specific language governing permissions and
  limitations under the License.
 */

package com.example.vncreatures.customItems.wakefulservice;

import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.media.RingtoneManager;
import android.net.Uri;
import android.preference.PreferenceManager;
import android.support.v4.app.NotificationCompat;
import android.support.v4.app.TaskStackBuilder;
import android.util.Log;

import com.commonsware.cwac.wakeful.WakefulIntentService;
import com.example.vncreatures.R;
import com.example.vncreatures.common.Common;
import com.example.vncreatures.controller.LoginActivity;
import com.example.vncreatures.model.discussion.ThreadModel;
import com.example.vncreatures.rest.HrmService;
import com.example.vncreatures.rest.HrmService.ThreadTaskCallback;

public class AppService extends WakefulIntentService {
    public AppService() {
        super("AppService");
    }

    @Override
    protected void doWakefulWork(Intent intent) {
        Log.d("Update", "Service updated");
        updateNotification();
    }

    private void updateNotification() {
        SharedPreferences pref = PreferenceManager
                .getDefaultSharedPreferences(getApplicationContext());
        String userId = pref.getString(Common.USER_ID, null);
        HrmService service = new HrmService();
        if (userId != null) {
            service.requestGetNotification(userId);
            service.setCallback(new ThreadTaskCallback() {

                @Override
                public void onSuccess(ThreadModel threadModel) {
                    int count = threadModel.countAllNotification();
                    if (count > 0) {
                        Uri alarmSound = RingtoneManager
                                .getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
                        if (alarmSound == null) {
                            alarmSound = RingtoneManager
                                    .getDefaultUri(RingtoneManager.TYPE_ALARM);
                            if (alarmSound == null) {
                                alarmSound = RingtoneManager
                                        .getDefaultUri(RingtoneManager.TYPE_RINGTONE);
                            }
                        }

                        NotificationCompat.Builder mBuilder = new NotificationCompat.Builder(
                                getApplicationContext())
                                .setSmallIcon(R.drawable.vnc_icon)
                                .setContentTitle(getString(R.string.app_name))
                                .setContentText(
                                        getString(
                                                R.string.notification_overall,
                                                count)).setSound(alarmSound)
                                .setAutoCancel(true);
                        // Creates an explicit intent for an Activity in your
                        // app
                        Intent resultIntent = new Intent(
                                getApplicationContext(), LoginActivity.class);

                        // The stack builder object will contain an artificial
                        // back
                        // stack for the
                        // started Activity.
                        // This ensures that navigating backward from the
                        // Activity
                        // leads out of
                        // your application to the Home screen.
                        TaskStackBuilder stackBuilder = TaskStackBuilder
                                .create(getApplicationContext());
                        // Adds the back stack for the Intent (but not the
                        // Intent
                        // itself)
                        stackBuilder.addParentStack(LoginActivity.class);
                        // Adds the Intent that starts the Activity to the top
                        // of
                        // the stack
                        stackBuilder.addNextIntent(resultIntent);
                        PendingIntent resultPendingIntent = stackBuilder
                                .getPendingIntent(0,
                                        PendingIntent.FLAG_UPDATE_CURRENT);
                        mBuilder.setContentIntent(resultPendingIntent);
                        NotificationManager mNotificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
                        // mId allows you to update the notification later on.
                        mNotificationManager.notify(Common.COMMON_ID,
                                mBuilder.build());
                    }
                }

                @Override
                public void onError() {

                }
            });
        }
    }
}
