package com.example.recipe_app;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;

import com.google.firebase.auth.FirebaseAuth;
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


                //db.collection("users").document(email).update(userMap);

        }
    }
}
