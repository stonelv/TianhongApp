package com.tianhong.app.tianhongapp.data.source.local;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by stonelv on 2017/5/2.
 */

public class ProductsDbHelper extends SQLiteOpenHelper {
    public static final int DATABASE_VERSION = 1;

    public static final String DATABASE_NAME = "TianhongApp.db";

    private static final String TEXT_TYPE = " TEXT";

    private static final String INTEGER_TYPE = " INTEGER";

    private static final String SHORT_TYPE = " SHORT";

    private static final String FLOAT_TYPE = " FLOAT";

    private static final String COMMA_SEP = ",";

    private static final String SQL_CREATE_ENTRIES =
            "CREATE TABLE " + ProductsPersistenceContract.ProductEntry.TABLE_NAME + " (" +
                    ProductsPersistenceContract.ProductEntry._ID + TEXT_TYPE + " PRIMARY KEY," +
                    ProductsPersistenceContract.ProductEntry.COLUMN_NAME_ENTRY_ID + INTEGER_TYPE + COMMA_SEP +
                    ProductsPersistenceContract.ProductEntry.COLUMN_NAME_NAME + TEXT_TYPE + COMMA_SEP +
                    ProductsPersistenceContract.ProductEntry.COLUMN_NAME_PRICE + FLOAT_TYPE + COMMA_SEP +
                    ProductsPersistenceContract.ProductEntry.COLUMN_NAME_DESCRIPTION + TEXT_TYPE  + COMMA_SEP +
                    ProductsPersistenceContract.ProductEntry.COLUMN_NAME_CLOSED + SHORT_TYPE +
                    " )";

    public ProductsDbHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    public void onCreate(SQLiteDatabase db) {
        db.execSQL(SQL_CREATE_ENTRIES);
    }

    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // Not required as at version 1
    }

    public void onDowngrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // Not required as at version 1
    }
}
