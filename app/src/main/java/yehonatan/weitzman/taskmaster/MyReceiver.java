package yehonatan.weitzman.taskmaster;

import static android.content.Context.NOTIFICATION_SERVICE;
import static android.provider.Settings.System.getString;



import android.app.Activity;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Build;
import android.util.Log;

import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;

public class MyReceiver extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {
        // שליחת כמה סוגי התראה
        String action = intent.getExtras().getString("MyReceiver");
        if (action != null) {
            switch (action) {
                case "Good Morning":
                    createNotification(context, "Good Morning! You have tasks");
                    break;
                case "New Task":
                    createNotification(context, "your task has been added");
                    break;
            }
        }
    }

    private void createNotification(Context context,String message) {
        // Create an Intent to launch the main activity when the notification is clicked
        Intent mainIntent = new Intent(context, MainActivity.class);
        mainIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        PendingIntent pendingIntent = PendingIntent.getActivity(context, 0, mainIntent, PendingIntent.FLAG_UPDATE_CURRENT);

        Uri soundUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);


        NotificationManager mNotificationManager = (NotificationManager) context.getSystemService( NOTIFICATION_SERVICE ) ;
        NotificationCompat.Builder mBuilder = new NotificationCompat.Builder(context , "default_notification_channel_id" ) ;
        mBuilder.setContentTitle( "TaskMaster" ) ;// כותרת
        mBuilder.setContentText( message ) ; // תוכן הודעה
        mBuilder.setTicker( "Notification Service2" );
        mBuilder.setSmallIcon(R.drawable. logo ) ;
        mBuilder.setAutoCancel( true ) ;
        mBuilder.setSound(soundUri);  // Set the sound for the notification
        mBuilder.setContentIntent(pendingIntent); // Set the PendingIntent
        if (android.os.Build.VERSION. SDK_INT >= android.os.Build.VERSION_CODES. O ) {
            int importance = NotificationManager. IMPORTANCE_HIGH ;
            NotificationChannel notificationChannel = new NotificationChannel( "Notification_Service" , "NOTIFICATION_CHANNEL_NAME" , importance) ;
            mBuilder.setChannelId( "NOTIFICATION_CHANNEL_ID" ) ;
            notificationChannel.setSound(soundUri, null);  // Set the sound for the channel
            assert mNotificationManager != null;
            mNotificationManager.createNotificationChannel(notificationChannel) ;
        }
        assert mNotificationManager != null;
        mNotificationManager.notify(( int ) System. currentTimeMillis () , mBuilder.build()) ;
    }





}