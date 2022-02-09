package com.example.braintraining;

import static androidx.core.content.PackageManagerCompat.LOG_TAG;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class Results extends AppCompatActivity {

    private static final String LOG_TAG = "ArraysState";

    Button btnAgain;
    Button btnGoHome;

    int expressionsCount;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_results);

        btnAgain = (Button) findViewById(R.id.btnAgain);
        btnGoHome = (Button) findViewById(R.id.btnGoHome);

        btnGoHome.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                startActivity(intent);
            }
        });

        btnAgain.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // получение кол-ва выражений из предыдущей (Training) activity
                Intent expressionsCountIntent = getIntent();
                expressionsCount = expressionsCountIntent.getIntExtra("expressionsCount", 50);
                Log.d(LOG_TAG, "Expressions count from Training: " + expressionsCount);

                // передача кол-ва выражений в Training activity
                Intent intent = new Intent(getApplicationContext(), Training.class);
                intent.putExtra("expressionsCount", expressionsCount);
                startActivity(intent);
            }
        });
    }
}