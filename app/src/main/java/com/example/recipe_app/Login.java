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

    Button b_login;
    EditText et_email, et_password;
    TextView register;

    private FirebaseAuth firebaseAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        et_email = (EditText) findViewById(R.id.et_login_email);
        et_password = (EditText) findViewById(R.id.et_login_password);
        b_login = (Button) findViewById(R.id.b_login);
        register = (TextView) findViewById(R.id.tv_login_register);

        firebaseAuth = FirebaseAuth.getInstance();


        b_login.setOnClickListener(this);
        register.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch(v.getId()) {
            case R.id.b_login:
                    signin(et_email.getText().toString(), et_password.getText().toString());
                break;

            case R.id.tv_login_register:
                startActivity(new Intent(this, Register.class));
                break;
        }
    }

    private void signin(String email, String password) {
        firebaseAuth.signInWithEmailAndPassword(email, password).addOnCompleteListener
                (new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if (task.isSuccessful()) {
                    // move to next page!
                    Toast.makeText(Login.this, "Login successful!", Toast.LENGTH_SHORT).show();
                    startActivity(new Intent(Login.this, HomePage.class));
                } else if (!task.isSuccessful()) {
                    String errorCode = ((FirebaseAuthException) task.getException()).getMessage();
                    Toast.makeText(Login.this, "Login failed! " + errorCode, Toast.LENGTH_LONG).show();
                }
            }
        });
    }
}
