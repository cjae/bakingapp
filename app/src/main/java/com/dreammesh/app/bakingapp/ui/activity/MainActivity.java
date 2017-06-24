package com.dreammesh.app.bakingapp.ui.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.annotation.VisibleForTesting;
import android.support.test.espresso.IdlingResource;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.dreammesh.app.bakingapp.IdlingResource.SimpleIdlingResource;
import com.dreammesh.app.bakingapp.R;
import com.dreammesh.app.bakingapp.data.model.BakingWrapper;
import com.dreammesh.app.bakingapp.data.rest.APIClient;
import com.dreammesh.app.bakingapp.data.rest.ServiceGenerator;
import com.dreammesh.app.bakingapp.ui.adapter.BakingRecyclerAdapter;
import com.dreammesh.app.bakingapp.ui.custom.RecyclerInsetsDecoration;
import com.dreammesh.app.bakingapp.util.CommonUtil;
import com.dreammesh.app.bakingapp.util.PrefManager;
import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindInt;
import butterknife.BindView;
import butterknife.ButterKnife;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.dreammesh.app.bakingapp.util.CommonUtil.RECIPE_KEY;

public class MainActivity extends AppCompatActivity implements
        BakingRecyclerAdapter.OnItemClickListener{

    public static String BAKING_LIST_KEY = "baking_list";

    @BindView(R.id.baking_pb)
    ProgressBar bakingPb;

    @BindView(R.id.list_baking_empty)
    TextView listBakingEmpty;

    @BindView(R.id.baking_rv)
    RecyclerView bakingRv;

    @BindInt(R.integer.grid_column_count)
    int columnGrid;

    List<BakingWrapper> data;

    BakingRecyclerAdapter mAdapter;

    // The Idling Resource which will be null in production.
    @Nullable
    private SimpleIdlingResource mIdlingResource;

    /**
     * Only called from test, creates and returns a new {@link SimpleIdlingResource}.
     */
    @VisibleForTesting
    @NonNull
    public IdlingResource getIdlingResource() {
        if (mIdlingResource == null) {
            mIdlingResource = new SimpleIdlingResource();
        }
        return mIdlingResource;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        ButterKnife.bind(this);

        // Get the IdlingResource instance
        getIdlingResource();

        initViews();

        if(savedInstanceState != null) {
            data = savedInstanceState.getParcelableArrayList(BAKING_LIST_KEY);
            showDataView();
            doDataDisplay(data);
        } else {
            if (CommonUtil.isNetworkAvailable(this)) {
                doGetRecipe();
            } else {
                listBakingEmpty.setText(getString(R.string.empty_baking_list_no_network));
                showErrorEmptyView();
            }
        }
    }

    private void initViews() {
        initLayoutManager();
        bakingRv.setHasFixedSize(true);
        bakingRv.setItemAnimator(new DefaultItemAnimator());

        data = new ArrayList<>();
        mAdapter = new BakingRecyclerAdapter(this, data, this);
        bakingRv.setAdapter(mAdapter);
    }

    private void initLayoutManager() {
        if(getResources().getBoolean(R.bool.is_tablet)){
            if(getResources().getBoolean(R.bool.is_landscape)) {
                GridLayoutManager mLayoutManager = new GridLayoutManager(this, columnGrid);
                bakingRv.setLayoutManager(mLayoutManager);
                bakingRv.addItemDecoration(new RecyclerInsetsDecoration(this));
            } else {
                GridLayoutManager mLayoutManager = new GridLayoutManager(this, columnGrid);
                bakingRv.setLayoutManager(mLayoutManager);
                bakingRv.addItemDecoration(new RecyclerInsetsDecoration(this));
            }
        } else {
            if(getResources().getBoolean(R.bool.is_landscape)) {
                GridLayoutManager mLayoutManager = new GridLayoutManager(this, columnGrid);
                bakingRv.setLayoutManager(mLayoutManager);
                bakingRv.addItemDecoration(new RecyclerInsetsDecoration(this));
            } else {
                LinearLayoutManager mLayoutManager = new LinearLayoutManager(this);
                bakingRv.setLayoutManager(mLayoutManager);
            }
        }
    }

    private void doGetRecipe() {
        showProgress();

        if (mIdlingResource != null) {
            mIdlingResource.setIdleState(false);
        }

        ServiceGenerator service = APIClient.createService(ServiceGenerator.class);
        Call<List<BakingWrapper>> call = service.getBakingData();
        call.enqueue(new Callback<List<BakingWrapper>>() {
            @Override
            public void onResponse(Call<List<BakingWrapper>> call, Response<List<BakingWrapper>> response) {
                showDataView();
                data = response.body();
                doDataDisplay(data);

                if (mIdlingResource != null) {
                    mIdlingResource.setIdleState(true);
                }
            }

            @Override
            public void onFailure(Call<List<BakingWrapper>> call, Throwable t) {
                showErrorEmptyView();
            }
        });
    }

    private void doDataDisplay(List<BakingWrapper> data) {
        if(data == null || data.isEmpty()) {
            showErrorEmptyView();
        } else {
            mAdapter.swapData(data);
        }
    }

    private void showProgress() {
        bakingPb.setVisibility(View.VISIBLE);
        bakingRv.setVisibility(View.INVISIBLE);
        listBakingEmpty.setVisibility(View.INVISIBLE);
    }

    private void showDataView() {
        bakingPb.setVisibility(View.INVISIBLE);
        bakingRv.setVisibility(View.VISIBLE);
        listBakingEmpty.setVisibility(View.INVISIBLE);
    }

    private void showErrorEmptyView() {
        bakingPb.setVisibility(View.INVISIBLE);
        bakingRv.setVisibility(View.INVISIBLE);
        listBakingEmpty.setVisibility(View.VISIBLE);
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        outState.putParcelableArrayList(BAKING_LIST_KEY, (ArrayList<BakingWrapper>) data);
    }

    @Override
    public void onItemClicked(int position) {
        Intent intent = new Intent(this, RecipeActivity.class);

        BakingWrapper bakingWrapper = data.get(position);
        doAddRecipeToDb(bakingWrapper);
        intent.putExtra(RECIPE_KEY, bakingWrapper);

        startActivity(intent);
    }

    private void doAddRecipeToDb(BakingWrapper bakingWrapper) {
        String json = new Gson().toJson(bakingWrapper);
        PrefManager.setSavedRecipe(this, json);
    }
}