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

    Button b_letsgo, b_signup;
    private FirebaseAuth firebaseAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        b_letsgo = (Button) findViewById(R.id.b_main_letsgo);
        b_signup = (Button) findViewById(R.id.b_main_signup);

        firebaseAuth = FirebaseAuth.getInstance();

        // Check if user is already logged in
        FirebaseUser user = firebaseAuth.getCurrentUser();
        if (user != null) {

            Toast.makeText(MainActivity.this, "Welcome back! Please sign in again.",
                    Toast.LENGTH_SHORT).show();
            firebaseAuth.signOut();
            //startActivity(this);
        }

        b_letsgo.setOnClickListener(this);
        b_signup.setOnClickListener(this);
    }


    @Override
    public void onClick(View v) {
        switch(v.getId()) {
            case R.id.b_main_letsgo:

                startActivity(new Intent(this, Login.class));
                break;

            case R.id.b_main_signup:

                startActivity(new Intent(this, Register.class));
                break;
        }
    }
}
