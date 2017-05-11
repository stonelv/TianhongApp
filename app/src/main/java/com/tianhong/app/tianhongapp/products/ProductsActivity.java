package com.tianhong.app.tianhongapp.products;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;

import com.tianhong.app.tianhongapp.R;
import com.tianhong.app.tianhongapp.ViewModelHolder;
import com.tianhong.app.tianhongapp.data.source.ProductsRepository;
import com.tianhong.app.tianhongapp.data.source.local.ProductsLocalDataSource;
import com.tianhong.app.tianhongapp.data.source.remote.ProductsRemoteDataSource;
import com.tianhong.app.tianhongapp.util.ActivityUtils;

public class ProductsActivity extends AppCompatActivity implements ProductsNavigator, ProductItemNavigator {

    public static final String PRODUCTS_VIEWMODEL_TAG = "PRODUCTS_VIEWMODEL_TAG";

    public ProductsViewModel mViewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_products);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });

        ProductsFragment productsFragment = findOrCreateViewFragment();

        mViewModel = findOrCreateViewModel();
        mViewModel.setNavigator(this);

        // Link View and ViewModel
        productsFragment.setViewModel(mViewModel);
    }

    private ProductsViewModel findOrCreateViewModel() {
        // In a configuration change we might have a ViewModel present. It's retained using the
        // Fragment Manager.
        @SuppressWarnings("unchecked")
        ViewModelHolder<ProductsViewModel> retainedViewModel =
                (ViewModelHolder<ProductsViewModel>) getSupportFragmentManager()
                        .findFragmentByTag(PRODUCTS_VIEWMODEL_TAG);

        if (retainedViewModel != null && retainedViewModel.getViewmodel() != null) {
            // If the model was retained, return it.
            return retainedViewModel.getViewmodel();
        } else {
            // There is no ViewModel yet, create it.
            ProductsViewModel viewModel = new ProductsViewModel(
                    ProductsRepository.getInstance(ProductsRemoteDataSource.getInstance(), ProductsLocalDataSource.getInstance(getApplicationContext())),
                    getApplicationContext());
            // and bind it to this Activity's lifecycle using the Fragment Manager.
            ActivityUtils.addFragmentToActivity(
                    getSupportFragmentManager(),
                    ViewModelHolder.createContainer(viewModel),
                    PRODUCTS_VIEWMODEL_TAG);
            return null;
        }
    }

    @NonNull
    private ProductsFragment findOrCreateViewFragment() {
        ProductsFragment productsFragment =
                (ProductsFragment) getSupportFragmentManager().findFragmentById(R.id.contentFrame);
        if (productsFragment == null) {
            // Create the fragment
            productsFragment = ProductsFragment.newInstance();
            ActivityUtils.addFragmentToActivity(
                    getSupportFragmentManager(), productsFragment, R.id.contentFrame);
        }
        return productsFragment;
    }

    @Override
    public void addNewProduct() {
        //Intent intent = new Intent(this, AddEditTaskActivity.class);
        //startActivityForResult(intent, AddEditTaskActivity.REQUEST_CODE);
    }

    @Override
    public void openProductDetails(int pdtId) {

    }
}
