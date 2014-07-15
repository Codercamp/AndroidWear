package net.alteridem.feedme;

import android.content.Context;
import android.os.Bundle;
import android.os.Parcelable;
import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class Menu {
    private static final String TAG = "FeedMe";

    public String titleText;
    public String summaryText;
    public String image;

    public static class MenuItem {
        MenuItem() { }
        public String name;
        public double price;

        public Bundle toBundle() {
            Bundle bundle = new Bundle();
            bundle.putString(Constants.MENU_ITEMS_NAME, name);
            bundle.putDouble(Constants.MENU_ITEMS_PRICE, price);
            return bundle;
        }

        public static MenuItem fromBundle(Bundle bundle) {
            MenuItem menuItem = new MenuItem();
            menuItem.name = bundle.getString(Constants.MENU_ITEMS_NAME);
            menuItem.price = bundle.getDouble(Constants.MENU_ITEMS_PRICE);
            return menuItem;
        }
    }
    ArrayList<MenuItem> menuItems;

    public Menu() {
        menuItems = new ArrayList<MenuItem>();
    }

    public static Menu fromJson(Context context, JSONObject json) {
        Menu menu = new Menu();
        try {
            menu.titleText = json.getString(Constants.RESTAURANT_FIELD_TITLE);
            menu.summaryText = json.getString(Constants.RESTAURANT_FIELD_SUMMARY);
            if (json.has(Constants.RESTAURANT_FIELD_IMAGE)) {
                menu.image = json.getString(Constants.RESTAURANT_FIELD_IMAGE);
            }

            JSONArray items = json.getJSONArray(Constants.MENU_ITEMS);
            for (int i = 0; i < items.length(); i++) {
                JSONObject item = items.getJSONObject(i);
                MenuItem menuItem = new MenuItem();
                menuItem.name = item.getString(Constants.MENU_ITEMS_NAME);
                menuItem.price = item.getDouble(Constants.MENU_ITEMS_PRICE);
                menu.menuItems.add(menuItem);
            }
        } catch (JSONException e) {
            Log.e(TAG, "Error loading menu: " + e);
            return null;
        }
        return menu;
    }

    public Bundle toBundle() {
        Bundle bundle = new Bundle();
        bundle.putString(Constants.RESTAURANT_FIELD_TITLE, titleText);
        bundle.putString(Constants.RESTAURANT_FIELD_SUMMARY, summaryText);
        bundle.putString(Constants.RESTAURANT_FIELD_IMAGE, image);
        if (menuItems != null) {
            ArrayList<Parcelable> stepBundles = new ArrayList<Parcelable>(menuItems.size());
            for (MenuItem menuItem : menuItems) {
                stepBundles.add(menuItem.toBundle());
            }
            bundle.putParcelableArrayList(Constants.MENU_ITEMS, stepBundles);
        }
        return bundle;
    }

    public static Menu fromBundle(Bundle bundle) {
        Menu menu = new Menu();
        menu.titleText = bundle.getString(Constants.RESTAURANT_FIELD_TITLE);
        menu.summaryText = bundle.getString(Constants.RESTAURANT_FIELD_SUMMARY);
        menu.image = bundle.getString(Constants.RESTAURANT_FIELD_IMAGE);
        ArrayList<Parcelable> stepBundles =
                bundle.getParcelableArrayList(Constants.MENU_ITEMS);
        if (stepBundles != null) {
            for (Parcelable stepBundle : stepBundles) {
                menu.menuItems.add(MenuItem.fromBundle((Bundle) stepBundle));
            }
        }
        return menu;
    }
}
