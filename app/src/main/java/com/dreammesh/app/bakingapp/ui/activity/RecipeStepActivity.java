package com.dreammesh.app.bakingapp.ui.activity;

import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;

import com.dreammesh.app.bakingapp.R;
import com.dreammesh.app.bakingapp.data.model.BakingWrapper;
import com.dreammesh.app.bakingapp.data.model.Step;
import com.dreammesh.app.bakingapp.ui.fragment.RecipeFragment;
import com.dreammesh.app.bakingapp.ui.fragment.RecipeStepFragment;

import butterknife.ButterKnife;

import static com.dreammesh.app.bakingapp.util.CommonUtil.RECIPE_KEY;
import static com.dreammesh.app.bakingapp.util.CommonUtil.RECIPE_STEP_ITEM_KEY;

public class RecipeStepActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recipe_step);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        if(getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }

        ButterKnife.bind(this);

        setUpViews();
    }

    private void setUpViews() {
        Step step = getIntent().getParcelableExtra(RECIPE_STEP_ITEM_KEY);

        RecipeStepFragment recipeFragment = RecipeStepFragment.newInstance(step);
        getSupportFragmentManager().beginTransaction()
                .add(R.id.step_container, recipeFragment)
                .commit();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id  = item.getItemId();

        if(id == android.R.id.home) {
            onBackPressed();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
