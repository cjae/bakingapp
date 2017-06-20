package com.dreammesh.app.bakingapp.ui.fragment;

import android.content.Context;
import android.graphics.Typeface;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.style.StyleSpan;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.dreammesh.app.bakingapp.R;
import com.dreammesh.app.bakingapp.data.model.BakingWrapper;
import com.dreammesh.app.bakingapp.data.model.Ingredient;
import com.dreammesh.app.bakingapp.data.model.Step;
import com.dreammesh.app.bakingapp.ui.adapter.RecipeStepAdapter;
import com.dreammesh.app.bakingapp.util.StringUtil;

import java.util.List;

import butterknife.BindString;
import butterknife.BindView;
import butterknife.ButterKnife;

import static com.dreammesh.app.bakingapp.util.CommonUtil.RECIPE_FRAGMENT_KEY;
import static com.dreammesh.app.bakingapp.util.CommonUtil.RECIPE_KEY;

/**
 * A placeholder fragment containing a simple view.
 */
public class RecipeFragment extends Fragment implements
        RecipeStepAdapter.OnStepClickListener {

    @BindView(R.id.recipe_ingredients)
    TextView recipeIngredientsTv;

    @BindView(R.id.recipe_steps)
    RecyclerView recipeStepsRv;

    BakingWrapper recipe;

    OnRecipeStepClickListener mCallback;

    public interface OnRecipeStepClickListener {
        void onRecipeStepSelected(Step step);
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);

        try {
            mCallback = (OnRecipeStepClickListener) context;
        } catch(ClassCastException e) {
            throw new ClassCastException(context.toString()
                    + " must implement OnRecipeStepClickListener");
        }
    }

    public RecipeFragment() {}

    public static RecipeFragment newInstance(BakingWrapper recipe) {
        RecipeFragment recipeFragment = new RecipeFragment();
        Bundle bundle = new Bundle();
        bundle.putParcelable(RECIPE_FRAGMENT_KEY, recipe);
        recipeFragment.setArguments(bundle);

        return recipeFragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_recipe, container, false);
        ButterKnife.bind(this, view);
        return view;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        recipe = getArguments().getParcelable(RECIPE_FRAGMENT_KEY);

        StringBuilder sb = new StringBuilder();

        if (recipe != null) {
            for (Ingredient ingredient : recipe.getIngredients()) {

                String name = ingredient.getIngredient();
                float quantity = ingredient.getQuantity();
                String measure = ingredient.getMeasure();

                sb.append("\n");
                sb.append(StringUtil.prepareIngredient(getContext(), name, quantity, measure));
            }
        }

        recipeIngredientsTv.setText(sb.toString().trim());

        if (recipe != null) {
            doInitRecycler(recipe.getSteps());
        }
    }

    private void doInitRecycler(List<Step> steps) {
        RecipeStepAdapter adapter = new RecipeStepAdapter(getContext(), steps, this);

        LinearLayoutManager layoutManager = new LinearLayoutManager(getContext());
        recipeStepsRv.setLayoutManager(layoutManager);
        recipeStepsRv.setHasFixedSize(true);
        recipeStepsRv.setNestedScrollingEnabled(false);
        recipeStepsRv.setAdapter(adapter);
        recipeStepsRv
                .addItemDecoration(new DividerItemDecoration(getContext(), DividerItemDecoration.VERTICAL));
    }

    @Override
    public void stepClicked(int stepId) {
        mCallback.onRecipeStepSelected(recipe.getSteps().get(stepId));
    }
}
