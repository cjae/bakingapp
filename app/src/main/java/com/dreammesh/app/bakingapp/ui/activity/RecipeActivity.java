package com.dreammesh.app.bakingapp.ui.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;

import com.dreammesh.app.bakingapp.R;
import com.dreammesh.app.bakingapp.data.model.BakingWrapper;
import com.dreammesh.app.bakingapp.data.model.Step;
import com.dreammesh.app.bakingapp.ui.fragment.RecipeFragment;

import static com.dreammesh.app.bakingapp.util.CommonUtil.RECIPE_KEY;
import static com.dreammesh.app.bakingapp.util.CommonUtil.RECIPE_STEP_ITEM_KEY;

public class RecipeActivity extends AppCompatActivity implements
        RecipeFragment.OnRecipeStepClickListener {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recipe);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        if(getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }

        setUpViews();
    }

    private void setUpViews() {
        BakingWrapper recipe = getIntent().getParcelableExtra(RECIPE_KEY);
        if(getSupportActionBar() != null) {
            getSupportActionBar().setTitle(recipe.getName());
        }

        RecipeFragment recipeFragment = RecipeFragment.newInstance(recipe);
        getSupportFragmentManager().beginTransaction()
                .add(R.id.fragment_container, recipeFragment)
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

    @Override
    public void onRecipeStepSelected(Step step) {
        Intent intent = new Intent(this, RecipeStepActivity.class);
        intent.putExtra(RECIPE_STEP_ITEM_KEY, step);
        startActivity(intent);
    }
}
