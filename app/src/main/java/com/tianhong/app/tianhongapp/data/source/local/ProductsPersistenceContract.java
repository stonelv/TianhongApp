package com.tianhong.app.tianhongapp.data.source.local;

import android.provider.BaseColumns;

/**
 * Created by stonelv on 2017/5/2.
 */

public class ProductsPersistenceContract {
    private ProductsPersistenceContract() {}

    public static abstract class ProductEntry implements BaseColumns {
        public static final String TABLE_NAME = "product";
        public static final String COLUMN_NAME_ENTRY_ID = "pdtId";
        public static final String COLUMN_NAME_NAME = "name";
        public static final String COLUMN_NAME_PRICE = "price";
        public static final String COLUMN_NAME_DESCRIPTION = "description";
        public static final String COLUMN_NAME_CLOSED = "closed";
    }
}
