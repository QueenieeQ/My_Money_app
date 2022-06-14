package com.queenieeq.mymoney.models;

import android.graphics.Color;

import com.queenieeq.mymoney.R;

public  class DefaultCategories {
    private static Category[] categories = new Category[]{
            new Category(":sarathi", "Sarathi", R.drawable.category_transport, Color.parseColor("#d32f2f")),
            new Category(":parivahan", "Parivahan", R.drawable.category_kids, Color.parseColor("#c2185b")),
            new Category(":work", "Work", R.drawable.category_briefcase, Color.parseColor("#f57c00")),
            new Category(":others", "Others", R.drawable.category_gift, Color.parseColor("#455a64")),
    };

    public static Category createDefaultCategoryModel(String visibleName) {
        return new Category("default", visibleName, R.drawable.category_sport,
                Color.parseColor("#26a69a"));
    }


    public static Category[] getDefaultCategories() {
        return categories;
    }
}
