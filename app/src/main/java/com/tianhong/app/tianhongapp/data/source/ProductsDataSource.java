package com.tianhong.app.tianhongapp.data.source;

import android.support.annotation.NonNull;

import com.tianhong.app.tianhongapp.data.Product;

import java.util.List;

/**
 * Created by stonelv on 2017/5/2.
 */

public interface ProductsDataSource {
    interface LoadProductsCallback {

        void onProductsLoaded(List<Product> tasks);

        void onDataNotAvailable();
    }

    interface GetProductCallback {

        void onProductLoaded(Product task);

        void onDataNotAvailable();
    }

    void getProducts(@NonNull LoadProductsCallback callback);

    void getProduct(@NonNull String taskId, @NonNull GetProductCallback callback);

    void saveProduct(@NonNull Product task);

    void refreshProducts();

    void deleteAllProducts();

    void deleteProduct(@NonNull String ProductId);
}
