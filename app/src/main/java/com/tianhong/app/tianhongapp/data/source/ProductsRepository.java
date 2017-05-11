package com.tianhong.app.tianhongapp.data.source;

import android.support.annotation.NonNull;

import com.tianhong.app.tianhongapp.data.Product;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import static com.google.common.base.Preconditions.checkNotNull;

/**
 * Created by stonelv on 2017/5/2.
 */

public class ProductsRepository implements ProductsDataSource
{
    private static ProductsRepository INSTANCE = null;

    private final ProductsDataSource mProductsRemoteDataSource;

    private final ProductsDataSource mProductsLocalDataSource;

    Map<String, Product> mCachedProducts;

    /**
     * Marks the cache as invalid, to force an update the next time data is requested. This variable
     * has package local visibility so it can be accessed from tests.
     */
    boolean mCacheIsDirty = false;

    // Prevent direct instantiation.
    private ProductsRepository(@NonNull ProductsDataSource productsRemoteDataSource,
                            @NonNull ProductsDataSource productsLocalDataSource) {
        mProductsRemoteDataSource = checkNotNull(productsRemoteDataSource);
        mProductsLocalDataSource = checkNotNull(productsLocalDataSource);
    }

    public static ProductsRepository getInstance(ProductsDataSource productsRemoteDataSource,
                                                 ProductsDataSource productsLocalDataSource) {
        if (INSTANCE == null) {
            INSTANCE = new ProductsRepository(productsRemoteDataSource, productsLocalDataSource);
        }
        return INSTANCE;
    }

    public static void destroyInstance() {
        INSTANCE = null;
    }

    @Override
    public void getProducts(@NonNull final LoadProductsCallback callback) {
        checkNotNull(callback);

        // Respond immediately with cache if available and not dirty
        if (mCachedProducts != null && !mCacheIsDirty) {
            callback.onProductsLoaded(new ArrayList<>(mCachedProducts.values()));
            return;
        }

        if (mCacheIsDirty) {
            // If the cache is dirty we need to fetch new data from the network.
            getTasksFromRemoteDataSource(callback);
        } else {
            // Query the local storage if available. If not, query the network.
            mProductsLocalDataSource.getProducts(new LoadProductsCallback() {
                @Override
                public void onProductsLoaded(List<Product> products) {
                    refreshCache(products);
                    callback.onProductsLoaded(new ArrayList<>(mCachedProducts.values()));
                }

                @Override
                public void onDataNotAvailable() {

                }
            });
        }
    }

    private void getTasksFromRemoteDataSource(@NonNull final LoadProductsCallback callback) {
        mProductsRemoteDataSource.getProducts(new LoadProductsCallback() {
            @Override
            public void onProductsLoaded(List<Product> products) {
                refreshCache(products);
                refreshLocalDataSource(products);
                callback.onProductsLoaded(new ArrayList<>(mCachedProducts.values()));
            }

            @Override
            public void onDataNotAvailable() {
                callback.onDataNotAvailable();
            }
        });
    }

    private void refreshCache(List<Product> products) {
        if (mCachedProducts == null) {
            mCachedProducts = new LinkedHashMap<>();
        }
        mCachedProducts.clear();
        for (Product product : products) {
            mCachedProducts.put(String.valueOf(product.getId()), product);
        }
        mCacheIsDirty = false;
    }

    private void refreshLocalDataSource(List<Product> products) {
        mProductsLocalDataSource.deleteAllProducts();
        for (Product product : products) {
            mProductsLocalDataSource.saveProduct(product);
        }
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
