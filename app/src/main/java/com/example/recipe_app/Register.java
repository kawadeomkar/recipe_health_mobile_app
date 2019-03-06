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
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthException;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;

public class Register extends AppCompatActivity implements View.OnClickListener {

    // Define class variables from the page
    private Button b_register;
    private EditText et_email, et_name, et_password;
    // firebase/debugging variables
    private static final String TAG = "Register.java";
    private static final String name = "name";
    private static final String password = "password";
    private FirebaseFirestore db = FirebaseFirestore.getInstance();
    private FirebaseAuth firebaseAuth;

    // on creation of activity
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        // set class variables equal to their inputs
        et_email = (EditText) findViewById(R.id.et_register_email);
        et_name = (EditText) findViewById(R.id.et_register_name);
        et_password = (EditText) findViewById(R.id.et_register_password);
        b_register = (Button) findViewById(R.id.b_register);

        // get current activity instance
        firebaseAuth = FirebaseAuth.getInstance();

        // listen if the user has clicked the register button
        b_register.setOnClickListener(this);
    }

    // Check which listener was called
    @Override
    public void onClick(View v) {
        switch(v.getId()) {
            // register button was clicked
            case R.id.b_register:
                // validated, upload data to the database
                if (validate()) {
                    final String email = et_email.getText().toString();
                    String password = et_password.getText().toString();
                    // create user and upload to authentication database
                    firebaseAuth.createUserWithEmailAndPassword(
                            email,
                            password
                    ).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                        // on completion of sending to firebase
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            // if account was made
                            if (task.isSuccessful()) {
                                // save user in database
                                saveUser();
                                Toast.makeText(Register.this, "Your account was made!",
                                        Toast.LENGTH_SHORT).show();
                                // include email value for next activity
                                startActivity(new Intent(Register.this, RegistrationInfo.class)
                                    .putExtra("email_from_reg", email));
                            } // display which error using errCode
                             else if (!task.isSuccessful()){
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

    /* simple validation function for register inputs
    returns true if no inputs were empty
    note: include better checks? display which field is missing
     */
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

    // save user to database
    public void saveUser() {
        String email = et_email.getText().toString();
        String user_password = et_password.getText().toString();
        String user_name = et_name.getText().toString();

        Map<String, Object> user = new HashMap<>();
        user.put(name, user_name);
        user.put(password, user_password);

        db.collection("users").document(email).set(user)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {

                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {

                    }
                });
    }
}


