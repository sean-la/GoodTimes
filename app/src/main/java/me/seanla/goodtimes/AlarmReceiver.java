package me.seanla.goodtimes;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

/**
 * Created by sean on 29/04/16.
 */
public class AlarmReceiver extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {
        Intent contentIntent = new Intent(context, AddNewGoodTime.class);
        int requestID = (int) System.currentTimeMillis();
        int flags = PendingIntent.FLAG_CANCEL_CURRENT;
        PendingIntent pIntent = PendingIntent.getActivity(context, requestID, contentIntent, flags);

        Notification notification = new Notification.Builder(context)
                .setContentTitle("Good Times")
                .setContentText("Remember to punch in a good time!")
                .setSmallIcon(R.drawable.ic_menu_star)
                .setContentIntent(pIntent)
                .build();

        NotificationManager manager = (NotificationManager) context.getSystemService(context.NOTIFICATION_SERVICE);
        manager.notify(0, notification);
    }
}
