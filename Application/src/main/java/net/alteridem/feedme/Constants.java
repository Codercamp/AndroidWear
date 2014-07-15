package net.alteridem.feedme;

public final class Constants {
    private Constants() {
    }
    public static final String RESTAURANT_LIST_FILE = "restaurantlist.json";
    public static final String RESTAURANT_TO_LOAD = "restaurant_name";

    public static final String RESTAURANT_FIELD_LIST = "restaurant_list";
    public static final String RESTAURANT_FIELD_NAME = "name";
    public static final String RESTAURANT_FIELD_SUMMARY = "summary";
    public static final String RESTAURANT_FIELD_TITLE = "title";
    public static final String RESTAURANT_FIELD_IMAGE = "img";

    public static final String MENU_ITEMS = "menu_items";
    public static final String MENU_ITEMS_NAME = "name";
    public static final String MENU_ITEMS_PRICE = "price";

    static final String ACTION_START_MENU = "net.alteridem.feedme.START_ORDER";
    public static final String EXTRA_RESTAURANT = "restaurant";

    public static final int NOTIFICATION_ID = 0;
    public static final int NOTIFICATION_IMAGE_WIDTH = 320;
    public static final int NOTIFICATION_IMAGE_HEIGHT = 320;
}
