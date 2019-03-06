package com.example.recipe_app;

import android.content.Context;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;

import java.util.ArrayList;
import java.util.List;

public class RegistrationIngredients extends AppCompatActivity {

    private LinearLayout parentLinearLayout;
    private List<String> ingredients;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registration_ingredients);

        //String email = getIntent().getStringExtra("email_from_regInfo");
        parentLinearLayout = (LinearLayout) findViewById(R.id.parent_linear_layout);

    }

    public void onAddField(View v) {
        LayoutInflater inflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        final View rowView = inflater.inflate(R.layout.field, null);
        // Add the new row before the add field button.
        parentLinearLayout.addView(rowView, parentLinearLayout.getChildCount() - 1);
    }

    public void onDelete(View v) {
        parentLinearLayout.removeView((View) v.getParent());
    }

    public void finish(View v) {

        ingredients = new ArrayList<>();

        Log.d("reg_ing", "the finish button was clicked");
        LinearLayout group = (LinearLayout) findViewById(R.id.parent_linear_layout);
        int count = group.getChildCount();
        Log.d("reg_ing", "view count " + Integer.toString(count));
        for (int i = 0; i < count; ++i) {
            View view = group.getChildAt(i);
            if (view instanceof LinearLayout) {
                Log.d("reg_ing", "this is a linear layout");
                LinearLayout rows = (LinearLayout) view;
                int row_count = rows.getChildCount();
                for (int j = 0; j < row_count; ++j) {
                    View inner_view = rows.getChildAt(j);
                    if (inner_view instanceof EditText) {
                        Log.d("reg_ing", ((EditText) inner_view).getText().toString());
                        ingredients.add(((EditText) inner_view).getText().toString());
                    }
                }
            }
        }

    }
}
