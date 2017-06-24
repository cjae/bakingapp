package com.dreammesh.app.bakingapp.ui.activity;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;

import com.dreammesh.app.bakingapp.R;
import com.dreammesh.app.bakingapp.data.model.Step;
import com.dreammesh.app.bakingapp.ui.fragment.RecipeStepFragment;

import java.util.List;

import butterknife.ButterKnife;

import static com.dreammesh.app.bakingapp.util.CommonUtil.RECIPE_STEP_ID_KEY;
import static com.dreammesh.app.bakingapp.util.CommonUtil.RECIPE_STEP_LIST_KEY;

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

        if(savedInstanceState == null) {
            List<Step> stepList = getIntent().getParcelableArrayListExtra(RECIPE_STEP_LIST_KEY);
            int currentStep = getIntent().getIntExtra(RECIPE_STEP_ID_KEY, 0);

            RecipeStepFragment recipeFragment = RecipeStepFragment.newInstance();
            recipeFragment.setCurrentStep(currentStep);
            recipeFragment.setStepList(stepList);
            getSupportFragmentManager().beginTransaction()
                    .add(R.id.step_container, recipeFragment)
                    .commit();
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
}
