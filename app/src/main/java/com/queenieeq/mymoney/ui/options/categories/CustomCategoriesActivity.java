package com.queenieeq.mymoney.ui.options.categories;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.ListView;

import java.util.ArrayList;

import com.queenieeq.mymoney.R;
import com.queenieeq.mymoney.base.BaseActivity;
import com.queenieeq.mymoney.firebase.FirebaseElement;
import com.queenieeq.mymoney.firebase.FirebaseObserver;
import com.queenieeq.mymoney.firebase.models.User;
import com.queenieeq.mymoney.firebase.viewmodel_factories.UserProfileViewModelFactory;
import com.queenieeq.mymoney.util.CategoriesHelper;
import com.queenieeq.mymoney.models.Category;

public class CustomCategoriesActivity extends BaseActivity {

    private User user;
    private ArrayList<Category> customCategoriesList;
    private CustomCategoriesAdapter customCategoriesAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_custom_categories);
        setSupportActionBar(findViewById(R.id.toolbar));
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle("Custom categories");

        UserProfileViewModelFactory.getModel(getUid(), this).observe(this, new FirebaseObserver<FirebaseElement<User>>() {
            @Override
            public void onChanged(FirebaseElement<User> firebaseElement) {
                if (firebaseElement.hasNoError()) {
                    CustomCategoriesActivity.this.user = firebaseElement.getElement();
                    dataUpdated();
                }
            }
        });

        ListView listView = findViewById(R.id.custom_categories_list_view);
        customCategoriesList = new ArrayList<>();
        customCategoriesAdapter = new CustomCategoriesAdapter(this, customCategoriesList, getApplicationContext());
        listView.setAdapter(customCategoriesAdapter);

        findViewById(R.id.add_custom_category_fab).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(CustomCategoriesActivity.this, AddCustomCategoryActivity.class));
            }
        });

    }

    private void dataUpdated() {
        if (user == null) return;
        customCategoriesList.clear();
        customCategoriesList.addAll(CategoriesHelper.getCustomCategories(user));
        customCategoriesAdapter.notifyDataSetChanged();

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem menuItem) {
        onBackPressed();
        return true;
    }
}
