package com.example.recipe_app;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;

public class RegistrationInfo extends AppCompatActivity implements View.OnClickListener{

    // Define class variables from the page
    private Button b_next;
    private EditText et_age, et_weight, et_height, et_email;
    private Spinner spin_gender, spin_weight_goal;

    private static final String TAG = "Register.java";
    private static final String age = "age";
    private static final String weight = "weight";
    private static final String height = "height";
    private static final String gender = "gender";
    private static final String weight_goal = "weight_goal";

    private FirebaseFirestore db = FirebaseFirestore.getInstance();
    private FirebaseAuth firebaseAuth;

    // on creation of activity
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registration_info);

        // set class variables equal to their inputs
        et_email = (EditText) findViewById(R.id.et_register_email);
        et_age = (EditText) findViewById(R.id.et_reg_age);
        et_weight = (EditText) findViewById(R.id.et_reg_height);
        et_height = (EditText) findViewById(R.id.et_reg_height);
        spin_gender = (Spinner) findViewById(R.id.spin_reg_gender);
        spin_weight_goal = (Spinner) findViewById(R.id.spin_reg_gender);
        b_next = (Button) findViewById(R.id.btn_reg_next);

        firebaseAuth = FirebaseAuth.getInstance();

        // call spinner and button listeners
        setSpinnerListeners();
        b_next.setOnClickListener(this);

    }

    // Check which listener was called
    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            // next button was clicked
            case R.id.btn_reg_next:
                if (validate()) {
                    // save user in database
                    saveUser();
                    // include email value for next activity
                    final String email = getIntent().getStringExtra("email_from_reg");
                    startActivity(new Intent(RegistrationInfo.this,
                            RegistrationIngredients.class)
                        .putExtra("email_from_regInfo", email));
                }

        }
    }

    /* validate inputs
    returns true if all inputs are not empty
    note: display which field is missing
     */
    private boolean validate() {
        boolean result = false;

        String age = et_age.getText().toString();
        String weight = et_weight.getText().toString();
        String height = et_height.getText().toString();
        String gender = spin_gender.getSelectedItem().toString();
        String weight_goal = spin_weight_goal.getSelectedItem().toString();

        if (age.isEmpty() || weight.isEmpty() || height.isEmpty() || gender.isEmpty()
                || weight_goal.isEmpty()) {
            Toast.makeText(this, "You're forgetting a field!", Toast.LENGTH_SHORT).show();
        } else {
            result = true;
        }

        return result;
    }

    // save user to database
    // note: need to implement some sort of db fail case, it will continue whether it fails or not
    public void saveUser() {
        String user_age = et_age.getText().toString();
        String user_weight = et_weight.getText().toString();
        String user_height = et_height.getText().toString();
        String user_gender = spin_gender.getSelectedItem().toString();
        String user_weight_goal = spin_weight_goal.getSelectedItem().toString();
        String email = getIntent().getStringExtra("email_from_reg");

        Map<String, Object> user = new HashMap<>();
        user.put(age, user_age);
        user.put(weight, user_weight);
        user.put(height, user_height);
        user.put(gender, user_gender);
        user.put(weight_goal, user_weight_goal);

        db.collection("users").document(email).set(user)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {

                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        // currently displays exception e (need to reformat)
                        Toast.makeText(RegistrationInfo.this,
                                e.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });
    }

    // set spinner listeners, currently do nothing on item selected and gives a message if no item
    // was selected
    public void setSpinnerListeners() {
        spin_gender.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                Toast.makeText(RegistrationInfo.this,
                        "Please enter a gender", Toast.LENGTH_SHORT).show();
            }
        });

        spin_weight_goal.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                Toast.makeText(RegistrationInfo.this,
                        "Please enter a weight goal", Toast.LENGTH_SHORT).show();
            }
        });
    }

}
