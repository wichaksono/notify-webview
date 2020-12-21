package com.webview.init;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.Ringtone;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Build;
import android.support.v4.app.NotificationCompat;
import android.support.v4.app.NotificationManagerCompat;
import android.webkit.JavascriptInterface;
import android.widget.RemoteViews;

import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;

public class WebViewInit {
    public Context mContext;

    /**
     * Instantiate the interface and set the context
     */
    WebViewInit(Context c) {
        mContext = c;
    }


    /**
     * retrieve the ids
     */
    @JavascriptInterface
    public void show(final String packageName, final String icon, final int timer, final String title, final String msg, final String Banner) {
        new android.os.Handler().postDelayed(
                new Runnable() {
                    public void run() {
                        myNotify(packageName, icon, title, msg, Banner);
                    }
                },
                timer);
    }

    void myNotify(String packageName, String icon, final String title, final String msg, final String Banner) {
        PendingIntent pendingIntent;
        Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse("market://details?id=" + packageName));
        pendingIntent = PendingIntent.getActivity(mContext, 0, intent, 0);

        String CHANNEL_ID = "www.onphpid.com";


        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            CharSequence name = "bambang App";// The user-visible name of the channel.
            String description = "For Bambang";
            int importance = NotificationManager.IMPORTANCE_DEFAULT;
            NotificationChannel channel = new NotificationChannel(CHANNEL_ID, name, importance);
            channel.setDescription(description);
            // Register the channel with the system; you can't change the importance
            // or other notification behaviors after this
            NotificationManager notificationManager = mContext.getSystemService(NotificationManager.class);
            notificationManager.createNotificationChannel(channel);
        }

        RemoteViews remoteViews = new RemoteViews(mContext.getPackageName(), R.layout.custom_notification);
        remoteViews.setTextViewText(R.id.titlenotif, title);
        remoteViews.setTextViewText(R.id.textnotif, msg);
        Bitmap myBitmap = null;
        try {
            URL url = new URL(icon);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setDoInput(true);
            connection.connect();
            InputStream input = connection.getInputStream();
            myBitmap = BitmapFactory.decodeStream(input);

            remoteViews.setImageViewBitmap(R.id.imageicon, myBitmap);

            url = new URL(Banner);
            connection = (HttpURLConnection) url.openConnection();
            connection.setDoInput(true);
            connection.connect();
            input = connection.getInputStream();
            Bitmap myBanner = BitmapFactory.decodeStream(input);
            remoteViews.setImageViewBitmap(R.id.bigimage, myBanner);
        } catch (Exception e) {

        }


        NotificationManagerCompat notificationManager = NotificationManagerCompat.from(mContext);
        NotificationCompat.Builder builder = new NotificationCompat.Builder(mContext, CHANNEL_ID);

        builder.setSmallIcon(R.mipmap.ic_launcher)
                .setPriority(NotificationCompat.PRIORITY_MAX)
                .setLargeIcon( myBitmap )
                .setContentTitle(title)
                .setContentText(msg)
                .setCustomBigContentView(remoteViews)
                .setContentIntent(pendingIntent)
                .setAutoCancel(true);

        int notification_id = 123123;

        notificationManager.notify(notification_id, builder.build());

        Uri notif = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
        Ringtone r = RingtoneManager.getRingtone(mContext, notif);
        r.play();
    }
}
