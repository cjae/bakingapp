package com.dreammesh.app.bakingapp.util;

import android.content.Context;
import android.text.Spannable;
import android.text.SpannableStringBuilder;
import android.text.style.StyleSpan;
import android.widget.TextView;

import com.dreammesh.app.bakingapp.R;

import java.util.Locale;

/**
 * Created by Jedidiah on 18/06/2017.
 */

public class StringUtil {

    public static String prepareIngredient(Context context, String name, float quantity, String measure) {

        String line = context.getResources().getString(R.string.item_ingredient_line);

        String quantityStr = String.format(Locale.US, "%s", quantity);
        if (quantity == (long) quantity) {
            quantityStr = String.format(Locale.US, "%d", (long) quantity);
        }

        return String.format(Locale.US, line, name, quantityStr, measure);
    }
}
