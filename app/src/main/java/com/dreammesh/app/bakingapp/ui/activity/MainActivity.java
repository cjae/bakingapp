package com.dreammesh.app.bakingapp.ui.activity;


import android.content.Intent;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.dreammesh.app.bakingapp.R;
import com.dreammesh.app.bakingapp.data.model.BakingWrapper;
import com.dreammesh.app.bakingapp.data.rest.APIClient;
import com.dreammesh.app.bakingapp.data.rest.ServiceGenerator;
import com.dreammesh.app.bakingapp.ui.adapter.BakingRecyclerAdapter;
import com.dreammesh.app.bakingapp.ui.custom.RecyclerInsetsDecoration;
import com.dreammesh.app.bakingapp.util.CommonUtil;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.dreammesh.app.bakingapp.util.CommonUtil.RECIPE_KEY;

public class MainActivity extends AppCompatActivity implements
        BakingRecyclerAdapter.OnItemClickListener{

    @BindView(R.id.baking_pb)
    ProgressBar bakingPb;

    @BindView(R.id.list_baking_empty)
    TextView listBakingEmpty;

    @BindView(R.id.baking_rv)
    RecyclerView bakingRv;

    List<BakingWrapper> data;

    BakingRecyclerAdapter mAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        ButterKnife.bind(this);

        initViews();

        if(savedInstanceState != null) {
            data = savedInstanceState.getParcelableArrayList("baking_list");
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
                GridLayoutManager mLayoutManager = new GridLayoutManager(this, 3);
                bakingRv.setLayoutManager(mLayoutManager);
                bakingRv.addItemDecoration(new RecyclerInsetsDecoration(this));
            } else {
                GridLayoutManager mLayoutManager = new GridLayoutManager(this, 2);
                bakingRv.setLayoutManager(mLayoutManager);
                bakingRv.addItemDecoration(new RecyclerInsetsDecoration(this));
            }
        } else {
            if(getResources().getBoolean(R.bool.is_landscape)) {
                GridLayoutManager mLayoutManager = new GridLayoutManager(this, 2);
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

        ServiceGenerator service = APIClient.createService(ServiceGenerator.class);
        Call<List<BakingWrapper>> call = service.getBakingData();
        call.enqueue(new Callback<List<BakingWrapper>>() {
            @Override
            public void onResponse(Call<List<BakingWrapper>> call, Response<List<BakingWrapper>> response) {
                showDataView();
                data = response.body();
                doDataDisplay(data);
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
        outState.putParcelableArrayList("baking_list", (ArrayList<BakingWrapper>) data);
    }

    @Override
    public void onItemClicked(int position) {
        Intent intent = new Intent(this, RecipeActivity.class);

        BakingWrapper bakingWrapper = data.get(position);
        intent.putExtra(RECIPE_KEY, bakingWrapper);

        startActivity(intent);
    }
}