package yehonatan.weitzman.taskmaster;

import static android.content.Context.NOTIFICATION_SERVICE;



import android.app.Activity;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.util.Log;

import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;

public class MyReceiver extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {
//        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {//
//        if (android.os.Build.VERSION. SDK_INT >= android.os.Build.VERSION_CODES. O ) {

//            NotificationChannel channel1 = new NotificationChannel("channel", "channel 1", NotificationManager.IMPORTANCE_HIGH);
//            channel1.setDescription("Channel 1...");
//            NotificationManager manager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
//            manager.createNotificationChannel(channel1);
//        }
//        Notification notification = new NotificationCompat.Builder(context, "channel").
//                setSmallIcon(R.drawable.logo).
//                setContentTitle("title").setContentText("message").
//                setPriority(NotificationCompat.PRIORITY_HIGH).build();
//
//        NotificationManagerCompat notificationManager = NotificationManagerCompat.from(context);
//        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
//            if (context.checkSelfPermission(android.Manifest.permission.POST_NOTIFICATIONS) != PackageManager.PERMISSION_GRANTED) {
//                //((Activity) context).requestPermissions(new String[]{android.Manifest.permission.POST_NOTIFICATIONS, Manifest.permission.FOREGROUND_SERVICE}, 100);
//                return;
//            }
//        }
//        notificationManager.notify(1, notification);

        createNotification(context,intent);

    }
    private void createNotification (Context context,Intent intent) {
        NotificationManager mNotificationManager = (NotificationManager) context.getSystemService( NOTIFICATION_SERVICE ) ;
        NotificationCompat.Builder mBuilder = new NotificationCompat.Builder(context , "default_notification_channel_id" ) ;
        mBuilder.setContentTitle( "TaskMaster" ) ;// כותרת
        mBuilder.setContentText( "Hello! now 9:00" ) ; // תוכן הודעה
        mBuilder.setSmallIcon(R.drawable.logo);
        mBuilder.setTicker( "Notification Service2" );
        mBuilder.setSmallIcon(R.drawable. ic_launcher_foreground ) ;
        mBuilder.setAutoCancel( true ) ;
        if (android.os.Build.VERSION. SDK_INT >= android.os.Build.VERSION_CODES. O ) {
            int importance = NotificationManager. IMPORTANCE_HIGH ;
            //String idTask = intent.getExtras().getString("idTask");
            NotificationChannel notificationChannel = new NotificationChannel( "Notification_Service" , "NOTIFICATION_CHANNEL_NAME" , importance) ;
            mBuilder.setChannelId( "NOTIFICATION_CHANNEL_ID" ) ;
            assert mNotificationManager != null;
            mNotificationManager.createNotificationChannel(notificationChannel) ;
        }
        assert mNotificationManager != null;
        mNotificationManager.notify(( int ) System. currentTimeMillis () , mBuilder.build()) ;
    }
}