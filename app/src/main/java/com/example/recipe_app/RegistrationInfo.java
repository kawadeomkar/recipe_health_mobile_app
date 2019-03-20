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

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class RegistrationInfo extends AppCompatActivity implements View.OnClickListener{

    // Define class variables from the page
    private Button b_next;
    private EditText et_age, et_weight, et_height, et_diet;
    private Spinner spin_gender, spin_weight_goal, spin_activity_level;

    private static final String TAG = "Register.java";
    private static final String age = "age";
    private static final String weight = "weight";
    private static final String height = "height";
    private static final String gender = "gender";
    private static final String weight_goal = "weight_goal";
    private static final String TDEE = "TDEE";
    private static final String favorites = "favorites";
    private static final String dietary_restrictions = "dietary_restrictions";
    private static final String activity_level = "activity_level";
    private static final String learning_random = "learning_random";
    private static final String caloriesLeft = "caloriesLeft";
    private static final String prevDate = "prevDate";

    private FirebaseFirestore db = FirebaseFirestore.getInstance();
    private FirebaseAuth firebaseAuth;

    // on creation of activity
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registration_info);

        // set class variables equal to their inputs
        et_diet = (EditText) findViewById(R.id.et_reginfo_diet);
        et_age = (EditText) findViewById(R.id.et_reg_age);
        et_weight = (EditText) findViewById(R.id.et_reg_weight);
        et_height = (EditText) findViewById(R.id.et_reg_height);
        spin_gender = (Spinner) findViewById(R.id.spin_reg_gender);
        spin_weight_goal = (Spinner) findViewById(R.id.spin_reg_goal);
        spin_activity_level = (Spinner) findViewById(R.id.spin_reg_activity);
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
        String activity_level = spin_activity_level.getSelectedItem().toString();
        String diet = et_diet.getText().toString();

        if (age.isEmpty() || weight.isEmpty() || height.isEmpty() || gender.isEmpty()
                || weight_goal.isEmpty() || activity_level.isEmpty()) {
            Toast.makeText(this, "You're forgetting a field!",
                    Toast.LENGTH_SHORT).show();
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
        String user_activity_level = spin_activity_level.getSelectedItem().toString();
        String email = getIntent().getStringExtra("email_from_reg");
        List<String> diet = Arrays.asList(et_diet.getText().toString().split(","));

        String user_TDEE = Double.toString(calculateTDEE(user_weight, user_height, user_age,
                user_gender, user_activity_level));

        Map<String, Object> user = new HashMap<>();
        Map<String, Integer> cuisines = new HashMap<>();
        cuisines.put("african", 1);
        cuisines.put("chinese", 1);
        cuisines.put("korean", 1);
        cuisines.put("japanese", 1);
        cuisines.put("vietnamese", 1);
        cuisines.put("thai", 1);
        cuisines.put("irish", 1);
        cuisines.put("italian", 1);
        cuisines.put("spanish", 1);
        cuisines.put("british", 1);
        cuisines.put("indian", 1);
        cuisines.put("mexican", 1);
        cuisines.put("french", 1);
        cuisines.put("eastern", 1);
        cuisines.put("middle", 1);
        cuisines.put("american", 1);
        cuisines.put("jewish", 1);
        cuisines.put("southern", 1);
        cuisines.put("caribbean", 1);
        cuisines.put("cajun", 1);
        cuisines.put("greek", 1);
        cuisines.put("nordic", 1);
        cuisines.put("german", 1);
        cuisines.put("european", 1);
        cuisines.put("eastern", 1);

        user.put(age, user_age);
        user.put(weight, user_weight);
        user.put(height, user_height);
        user.put(gender, user_gender);
        user.put(weight_goal, user_weight_goal);
        user.put(TDEE, user_TDEE);
        user.put(favorites, new ArrayList<String>());
        user.put(dietary_restrictions, diet);
        user.put(activity_level, user_activity_level);
        user.put(caloriesLeft, Integer.toString((int)Double.parseDouble(user_TDEE)));
        Date date = new Date();
        user.put(prevDate, date.toString());
        user.put(learning_random, cuisines);

        db.collection("users").document(email).collection("activities")
                .document("account_information").set(user)
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

        // hacky fix to create favorite section
        db.collection("users").document(email).collection("activities")
                .document("favorite_recipes").set(new HashMap<>());
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

    private double calculateTDEE(String weight, String height, String age, String gender,
                                 String activity_level) {

        double weight_kg = Integer.parseInt(weight) * 0.453592;
        double height_cm = Integer.parseInt(height) * 2.54;
        double BMR = 0;
        double TDEE = 0;

        if (gender.equals("Male")) {
            BMR = 9.99 * weight_kg + 6.25 * height_cm - 4.92 * Integer.parseInt(age) + 5;

            if (activity_level.equals("Sedentary")) {
                TDEE = BMR * 1.2;
            } else if (activity_level.equals("Lightly Active")) {
                TDEE = BMR * 1.375;
            } else if (activity_level.equals("Moderately Active")) {
                TDEE = BMR * 1.55;
            } else if (activity_level.equals("Very Active")) {
                TDEE = BMR * 1.725;
            } else if (activity_level.equals("Highly Active")) {
                TDEE = BMR * 1.9;
            }

        } else if (gender.equals("Female")) {
            BMR = 9.99 * weight_kg + 6.25 * height_cm - 5 - 4.92 * Integer.parseInt(age) - 161;
            if (activity_level.equals("Sedentary")) {
                TDEE = BMR * 1.2;
            } else if (activity_level.equals("Lightly Active")) {
                TDEE = BMR * 1.375;
            } else if (activity_level.equals("Moderately Active")) {
                TDEE = BMR * 1.55;
            } else if (activity_level.equals("Very Active")) {
                TDEE = BMR * 1.725;
            } else if (activity_level.equals("Highly Active")) {
                TDEE = BMR * 1.9;
            }
        }
        if (this.weight_goal.contains("Bulk up!")) {
            return TDEE * 1.1;
        }
        if (this.weight_goal.contains("Cut fat!")) {
            return TDEE * 0.9;
        }
        else {
            return TDEE;
        }
    }
}
