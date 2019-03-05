package com.example.recipe_app;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthException;

public class Login extends AppCompatActivity implements View.OnClickListener {

    // Define class variables from the page
    private Button b_login;
    private EditText et_email, et_password;
    private TextView register;
    // firebase object
    private FirebaseAuth firebaseAuth;

    // on creation of activity
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        // set class variables equal to their inputs
        et_email = (EditText) findViewById(R.id.et_login_email);
        et_password = (EditText) findViewById(R.id.et_login_password);
        b_login = (Button) findViewById(R.id.b_login);
        register = (TextView) findViewById(R.id.tv_login_register);

        // get instance of firebase authentication
        firebaseAuth = FirebaseAuth.getInstance();

        // listen to which is going to be clicked
        b_login.setOnClickListener(this);
        register.setOnClickListener(this);
    }

    // Check which listener was called
    @Override
    public void onClick(View v) {
        switch(v.getId()) {
            // user pressed login button
            case R.id.b_login:
                    // if inputs are valid, sign in the user
                    if (validate()) {
                        signIn();
                    }
                break;
            // user pressed on register button, send to register activity
            case R.id.tv_login_register:
                startActivity(new Intent(this, Register.class));
                break;
        }
    }

    /* simple validation function, checks if inputs are empty
    returns true if inputs are correct format
    note: add better check, display which field is missing
     */
    private boolean validate() {
        boolean result = false;

        String email = et_email.getText().toString();
        String password = et_password.getText().toString();

        if (email.isEmpty() || password.isEmpty()) {
            Toast.makeText(this, "You're forgetting a field!", Toast.LENGTH_SHORT).show();
        } else {
            result = true;
        }

        return result;
    }

    /* function that signs in user using firebase authentication
    Does not return anything, sends user to next activity
     */
    private void signIn() {
        // convert inputs to strings
        String email = et_email.getText().toString();
        String password = et_password.getText().toString();
        // firebase authentication
        firebaseAuth.signInWithEmailAndPassword(email, password).addOnCompleteListener
                (new OnCompleteListener<AuthResult>() {
            // when firebase authentication is completed
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                // user was able to login, move him to home page activity
                if (task.isSuccessful()) {
                    Toast.makeText(Login.this, "Login successful!", Toast.LENGTH_SHORT).show();
                    startActivity(new Intent(Login.this, HomePage.class));
                } // could not login, display error
                else if (!task.isSuccessful()) {
                    String errorCode = ((FirebaseAuthException) task.getException()).getMessage();
                    Toast.makeText(Login.this, "Login failed! " + errorCode, Toast.LENGTH_LONG).show();
                }
            }
        });
    }
}
