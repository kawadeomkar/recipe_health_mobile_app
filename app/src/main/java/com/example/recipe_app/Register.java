package com.example.recipe_app;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthException;

public class Register extends AppCompatActivity implements View.OnClickListener {

    Button b_register;
    EditText et_email, et_name, et_password;
    private FirebaseAuth firebaseAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        et_email = (EditText) findViewById(R.id.et_register_email);
        et_name = (EditText) findViewById(R.id.et_register_name);
        et_password = (EditText) findViewById(R.id.et_register_password);
        b_register = (Button) findViewById(R.id.b_register);

        firebaseAuth = FirebaseAuth.getInstance();

        b_register.setOnClickListener(this);
    }


    @Override
    public void onClick(View v) {
        switch(v.getId()) {
            case R.id.b_register:
                // upload data to the database
                if (validate()) {
                    String email = et_email.getText().toString().trim();
                    String password = et_password.getText().toString().trim();

                    firebaseAuth.createUserWithEmailAndPassword(
                            email,
                            password
                    ).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if (task.isSuccessful()) {
                                Toast.makeText(Register.this, "Your account was made!",
                                        Toast.LENGTH_SHORT).show();
                                startActivity(new Intent(Register.this, MainActivity.class));
                            } else if (!task.isSuccessful()){
                                String errorCode = ((FirebaseAuthException) task.getException()).getMessage();
                                Toast.makeText(Register.this, "Registration failed: " + errorCode,
                                        Toast.LENGTH_LONG).show();
                            }

                        }
                    });
                }
                break;
        }
    }

    // simple validation function for register inputs
    private boolean validate() {
        Boolean result = false;

        String name = et_name.getText().toString();
        String email = et_email.getText().toString();
        String password = et_password.getText().toString();

        if (name.isEmpty() || password.isEmpty() || email.isEmpty()) {
            Toast.makeText(this, "You're forgetting a field!", Toast.LENGTH_SHORT).show();
        } else {
            result = true;
        }

        return result;
    }
}


