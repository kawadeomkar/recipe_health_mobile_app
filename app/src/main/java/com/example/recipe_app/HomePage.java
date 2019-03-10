package com.example.recipe_app;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;

import com.android.volley.RequestQueue;
import com.android.volley.toolbox.Volley;

import java.util.ArrayList;
import java.util.List;

public class HomePage extends AppCompatActivity {

    private BottomNavigationView.OnNavigationItemSelectedListener navListener =
            new BottomNavigationView.OnNavigationItemSelectedListener() {
                @Override
                public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
                    Fragment selectedFragment = null;

                    switch (menuItem.getItemId()) {
                        case R.id.nav_recipes:
                            selectedFragment = new RecipeFragment();
                            break;

                        case R.id.nav_fridge:
                            selectedFragment = new FridgeFragment();
                            break;

                        case R.id.nav_account:
                            selectedFragment = new AccountFragment();
                            break;
                    }

                    getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,
                            selectedFragment).commit();
                    return true;
                }
            };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home_page);

        BottomNavigationView bottomNav = findViewById(R.id.bottom_navigation);
        bottomNav.setOnNavigationItemSelectedListener(navListener);

        getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,
                new RecipeFragment()).commit();
    }

    // TESTING get the recipes
    private void setUpHTTPRecipes() {
        SpoonAPI spoon = new SpoonAPI();
        List<String> t = new ArrayList<>();
        t.add("apples");
        t.add("sugar");
        t.add("flour");


        RequestQueue requestQueue = Volley.newRequestQueue(this);
        List<RecipeTemp> temp = spoon.getRecipeComplex(t, 3, requestQueue);
        if (temp.size() == 3) {
        }
    }
}
