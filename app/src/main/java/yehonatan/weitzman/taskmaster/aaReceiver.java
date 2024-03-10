package yehonatan.weitzman.taskmaster;

import android.app.Activity;
import android.app.Application;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.util.Log;
import android.widget.Toast;

import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;

public class aaReceiver extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {
        Log.d("===","====");
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                    NotificationChannel channel1 = new NotificationChannel("TaskMaster", "Task Master", NotificationManager.IMPORTANCE_HIGH);
                    channel1.setDescription("Task Master...");
                    NotificationManager manager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
                    manager.createNotificationChannel(channel1);
                }
                Notification notification = new NotificationCompat.Builder(context, "channel").
                        //setSmallIcon(R.drawable.baseline_drive_eta_24)
                        setContentTitle("title").setContentText("this is a Notification :)").
                        setPriority(NotificationCompat.PRIORITY_HIGH).build();

                NotificationManagerCompat notificationManager = NotificationManagerCompat.from(context);
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                    if (context.checkSelfPermission(android.Manifest.permission.POST_NOTIFICATIONS) != PackageManager.PERMISSION_GRANTED) {
                        // ((Activity) context).requestPermissions(new String[]{android.Manifest.permission.POST_NOTIFICATIONS, Manifest.permission.FOREGROUND_SERVICE}, 100);
                        return;
                    }
                }
                notificationManager.notify(1, notification);
            }

    }

