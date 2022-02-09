package com.example.braintraining;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

public class Results extends AppCompatActivity {

    private static final String LOG_TAG = "ArraysState";

    Button btnAgain;
    Button btnGoHome;
    TextView txtTime;
    TextView txtMistakes;

    int expressionsCount;
    Double time = 0.0;

    List<Integer> userAnswers = new ArrayList<>();              // массив пользовательских ответов
    List<Integer> rightAnswers = new ArrayList<>();             // массив правильных ответов

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_results);

        btnAgain = (Button) findViewById(R.id.btnAgain);
        btnGoHome = (Button) findViewById(R.id.btnGoHome);
        txtTime = (TextView) findViewById(R.id.txtTimeResult);
        txtMistakes = (TextView) findViewById(R.id.txtMistakes);

        // получение времени выполнения задания
        Intent intent = getIntent();
        time = intent.getDoubleExtra("time", 0.0);
        Log.d(LOG_TAG, "Time from Training: " + time);
        txtTime.setText("Time: " + getTimerText(time));

        // получение пользовательских ответов
        userAnswers = intent.getIntegerArrayListExtra("userAnswers");

        // получение верных ответов
        rightAnswers = intent.getIntegerArrayListExtra("rightAnswers");

        // вывод кол-ва ошибок
        txtMistakes.setText("Mistakes: " + Integer.toString(getMistakesCount()));

        btnGoHome.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                time = 0.0;
                Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                startActivity(intent);
            }
        });

        btnAgain.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // обнуление времени решения задания
                time = 0.0;
                // получение кол-ва выражений из предыдущей (Training) activity
                //Intent expressionsCountIntent = getIntent();
                expressionsCount = intent.getIntExtra("expressionsCount", 50);
                Log.d(LOG_TAG, "Expressions count from Training: " + expressionsCount);

                // передача кол-ва выражений в Training activity
                Intent intent = new Intent(getApplicationContext(), Training.class);
                intent.putExtra("expressionsCount", expressionsCount);
                startActivity(intent);
            }
        });
    }

    // получение времени
    String getTimerText(Double time) {
        int rounded = (int) Math.round(time);
        int seconds = ((rounded % 86400) % 3600) % 60;
        int minutes = ((rounded % 86400) % 3600) / 60;

        return formatTime(seconds, minutes);
    }

    // форматирование вывода времени
    String formatTime(int seconds, int minutes) {
        return String.format("%02d", minutes) + " : " + String.format("%02d", seconds);
    }

    //вычисление кол-ва ошибок
    int getMistakesCount(){
        int mistakesCount = 0;
        for(int i = 0; i < userAnswers.size(); i++){
            if(userAnswers.get(i) != rightAnswers.get(i)){
                mistakesCount++;
            }
        }
        return mistakesCount;
    }

    void generateTextViewFromArray(){

    }
}