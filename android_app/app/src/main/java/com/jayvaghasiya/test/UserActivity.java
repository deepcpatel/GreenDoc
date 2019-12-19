package com.jayvaghasiya.test;

import android.content.Intent;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;

public class UserActivity extends AppCompatActivity {
    Button startButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getSupportActionBar().hide();
        setContentView(R.layout.activity_user);

        View lay = (View) findViewById(R.id.userLayout);
        lay.setBackgroundResource(R.drawable.leaf);

        startButton = findViewById(R.id.startButton);

        startButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                EditText name = findViewById(R.id.nameText);
                SharedPreferences preferences = getApplicationContext().getSharedPreferences("MyPref", 0);
                SharedPreferences.Editor editor = preferences.edit();
                editor.putString("name", name.getText().toString());
                editor.commit();
                Intent i = new Intent(getApplicationContext(), MainActivity.class);
                startActivity(i);
            }
        });
    }
}
