package net.alteridem.feedme;

import android.app.Notification;
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
    private Menu mMenu;

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
        if (intent.getAction().equals(Constants.ACTION_START_MENU)) {
            createNotification(intent);
            return START_STICKY;
        }
        return START_NOT_STICKY;
    }

    private void createNotification(Intent intent) {
        mMenu = Menu.fromBundle(intent.getBundleExtra(Constants.EXTRA_RESTAURANT));
        ArrayList<Notification> notificationPages = new ArrayList<Notification>();

        int stepCount = mMenu.menuItems.size();

        // TODO: Replace steps with menu items
        for (int i = 0; i < stepCount; ++i) {
            Menu.MenuItem menuItem = mMenu.menuItems.get(i);
            NotificationCompat.BigTextStyle style = new NotificationCompat.BigTextStyle();
            style.bigText(menuItem.name);
            style.setBigContentTitle(String.format(
                    getResources().getString(R.string.item_count), i + 1, stepCount));
            style.setSummaryText("");
            NotificationCompat.Builder builder = new NotificationCompat.Builder(this);
            builder.setStyle(style);
            notificationPages.add(builder.build());
        }

        NotificationCompat.Builder builder = new NotificationCompat.Builder(this);

        if (mMenu.image != null) {
            Bitmap image = Bitmap.createScaledBitmap(
                    AssetUtils.loadBitmapAsset(this, mMenu.image),
                    Constants.NOTIFICATION_IMAGE_WIDTH, Constants.NOTIFICATION_IMAGE_HEIGHT, false);
            builder.setLargeIcon(image);
        }
        builder.setContentTitle(mMenu.titleText);
        builder.setContentText(mMenu.summaryText);
        builder.setSmallIcon(R.mipmap.ic_notification_recipe);

        Notification notification = builder
                .extend(new NotificationCompat.WearableExtender()
                        .addPages(notificationPages))
                .build();
        mNotificationManager.notify(Constants.NOTIFICATION_ID, notification);
    }
}
