package com.example.braintraining;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

public class Results extends AppCompatActivity {

    private static final String LOG_TAG = "ArraysState";

    Button btnAgain;
    Button btnGoHome;
    TextView txtTime;
    TextView txtMistakes;
    TextView rightAnswersHeader;
    TextView userAnswersHeader;
    LinearLayout rightAnswersArrayLinearLayout;
    LinearLayout userAnswersArrayLinearLayout;

    int expressionsCount = 0;
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
        rightAnswersHeader = (TextView) findViewById(R.id.rightAnswersHeader);
        userAnswersHeader = (TextView) findViewById(R.id.userAnswersHeader);
        rightAnswersArrayLinearLayout = (LinearLayout) findViewById(R.id.rightAnswersArrayLinearLayout);
        userAnswersArrayLinearLayout = (LinearLayout) findViewById(R.id.userAnswersArrayLinearLayout);

        // получение времени выполнения задания
        Intent intent = getIntent();
        time = intent.getDoubleExtra("time", 0.0);
        Log.d(LOG_TAG, "Time from Training: " + time);
        txtTime.setText("Time: " + getTimerText(time));

        // получение пользовательских ответов
        userAnswers = intent.getIntegerArrayListExtra("userAnswers");

        // получение верных ответов
        rightAnswers = intent.getIntegerArrayListExtra("rightAnswers");

        generateTextViewFromArray();

        // вывод кол-ва ошибок
        txtMistakes.setText("Mistakes: " + Integer.toString(expressionsCount) + " / " + Integer.toString(rightAnswers.size()));

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

    void generateTextViewFromArray(){
        for(int i = 0; i < userAnswers.size(); i++){
            // натройка оформления вывода правильного ответа
            TextView rightItem = new TextView(Results.this);
            LinearLayout.LayoutParams textViewLayoutParams = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
            rightItem.setBackgroundResource(R.color.okColor);
            rightItem.setGravity(Gravity.CENTER);
            rightItem.setTextSize(20);
            textViewLayoutParams.setMargins(10, 10, 10, 10);
            rightItem.setLayoutParams(textViewLayoutParams);

            // натройка оформления вывода пользовательского ответа
            TextView userItem = new TextView(Results.this);

            // установка цвета ответа
            if(Integer.toString(rightAnswers.get(i)) == Integer.toString(userAnswers.get(i))){
                userItem.setBackgroundResource(R.color.okColor);
            } else {
                userItem.setBackgroundResource(R.color.noColor);

                // подсчёт кол-ва ошибок
                expressionsCount++;
            }

            userItem.setGravity(Gravity.CENTER);
            userItem.setTextSize(20);
            userItem.setLayoutParams(textViewLayoutParams);

            // вывод текущего верного ответа
            rightItem.setText(Integer.toString(rightAnswers.get(i)));
            rightAnswersArrayLinearLayout.addView(rightItem);

            // вывод текущего пользовательського ответа
            userItem.setText(Integer.toString(userAnswers.get(i)));
            userAnswersArrayLinearLayout.addView(userItem);
        }
    }
}