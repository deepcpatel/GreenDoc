package com.jayvaghasiya.test;

import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.sql.SQLOutput;

public class ResultActivity extends AppCompatActivity {

    TextView titleTextView;
    TextView causeTextView;
    TextView remediesTextView;
    Button checkAnother;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_result);

        Bundle ex = getIntent().getExtras();
        String disease = ex.getString("result");
//        String disease = "Tomato Early Blight";

        titleTextView = findViewById(R.id.diseaseTitleTextView);
        causeTextView = findViewById(R.id.causeTextView);
        remediesTextView = findViewById(R.id.remediesTextView);
        checkAnother = findViewById(R.id.checkAnother);

        checkAnother.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(getApplicationContext(), MainActivity.class);
                startActivity(i);
            }
        });

        try {
            JSONObject obj = new JSONObject(loadJSONFromAsset(getApplicationContext()));
//            System.out.println(obj);
            JSONArray dis = obj.getJSONArray(disease);
            String causeString = dis.getJSONObject(0).getString("Causes");
            String remediesString = dis.getJSONObject(1).getString("Remedies");
            titleTextView.setText(disease);
            causeTextView.setText(causeString);
            remediesTextView.setText(remediesString);
//            Toast toast = Toast.makeText(getApplicationContext(),
//                    remediesString,
//                    Toast.LENGTH_LONG);
//            toast.show();
//
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    public String loadJSONFromAsset(Context context) {
        String json = null;
        try {
            InputStream is = context.getAssets().open("causesremedies.json");

            int size = is.available();

            byte[] buffer = new byte[size];

            is.read(buffer);

            is.close();

            json = new String(buffer, "UTF-8");


        } catch (IOException ex) {
            ex.printStackTrace();
            return null;
        }
        return json;

    }
}
