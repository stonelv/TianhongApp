package com.tianhong.app.tianhongapp.products;

import android.content.Context;
import android.support.annotation.Nullable;

import com.tianhong.app.tianhongapp.ProductViewModel;
import com.tianhong.app.tianhongapp.data.source.ProductsRepository;

import java.lang.ref.WeakReference;

/**
 * Created by stonelv on 2017/5/3.
 */

public class ProductItemViewModel extends ProductViewModel {

    // This navigator is s wrapped in a WeakReference to avoid leaks because it has references to an
    // activity. There's no straightforward way to clear it for each item in a list adapter.
    @Nullable
    private WeakReference<ProductItemNavigator> mNavigator;

    public ProductItemViewModel(Context context, ProductsRepository productsRepository) {
        super(context, productsRepository);
    }

    public void setNavigator(ProductItemNavigator navigator) {
        mNavigator = new WeakReference<>(navigator);
    }

    /**
     * Called by the Data Binding library when the row is clicked.
     */
    public void productClicked() {
        int productId = getProductId();
        if (productId == 0) {
            // Click happened before task was loaded, no-op.
            return;
        }
        if (mNavigator != null && mNavigator.get() != null) {
            mNavigator.get().openProductDetails(productId);
        }
    }
}
