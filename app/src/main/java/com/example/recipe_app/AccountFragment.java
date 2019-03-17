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

    String age, weight, height, gender, weight_goal;
    ArrayList<String> dietary_restrictions;

    private FirebaseFirestore db = FirebaseFirestore.getInstance();
    private FirebaseAuth firebaseAuth;


    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        System.out.println("RUNNING ON CREATE VIEW");
        firebaseAuth = FirebaseAuth.getInstance();
        super.onCreate(savedInstanceState);
        return inflater.inflate(R.layout.fragment_account, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        getView().findViewById(R.id.save_button).setOnClickListener(this);
        String email = "";
        Bundle bundle = this.getArguments();
        email = bundle.getString("email");
        DocumentReference currentInfo = db.collection("users").document(email);
        currentInfo.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                EditText temp;
                Spinner temp2;
                if (task.isSuccessful()) {
                    DocumentSnapshot document = task.getResult();
                    if (document.exists()) {
                        // Get the data back
                        Object doc = document.getData();

                        // Set age text field to age in database
                        temp = getView().findViewById(R.id.editAge);
                        temp.setText(((Map) doc).get("age").toString());

                        // Set weight text field to weight in database
                        temp = getView().findViewById(R.id.editWeight);
                        temp.setText(((Map) doc).get("weight").toString());

                        // Set height text field to height in database
                        temp = getView().findViewById(R.id.editHeight);
                        temp.setText(((Map) doc).get("height").toString());

                        // Set gender spinner field to gender in database
                        temp2 = getView().findViewById(R.id.editGender);
                        String gen = ((Map) doc).get("gender").toString();
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

                System.out.println("Changing values:" +
                        "\nAge = " + age +
                        "\nWeight = " + weight +
                        "\nHeight = " + height +
                        "\nGender = " + gender +
                        "\nWeight Goal = " + weight_goal);

                Map<String, Object> userMap = new HashMap<>();
                userMap.put("age", age);
                userMap.put("weight", weight);
                userMap.put("height", height);
                userMap.put("gender", gender);
                userMap.put("weight_goal", weight_goal);

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
}
