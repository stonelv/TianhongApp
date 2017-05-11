package com.tianhong.app.tianhongapp.products;

import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.PopupMenu;

import com.tianhong.app.tianhongapp.R;
import com.tianhong.app.tianhongapp.data.Product;
import com.tianhong.app.tianhongapp.data.source.ProductsRepository;
import com.tianhong.app.tianhongapp.data.source.local.ProductsLocalDataSource;
import com.tianhong.app.tianhongapp.data.source.remote.ProductsRemoteDataSource;
import com.tianhong.app.tianhongapp.databinding.ProductItemBinding;
import com.tianhong.app.tianhongapp.databinding.ProductsListBinding;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by stonelv on 2017/5/3.
 */

public class ProductsFragment extends Fragment {

    private ProductsViewModel mViewModel;
    private ProductsListBinding mListBinding;
    private ProductsAdapter mListAdapter;

    public ProductsFragment() {

    }

    public static ProductsFragment newInstance() {
        return new ProductsFragment();
    }

    public void setViewModel(ProductsViewModel viewModel) {
        mViewModel = viewModel;
    }

    @Override
    public void onResume() {
        super.onResume();
        mViewModel.start();
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        mListBinding = ProductsListBinding.inflate(inflater, container, false);

        mListBinding.setView(this);

        mListBinding.setViewmodel(mViewModel);

        setHasOptionsMenu(true);

        View root = mListBinding.getRoot();

        return root;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.menu_clear:
                //mViewModel.
                break;
            case R.id.menu_filter:
                showFilteringPopUpMenu();
                break;
            case R.id.menu_refresh:
                mViewModel.loadProducts(true);
                break;
        }
        return true;
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.products_list_menu, menu);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        //setupSnackbar();

        //setupFab();

        setupListAdapter();

        //setupRefreshLayout();
    }

    @Override
    public void onDestroy() {
        mListAdapter.onDestroy();
        super.onDestroy();
    }

    private void showFilteringPopUpMenu() {
        PopupMenu popup = new PopupMenu(getContext(), getActivity().findViewById(R.id.menu_filter));
        popup.getMenuInflater().inflate(R.menu.filter_products, popup.getMenu());

        popup.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
            public boolean onMenuItemClick(MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.active:
                        mViewModel.setFiltering(ProductsFilterType.AVAILABLE_PRODUCTS);
                        break;
                    case R.id.closed:
                        mViewModel.setFiltering(ProductsFilterType.CLOSED_PRODUCTS);
                        break;
                    default:
                        mViewModel.setFiltering(ProductsFilterType.ALL_PRODUCTS);
                        break;
                }
                mViewModel.loadProducts(false);
                return true;
            }
        });

        popup.show();
    }

    /*private void setupSnackbar() {
        mTasksViewModel.snackbarText.addOnPropertyChangedCallback(
                new Observable.OnPropertyChangedCallback() {
                    @Override
                    public void onPropertyChanged(Observable observable, int i) {
                        SnackbarUtils.showSnackbar(getView(), mTasksViewModel.getSnackbarText());
                    }
                });
    }

    private void setupFab() {
        FloatingActionButton fab =
                (FloatingActionButton) getActivity().findViewById(R.id.fab_add_task);

        fab.setImageResource(R.drawable.ic_add);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mTasksViewModel.addNewTask();
            }
        });
    }*/

    private void setupListAdapter() {
        ListView listView =  mListBinding.productsList;

        mListAdapter = new ProductsAdapter(
                new ArrayList<Product>(0),
                (ProductsActivity) getActivity(),
                ProductsRepository.getInstance(ProductsRemoteDataSource.getInstance(), ProductsLocalDataSource.getInstance(getContext().getApplicationContext())),
                mViewModel);
        listView.setAdapter(mListAdapter);
    }

    /*private void setupRefreshLayout() {
        ListView listView =  mTasksFragBinding.tasksList;
        final ScrollChildSwipeRefreshLayout swipeRefreshLayout = mTasksFragBinding.refreshLayout;
        swipeRefreshLayout.setColorSchemeColors(
                ContextCompat.getColor(getActivity(), R.color.colorPrimary),
                ContextCompat.getColor(getActivity(), R.color.colorAccent),
                ContextCompat.getColor(getActivity(), R.color.colorPrimaryDark)
        );
        // Set the scrolling view in the custom SwipeRefreshLayout.
        swipeRefreshLayout.setScrollUpChild(listView);
    }*/

    public static class ProductsAdapter extends BaseAdapter {

        @Nullable
        private ProductItemNavigator mProductItemNavigator;

        private ProductsViewModel mProductsViewModel;

        private List<Product> mProducts;

        private ProductsRepository mProductsRepository;

        public ProductsAdapter(List<Product> products, ProductsActivity productItemNavigator,
                            ProductsRepository productsRepository,
                            ProductsViewModel productsViewModel) {
            mProductItemNavigator = productItemNavigator;
            mProductsRepository = productsRepository;
            mProductsViewModel = productsViewModel;
            setList(products);

        }

        public void onDestroy() {
            mProductItemNavigator = null;
        }

        public void replaceData(List<Product> products) {
            setList(products);
        }

        @Override
        public int getCount() {
            return mProducts != null ? mProducts.size() : 0;
        }

        @Override
        public Product getItem(int i) {
            return mProducts.get(i);
        }

        @Override
        public long getItemId(int i) {
            return i;
        }

        @Override
        public View getView(int i, View view, ViewGroup viewGroup) {
            Product Product = getItem(i);
            ProductItemBinding binding;
            if (view == null) {
                // Inflate
                LayoutInflater inflater = LayoutInflater.from(viewGroup.getContext());

                // Create the binding
                binding = ProductItemBinding.inflate(inflater, viewGroup, false);
            } else {
                // Recycling view
                binding = DataBindingUtil.getBinding(view);
            }

            final ProductItemViewModel viewmodel = new ProductItemViewModel(
                    viewGroup.getContext().getApplicationContext(),
                    mProductsRepository
            );

            viewmodel.setNavigator(mProductItemNavigator);

            binding.setViewmodel(viewmodel);
            // To save on PropertyChangedCallbacks, wire the item's snackbar text observable to the
            // fragment's.
            /*viewmodel.snackbarText.addOnPropertyChangedCallback(
                    new Observable.OnPropertyChangedCallback() {
                        @Override
                        public void onPropertyChanged(Observable observable, int i) {
                            mProductsViewModel.snackbarText.set(viewmodel.getSnackbarText());
                        }
                    });*/
            viewmodel.setProduct(Product);

            return binding.getRoot();
        }


        private void setList(List<Product> Products) {
            mProducts = Products;
            notifyDataSetChanged();
        }
    }
}
