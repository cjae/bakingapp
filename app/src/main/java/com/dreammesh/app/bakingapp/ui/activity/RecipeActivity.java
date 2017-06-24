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
import com.dreammesh.app.bakingapp.ui.fragment.RecipeStepFragment;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindBool;
import butterknife.ButterKnife;

import static com.dreammesh.app.bakingapp.util.CommonUtil.RECIPE_KEY;
import static com.dreammesh.app.bakingapp.util.CommonUtil.RECIPE_STEP_ID_KEY;
import static com.dreammesh.app.bakingapp.util.CommonUtil.RECIPE_STEP_LIST_KEY;

public class RecipeActivity extends AppCompatActivity implements
        RecipeFragment.OnRecipeStepClickListener {

    @BindBool(R.bool.is_tablet)
    boolean isTablet;

    private List<Step> stepList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recipe);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }
        ButterKnife.bind(this);

        BakingWrapper recipe = getIntent().getParcelableExtra(RECIPE_KEY);
        stepList = recipe.getSteps();

        if (getSupportActionBar() != null) {
            getSupportActionBar().setTitle(recipe.getName());
        }

        if (isTablet) {
            if (savedInstanceState == null) {
                RecipeFragment recipeFragment = RecipeFragment.newInstance(recipe);
                recipeFragment.setCurrentStep(0);
                recipeFragment.setStepList(stepList);
                recipeFragment.setIngredientList(recipe.getIngredients());
                getSupportFragmentManager().beginTransaction()
                        .add(R.id.fragment_container, recipeFragment)
                        .commit();

                RecipeStepFragment recipeStepFragment = RecipeStepFragment.newInstance();
                recipeStepFragment.setCurrentStep(0);
                recipeStepFragment.setStepList(stepList);
                getSupportFragmentManager().beginTransaction()
                        .add(R.id.step_container, recipeStepFragment)
                        .commit();
            }
        } else {
            if (savedInstanceState == null) {
                RecipeFragment recipeFragment = RecipeFragment.newInstance(recipe);
                recipeFragment.setCurrentStep(0);
                recipeFragment.setStepList(stepList);
                recipeFragment.setIngredientList(recipe.getIngredients());
                getSupportFragmentManager().beginTransaction()
                        .add(R.id.fragment_container, recipeFragment)
                        .commit();
            }
        }
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
    public void onRecipeStepSelected(int step_id) {
        if(isTablet) {
            RecipeStepFragment recipeStepFragment = RecipeStepFragment.newInstance();
            recipeStepFragment.setCurrentStep(step_id);
            recipeStepFragment.setStepList(stepList);
            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.step_container, recipeStepFragment)
                    .commit();
        } else {
            Intent intent = new Intent(this, RecipeStepActivity.class);
            intent.putExtra(RECIPE_STEP_ID_KEY, step_id);
            intent.putParcelableArrayListExtra(RECIPE_STEP_LIST_KEY, (ArrayList<Step>) stepList);
            startActivity(intent);
        }
    }
}
