package com.example.recipe_app;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

public class RegistrationInfo extends AppCompatActivity implements View.OnClickListener {

    // Define class variables from the page
    private Button b_next;
    private EditText et_age, et_weight, et_height;
    private Spinner spin_gender, spin_weight_goal;

    // on creation of activity
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registration_info);

        // set class variables equal to their inputs
        et_age = (EditText) findViewById(R.id.et_reg_age);
        et_weight = (EditText) findViewById(R.id.et_reg_height);
        et_height = (EditText) findViewById(R.id.et_reg_height);
        spin_gender = (Spinner) findViewById(R.id.spin_reg_gender);
        spin_weight_goal = (Spinner) findViewById(R.id.spin_reg_gender);
        b_next = (Button) findViewById(R.id.btn_reg_next);

        // listen if the user has clicked the next button
        b_next.setOnClickListener(this);
    }

    // Check which listener was called
    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            // next button was clicked
            case R.id.btn_reg_next:
                if (validate()) {
                    //startActivity(new Intent(Register.this, MainActivity.class));
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
    private void next() {

    }
}
