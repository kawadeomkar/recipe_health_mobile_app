package com.example.recipe_app;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class RegistrationIngredients extends AppCompatActivity {

    private LinearLayout parentLinearLayout;
    private List<String> ingredients_list;
    private FirebaseFirestore db = FirebaseFirestore.getInstance();
    private static final String ingredients = "ingredients";
    private String email;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registration_ingredients);

        email = getIntent().getStringExtra("email_from_regInfo");
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

        ingredients_list = new ArrayList<>();

        LinearLayout group = (LinearLayout) findViewById(R.id.parent_linear_layout);
        int count = group.getChildCount();
        for (int i = 0; i < count; ++i) {
            View view = group.getChildAt(i);
            if (view instanceof LinearLayout) {
                LinearLayout rows = (LinearLayout) view;
                int row_count = rows.getChildCount();
                for (int j = 0; j < row_count; ++j) {
                    View inner_view = rows.getChildAt(j);
                    if (inner_view instanceof EditText) {
                        ingredients_list.add(((EditText) inner_view).getText().toString());
                    }
                }
            }
        }
        // save ingredients to db and send to homepage
        saveIngredients();
        Toast.makeText(this, "Your ingredients have been saved!", Toast.LENGTH_SHORT).show();
        startActivity(new Intent(RegistrationIngredients.this, HomePage.class)
                .putExtra("email _from_regIng", email));
    }

    // save ingredients into the database
    public void saveIngredients() {
        Map<String, Object> user = new HashMap<>();
        user.put(ingredients, ingredients_list);

        db.collection("users").document(email).collection("activities")
                .document("ingredients").set(user)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {

                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        // currently displays exception e (need to reformat)
                        Toast.makeText(RegistrationIngredients.this,
                                e.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });
    }
}
