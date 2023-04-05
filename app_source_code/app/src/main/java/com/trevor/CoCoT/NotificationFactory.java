package com.trevor.CoCoT;


import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Build;

public class NotificationFactory {

    public static void registerNotificationChannel(Context context) {
        // Create the NotificationChannel, but only on API 26+ because
        // the NotificationChannel class is new and not in the support library
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            CharSequence name = "CoCoT";
            String description = "default and all notifications for CoCoT";
            int importance = NotificationManager.IMPORTANCE_HIGH;
            NotificationChannel channel = new NotificationChannel(constants.CHANNEL_ID, name, importance);
            channel.setDescription(description);
            // Register the channel with the system; you can't change the importance
            // or other notification behaviors after this
            NotificationManager notificationManager = context.getSystemService(NotificationManager.class);
            notificationManager.createNotificationChannel(channel);
        }
    }

    public static Notification createExperimentRunningNotification(Context context) {
        Notification.Builder builder = null;
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
//            Intent intent = new Intent(context, MainActivity.class);
            Intent intent = context.getPackageManager().getLaunchIntentForPackage(context.getPackageName());
            PendingIntent pendingIntent = PendingIntent.getActivity(context, 0, intent, PendingIntent.FLAG_IMMUTABLE);

            builder = new Notification.Builder(context, constants.CHANNEL_ID)
                    .setSmallIcon(R.drawable.cocotlogofront)
                    .setContentTitle("CoCoT")
                    .setContentText("Experiment is running, make sure to keep app in foreground, don't lock your phone, and stop it when you are done.")
                    .setSmallIcon(R.drawable.cocotlogofront)
                    .setContentIntent(pendingIntent)
                    .setOngoing(true)
                    .setColor(getNotificationColor())
                    .setColorized(true)
                    .setAutoCancel(false)
                    .setUsesChronometer(true);

        }

        return builder.build();
    }


    private static int getNotificationColor(){
        Color color = new Color();
        return color.argb(255, 44, 160, 90);
    }

}
