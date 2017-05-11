package com.tianhong.app.tianhongapp.products;

import android.content.Context;
import android.databinding.BaseObservable;
import android.databinding.Bindable;
import android.databinding.ObservableArrayList;
import android.databinding.ObservableBoolean;
import android.databinding.ObservableField;
import android.databinding.ObservableList;
import android.graphics.drawable.Drawable;

import com.tianhong.app.tianhongapp.R;
import com.tianhong.app.tianhongapp.data.Product;
import com.tianhong.app.tianhongapp.data.source.ProductsDataSource;
import com.tianhong.app.tianhongapp.data.source.ProductsRepository;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by stonelv on 2017/4/28.
 */

public class ProductsViewModel extends BaseObservable {

    // These observable fields will update Views automatically
    public final ObservableList<Product> items = new ObservableArrayList<>();
    public final ObservableBoolean dataLoading = new ObservableBoolean(false);

    private final ProductsRepository mProductsRepository;
    private final ObservableBoolean mIsDataLoadingError = new ObservableBoolean(false);
    private Context mContext; // To avoid leaks, this must be an Application Context.
    private ProductsFilterType mCurrentFiltering = ProductsFilterType.AVAILABLE_PRODUCTS;
    public final ObservableField<String> currentFilteringLabel = new ObservableField<>();
    public final ObservableField<String> noProductsLabel = new ObservableField<>();
    public final ObservableField<Drawable> noProductIconRes = new ObservableField<>();
    public final ObservableBoolean productsAddViewVisible = new ObservableBoolean();

    private ProductsNavigator mNavigator;

    public ProductsViewModel(ProductsRepository repository, Context context) {
        mContext = context.getApplicationContext();
        mProductsRepository = repository;

        setFiltering(ProductsFilterType.AVAILABLE_PRODUCTS);
    }

    void setNavigator(ProductsNavigator navigator) {
        mNavigator = navigator;
    }

    public void start() {
        loadProducts(false);
    }

    public void addNewProduct() {
        if (mNavigator != null) {
            mNavigator.addNewProduct();
        }
    }

    @Bindable
    public boolean isEmpty() {
        return items.isEmpty();
    }

    public void loadProducts(boolean forceUpdate) {
        loadProducts(forceUpdate, true);
    }

    private void loadProducts(boolean forceUpdate, final boolean showLoadingUI) {
        if (showLoadingUI) {
            dataLoading.set(true);
        }
        if (forceUpdate) {

            mProductsRepository.refreshProducts();
        }

        // The network request might be handled in a different thread so make sure Espresso knows
        // that the app is busy until the response is handled.
        //EspressoIdlingResource.increment(); // App is busy until further notice

        mProductsRepository.getProducts(new ProductsDataSource.LoadProductsCallback() {
            @Override
            public void onProductsLoaded(List<Product> products) {
                List<Product> productsToShow = new ArrayList<Product>();

                // This callback may be called twice, once for the cache and once for loading
                // the data from the server API, so we check before decrementing, otherwise
                // it throws "Counter has been corrupted!" exception.
                //if (!EspressoIdlingResource.getIdlingResource().isIdleNow()) {
                //    EspressoIdlingResource.decrement(); // Set app as idle.
                //}

                // We filter the tasks based on the requestType
                for (Product product : products) {
                    switch (mCurrentFiltering) {
                        case ALL_PRODUCTS:
                            productsToShow.add(product);
                            break;
                        case AVAILABLE_PRODUCTS:
                            if (!product.IsClosed()) {
                                productsToShow.add(product);
                            }
                            break;
                        case CLOSED_PRODUCTS:
                            if (product.IsClosed()) {
                                productsToShow.add(product);
                            }
                            break;
                        default:
                            productsToShow.add(product);
                            break;
                    }
                }
                if (showLoadingUI) {
                    dataLoading.set(false);
                }
                mIsDataLoadingError.set(false);

                items.clear();
                items.addAll(productsToShow);
                //notifyPropertyChanged(BR); // It's a @Bindable so update manually
            }

            @Override
            public void onDataNotAvailable() {
                mIsDataLoadingError.set(true);
            }
        });
    }

    public void setFiltering(ProductsFilterType requestType) {
        mCurrentFiltering = requestType;

        // Depending on the filter type, set the filtering label, icon drawables, etc.
        switch (requestType) {
            case ALL_PRODUCTS:
                currentFilteringLabel.set(mContext.getString(R.string.label_all));
                noProductsLabel.set(mContext.getResources().getString(R.string.no_products_all));
                noProductIconRes.set(mContext.getResources().getDrawable(
                        R.drawable.ic_assignment_turned_in_24dp));
                productsAddViewVisible.set(true);
                break;
            case AVAILABLE_PRODUCTS:
                currentFilteringLabel.set(mContext.getString(R.string.label_available));
                noProductsLabel.set(mContext.getResources().getString(R.string.no_products_available));
                noProductIconRes.set(mContext.getResources().getDrawable(
                        R.drawable.ic_check_circle_24dp));
                productsAddViewVisible.set(false);
                break;
            case CLOSED_PRODUCTS:
                currentFilteringLabel.set(mContext.getString(R.string.label_closed));
                noProductsLabel.set(mContext.getResources().getString(R.string.no_products_closed));
                noProductIconRes.set(mContext.getResources().getDrawable(
                        R.drawable.ic_verified_user_24dp));
                productsAddViewVisible.set(false);
                break;
        }
    }
}
