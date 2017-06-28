package com.dreammesh.app.bakingapp.ui.adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.dreammesh.app.bakingapp.R;
import com.dreammesh.app.bakingapp.data.model.Step;

import java.util.List;

import butterknife.BindColor;
import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by Jedidiah on 18/06/2017.
 */

public class RecipeStepAdapter extends RecyclerView.Adapter<RecipeStepAdapter.RecipeStepViewHolder> {

    private Context mContext;
    private List<Step> data;
    private OnStepClickListener recipeClickListener;

    private int currentPos;

    public interface OnStepClickListener {
        void stepClicked(int stepId);
    }

    public RecipeStepAdapter(Context mContext, List<Step> data, OnStepClickListener recipeClickListener) {
        this.mContext = mContext;
        this.data = data;
        this.recipeClickListener = recipeClickListener;
    }

    @Override
    public RecipeStepViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.recipe_step_item, parent, false);
        return new RecipeStepViewHolder(v);
    }

    @Override
    public void onBindViewHolder(RecipeStepViewHolder holder, int position) {
        Step step = data.get(position);

        holder.bind(step, position);
    }

    public void setCurrentPos(int position) {
        currentPos = position;
    }

    @Override
    public int getItemCount() {
        return data.size();
    }

    class RecipeStepViewHolder extends RecyclerView.ViewHolder implements
            View.OnClickListener{

        @BindView(R.id.list_step_layout)
        View listStepLayout;

        @BindView(R.id.step_description)
        TextView stepDescription;

        @BindView(R.id.goto_step_icon)
        ImageView gotoStepIcon;

        @BindColor(R.color.app_white)
        int normalItemBackground;

        @BindColor(R.color.primaryColorLight)
        int currentItemBackground;

        private int currentId;

        RecipeStepViewHolder(View itemView) {
            super(itemView);

            ButterKnife.bind(this, itemView);

            itemView.setOnClickListener(this);
        }

        /*
        Adopted from: https://github.com/djkovrik/BakingApp/blob/master/app/src/main/java/com/
        sedsoftware/bakingapp/features/recipedetails/RecipeDetailsAdapter.java
         */
        void bind(Step step, int bindPosition) {

            currentId = getAdapterPosition();

            String description = step.getShortDescription();
            stepDescription.setText(String.format(mContext.getString(R.string.step_format), currentId, description));

            String video = step.getVideoURL();

            if (video.isEmpty()) {
                gotoStepIcon.setVisibility(View.INVISIBLE);
            } else {
                gotoStepIcon.setVisibility(View.VISIBLE);
            }

            if (currentPos == bindPosition) {
                listStepLayout.setBackgroundColor(currentItemBackground);
                stepDescription.setTextColor(normalItemBackground);
            } else {
                listStepLayout.setBackgroundColor(normalItemBackground);
                stepDescription.setTextColor(currentItemBackground);
            }
        }

        @Override
        public void onClick(View view) {
            currentPos = currentId;
            recipeClickListener.stepClicked(getAdapterPosition());
            notifyDataSetChanged();
        }
    }
}
