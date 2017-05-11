package com.tianhong.app.tianhongapp.data.source.local;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.support.annotation.NonNull;

import com.tianhong.app.tianhongapp.data.Product;
import com.tianhong.app.tianhongapp.data.source.ProductsDataSource;
import com.tianhong.app.tianhongapp.data.source.local.ProductsPersistenceContract.ProductEntry;

import java.util.ArrayList;
import java.util.List;

import static com.google.common.base.Preconditions.checkNotNull;

/**
 * Created by stonelv on 2017/5/2.
 */

public class ProductsLocalDataSource implements ProductsDataSource {
    private static ProductsLocalDataSource INSTANCE;

    private ProductsDbHelper mDbHelper;

    // Prevent direct instantiation.
    private ProductsLocalDataSource(@NonNull Context context) {
        checkNotNull(context);
        mDbHelper = new ProductsDbHelper(context);
    }

    public static ProductsLocalDataSource getInstance(@NonNull Context context) {
        if (INSTANCE == null) {
            INSTANCE = new ProductsLocalDataSource(context);
        }
        return INSTANCE;
    }

    @Override
    public void getProducts(@NonNull LoadProductsCallback callback) {
        List<Product> products = new ArrayList<>();
        SQLiteDatabase db = mDbHelper.getReadableDatabase();
        String[] projection = {
                ProductEntry.COLUMN_NAME_ENTRY_ID,
                ProductEntry.COLUMN_NAME_NAME,
                ProductEntry.COLUMN_NAME_PRICE,
                ProductEntry.COLUMN_NAME_DESCRIPTION
        };

        Cursor c = db.query(
                ProductEntry.TABLE_NAME, projection, null, null, null, null, null);

        if (c != null && c.getCount() > 0) {
            while (c.moveToNext()) {
                int pdtId = c.getInt(c.getColumnIndexOrThrow(ProductEntry.COLUMN_NAME_ENTRY_ID));
                String pdtName = c.getString(c.getColumnIndexOrThrow(ProductEntry.COLUMN_NAME_NAME));
                Float pdtPrice = c.getFloat(c.getColumnIndexOrThrow(ProductEntry.COLUMN_NAME_PRICE));
                String pdtDesc = c.getString(c.getColumnIndexOrThrow(ProductEntry.COLUMN_NAME_DESCRIPTION));
                short closed = c.getShort(c.getColumnIndexOrThrow(ProductEntry.COLUMN_NAME_CLOSED));
                Product pdtInfo = new Product(pdtId, pdtName, pdtPrice, pdtDesc, closed == 0 ? false : true);
                products.add(pdtInfo);
            }
        }
        if (c != null)
            c.close();

        db.close();

        if (products.isEmpty())
            callback.onDataNotAvailable();
        else
            callback.onProductsLoaded(products);
    }

    @Override
    public void getProduct(@NonNull String taskId, @NonNull GetProductCallback callback) {

    }

    @Override
    public void saveProduct(@NonNull Product task) {

    }

    @Override
    public void refreshProducts() {

    }

    @Override
    public void deleteAllProducts() {

    }

    @Override
    public void deleteProduct(@NonNull String ProductId) {

    }
}
