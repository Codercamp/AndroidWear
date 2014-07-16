package net.alteridem.feedme;

import android.app.Notification;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Binder;
import android.os.IBinder;
import android.support.v4.app.NotificationManagerCompat;
import android.support.v4.app.NotificationCompat;

import java.util.ArrayList;

public class MenuService extends Service {
    private NotificationManagerCompat mNotificationManager;
    private Binder mBinder = new LocalBinder();

    public class LocalBinder extends Binder {
        MenuService getService() {
            return MenuService.this;
        }
    }

    @Override
    public void onCreate() {
        mNotificationManager = NotificationManagerCompat.from(this);
    }

    @Override
    public IBinder onBind(Intent intent) {
        return mBinder;
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        if (intent != null && intent.getAction().equals(Constants.ACTION_START_MENU)) {
            createNotification(intent);
            return START_STICKY;
        }
        return START_NOT_STICKY;
    }

    private void createNotification(Intent intent) {
        Menu menu = Menu.fromBundle(intent.getBundleExtra(Constants.EXTRA_RESTAURANT));

        // Create an intent to launch back to the menu on the phone
        Intent viewIntent = new Intent(this, RestaurantActivity.class);
        viewIntent.putExtra(Constants.RESTAURANT_TO_LOAD, menu.json);  // This is the restaurant name
        PendingIntent viewPendingIntent = PendingIntent.getActivity(this, 0, viewIntent, 0);

        // Add a button to purchase a beer
        Intent beerIntent = new Intent(this, BeerActivity.class);
        PendingIntent beerPendingIntent = PendingIntent.getActivity(this, 0, beerIntent, 0);

        // Create the notification
        NotificationCompat.Builder builder = new NotificationCompat.Builder(this)
            .setContentTitle(menu.titleText)
            .setContentText(getString(R.string.notification_order))
            .setSmallIcon(R.mipmap.ic_notification)
            .setContentIntent(viewPendingIntent)
            .addAction(R.mipmap.ic_notification, getString(R.string.order_beer), beerPendingIntent);

        // If we have a restaurant image, add it as the background image to the notification
        if (menu.image != null) {
            Bitmap image = Bitmap.createScaledBitmap(
                    AssetUtils.loadBitmapAsset(this, menu.image),
                    Constants.NOTIFICATION_IMAGE_WIDTH, Constants.NOTIFICATION_IMAGE_HEIGHT, false);
            builder.setLargeIcon(image);
        }

        // Send the notification
        Notification notification = builder.build();
        NotificationManagerCompat notificationManager = NotificationManagerCompat.from(this);
        notificationManager.notify(Constants.NOTIFICATION_ID, notification);
    }
}
