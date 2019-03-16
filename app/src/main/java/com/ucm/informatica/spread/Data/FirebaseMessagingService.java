package com.ucm.informatica.spread.Data;

import android.app.PendingIntent;
import android.content.Intent;
import android.support.v4.app.NotificationCompat;
import android.support.v4.app.NotificationManagerCompat;

import com.google.firebase.messaging.RemoteMessage;
import com.ucm.informatica.spread.Activities.AlertDetailsActivity;
import com.ucm.informatica.spread.R;

import java.util.Map;

import static com.ucm.informatica.spread.Utils.Constants.Notifications.*;

public class FirebaseMessagingService extends com.google.firebase.messaging.FirebaseMessagingService {

    @Override
    public void onNewToken(String data) {
        super.onNewToken(data);
    }

    @Override
    public void onMessageReceived(RemoteMessage remoteMessage) {
        Map<String, String> notificationData = remoteMessage.getData();

        Intent notificationIntent = new Intent(this, AlertDetailsActivity.class);
        notificationIntent.putExtra(NOTIFICATION_MESSAGE, notificationData.get(NOTIFICATION_DATA));
        notificationIntent.addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP | Intent.FLAG_ACTIVITY_CLEAR_TOP);
        PendingIntent pendingIntent = PendingIntent.getActivity(this,0,
                notificationIntent, PendingIntent.FLAG_UPDATE_CURRENT);

        NotificationCompat.Builder builder = new NotificationCompat.Builder(this, NOTIFICATION_CHANNEL_ID)
                .setContentTitle(notificationData.get(NOTIFICATION_DATA_TITLE))
                .setContentText(notificationData.get(NOTIFICATION_DATA_SUBTITLE))
                .setSmallIcon(R.drawable.ic_alert)
                .setPriority(NotificationCompat.PRIORITY_DEFAULT)
                .setContentIntent(pendingIntent)
                .setColor(getResources().getColor(R.color.snackbarAlertColor))
                .setAutoCancel(true);

        NotificationManagerCompat notificationManager = NotificationManagerCompat.from(this);

        notificationManager.notify(0, builder.build());
    }
}