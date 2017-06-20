package com.dreammesh.app.bakingapp.data.model;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Jedidiah on 01/06/2017.
 */

public class BakingWrapper implements Parcelable {

    private int id;
    private String name;
    private List<Ingredient> ingredients;
    private List<Step> steps;
    private int servings;
    private String image;

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public List<Ingredient> getIngredients() {
        return ingredients;
    }

    public List<Step> getSteps() {
        return steps;
    }

    public int getServings() {
        return servings;
    }

    public String getImage() {
        return image;
    }


    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(this.id);
        dest.writeString(this.name);
        dest.writeList(this.ingredients);
        dest.writeList(this.steps);
        dest.writeInt(this.servings);
        dest.writeString(this.image);
    }

    public BakingWrapper() {
    }

    private BakingWrapper(Parcel in) {
        this.id = in.readInt();
        this.name = in.readString();
        this.ingredients = new ArrayList<Ingredient>();
        in.readList(this.ingredients, Ingredient.class.getClassLoader());
        this.steps = new ArrayList<Step>();
        in.readList(this.steps, Step.class.getClassLoader());
        this.servings = in.readInt();
        this.image = in.readString();
    }

    public static final Creator<BakingWrapper> CREATOR = new Creator<BakingWrapper>() {
        @Override
        public BakingWrapper createFromParcel(Parcel source) {
            return new BakingWrapper(source);
        }

        @Override
        public BakingWrapper[] newArray(int size) {
            return new BakingWrapper[size];
        }
    };
}
