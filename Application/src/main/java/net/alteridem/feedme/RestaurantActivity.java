package net.alteridem.feedme;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import org.json.JSONObject;

public class RestaurantActivity extends Activity {
    private static final String TAG = "FeedMe";
    private String mRestaurantName;
    private Menu mMenu;
    private ImageView mImageView;
    private TextView mTitleTextView;
    private TextView mSummaryTextView;
    private LinearLayout mMenuItemsLayout;

    @Override
    protected void onStart() {
        super.onStart();
        Intent intent = getIntent();
        mRestaurantName = intent.getStringExtra(Constants.RESTAURANT_TO_LOAD);
        if (Log.isLoggable(TAG, Log.DEBUG)) {
            Log.d(TAG, "Intent: " + intent.toString() + " " + mRestaurantName);
        }
        loadMenu();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.menu);
        mTitleTextView = (TextView) findViewById(R.id.restaurantTextTitle);
        mSummaryTextView = (TextView) findViewById(R.id.restaurantTextSummary);
        mImageView = (ImageView) findViewById(R.id.restaurantImageView);
        mMenuItemsLayout = (LinearLayout) findViewById(R.id.layoutMenuItems);
    }

    @Override
    public boolean onCreateOptionsMenu(android.view.Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(android.view.MenuItem item) {
        switch(item.getItemId()) {
            case R.id.action_order:
                startOrder();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private void loadMenu() {
        if ( mRestaurantName == null )
            return;

        JSONObject jsonObject = AssetUtils.loadJSONAsset(this, mRestaurantName);
        if (jsonObject != null) {
            mMenu = Menu.fromJson(this, jsonObject, mRestaurantName);
            if (mMenu != null) {
                displayMenu(mMenu);
            }
        }
    }

    private void displayMenu(Menu menu) {
        Animation fadeIn = AnimationUtils.loadAnimation(this, android.R.anim.fade_in);
        mTitleTextView.setAnimation(fadeIn);
        mTitleTextView.setText(menu.titleText);
        mSummaryTextView.setText(menu.summaryText);
        if (menu.image != null) {
            mImageView.setAnimation(fadeIn);
            Bitmap recipeImage = AssetUtils.loadBitmapAsset(this, menu.image);
            mImageView.setImageBitmap(recipeImage);
        }
        findViewById(R.id.itemsHeader).setAnimation(fadeIn);

        findViewById(R.id.itemsHeader).setVisibility(View.VISIBLE);

        LayoutInflater inf = LayoutInflater.from(this);
        mMenuItemsLayout.removeAllViews();
        int stepNumber = 1;
        for (Menu.MenuItem step : menu.menuItems) {
            View view = inf.inflate(R.layout.menu_item, null);
            ((TextView) view.findViewById(R.id.itemName)).setText( step.name );
            ((TextView) view.findViewById(R.id.itemPrice)).setText( String.format("$%1.2f", step.price ) );
            mMenuItemsLayout.addView(view);
        }
    }

    private void startOrder() {
        Intent intent = new Intent(this, MenuService.class);
        intent.setAction(Constants.ACTION_START_MENU);
        intent.putExtra(Constants.EXTRA_RESTAURANT, mMenu.toBundle());
        startService(intent);
    }
}
