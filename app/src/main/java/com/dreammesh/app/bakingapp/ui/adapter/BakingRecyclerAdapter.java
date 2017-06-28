package com.dreammesh.app.bakingapp.ui.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import com.dreammesh.app.bakingapp.R;
import com.dreammesh.app.bakingapp.data.model.BakingWrapper;
import com.squareup.picasso.Picasso;

import java.util.List;

/**
 * Created by Jedidiah on 01/06/2017.
 */

public class BakingRecyclerAdapter extends RecyclerView.Adapter<BakingRecyclerAdapter.BakingViewHolder>{

    private Context context;
    private List<BakingWrapper> data;
    private OnItemClickListener mListener;

    public BakingRecyclerAdapter(Context context, List<BakingWrapper> data,
                                 OnItemClickListener mListener) {
        this.context = context;
        this.data = data;
        this.mListener = mListener;
    }

    @Override
    public BakingViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.baking_item, parent, false);
        return new BakingViewHolder(v);
    }

    @Override
    public void onBindViewHolder(final BakingViewHolder holder, final int position) {
        BakingWrapper item = data.get(position);

        holder.itemName.setText(item.getName());

        String servings = String.format(context.getString(R.string.servings_format), item.getServings());
        holder.itemServings.setText(servings);

        int errorDrawable = 0;
        if(item.getId() == 1) {
            errorDrawable = R.drawable.pic2;
        } else if(item.getId() == 2) {
            errorDrawable = R.drawable.pic3;
        } else if(item.getId() == 3) {
            errorDrawable = R.drawable.pic;
        } else if(item.getId() == 4) {
            errorDrawable = R.drawable.cheese;
        } else {
            errorDrawable = R.drawable.recipe_placeholder;
        }

        if(item.getImage().isEmpty()) {
            Picasso.with(context)
                    .load(errorDrawable)
                    .into(holder.itemBg);
        } else {
            Picasso.with(context)
                    .load(item.getImage())
                    .error(errorDrawable)
                    .into(holder.itemBg);
        }
    }

    @Override
    public int getItemCount() {
        return data.size();
    }

    public void swapData(List<BakingWrapper> data) {
        this.data = data;
        if(data != null){
            notifyDataSetChanged();
        }
    }

    public interface OnItemClickListener {
        void onItemClicked(int position);
    }

    class BakingViewHolder extends RecyclerView.ViewHolder implements
            View.OnClickListener{

        FrameLayout itemContainer;
        TextView itemName;
        TextView itemServings;
        ImageView itemBg;

        BakingViewHolder(View itemView) {
            super(itemView);

            itemContainer = (FrameLayout) itemView.findViewById(R.id.item_container);
            itemName = (TextView) itemView.findViewById(R.id.item_name);
            itemServings = (TextView) itemView.findViewById(R.id.item_servings);
            itemBg = (ImageView) itemView.findViewById(R.id.item_bg);

            itemContainer.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            mListener.onItemClicked(getAdapterPosition());
        }
    }
}
