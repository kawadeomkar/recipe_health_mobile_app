package com.example.recipe_app;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    // Define class variables from the page
    private Button b_letsgo, b_signup;
    private FirebaseAuth firebaseAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // set class variables equal to their inputs
        b_letsgo = (Button) findViewById(R.id.b_main_letsgo);
        b_signup = (Button) findViewById(R.id.b_main_signup);

        // get instance of firebase authentication object and check if user is already logged in
        // note: currently do not have logout button, hard coded the sign out
        firebaseAuth = FirebaseAuth.getInstance();
        FirebaseUser user = firebaseAuth.getCurrentUser();
        if (user != null) { // user is signed in! send him to home page
            Toast.makeText(MainActivity.this, "Welcome back! Please sign in again.",
                    Toast.LENGTH_SHORT).show();
            firebaseAuth.signOut();
            //startActivity(this);
        }

        // listen if the user has clicked the next button
        b_letsgo.setOnClickListener(this);
        b_signup.setOnClickListener(this);
    }

    // Check which listener was called
    @Override
    public void onClick(View v) {
        switch(v.getId()) {
            // user presses on lets go button to log in
            case R.id.b_main_letsgo:
                startActivity(new Intent(this, Login.class));
                break;
            // user presses on sign up button
            case R.id.b_main_signup:
                startActivity(new Intent(this, Register.class));
                break;
        }
    }
}
