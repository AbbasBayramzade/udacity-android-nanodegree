package com.ma.traveldroid.data;

import android.content.ContentResolver;
import android.net.Uri;
import android.provider.BaseColumns;

public class CountryContract {

    public static final String CONTENT_AUTHORITY = "com.ma.traveldroid";

    // Content Uri without data type. Parse string into URI
    public static final Uri BASE_CONTENT_URI = Uri.parse("content://" + CONTENT_AUTHORITY);

    public static final String PATH_COUNTRIES = "countries";

    private CountryContract(){}

    public static final class CountryEntry implements BaseColumns{
        public static final Uri CONTENT_URI = Uri.withAppendedPath(BASE_CONTENT_URI, PATH_COUNTRIES);

        public static final String TABLE_NAME_COUNTRIES = "countries";

        public static final String _ID = BaseColumns._ID;

        public static final String COLUMN_COUNTRY_NAME = "name";

        public static final String COLUMN_VISITED_PERIOD = "visited_period";

        public static final String COLUMN_MAP_CONTEXT = "map_content";

        public static final String CONTENT_LIST_TYPE =
                ContentResolver.CURSOR_DIR_BASE_TYPE + "/" + CONTENT_AUTHORITY + "/" + PATH_COUNTRIES;


        public static final String CONTENT_COUNTRY_ITEM_TYPE =
                ContentResolver.CURSOR_ITEM_BASE_TYPE + "/" + CONTENT_AUTHORITY + "/" + PATH_COUNTRIES;
    }
}
