package com.solution.alnahar.foodappserverside.service;

import android.annotation.TargetApi;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Build;
import android.support.v4.app.NotificationCompat;

import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;
import com.solution.alnahar.foodappserverside.Common.Common;
import com.solution.alnahar.foodappserverside.MainActivity;
import com.solution.alnahar.foodappserverside.R;
import com.solution.alnahar.foodappserverside.notificationHelper.NotificationHelper;
import com.solution.alnahar.foodappserverside.orderStatus.OrderStatusActivity;

import java.util.Random;

public class MyFirebaseMessagingService extends FirebaseMessagingService {
    @Override
    public void onMessageReceived(RemoteMessage remoteMessage) {
        super.onMessageReceived(remoteMessage);

        if(Build.VERSION.SDK_INT>=Build.VERSION_CODES.O)
            sendNotificationApi26(remoteMessage);
        else
            sendNotification(remoteMessage);


    }


    @TargetApi(Build.VERSION_CODES.O)
    private void sendNotificationApi26(RemoteMessage remoteMessage) {

        RemoteMessage.Notification  notification=remoteMessage.getNotification();
        String title= notification.getTitle();
        String content=notification.getBody();

        // check to notification and go to  order list
        Intent intent=new Intent(this,OrderStatusActivity.class);
        intent.putExtra("userPhone", Common.currentUser.getPhone());
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);

        PendingIntent pendingIntent=PendingIntent.getActivity(this,0,intent,PendingIntent.FLAG_ONE_SHOT);

        Uri defaultSoundUri= RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);

        NotificationHelper notificationHelper=new NotificationHelper(this);
        Notification.Builder builder= notificationHelper.getFoodOrderNotificationChannel(title,content,pendingIntent,defaultSoundUri);


        // get random id  for notifiction to show all notification
        notificationHelper.getManager().notify(new Random().nextInt(),builder.build());

    }

    private void sendNotification(RemoteMessage remoteMessage) {

        RemoteMessage.Notification notification=remoteMessage.getNotification();
        Intent intent=new Intent(this, OrderStatusActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);

        PendingIntent pendingIntent=PendingIntent.getActivity(this,0,intent,PendingIntent.FLAG_ONE_SHOT);

        Uri defaultSoundUri= RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);

        NotificationCompat.Builder  builder=new NotificationCompat.Builder(getBaseContext());

        builder.setAutoCancel(true)
                .setDefaults(Notification.DEFAULT_ALL)
                .setContentTitle(notification.getTitle())
                .setContentText(notification.getBody())
                .setSmallIcon(R.drawable.ic_notifications_active_black_24dp)
                .setContentInfo("Server")
                .setContentIntent(pendingIntent)
                .setAutoCancel(true)
                .setSound(defaultSoundUri);



        NotificationManager notificationManager= (NotificationManager) getBaseContext().getSystemService(Context.NOTIFICATION_SERVICE);
        notificationManager.notify(0,builder.build());


    }
}
