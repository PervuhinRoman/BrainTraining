package com.example.braintraining;

import androidx.appcompat.app.AppCompatActivity;
import androidx.drawerlayout.widget.DrawerLayout;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
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
    TextView title;
    TextView rightAnswersHeader;
    TextView userAnswersHeader;
    LinearLayout rightAnswersArrayLinearLayout;
    LinearLayout userAnswersArrayLinearLayout;
    DrawerLayout navigationDrawerLayout;
    ImageView menuIcon;

    int expressionsCount = 0;
    Double time = 0.0;

    List<Integer> userAnswers = new ArrayList<>();              // массив пользовательских ответов
    List<Integer> rightAnswers = new ArrayList<>();             // массив правильных ответов

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_results);

        btnAgain = findViewById(R.id.btnAgain);
        btnGoHome = findViewById(R.id.btnGoHome);
        txtTime = findViewById(R.id.solution_time);
        txtMistakes = findViewById(R.id.mistakes_count);
        rightAnswersHeader = findViewById(R.id.rightAnswersHeader);
        userAnswersHeader = findViewById(R.id.userAnswersHeader);
        rightAnswersArrayLinearLayout = findViewById(R.id.rightAnswersArrayLinearLayout);
        userAnswersArrayLinearLayout = findViewById(R.id.userAnswersArrayLinearLayout);
        navigationDrawerLayout = findViewById(R.id.navigation_drawer_layout);
        menuIcon = findViewById(R.id.menu_icon);
        title = findViewById(R.id.title_of_appBar);
        title.setText(R.string.title_of_resultsActivity);

        // обработка navigation drawer
        menuIcon.setOnClickListener(new View.OnClickListener() {
            @SuppressLint("WrongConstant")
            @Override
            public void onClick(View view) {
                navigationDrawerLayout.openDrawer(Gravity.START);
            }
        });

        // получение времени выполнения задания
        Intent intent = getIntent();
        time = intent.getDoubleExtra("time", 0.0);
        Log.d(LOG_TAG, "Time from Training: " + time);
        txtTime.setText(getResources().getString(R.string.solution_time) + " " + getTimerText(time));

        // получение пользовательских ответов
        userAnswers = intent.getIntegerArrayListExtra("userAnswers");

        // получение верных ответов
        rightAnswers = intent.getIntegerArrayListExtra("rightAnswers");

        generateTextViewFromArray();

        // вывод кол-ва ошибок
        txtMistakes.setText(getResources().getString(R.string.mistakes_count) + " " + expressionsCount + " / " + rightAnswers.size());

        // обрабока нажатия на кнопку Home
        btnGoHome.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                time = 0.0;
                Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                startActivity(intent);
            }
        });

        // обработка нажатия на кнопку Again
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
            // настройка layout атрибутов
            LinearLayout.LayoutParams textViewLayoutParams = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
            textViewLayoutParams.setMargins(10, 10, 10, 10);

            // натройка оформления вывода правильного ответа
            TextView rightItem = new TextView(Results.this, null, 0, R.style.custom_right_answers);
            rightItem.setTypeface(Typeface.DEFAULT_BOLD);
            rightItem.setLayoutParams(textViewLayoutParams);

            // натройка оформления вывода пользовательского ответа
            TextView userItem = new TextView(Results.this, null, 0, R.style.custom_wrong_answers);
            userItem.setTypeface(Typeface.DEFAULT_BOLD);
            userItem.setLayoutParams(textViewLayoutParams);

            // установка цвета ответа
            if(Integer.toString(rightAnswers.get(i)) == Integer.toString(userAnswers.get(i))){
                userItem.setBackgroundResource(R.drawable.custom_right_answers_background);
            } else {
                userItem.setBackgroundResource(R.drawable.custom_wrong_answers_background);

                // подсчёт кол-ва ошибок
                expressionsCount++;
            }

            // вывод текущего верного ответа
            rightItem.setText(Integer.toString(rightAnswers.get(i)));
            rightAnswersArrayLinearLayout.addView(rightItem);

            // вывод текущего пользовательського ответа
            userItem.setText(Integer.toString(userAnswers.get(i)));
            userAnswersArrayLinearLayout.addView(userItem);
        }
    }
}