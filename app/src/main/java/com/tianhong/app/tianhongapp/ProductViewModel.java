package com.tianhong.app.tianhongapp;

import android.content.Context;
import android.databinding.BaseObservable;
import android.databinding.Bindable;
import android.databinding.Observable;
import android.databinding.ObservableField;
import android.databinding.ObservableFloat;
import android.databinding.ObservableInt;
import android.support.annotation.Nullable;

import com.tianhong.app.tianhongapp.data.Product;
import com.tianhong.app.tianhongapp.data.source.ProductsDataSource;
import com.tianhong.app.tianhongapp.data.source.ProductsRepository;

/**
 * Created by stonelv on 2017/5/3.
 */

public class ProductViewModel extends BaseObservable implements ProductsDataSource.GetProductCallback {

    public final ObservableInt id = new ObservableInt();

    public final ObservableField<String> name = new ObservableField<>();

    public final ObservableFloat price = new ObservableFloat();

    public final ObservableField<String> description = new ObservableField<>();

    private final ObservableField<Product> mProductObservable = new ObservableField<>();

    private final ProductsRepository mProductsRepository;

    private final Context mContext;

    private boolean mIsDataLoading;

    public ProductViewModel(Context context, ProductsRepository productsRepository) {
        mContext = context.getApplicationContext(); // Force use of Application Context.
        mProductsRepository = productsRepository;

        // Exposed observables depend on the mTaskObservable observable:
        mProductObservable.addOnPropertyChangedCallback(new OnPropertyChangedCallback() {
            @Override
            public void onPropertyChanged(Observable observable, int i) {
                Product product = mProductObservable.get();
                if (product != null) {
                    id.set(product.getId());
                    name.set(product.getName());
                    price.set(product.getPrice());
                    description.set(product.getDescription());
                } else {
                    name.set(mContext.getString(R.string.no_data));
                    description.set(mContext.getString(R.string.no_data_description));
                }
            }
        });
    }

    public void start(String productId) {
        if (productId != null) {
            mIsDataLoading = true;
            mProductsRepository.getProduct(productId, this);
        }
    }

    public void setProduct(Product product) {
        mProductObservable.set(product);
    }

    // "completed" is two-way bound, so in order to intercept the new value, use a @Bindable
    // annotation and process it in the setter.
    @Bindable
    public boolean getIsClosed() {
        return mProductObservable.get().IsClosed();
    }

    public void setIsClosed(boolean closed) {
        if (mIsDataLoading) {
            return;
        }
        Product product = mProductObservable.get();
        // Update the entity
        product.setClosed(closed);
    }

    @Bindable
    public boolean isDataAvailable() {
        return mProductObservable.get() != null;
    }

    @Bindable
    public boolean isDataLoading() {
        return mIsDataLoading;
    }

    // This could be an observable, but we save a call to Task.getTitleForList() if not needed.
    @Bindable
    public String getTitleForList() {
        if (mProductObservable.get() == null) {
            return "No data";
        }
        return mProductObservable.get().getName();
    }

    @Nullable
    protected int getProductId() {
        return mProductObservable.get().getId();
    }

    @Override
    public void onProductLoaded(Product product) {
        mProductObservable.set(product);
        mIsDataLoading = false;
        notifyChange(); // For the @Bindable properties
    }

    @Override
    public void onDataNotAvailable() {
        mProductObservable.set(null);
        mIsDataLoading = false;
    }
}
