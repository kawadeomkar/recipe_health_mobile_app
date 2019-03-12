package com.example.recipe_app;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;

public class HomePage extends AppCompatActivity {

    private FirebaseAuth firebaseAuth;
    private FirebaseFirestore db;
    private String email;

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

                    FragmentManager fm = getSupportFragmentManager();
                    // create arguments to be sent to recipeFragment
                    Bundle args = new Bundle();
                    args.putString("email", email);
                    selectedFragment.setArguments(args);
                    fm.beginTransaction().replace(R.id.fragment_container, selectedFragment).commit();
                    return true;
                }
            };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home_page);

        // get instance of firebaseAuth, check if user is signed in
        firebaseAuth = FirebaseAuth.getInstance();
        FirebaseUser user = firebaseAuth.getCurrentUser();
        if (user == null) { // user is not signed in! send him to home page
            Toast.makeText(HomePage.this, "Welcome back! Please sign in again.",
                    Toast.LENGTH_SHORT).show();
            firebaseAuth.signOut();
            startActivity(new Intent(this, MainActivity.class));
        }

        // instantiate bottom navigation vieew
        BottomNavigationView bottomNav = findViewById(R.id.bottom_navigation);
        bottomNav.setOnNavigationItemSelectedListener(navListener);
        // start up recipeFragment with fragmentManager
        FragmentManager fm = this.getSupportFragmentManager();
        // create arguments to be sent to recipeFragment
        Bundle args = new Bundle();
        email = getIntent().getStringExtra("email_from_login");
        args.putString("email", email);
        RecipeFragment recipeFrag = new RecipeFragment();
        recipeFrag.setArguments(args);
        // start transaction
        fm.beginTransaction().replace(R.id.fragment_container, recipeFrag).commit();
    }

    // TESTING get the recipes
    private void setUpHTTPRecipes() {
        SpoonAPI spoon = new SpoonAPI();
        db = FirebaseFirestore.getInstance();







    }
}
