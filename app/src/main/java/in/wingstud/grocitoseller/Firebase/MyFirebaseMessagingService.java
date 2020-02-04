package in.wingstud.grocitoseller.Firebase;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.Context;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.media.AudioAttributes;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Build;
import androidx.core.app.NotificationCompat;
import android.util.Log;


import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;

import java.util.Map;

import in.wingstud.grocitoseller.Common.Constrants;
import in.wingstud.grocitoseller.Common.SharedPrefManager;
import in.wingstud.grocitoseller.R;
import in.wingstud.grocitoseller.activity.Dashboard;

public class MyFirebaseMessagingService extends FirebaseMessagingService {

    private static final String CHANNEL_NAME = "Grocito";
    private static final String CHANNEL_DESC = "Firebase Cloud Messaging";
    private static final String TAG = "tag";

    private int numMessages = 0;
//    private FCMData fcmData;
    int count = 0;

    @Override
    public void onNewToken(String s) {
        super.onNewToken(s);
        Log.e("NEW_TOKEN", s);
    }

    @Override
    public void onMessageReceived(RemoteMessage remoteMessage) {
        super.onMessageReceived(remoteMessage);
        if (remoteMessage.getData().size() > 0) {
            Log.d(TAG, "Message data payload: " + remoteMessage.getData());

            RemoteMessage.Notification notification = remoteMessage.getNotification();
            Map<String, String> data = remoteMessage.getData();
            Log.d("FROM", remoteMessage.getFrom());

            sendNotification(notification, data);
        }

        if (remoteMessage.getNotification() != null) {
            Log.d(TAG, "Message Notification Body: " + remoteMessage.getNotification().getBody());
        }
    }

    private void sendNotification(RemoteMessage.Notification notification, Map<String, String> data) {

//        try {
//            fcmData = new FCMData();
//            fcmData.setBody(data.get("body"));
//            fcmData.setTitle(data.get("title"));
//            fcmData.setType(data.get("type"));
//
//            Log.e("NOTIFICATION BODY", data.get("body"));
//            Log.e("NOTIFICATION TITLE", data.get("title"));
//            Log.e("NOTIFICATION TYPE", data.get("type"));
//        } catch (Exception e) {
//            e.printStackTrace();
//        }


//        NotificationCompat.BigTextStyle style = new NotificationCompat.BigTextStyle();
//        style.bigText(fcmData.getBody());
//        style.setBigContentTitle(fcmData.getTitle());
//
//        Log.d("DataStore", fcmData.toString());
//
//
//        Intent intent = new Intent(this, HomeScreen.class);
//        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP
//                | Intent.FLAG_ACTIVITY_CLEAR_TASK
//                | Intent.FLAG_ACTIVITY_NEW_TASK);
//
//        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT);

        NotificationCompat.Builder notificationBuilder = new NotificationCompat.Builder(this, "Grocito")
                .setContentTitle("Grocito")
//                .setContentText("")
                .setAutoCancel(true)
                .setSound(RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION))
//                .setContentIntent(pendingIntent)
//                .setStyle(style)
                .setLargeIcon(BitmapFactory.decodeResource(getResources(), R.drawable.logo))
                .setColor(getResources().getColor(R.color.colorPrimary))
                .setLights(Color.RED, 1000, 300)
                .setDefaults(Notification.DEFAULT_VIBRATE)
                .setNumber(++numMessages)
                .setSmallIcon(R.drawable.logo);

        NotificationManager notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationChannel channel = new NotificationChannel(
                    "Grocito", CHANNEL_NAME, NotificationManager.IMPORTANCE_DEFAULT
            );
            channel.setDescription(CHANNEL_DESC);
            channel.setShowBadge(true);
            channel.canShowBadge();
            channel.enableLights(true);
            channel.setLightColor(Color.RED);
            Uri notificationSoundUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);


            if (notificationSoundUri != null) {
                // Changing Default mode of notification_green
                notificationBuilder.setDefaults(Notification.DEFAULT_VIBRATE);

                // Creating an Audio Attribute
                AudioAttributes audioAttributes = new AudioAttributes.Builder()
                        .setContentType(AudioAttributes.CONTENT_TYPE_SONIFICATION)
                        .setUsage(AudioAttributes.USAGE_ALARM)
                        .build();

                channel.setSound(notificationSoundUri, audioAttributes);
            }

            channel.enableVibration(true);
            channel.setVibrationPattern(new long[]{100, 200, 300, 400, 500});

            assert notificationManager != null;
            notificationManager.createNotificationChannel(channel);
        }

        assert notificationManager != null;
        notificationManager.notify(0, notificationBuilder.build());

//        if (SharedPrefManager.getCartItemCount(Constrants.CartItemCount)==0){
//            count=1;
//        }else {
//            count = SharedPrefManager.getCartItemCount(Constrants.CartItemCount);
//            count+=1;
//        }
//        Dashboard.getInstance().setCountItem(count);

    }

}
