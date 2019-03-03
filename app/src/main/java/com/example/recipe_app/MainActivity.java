package com.example.recipe_app;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    Button b_logout;
    EditText et_email, et_name;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        et_email = (EditText) findViewById(R.id.et_email);
        et_name = (EditText) findViewById(R.id.et_name);
        b_logout = (Button) findViewById(R.id.b_logout);

        b_logout.setOnClickListener(this);
    }


    @Override
    public void onClick(View v) {
        switch(v.getId()) {
            case R.id.b_logout:

                startActivity(new Intent(this, Login.class));
                break;
        }
    }
}
