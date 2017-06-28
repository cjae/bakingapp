package com.dreammesh.app.bakingapp.ui.fragment;

import android.content.Context;
import android.graphics.Typeface;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.support.v4.widget.NestedScrollView;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.style.StyleSpan;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.dreammesh.app.bakingapp.R;
import com.dreammesh.app.bakingapp.data.model.BakingWrapper;
import com.dreammesh.app.bakingapp.data.model.Ingredient;
import com.dreammesh.app.bakingapp.data.model.Step;
import com.dreammesh.app.bakingapp.ui.adapter.RecipeStepAdapter;
import com.dreammesh.app.bakingapp.util.CommonUtil;
import com.dreammesh.app.bakingapp.util.StringUtil;

import java.util.ArrayList;
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

    // Final Strings to store state information about the list of steps and list index
    public static final String STEP_ID_LIST = "stepList";
    public static final String INGREDIENTS_ID_LIST = "ingredientList";
    public static final String LIST_INDEX = "list_index";
    public static final String RECIPE_SCROLL_POSITION = "scroll_position";

    @BindView(R.id.nestedScrollView)
    NestedScrollView nestedScrollView;

    @BindView(R.id.recipe_ingredients)
    TextView recipeIngredientsTv;

    @BindView(R.id.recipe_steps)
    RecyclerView recipeStepsRv;

    private List<Step> stepList;
    private List<Ingredient> ingredientList;
    private int currentStep;

    OnRecipeStepClickListener mCallback;
    private int[] scrollPosition;

    public interface OnRecipeStepClickListener {
        void onRecipeStepSelected(int step_id);
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
        if(savedInstanceState != null) {
            stepList = savedInstanceState.getParcelableArrayList(STEP_ID_LIST);
            ingredientList = savedInstanceState.getParcelableArrayList(INGREDIENTS_ID_LIST);
            currentStep = savedInstanceState.getInt(LIST_INDEX);

            scrollPosition = savedInstanceState.getIntArray(RECIPE_SCROLL_POSITION);
        }

        View view = inflater.inflate(R.layout.fragment_recipe, container, false);
        ButterKnife.bind(this, view);
        return view;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        recipeIngredientsTv.setText(CommonUtil.getStringIngredients(getActivity(), ingredientList));

        doInitRecycler();
    }

    private void doInitRecycler() {
        RecipeStepAdapter adapter = new RecipeStepAdapter(getContext(), stepList, this);
        adapter.setCurrentPos(currentStep);

        LinearLayoutManager layoutManager = new LinearLayoutManager(getContext());
        recipeStepsRv.setLayoutManager(layoutManager);
        recipeStepsRv.setHasFixedSize(true);
        recipeStepsRv.setNestedScrollingEnabled(false);
        recipeStepsRv.setAdapter(adapter);
        recipeStepsRv
                .addItemDecoration(new DividerItemDecoration(getContext(), DividerItemDecoration.VERTICAL));

        if(scrollPosition != null)
            nestedScrollView.post(new Runnable() {
                public void run() {
                    nestedScrollView.scrollTo(scrollPosition[0], scrollPosition[1]);
                }
            });
    }

    @Override
    public void stepClicked(int stepId) {
        currentStep = stepId;
        mCallback.onRecipeStepSelected(stepId);
    }

    @Override
    public void onSaveInstanceState(Bundle currentState) {
        currentState.putParcelableArrayList(STEP_ID_LIST, (ArrayList<Step>) stepList);
        currentState.putParcelableArrayList(INGREDIENTS_ID_LIST, (ArrayList<Ingredient>) ingredientList);
        currentState.putInt(LIST_INDEX, currentStep);

        currentState.putIntArray(RECIPE_SCROLL_POSITION,
                new int[]{ nestedScrollView.getScrollX(), nestedScrollView.getScrollY()});
    }

    public void setCurrentStep(int currentStep) {
        this.currentStep = currentStep;
    }

    public void setStepList(List<Step> stepList) {
        this.stepList = stepList;
    }

    public void setIngredientList(List<Ingredient> ingredientList) {
        this.ingredientList = ingredientList;
    }
}
