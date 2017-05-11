package com.tianhong.app.tianhongapp.data.source.remote;

import android.support.annotation.NonNull;

import com.tianhong.app.tianhongapp.data.Product;
import com.tianhong.app.tianhongapp.data.source.ProductsDataSource;

import java.util.LinkedHashMap;
import java.util.Map;

/**
 * Created by stonelv on 2017/5/2.
 */

public class ProductsRemoteDataSource implements ProductsDataSource {
    private static ProductsRemoteDataSource INSTANCE;

    private static final int SERVICE_LATENCY_IN_MILLIS = 2000;

    private final static Map<String, Product> PRODUCTS_SERVICE_DATA;

    static {
        PRODUCTS_SERVICE_DATA = new LinkedHashMap<>(2);
        addProduct(new Product(1, "aaa", 1.00f, "aaaa", false));
        addProduct(new Product(1, "bbb", 10.00f, "bbbb", false));
        addProduct(new Product(1, "ccc", 50.00f, "cccc", true));
    }

    public static ProductsRemoteDataSource getInstance() {
        if (INSTANCE == null) {
            INSTANCE = new ProductsRemoteDataSource();
        }
        return INSTANCE;
    }

    // Prevent direct instantiation.
    private ProductsRemoteDataSource() {}

    private static void addProduct(@NonNull Product product) {
        Product newProduct = new Product(product.getId(), product.getName(), product.getPrice(),
                product.getDescription(), product.IsClosed());
        PRODUCTS_SERVICE_DATA.put(String.valueOf(newProduct.getId()), newProduct);
    }

    @Override
    public void getProducts(@NonNull LoadProductsCallback callback) {

    }

    @Override
    public void getProduct(@NonNull String taskId, @NonNull GetProductCallback callback) {

    }

    @Override
    public void saveProduct(@NonNull Product product) {

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
