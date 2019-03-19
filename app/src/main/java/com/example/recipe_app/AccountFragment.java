package com.example.recipe_app;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.Spinner;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class AccountFragment extends Fragment implements View.OnClickListener{

    String age, weight, height, gender, weight_goal, activity_level;
    ArrayList<String> dietary_restrictions;

    private FirebaseFirestore db = FirebaseFirestore.getInstance();
    private FirebaseAuth firebaseAuth;
    private DocumentReference docRef;
    private String email;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        System.out.println("RUNNING ON CREATE VIEW");
        Bundle bundle = this.getArguments();
        email = bundle.getString("email");
        firebaseAuth = FirebaseAuth.getInstance();

        super.onCreate(savedInstanceState);
        return inflater.inflate(R.layout.fragment_account, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        getView().findViewById(R.id.save_button).setOnClickListener(this);

        docRef = db.collection("users").document(email)
                .collection("activities").document("account_information");
        docRef.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                EditText temp;
                Spinner temp2;
                if (task.isSuccessful()) {
                    DocumentSnapshot document = task.getResult();
                    if (document.exists()) {
                        // Get the data back
                        Map<String, Object> doc = document.getData();

                        // Set age text field to age in database
                        temp = getView().findViewById(R.id.editAge);
                        temp.setText(doc.get("age").toString());

                        // Set weight text field to weight in database
                        temp = getView().findViewById(R.id.editWeight);
                        temp.setText(doc.get("weight").toString());

                        // Set height text field to height in database
                        temp = getView().findViewById(R.id.editHeight);
                        temp.setText(doc.get("height").toString());

                        // Set calories text field to calories in database
//                        temp = getView().findViewById(R.id.calories);
//                        temp.setText(doc.get("TDEE").toString());


                        // Set gender spinner field to gender in database
                        temp2 = getView().findViewById(R.id.editGender);

                        String gen = (doc.get("gender").toString());

                        if (gen.equals("Male")) {
                            temp2.setSelection(0);
                        }
                        else {
                            temp2.setSelection(1);
                        }

                        // Set weight_goal spinner field to weight_gaol in database
                        temp2 = getView().findViewById(R.id.editWeightGoal);
                        String goal = ((Map) doc).get("weight_goal").toString();
                        if (goal.equals("Bulk up!")) {
                            temp2.setSelection(0);
                        }
                        else if (goal.equals("Cut fat!")) {
                            temp2.setSelection(1);
                        }
                        else {
                            temp2.setSelection(2);
                        }

                        Log.d("Register.java", "Doc data: " + document.getData());
                    }
                    else {
                        Log.d("Register.java", "No such doc");
                    }
                }
                else {
                    Log.d("Register.java", "get failed with " + task.getException());
                }
            }
        });
    }

    // Check which listener was called
    @Override
    public void onClick(View v) {
        System.out.println("CLICK TRIGGERED");
        switch (v.getId()) {
            // save button was clicked
            case R.id.save_button:
                EditText temp = getView().findViewById(R.id.editAge);
                age = temp.getText().toString();

                // weight age in class variable
                temp = getView().findViewById(R.id.editWeight);
                weight = temp.getText().toString();

                // save height in class variable
                temp = getView().findViewById(R.id.editHeight);
                height = temp.getText().toString();

                // save gender in class variable
                Spinner temp2 = getView().findViewById(R.id.editGender);
                gender = temp2.getSelectedItem().toString();

                // save weight_goal in class variable
                temp2 = getView().findViewById(R.id.editWeightGoal);
                weight_goal = temp2.getSelectedItem().toString();


                Spinner spin_act = getView().findViewById(R.id.editActivityLevel);
                activity_level = spin_act.getSelectedItem().toString();

                System.out.println("Changing values:" +
                        "\nAge = " + age +
                        "\nWeight = " + weight +
                        "\nHeight = " + height +
                        "\nGender = " + gender +
                        "\nWeight Goal = " + weight_goal +
                        "\nActivity Level = " + activity_level);

                String TDEE = Double.toString(calculateTDEE(weight, height, age, gender, activity_level));

                Map<String, Object> userMap = new HashMap<>();
                userMap.put("age", age);
                userMap.put("weight", weight);
                userMap.put("height", height);
                userMap.put("gender", gender);
                userMap.put("weight_goal", weight_goal);
                userMap.put("TDEE", TDEE);

                // TODO: get the variable email, similar to RegistrationInfo.java
                //getActivity().getIntent().getStringExtra("email_from_reg"); DOESNT WORK
                // get bundle arguments
                String email = "";
                Bundle bundle = this.getArguments();
                email = bundle.getString("email");


                db.collection("users").document(email).collection("activities")
                        .document("account_information").update(userMap);

        }
    }

    private double calculateTDEE(String weight, String height, String age, String gender, String activity_level) {


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

        return TDEE;

    }
}
